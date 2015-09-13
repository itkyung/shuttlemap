package com.shuttlemap.web;

import java.util.List;

import com.shuttlemap.web.delegate.CompanyDelegate;
import com.shuttlemap.web.delegate.RouteDelegate;
import com.shuttlemap.web.delegate.RouteParam;
import com.shuttlemap.web.delegate.ShuttleDelegate;
import com.shuttlemap.web.delegate.ShuttleParam;
import com.shuttlemap.web.entity.Company;
import com.shuttlemap.web.entity.Shuttle;
import com.shuttlemap.web.entity.ShuttleRoute;
import com.shuttlemap.web.entity.User;

public interface IShuttleService {
	
	List<ShuttleDelegate> searchShuttle(String keyword,Company company, int start,int limits);
	List<ShuttleDelegate> searchShuttle(String keyword,int start,int limits);
	void addBookmark(User owner, Shuttle shuttle, String markName);
	void removeBookmakr(String id);
	Shuttle loadShuttle(String id);
	List<ShuttleDelegate> listBookmark(User user);
	List<ShuttleRoute> getRoutes(String id);
	
	List<RouteDelegate> findRoutesAndLocation(String id);

	List<ShuttleDelegate> findShuttle(Company company);
	
	Shuttle saveShuttle(ShuttleParam param);
	void saveRoute(RouteParam param);
	void updateDriverLocation(User user, double latitude,double longitude);
	void updateDriverLocation(User user, double latitude,double longitude, boolean forceStart);
	Shuttle copyShuttle(String shuttleId);
}
