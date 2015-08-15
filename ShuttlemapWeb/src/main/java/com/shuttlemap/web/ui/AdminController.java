package com.shuttlemap.web.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuttlemap.web.DatatableJson;
import com.shuttlemap.web.IShuttleService;
import com.shuttlemap.web.Role;
import com.shuttlemap.web.common.CommonUtils;
import com.shuttlemap.web.delegate.CompanyDelegate;
import com.shuttlemap.web.delegate.CompanySearchModel;
import com.shuttlemap.web.delegate.RouteDelegate;
import com.shuttlemap.web.delegate.RouteParam;
import com.shuttlemap.web.delegate.RouteSearchModel;
import com.shuttlemap.web.delegate.ShuttleDelegate;
import com.shuttlemap.web.delegate.ShuttleParam;
import com.shuttlemap.web.delegate.ShuttleSearchModel;
import com.shuttlemap.web.delegate.UserDelegate;
import com.shuttlemap.web.dto.CompanyDTO;
import com.shuttlemap.web.entity.Company;
import com.shuttlemap.web.entity.Shuttle;
import com.shuttlemap.web.entity.ShuttleRoute;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.entity.UserType;
import com.shuttlemap.web.identity.ILogin;
import com.shuttlemap.web.identity.IUserService;
import com.shuttlemap.web.identity.OsType;



@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private ILogin login;
	@Autowired
	private IUserService userService;
	@Autowired
	private IShuttleService shuttleService;
	
	private Log log = LogFactory.getLog(AdminController.class);
	
	@RequestMapping("/home")
	public String home() {
		
		return "admin/home";
	}
	
	@RequestMapping("/listCompany")
	public String listCompany(){
		
		return "admin/listCompany";
	}
	
	@RequestMapping("/listShuttle")
	public String listShuttle(){
		
		return "admin/listShuttle";
	}
	

	@RequestMapping(value="/searchCompany",method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String searchCompany(@ModelAttribute("condition") CompanySearchModel condition,BindingResult result,HttpServletRequest request) throws IOException{
		
		int _perPage = condition.getLength();
		
		condition.setStart(condition.getStart());
		condition.setLimit(_perPage);
		
		int totalCount = userService.countCompany(condition);
		List<CompanyDelegate> results = userService.findCompany(condition);
		
		int totalPage = 0;
		if(totalCount > 0){
			totalPage =  (totalCount % _perPage) == 0 ? totalCount/_perPage : (totalCount/_perPage)+1;
		}
		
		DatatableJson json = new DatatableJson(condition.getDraw(), totalCount, results.size(), results.toArray());
		
		return CommonUtils.toJson(json);
	}
	
	@RequestMapping("/editCompanyForm")
	public String editCompanyForm(@RequestParam(value="id",required=false) String id,Model model){
		if(id != null){
			Company company = userService.loadCompany(id);
			List<User> users = userService.findUser(company);
			model.addAttribute("company",company);
			if(users.size() > 0){
				model.addAttribute("companyUser",users.get(0));
			}
		}
		return "admin/editCompanyForm";
	}
	
	@RequestMapping(value="/searchShuttle",method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String searchShuttle(@ModelAttribute("condition") ShuttleSearchModel condition,BindingResult result,HttpServletRequest request) throws IOException{
		int _perPage = condition.getLength();
		
		condition.setStart(condition.getStart());
		condition.setLimit(_perPage);
		
		if(condition.getCompanyId() != null){
			Company company = userService.loadCompany(condition.getCompanyId());
			List<ShuttleDelegate> results = shuttleService.findShuttle(company);
			DatatableJson json = new DatatableJson(condition.getDraw(), results.size(), results.size(), results.toArray());
			return CommonUtils.toJson(json);
		}else{
			List<ShuttleDelegate> results = shuttleService.searchShuttle(condition.getKeyword(), condition.getStart(), condition.getLimit());
			DatatableJson json = new DatatableJson(condition.getDraw(), results.size(), results.size(), results.toArray());
			return CommonUtils.toJson(json);
		}
	}
	
	@RequestMapping(value="/saveCompany",method=RequestMethod.POST)
	public String saveCompany(@ModelAttribute CompanyDTO companyDto,BindingResult result,Model model) throws IOException{
		Company company = null;
		
		if(companyDto.getCompanyId() != null && !"".equals(companyDto.getCompanyId())) {
			company = userService.loadCompany(companyDto.getCompanyId());
		}else{
			company = new Company();
		}
		try{
			userService.saveCompanyAndUser(company, companyDto.getUserLoginId(), companyDto.getUserPassword());
			model.addAttribute("companyId", company.getId());
		}catch(Exception e){
			log.error("Save Error : ",e);
		}
		return "admin/saveCompanyResult";
	}
	
	@RequestMapping("/editShuttleForm")
	public String editShuttleForm(@RequestParam(value="companyId",required=false) String companyId,
			@RequestParam(value="id",required=false) String id,Model model){
		if(id != null){
			Shuttle shuttle = shuttleService.loadShuttle(id);
			model.addAttribute("shuttle",shuttle);
			model.addAttribute("companyId", shuttle.getCompany().getId());
		}else{
			model.addAttribute("companyId", companyId);
		}
		return "admin/editShuttleForm";
	}
	
	@RequestMapping(value="/searchDriver",method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String searchDriver(@RequestParam("phone") String phone) {
		List<UserDelegate> results = userService.findUserByPhone(phone, UserType.DRIVER);
		return CommonUtils.toJson(results);
	}
	
	@RequestMapping(value="/searchRoute",method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String searchRoute(@ModelAttribute("condition") RouteSearchModel condition,BindingResult result,HttpServletRequest request) throws IOException{
		int _perPage = condition.getLength();
		
		condition.setStart(condition.getStart());
		condition.setLimit(_perPage);
		
		List<ShuttleRoute> results = null;
		if(condition.getShuttleId() != null){
			results = shuttleService.getRoutes(condition.getShuttleId());
		}else{
			results = new ArrayList<ShuttleRoute>();
		}
		
		DatatableJson json = new DatatableJson(condition.getDraw(), results.size(), results.size(), results.toArray());
		return CommonUtils.toJson(json);
	}
	
	
	@RequestMapping(value="/saveDriver",method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String saveDriver(@RequestParam("driverName") String name, @RequestParam("driverPhone") String phone,
			@RequestParam("loginId") String loginId,@RequestParam("password") String password) {
		try{
			userService.registUser(loginId, password, name, phone, UserType.DRIVER, "", OsType.Android);
			return CommonUtils.toJsonResult(true, 0, null);
		}catch(Exception e){
			log.error("Error",e);
			return CommonUtils.toJsonResult(false, 1, null);
		}
		
	}
	
	@RequestMapping(value="/saveShuttle",method=RequestMethod.POST)
	public String saveShuttle(@ModelAttribute ShuttleParam param,BindingResult bindingResult,ModelMap model) throws IOException{
		
		Shuttle shuttle = shuttleService.saveShuttle(param);
		model.addAttribute("shuttle",shuttle);
		return "admin/saveShuttleResult";
	}
	
	
	@RequestMapping(value = "/saveRoute",method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String saveRoute(@ModelAttribute RouteParam param,BindingResult bindingResult) {
		shuttleService.saveRoute(param);
		return CommonUtils.toJsonResult(true, 0, null);
	}
	
	@RequestMapping("/listUser")
	public String listUser(){
		
		return "admin/listUser";
	}
	
	@RequestMapping(value="/searchUser",method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String searchUser(@ModelAttribute("condition") CompanySearchModel condition,BindingResult result,HttpServletRequest request) throws IOException{
		
		int _perPage = condition.getLength();
		
		condition.setStart(condition.getStart());
		condition.setLimit(_perPage);
		
		int totalCount = userService.countUser(condition.getKeyword(), condition.getUserType());
		List<UserDelegate> results = userService.findUserByName(condition.getKeyword(), condition.getUserType(), condition.getStart(), condition.getLimit());
		
		int totalPage = 0;
		if(totalCount > 0){
			totalPage =  (totalCount % _perPage) == 0 ? totalCount/_perPage : (totalCount/_perPage)+1;
		}
		
		DatatableJson json = new DatatableJson(condition.getDraw(), totalCount, results.size(), results.toArray());
		
		return CommonUtils.toJson(json);
	}
	
}
