package com.shuttlemap.web.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name=Shuttle.TABLE_NAME)
public class Shuttle {
	public static final String TABLE_NAME = "SM_SHUTTLE";
	
	@Id @Column(length=36) @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")	
	private String id;
	
	private String name;
	
	@ManyToOne
	@JoinColumn(name="company_fk")
	private Company company;
	
	@ManyToOne
	@JoinColumn(name="driver_fk")
	private User driver;
	
	private String carNo;	//차량번호.
	
	@Enumerated(EnumType.STRING)
	private CarType carType;	//차종.
	
	private float startLatitude;
	
	private float startLongitude;
	
	private String startPointName;
	
	private float endLatitude;
	
	private float endLongitude;
	
	private String endPointName;
	
	private int startHour;	//24시간 기준으로 출발지에서 출발시간.(운행시작시간)
	
	private int startMinute;

	private int endHour;	//24시간 기준으로 운행종료시간 
	
	private int endMinute;
	
	@Enumerated(EnumType.STRING)
	private ShuttleScheduleType scheduleType;	
	
	private Date created;
	
	private Date updated;
	
	private boolean active;
	
	@OneToMany(mappedBy = "shuttle", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("idx")
	private List<ShuttleRoute> routes;
	
	@Enumerated(EnumType.STRING)
	private ShuttleStatus status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	public float getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(float startLatitude) {
		this.startLatitude = startLatitude;
	}

	public float getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(float startLongitude) {
		this.startLongitude = startLongitude;
	}

	public String getStartPointName() {
		return startPointName;
	}

	public void setStartPointName(String startPointName) {
		this.startPointName = startPointName;
	}

	public float getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(float endLatitude) {
		this.endLatitude = endLatitude;
	}

	public float getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(float endLongitude) {
		this.endLongitude = endLongitude;
	}

	public String getEndPointName() {
		return endPointName;
	}

	public void setEndPointName(String endPointName) {
		this.endPointName = endPointName;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public List<ShuttleRoute> getRoutes() {
		return routes;
	}

	public void setRoutes(List<ShuttleRoute> routes) {
		this.routes = routes;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public CarType getCarType() {
		return carType;
	}

	public void setCarType(CarType carType) {
		this.carType = carType;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public int getEndMinute() {
		return endMinute;
	}

	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}

	public ShuttleScheduleType getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(ShuttleScheduleType scheduleType) {
		this.scheduleType = scheduleType;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ShuttleStatus getStatus() {
		return status;
	}

	public void setStatus(ShuttleStatus status) {
		this.status = status;
	}
	
	
}
