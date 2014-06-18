package com.shuttlemap.web.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.google.gson.annotations.Expose;

/**
 * 사용자의 Role 
 * @author itkyung
 *
 */
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="IDENTITY")
@Cacheable
@Entity
@Table(name=UserRoles.TABLE_NAME)
public class UserRoles {
	public static final String TABLE_NAME = "BT_USER_ROLES";
	
	@Id @Column(length=36) @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")		
	@Expose
	private String id;
	
	@ManyToOne
	@JoinColumn(name="user_fk")
	private User user;
	
	private String roleName;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
