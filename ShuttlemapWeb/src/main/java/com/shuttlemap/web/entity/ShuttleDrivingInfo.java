package com.shuttlemap.web.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.google.gson.annotations.Expose;

/**
 * 해당 셔틀의 현재 운행 상태 정보.
 * @author itkyung
 *
 */
@Entity
@Table(name=ShuttleDrivingInfo.TABLE_NAME)
public class ShuttleDrivingInfo {
	public static final String TABLE_NAME = "SM_DRIVING_INFO";
	
	@Expose
	@Id @Column(length=36) @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")	
	private String id;
	
	@ManyToOne
	@JoinColumn(name="shuttle_fk")
	private Shuttle shuttle;
	
	@ManyToOne
	@JoinColumn(name="route_fk")
	private ShuttleRoute lastArrivedRoute;	//마지막에 도착한 가장 가까운 위치 
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateDate;
	
	private boolean active;	//운행중인 상태가 종료되면 inactive로 바뀐다.

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Shuttle getShuttle() {
		return shuttle;
	}

	public void setShuttle(Shuttle shuttle) {
		this.shuttle = shuttle;
	}

	public ShuttleRoute getLastArrivedRoute() {
		return lastArrivedRoute;
	}

	public void setLastArrivedRoute(ShuttleRoute lastArrivedRoute) {
		this.lastArrivedRoute = lastArrivedRoute;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
}
