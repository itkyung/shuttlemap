package com.shuttlemap.web.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shuttlemap.web.IShuttleDAO;
import com.shuttlemap.web.IShuttleService;
import com.shuttlemap.web.common.DistanceUtils;
import com.shuttlemap.web.common.IFileService;
import com.shuttlemap.web.delegate.RouteDelegate;
import com.shuttlemap.web.delegate.RouteParam;
import com.shuttlemap.web.delegate.ShuttleDelegate;
import com.shuttlemap.web.delegate.ShuttleParam;
import com.shuttlemap.web.entity.BookmarkShuttle;
import com.shuttlemap.web.entity.Company;
import com.shuttlemap.web.entity.Shuttle;
import com.shuttlemap.web.entity.ShuttleDrivingInfo;
import com.shuttlemap.web.entity.ShuttleDrivingStatus;
import com.shuttlemap.web.entity.ShuttleRoute;
import com.shuttlemap.web.entity.ShuttleScheduleType;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.identity.IUserService;

@Service
public class ShuttleService implements IShuttleService {
	@Autowired private IShuttleDAO dao;
	@Autowired private IFileService fileService;
	@Autowired private IUserService userService;
	
	private static final int DISTANCE_THRESHOLD = 100; //거리 Range meter값.
	
	private Log log = LogFactory.getLog(ShuttleService.class);
	
	@Override
	public List<ShuttleDelegate> searchShuttle(String keyword, int start,
			int limits) {
		
		List<Shuttle> shuttles = dao.searchShuttle(keyword, null, start, limits);
		
		List<ShuttleDelegate> results = new ArrayList<ShuttleDelegate>();
		for(Shuttle shuttle : shuttles){
			ShuttleDelegate del = new ShuttleDelegate(shuttle);
			results.add(del);
		}
		
		return results;
	}

	@Transactional
	@Override
	public void addBookmark(User owner, Shuttle shuttle, String markName) {
		dao.addBookmark(owner, shuttle, markName);
	}

	@Transactional
	@Override
	public void removeBookmakr(String id) {
		dao.removeBookmakr(id);
	}


	@Override
	public Shuttle loadShuttle(String id) {
		return dao.loadShuttle(id);
	}


	@Override
	public List<ShuttleDelegate> listBookmark(User user) {
		List<BookmarkShuttle> shuttles = dao.listBookmark(user);
		
		List<ShuttleDelegate> results = new ArrayList<ShuttleDelegate>();
		for(BookmarkShuttle bs : shuttles){
			ShuttleDelegate sd = new ShuttleDelegate(bs.getShuttle());
			sd.setBookmarkName(bs.getBookmarkName());
			sd.setBookmarkId(bs.getId());
			results.add(sd);
		}
		
		return results;
	}

	@Override
	public List<ShuttleRoute> getRoutes(String id) {
		if(id.length() == 0){
			return new ArrayList<ShuttleRoute>();
		}
		Shuttle shuttle = dao.loadShuttle(id);
		return shuttle.getRoutes();
	}

	@Override
	public List<ShuttleDelegate> findShuttle(Company company) {
		List<Shuttle> shuttles = dao.findShuttle(company);
		List<ShuttleDelegate> results = new ArrayList<ShuttleDelegate>();
		
		for(Shuttle shuttle : shuttles){
			ShuttleDelegate del = new ShuttleDelegate(shuttle);
			results.add(del);
		}
		
		return results;
	}

	@Transactional
	@Override
	public Shuttle saveShuttle(ShuttleParam param) {
		Shuttle shuttle = null;
		boolean isCreate = false;
		
		if(param.getShuttleId() != null && param.getShuttleId().length() > 0){
			shuttle = dao.loadShuttle(param.getShuttleId());
		}else{
			isCreate = true;
			shuttle = new Shuttle();
			Company company = userService.loadCompany(param.getCompanyId());
			shuttle.setCompany(company);
		}
		
		if(param.getRouteFile() != null && !param.getRouteFile().isEmpty()){
			try{
				String routeFilePath = fileService.saveFile(param.getRouteFile(), param.getCompanyId());
				shuttle.setRouteFilePath(routeFilePath);
			}catch(Exception e){
				log.error("Error ", e);
			}
		}
		
		shuttle.setName(param.getName());
		shuttle.setActive(true);
		shuttle.setCarNo(param.getCarNo());
		shuttle.setStatus(param.getStatus());
		shuttle.setStartHour(param.getStartHour());
		shuttle.setStartMinute(param.getStartMinute());
		shuttle.setEndHour(param.getEndHour());
		shuttle.setEndMinute(param.getEndMinute());
		shuttle.setScheduleType(ShuttleScheduleType.NORMAL);
		shuttle.setGoogleMapUrl(param.getGoogleMapUrl());
		shuttle.setCarType(param.getCarType());
		
		if(param.getDriverId() != null && param.getDriverId().length() > 0){
			shuttle.setDriver(userService.loadUser(param.getDriverId()));
		}
		
		
		if(isCreate){
			dao.createShuttle(shuttle);
		}else{
			dao.updateShuttle(shuttle);
		}
		
		return shuttle;
	}

	@Transactional
	@Override
	public void saveRoute(RouteParam param) {
		Shuttle shuttle = null;
		ShuttleRoute route = null;
		List<ShuttleRoute> routes = null;
		if(param.getRouteId() != null && param.getRouteId().length() > 0){
			route = dao.loadRoute(param.getRouteId());
			shuttle = route.getShuttle();
			routes = shuttle.getRoutes();
		}else{
			shuttle = dao.loadShuttle(param.getShuttleId());
			route = new ShuttleRoute();
			route.setShuttle(shuttle);

			routes = shuttle.getRoutes();
			if(routes == null){
				routes = new ArrayList<ShuttleRoute>();
				shuttle.setRoutes(routes);
			}
			routes.add(route);
		}
		
		route.setRouteName(param.getRouteName());
		route.setLatitude(new BigDecimal(param.getLatitude()));
		route.setLongitude(new BigDecimal(param.getLongitude()));
		
		route.setIdx(param.getRouteIdx());
		route.setCreated(new Date());
		if(param.getRouteId() != null && param.getRouteId().length() > 0){
			dao.updateRoute(route);
		}else{
			dao.createRoute(route);
		}
		
		ShuttleRoute firstRoute = routes.get(0);
		shuttle.setStartLatitude(firstRoute.getLatitude());
		shuttle.setStartLongitude(firstRoute.getLongitude());
		
		ShuttleRoute lastRoute = routes.get(routes.size()-1);
		shuttle.setEndLatitude(lastRoute.getLatitude());
		shuttle.setEndLongitude(lastRoute.getLongitude());
		
		dao.updateShuttle(shuttle);
	}
	
	/**
	 * 운전사의 현재 위치에 따라서 운영하는 셔틀의 현재 위치정보를 업데이트한다.
	 * 1. 해당 운전사가 운전하는 셔틀정보를 얻는다. (여러개가 존재할수 있음.)
	 * 2. 해당 셔틀중에서 현재시간에서 10분안에 
	 * 
	 */
	@Async
	@Transactional
	@Override
	public void updateDriverLocation(User user, double latitude,
			double longitude) {
		
		List<Shuttle> shuttles = dao.findByDriver(user);
		
		for(Shuttle shuttle : shuttles){
			updateShuttleLocation(shuttle, latitude, longitude);
		}
	}

	
	private void updateShuttleLocation(Shuttle shuttle,  double latitude, double longitude){
		ShuttleDrivingInfo drivingInfo = dao.findCurrentDrivingInfo(shuttle);
		
		if(drivingInfo != null){
			//운행중이면 마지막 위치 다음 경로중에서 순서대로 특정거리 안쪽에 존재하는 정류장으로 위치를 이동시킨다.
			ShuttleRoute lastRoute = drivingInfo.getLastArrivedRoute();
			
			ShuttleRoute nearstRoute = findNearstRoute(shuttle, lastRoute, latitude, longitude);
			if(nearstRoute != null) {
				ShuttleRoute endRoute = shuttle.getRoutes().get(shuttle.getRoutes().size()-1);
				if(nearstRoute.equals(endRoute)){
					//목적지 도착으로 운행중 상태를 없앰.
					drivingInfo.setActive(false);
					drivingInfo.setLastArrivedRoute(nearstRoute);
					drivingInfo.setLastUpdateDate(new Date());
				}else{
					drivingInfo.setLastArrivedRoute(nearstRoute);
					drivingInfo.setLastUpdateDate(new Date());
				}
				dao.updateDrivingInfo(drivingInfo);
			}
		}else{
			//아직 운행중이지 않으면 셔틀의 출발 시간에서 +- 10분 안쪽이면서 출발지에서 특정거리 안에 존재하면 출발상태로 처리한다.
			if(isShuttleStartTime(shuttle)){
				double distance = DistanceUtils.getDistance(shuttle.getStartLatitude().doubleValue(), shuttle.getStartLongitude().doubleValue(),
						latitude, latitude);
				if(distance <= DISTANCE_THRESHOLD) {
					ShuttleRoute first = shuttle.getRoutes().get(0);
					ShuttleDrivingInfo info = new ShuttleDrivingInfo();
					info.setShuttle(shuttle);
					info.setLastArrivedRoute(first);
					info.setActive(true);
					info.setLastUpdateDate(new Date());
					dao.createDrivingInfo(info);
				}
			}
		}
		
	}
	
	/*
	 * 현재 시간이 출발시간대비 +-10분 안에 존재하는지를 확인한다.
	 */
	private boolean isShuttleStartTime(Shuttle shuttle){
		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, shuttle.getStartHour());
		startTime.set(Calendar.MINUTE, shuttle.getStartMinute());
		
		Calendar minRange = Calendar.getInstance();
		minRange.add(Calendar.MINUTE, -10);
		
		Calendar maxRange = Calendar.getInstance();
		maxRange.add(Calendar.MINUTE, 10);
		
		return startTime.getTime().after(minRange.getTime()) && startTime.getTime().before(maxRange.getTime());
	}
	
	
	
	/**
	 * 해당 셔틀의 정거장중에서 넘겨진 위치에서 특정 거리안에 존재하는(원으로 직선거리) 정거장을 리턴한다.
	 * lastRoute 다음순서에 존재하는 정류장중에서 찾는다.
	 * 특정거리안에 존재하지 않으면 null을 리턴한다.
	 * 
	 * @param shuttle
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	private ShuttleRoute findNearstRoute(Shuttle shuttle,ShuttleRoute lastRoute, double latitude, double longitude) {
		List<ShuttleRoute> routes = shuttle.getRoutes();
		
		boolean finded = false;
		
		for(ShuttleRoute route : routes){
			if(lastRoute.equals(route)){
				finded = true;
				continue;
			}
			if(finded){
				//마지막 정류장 다음부터만 체크함.
				double distance = DistanceUtils.getDistance(route.getLatitude().doubleValue(), route.getLongitude().doubleValue(), 
						latitude, longitude);
				if(distance <= DISTANCE_THRESHOLD){
					return route;
				}
			}
		}
		
		return null;
	}

	/**
	 * 셔틀의 현재위치 정보를 포함한 경로를 리턴한다.
	 */
	@Override
	public List<RouteDelegate> findRoutesAndLocation(String id) {
		List<RouteDelegate> delegates = new ArrayList<RouteDelegate>();
		if(id.length() == 0){
			return delegates;
		}
		Shuttle shuttle = dao.loadShuttle(id);
		ShuttleDrivingInfo drivingInfo = dao.findCurrentDrivingInfo(shuttle);
		
		for(ShuttleRoute route : shuttle.getRoutes()){
			RouteDelegate delegate = new RouteDelegate(route);
			
			if(drivingInfo != null) {
				if(route.equals(drivingInfo.getLastArrivedRoute())){
					delegate.setArrived(true);
					delegate.setArriveDate(drivingInfo.getLastUpdateDate().getTime());
				}
			}
			delegates.add(delegate);
		}
		
		return delegates;
	}

	
	@Override
	public List<ShuttleDelegate> searchShuttle(String keyword, Company company,
			int start, int limits) {
		List<Shuttle> shuttles = dao.searchShuttle(keyword, company, start, limits);
		
		List<ShuttleDelegate> results = new ArrayList<ShuttleDelegate>();
		for(Shuttle shuttle : shuttles){
			ShuttleDelegate del = new ShuttleDelegate(shuttle);
			results.add(del);
		}
		
		return results;
	}

	@Transactional
	@Override
	public Shuttle copyShuttle(String shuttleId) {
		Shuttle old = dao.loadShuttle(shuttleId);
		
		Shuttle shuttle = new Shuttle();
		shuttle.setActive(true);
		shuttle.setCompany(old.getCompany());
		shuttle.setCarNo(old.getCarNo());
		shuttle.setDriver(old.getDriver());
		shuttle.setCarType(old.getCarType());
		shuttle.setEndHour(old.getEndHour());
		shuttle.setEndMinute(old.getEndMinute());
		shuttle.setEndLatitude(old.getEndLatitude());
		shuttle.setEndLongitude(old.getEndLongitude());
		shuttle.setGoogleMapUrl(old.getGoogleMapUrl());
		shuttle.setName(old.getName() + "-복사본");
		shuttle.setRouteFilePath(old.getRouteFilePath());
		shuttle.setScheduleType(old.getScheduleType());
		shuttle.setStartHour(old.getStartHour());
		shuttle.setStartLatitude(old.getStartLatitude());
		shuttle.setStartLongitude(old.getStartLongitude());
		shuttle.setStartMinute(old.getStartMinute());
		shuttle.setStatus(old.getStatus());
		
		List<ShuttleRoute> routes = new ArrayList<ShuttleRoute>();
		for(ShuttleRoute route : old.getRoutes()) {
			routes.add(copyRoute(route, shuttle));
		}
		
		shuttle.setRoutes(routes);
		dao.createShuttle(shuttle);
		return shuttle;
	}
	
	private ShuttleRoute copyRoute(ShuttleRoute route,Shuttle shuttle) {
		ShuttleRoute newR = new ShuttleRoute();
		newR.setIdx(route.getIdx());
		newR.setLatitude(route.getLatitude());
		newR.setLongitude(route.getLongitude());
		newR.setRouteName(route.getRouteName());
		newR.setShuttle(shuttle);
		
		return newR;
	}
}
