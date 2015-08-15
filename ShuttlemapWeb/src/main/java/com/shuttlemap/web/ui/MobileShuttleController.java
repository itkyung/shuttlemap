package com.shuttlemap.web.ui;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.shuttlemap.web.IShuttleService;
import com.shuttlemap.web.common.CommonUtils;
import com.shuttlemap.web.delegate.ShuttleDelegate;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.identity.ILogin;

@Controller
@RequestMapping("/mobile/shuttle")
public class MobileShuttleController {
	@Autowired ILogin login;
	@Autowired IShuttleService shuttleService;
	
	private final int PAGE_SIZE = 30;
	
	@RequestMapping(value="/searchShuttle",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String searchShuttle(@RequestParam(value="keyword",required=false) String keyword,
			@RequestParam("page") int page){
		
		int start = (page-1) * PAGE_SIZE;
		int limit = PAGE_SIZE;
		
		List<ShuttleDelegate> results = shuttleService.searchShuttle(keyword, start, limit);
		
		return CommonUtils.toJsonResult(true, 0, results);
	}
	
	
	@RequestMapping(value="/listBookmark",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String listBookmark(){
		User user = login.getCurrentUser();
	
		List<ShuttleDelegate> results = shuttleService.listBookmark(user);
		return CommonUtils.toJsonResult(true, 0, results);
	}
	
	@RequestMapping(value="/addBookmark",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String addBookmark(@RequestParam("shuttleId") String shuttleId,
			@RequestParam("bookmarkName") String bookmarkName){
		User user = login.getCurrentUser();
		
		shuttleService.addBookmark(user, shuttleService.loadShuttle(shuttleId), bookmarkName);
		return CommonUtils.toJsonResult(true, 0, null);
	}
	
	@RequestMapping(value="/removeBookmark",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String removeBookmark(@RequestParam("bookmarkId") String bookmarkId){
		
		shuttleService.removeBookmakr(bookmarkId);
		return CommonUtils.toJsonResult(true, 0, null);
	}
	
	@RequestMapping(value="/listRoutes",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String listRoutes(@RequestParam("id") String id){
		return CommonUtils.toJsonResult(true, 0, shuttleService.findRoutesAndLocation(id));
	}
	
}
