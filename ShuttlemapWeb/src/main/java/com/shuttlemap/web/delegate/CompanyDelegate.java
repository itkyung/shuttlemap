package com.shuttlemap.web.delegate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.google.gson.annotations.Expose;
import com.shuttlemap.web.entity.Company;

public class CompanyDelegate {
	@Expose
	private String id;
	@Expose
	private String companyLogo;
	@Expose
	private String name;
	@Expose
	private String presidentName;
	@Expose
	private String licenseNo;
	@Expose
	private String email;
	@Expose
	private String mobilePhone;
	
	@Expose
	private String companyType;
	@Expose
	private String contactPerson;
	@Expose
	private String phone;
	@Expose
	private String createdStr;
	
	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public CompanyDelegate(){
		
	}
	
	public CompanyDelegate(Company company){
		this.id = company.getId();
		this.companyLogo = company.getCompanyLogo();
		this.name = company.getName();
		this.companyType = company.getCompanyType().name();
		this.contactPerson = company.getContactPerson();
		this.createdStr = df.format(company.getCreated());
		this.phone = company.getPhone();
		this.mobilePhone = company.getMobilePhone();
		this.presidentName = company.getPresidentName();
		this.licenseNo = company.getLicenseNo();
		this.email = company.getEmail();
	}

	public String getId() {
		return id;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public String getName() {
		return name;
	}

	public String getCompanyType() {
		return companyType;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public String getPhone() {
		return phone;
	}

	public String getCreatedStr() {
		return createdStr;
	}

	public String getPresidentName() {
		return presidentName;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public String getEmail() {
		return email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}
	
	
}
