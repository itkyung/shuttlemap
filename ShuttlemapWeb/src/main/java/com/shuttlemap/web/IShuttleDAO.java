package com.shuttlemap.web;

import java.util.List;

import com.shuttlemap.web.delegate.ShuttleDelegate;
import com.shuttlemap.web.entity.BookmarkShuttle;
import com.shuttlemap.web.entity.Company;
import com.shuttlemap.web.entity.Shuttle;
import com.shuttlemap.web.entity.ShuttleDrivingInfo;
import com.shuttlemap.web.entity.ShuttleRoute;
import com.shuttlemap.web.entity.User;

public interface IShuttleDAO {
	
	List<Shuttle> searchShuttle(String keyword, int start,int limit, Company... companies);
	int countShuttle(String keyword,Company... companies);
	
	void createShuttle(Shuttle shuttle);
	void updateShuttle(Shuttle shuttle);
	void createRoute(ShuttleRoute route);
	void updateRoute(ShuttleRoute route);
	
	Shuttle loadShuttle(String id);
	ShuttleRoute loadRoute(String id);
	void addBookmark(User owner,Shuttle shuttle,String markName);
	void removeBookmakr(String id);
	List<BookmarkShuttle> listBookmark(User owner);
	List<Shuttle> findShuttle(Company company);
	List<Shuttle> findByDriver(User driver);
	ShuttleDrivingInfo findCurrentDrivingInfo(Shuttle shuttle);
	void createDrivingInfo(ShuttleDrivingInfo info);
	void updateDrivingInfo(ShuttleDrivingInfo info);
}
