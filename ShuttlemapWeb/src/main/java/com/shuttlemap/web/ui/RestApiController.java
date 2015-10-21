package com.shuttlemap.web.ui;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shuttlemap.web.IShuttleService;
import com.shuttlemap.web.common.CommonUtils;
import com.shuttlemap.web.dto.CompanyDTO;
import com.shuttlemap.web.dto.rest.CompanySaveRequestDto;
import com.shuttlemap.web.dto.rest.CompanySaveResponseDto;
import com.shuttlemap.web.entity.Association;
import com.shuttlemap.web.entity.Company;
import com.shuttlemap.web.entity.CompanyType;
import com.shuttlemap.web.identity.IAssociationService;
import com.shuttlemap.web.identity.IUserService;

@Controller
@RequestMapping("/rest")
public class RestApiController {
	@Autowired IUserService userService;
	@Autowired IShuttleService shuttleService;
	@Autowired IAssociationService associationService;
	
	private Log log = LogFactory.getLog(RestApiController.class);
	
	@RequestMapping(value="/company/regist",method=RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String registCompany(@RequestBody CompanySaveRequestDto requestDto,BindingResult result,Model model) throws IOException{
		CompanySaveResponseDto responseDto = new CompanySaveResponseDto();
		
		Company company =  new Company();
		company.setActive(true);
		Association association = associationService.getDefault();
		company.setAssociation(association);
		
		try{
			populateCompany(company, requestDto);
			userService.saveCompanyAndUser(company, requestDto.getUserLoginId(), requestDto.getUserPassword());
			
			responseDto.setCompanyId(company.getId());
			responseDto.setSuccess(true);
			
		}catch(Exception e){
			log.error("Save Error : ",e);
			responseDto.setSuccess(false);
			responseDto.setErrorMsg(e.getMessage());
		}
		
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(responseDto);
	}
	
	private void populateCompany(Company company, CompanySaveRequestDto companyDto)  throws Exception {
		validate(companyDto.getName(), "기관명");
		validate(companyDto.getCompanyType(), "기관종류");
		validate(companyDto.getLicenseNo(), "사업자등록번호");
		validate(companyDto.getContactPerson(), "담당자");
		validate(companyDto.getPhone() ,"전화번호");
		validate(companyDto.getAddress() ,"기본주소");
		validate(companyDto.getUserLoginId() ,"기관관리자 아이디");
		validate(companyDto.getUserPassword() ,"기관관리자 비밀번호");
		
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
		
	}
	
	private void validate(CompanyType value, String label) throws Exception {
		if (value == null || "".equals(value.name())) {
			throw new Exception("[" + label + "]의 값이 없습니다.");
		}
	}
	
	private void validate(String value, String label) throws Exception {
		if (value == null || "".equals(value)) {
			throw new Exception(label + "의 값이 없습니다.");
		}
	}
}
