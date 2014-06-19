package com.shuttlemap.web.identity.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.entity.UserRoles;
import com.shuttlemap.web.identity.IUserDAO;
import com.shuttlemap.web.identity.SocialType;

@Repository("userDAO")
public class UserDAO implements IUserDAO {

	@PersistenceContext(type=PersistenceContextType.TRANSACTION)
	private EntityManager em;
	
	private DateFormat fm = new SimpleDateFormat("yyyy/MM/dd");
	
	
	@Override
	public User load(Long id) {
		try{
			Query query = em.createQuery("SELECT a from User a WHERE a.id = :id");
			query.setParameter("id", id);
			return (User)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public User findByLoginId(String loginId) {
		try{
			Query query = em.createQuery("SELECT a from User a WHERE a.loginId = :loginId");
			query.setParameter("loginId", loginId);
			query.setHint("org.hibernate.cacheable", true);
			return (User)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<User> findByName(String name) {
		try{
			Query query = em.createQuery("SELECT a from User a WHERE a.name = :name");
			query.setParameter("name", name);
			query.setHint("org.hibernate.cacheable", true);
			return query.getResultList();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public User findBySocialId(SocialType type, String socialId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createUser(User user) {
		user.setCreated(new Date());
		user.setUpdated(new Date());
		em.persist(user);
	}

	@Override
	public void addRoleToUser(User user, String roleName) {
		Set<UserRoles> roles = user.getRoles();
		if(roles == null){
			roles = new HashSet<UserRoles>();
			user.setRoles(roles);
		}
		boolean needAdd = true;
		
		for(UserRoles role : roles){
			if(role.getRoleName().equals(roleName)){
				needAdd = false;
				break;
			}
		}
		
		if(needAdd){
			UserRoles newRole = new UserRoles();
			newRole.setUser(user);
			newRole.setRoleName(roleName);
			roles.add(newRole);
		}
	}

	@Override
	public void replaceRoleToUser(User user, String oldRole, String newRoleName) {
		Set<UserRoles> roles = user.getRoles();
		if(roles == null){
			roles = new HashSet<UserRoles>();
			user.setRoles(roles);
		}
		boolean needAdd = true;
		
		Iterator<UserRoles> iter = roles.iterator();
		
		while(iter.hasNext()){
			UserRoles role = iter.next();
			if(role.getRoleName().equals(oldRole))
				iter.remove();
		}
		
		UserRoles newRole = new UserRoles();
		newRole.setUser(user);
		newRole.setRoleName(newRoleName);
		roles.add(newRole);
		
		em.merge(user);
	}

	@Override
	public void updateUser(User user) {
		user.setUpdated(new Date());
		em.merge(user);
	}

	@Override
	public User loadUser(String userId) {
		return em.getReference(User.class, userId);
	}

	@Override
	public List<User> getUsers(String role) {
		String queryString = "select userRoles.user from UserRoles as userRoles where userRoles.roleName = :role";
		Query query = em.createQuery(queryString);
		query.setParameter("role", role);
		return (List<User>)query.getResultList();
	}

	@Override
	public List<User> searchUsers(String keyword, int start, int limits) {
		StringBuffer sql = new StringBuffer("FROM User a WHERE a.active = :active ");
		
		if(keyword != null){
			sql.append("AND a.name like :keyword ");
		}
		sql.append("Order by a.name asc");
		
		Query query = em.createQuery(sql.toString());
		query.setParameter("active", true);
		if(keyword != null)
			query.setParameter("keyword", "%"+keyword+"%");
		query.setFirstResult(start);
		query.setMaxResults(limits);
		
		query.setHint("org.hibernate.cacheable", true);
		
		return query.getResultList();
	}

}
