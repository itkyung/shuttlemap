package com.shuttlemap.web.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.shuttlemap.web.delegate.DataTableSearchModel;
import com.shuttlemap.web.delegate.RouteDelegate;
import com.shuttlemap.web.delegate.RouteParam;
import com.shuttlemap.web.delegate.RouteSearchModel;
import com.shuttlemap.web.delegate.ShuttleDelegate;
import com.shuttlemap.web.delegate.ShuttleParam;
import com.shuttlemap.web.delegate.ShuttleSearchModel;
import com.shuttlemap.web.delegate.UserDelegate;
import com.shuttlemap.web.dto.AssociationDTO;
import com.shuttlemap.web.dto.CompanyDTO;
import com.shuttlemap.web.entity.Association;
import com.shuttlemap.web.entity.Company;
import com.shuttlemap.web.entity.Shuttle;
import com.shuttlemap.web.entity.ShuttleDrivingStatus;
import com.shuttlemap.web.entity.ShuttleRoute;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.entity.UserType;
import com.shuttlemap.web.identity.IAssociationService;
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
	@Autowired
	private IAssociationService associationService;
	
	
	private Log log = LogFactory.getLog(AdminController.class);
	
	@RequestMapping("/home")
	public String home(Model model) {
		
		return "admin/home";
	}
	
	@RequestMapping("/editAssociationFormProxy")
	public String editAssociationFormProxy() {
		User currentUser = login.getCurrentUser();
		Association association = currentUser.getAssociation();
		return "redirect:/admin/editAssociationForm?id=" + association.getId();
	}
	
	@RequestMapping("/listCompanyProxy")
	public String listCompanyProxy(){
		User currentUser = login.getCurrentUser();
		Association association = currentUser.getAssociation();
		return "redirect:/admin/listCompany?associationId=" + association.getId();
	}
	
	@RequestMapping("/listCompany")
	public String listCompany(@RequestParam(value="associationId",required=false) String associationId,Model model){
		if(associationId != null){
			model.addAttribute("associationId",associationId);
			Association association = associationService.load(associationId);
			model.addAttribute("associationName", association.getName());
		}
		return "admin/listCompany";
	}
	
	@RequestMapping("/listShuttle")
	public String listShuttle(){
		
		return "admin/listShuttle";
	}
	
	@RequestMapping(value="/listAssociation")
	public String listAssociation(){
		return "admin/listAssociation";
	}
	
	@RequestMapping(value="/searchAssociation",method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String searchAssociation(@ModelAttribute("condition") DataTableSearchModel condition,BindingResult result,HttpServletRequest request) throws IOException{
		List<Association> results = associationService.findAll();
		
		DatatableJson json = new DatatableJson(condition.getDraw(), results.size(), results.size(), results.toArray());
		
		return CommonUtils.toJson(json);
	}
	
	@RequestMapping("/editAssociationForm")
	public String editAssociationForm(@RequestParam(value="id",required=false) String id,Model model){
		if(id != null){
			Association association = associationService.load(id);
			model.addAttribute("association",association);
			List<User> users = userService.findUser(association);
			if(users.size() > 0){
				model.addAttribute("associationUser",users.get(0));
			}
		}
		
		return "admin/editAssociationForm";
	}
	
	@RequestMapping(value="/saveAssociation",method=RequestMethod.POST)
	public String saveAssociation(@ModelAttribute AssociationDTO dto,BindingResult result,Model model) throws IOException{
		Association association = null;
		
		if(dto.getAssociationId() != null && !"".equals(dto.getAssociationId())) {
			association = associationService.load(dto.getAssociationId());
		}else{
			association = new Association();
			association.setActive(true);
		}
		
		try{
			association.setName(dto.getName());
			association.setPresidentName(dto.getPresidentName());
			association.setPhone(dto.getPhone());
			association.setAddress(dto.getAddress());
			association.setEmail(dto.getEmail());
			association.setMobilePhone(dto.getMobilePhone());
			association.setRegNo(dto.getRegNo());
			association.setFaxNo(dto.getFaxNo());
			association.setAddressDetail(dto.getAddressDetail());
			association.setZipCode(dto.getZipCode());
			
			associationService.saveAssociation(association, dto.getUserLoginId(), dto.getUserPassword());
			model.addAttribute("associationId", association.getId());
			model.addAttribute("success",true);
		}catch(Exception e){
			log.error("Save Error : ",e);
			model.addAttribute("success",false);
			model.addAttribute("errorMsg",e.getMessage());
		}
		return "admin/saveAssociationResult";
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
	
	@RequestMapping("/editCompanyFormProxy")
	public String editCompanyFormProxy() {
		User currentUser = login.getCurrentUser();
		Company company = currentUser.getCompany();
		return "redirect:/admin/editCompanyForm?id=" + company.getId();
	}
	
	@RequestMapping("/editCompanyForm")
	public String editCompanyForm(@RequestParam(value="id",required=false) String id,
			@RequestParam(value="associationId",required=false) String associationId,
			Model model){
		User currentUser = login.getCurrentUser();
		if(id != null){
			Company company = userService.loadCompany(id);
			List<User> users = userService.findUser(company);
			model.addAttribute("company",company);
			if(users.size() > 0){
				model.addAttribute("companyUser",users.get(0));
			}
			model.addAttribute("association", company.getAssociation());
		} else {
			Association association;
			if(associationId == null) {
				//현재 로그인한 사람의 (협회관리자)의 associationId를 얻는다.
				
				Company curCom = currentUser.getCompany();
				if(curCom == null) {
					association = currentUser.getAssociation();
				}else{
					association = curCom.getAssociation();
				}
				
			}else{
				association = associationService.load(associationId);
			}
			model.addAttribute("association",association);
		}
		
		if (login.isInRole(currentUser, Role.ASSOCIATION_ROLE)){
			model.addAttribute("associationManager",true);
		}else{
			model.addAttribute("associationManager",false);
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
	
	@RequestMapping(value="/searchShuttleForCopy",method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String searchShuttleForCopy(@RequestParam("searchName") String searchName,@RequestParam("companyId") String companyId,
			HttpServletRequest request) throws IOException{
		Company company = userService.loadCompany(companyId);
		List<ShuttleDelegate> results = shuttleService.searchShuttle(searchName,company,0,10);
		return CommonUtils.toJson(results);
	}
	
	
	
	@RequestMapping(value="/saveCompany",method=RequestMethod.POST)
	public String saveCompany(@ModelAttribute CompanyDTO companyDto,BindingResult result,Model model) throws IOException{
		Company company = null;
		
		if(companyDto.getCompanyId() != null && !"".equals(companyDto.getCompanyId())) {
			company = userService.loadCompany(companyDto.getCompanyId());
		}else{
			company = new Company();
			company.setActive(true);
			model.addAttribute("associationId", companyDto.getAssociationId());
			//assocaition이 없으면 기본으로 설정한다.
			if(companyDto.getAssociationId() == null) {
				Association association = associationService.getDefault();
				company.setAssociation(association);
				model.addAttribute("associationId", association.getId());
			} 
		}
		
		populateCompany(company, companyDto);
		
		try{
			userService.saveCompanyAndUser(company, companyDto.getUserLoginId(), companyDto.getUserPassword());
			model.addAttribute("companyId", company.getId());
			model.addAttribute("success",true);
		}catch(Exception e){
			log.error("Save Error : ",e);
			model.addAttribute("success",false);
			model.addAttribute("errorMsg",e.getMessage());
		}
		return "admin/saveCompanyResult";
	}
	
	private void populateCompany(Company company, CompanyDTO companyDto) {
		
		company.setName(companyDto.getName());
		company.setCompanyType(companyDto.getCompanyType());
		company.setContactPerson(companyDto.getContactPerson());
		company.setPresidentName(companyDto.getPresidentName());
		company.setPhone(companyDto.getPhone());
		company.setMobilePhone(companyDto.getMobilePhone());
		company.setEmail(companyDto.getEmail());
		company.setFaxNo(companyDto.getFaxNo());
		company.setLicenseNo(companyDto.getLicenseNo());
		company.setZipCode(companyDto.getZipCode());
		company.setAddress(companyDto.getAddress());
		company.setAddressDetail(companyDto.getAddressDetail());
		
		
		if(companyDto.getAssociationId() != null) {
			Association association = associationService.load(companyDto.getAssociationId());
			company.setAssociation(association);
		}
	}
	
	@RequestMapping("/editShuttleForm")
	public String editShuttleForm(@RequestParam(value="companyId",required=false) String companyId,
			@RequestParam(value="id",required=false) String id,Model model){
		if(id != null){
			Shuttle shuttle = shuttleService.loadShuttle(id);
			model.addAttribute("shuttle",shuttle);
			model.addAttribute("companyId", shuttle.getCompany().getId());
			model.addAttribute("companyName", shuttle.getCompany().getName());
		}else{
			model.addAttribute("companyId", companyId);
			Company company = userService.loadCompany(companyId);
			model.addAttribute("companyName", company.getName());
		}
		return "admin/editShuttleForm";
	}
	
	@RequestMapping("/copyShuttle")
	public String copyShuttle(@RequestParam(value="targetId",required=true) String id, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		Shuttle shuttle = shuttleService.copyShuttle(id);
		request.setAttribute("id", shuttle.getId());
		request.setAttribute("companyId", shuttle.getCompany().getId());
		request.getRequestDispatcher("/admin/editShuttleForm").forward(request, response);
		return null;
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
	
	@RequestMapping(value="/test")
	public String test(){
		User user = userService.findByLoginId("driver1");
		shuttleService.updateDriverLocation(user, 37.50525, 127.08880, true);
		
		return null;
	}
	
}
