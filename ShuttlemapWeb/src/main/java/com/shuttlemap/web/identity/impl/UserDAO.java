package com.shuttlemap.web.identity.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.shuttlemap.web.delegate.CompanyDelegate;
import com.shuttlemap.web.delegate.CompanySearchModel;
import com.shuttlemap.web.entity.Association;
import com.shuttlemap.web.entity.Company;
import com.shuttlemap.web.entity.CompanyType;
import com.shuttlemap.web.entity.CurrentLocation;
import com.shuttlemap.web.entity.Friends;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.entity.UserRoles;
import com.shuttlemap.web.entity.UserType;
import com.shuttlemap.web.identity.IUserDAO;
import com.shuttlemap.web.identity.SocialType;

@Repository("userDAO")
public class UserDAO implements IUserDAO {

	@PersistenceContext(unitName="shuttlemappu",type=PersistenceContextType.TRANSACTION)
	private EntityManager em;
	
	private DateFormat fm = new SimpleDateFormat("yyyy/MM/dd");
	
	@Autowired
	private AssociationDao associationDao;
	
	
	@Override
	public User load(String id) {
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
			Query query = em.createQuery("SELECT a from User a WHERE a.loginId = :loginId and a.active = :active");
			query.setParameter("loginId", loginId);
			query.setParameter("active", true);
			query.setHint("org.hibernate.cacheable", true);
			return (User)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<User> findByName(String name) {
		try{
			Query query = em.createQuery("SELECT a from User a WHERE a.name = :name and a.active = :active");
			query.setParameter("name", name);
			query.setParameter("active", true);
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

	@Override
	public void createFriends(Friends friend) {
		em.persist(friend);
	}

	@Override
	public void updateFriends(Friends friend) {
		em.merge(friend);
	}

	@Override
	public Friends loadFriends(String id) {
		Query query = em.createQuery("FROM " + Friends.class.getName() + " a WHERE a.id = :id");
		query.setParameter("id", id);
		query.setHint("org.hibernate.cacheable", true);
		return (Friends)query.getSingleResult();
	}

	@Override
	public List<Friends> findFriends(User user, Boolean approved, int start, int limits) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("FROM Friends a WHERE a.reject = :reject AND ");
		if(approved != null){
			sqlBuffer.append("a.approved = :approved AND ");
		}
		
		sqlBuffer.append("( a.requester = :requester OR a.receiver = :receiver )");
		sqlBuffer.append("Order by a.created desc");
		
		Query query = em.createQuery(sqlBuffer.toString());
		if(approved != null){
			query.setParameter("approved", approved);
		}
		query.setParameter("reject", false);
		query.setParameter("requester", user);
		query.setParameter("receiver", user);
		
		query.setFirstResult(start);
		query.setMaxResults(limits);
		query.setHint("org.hibernate.cacheable", true);
		
		return query.getResultList();
	}

	@Override
	public List<Friends> findFriends(User requester, User receiver,
			boolean approved, boolean rejected, int start, int limits) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("FROM Friends a WHERE ");
		if(requester != null){
			sqlBuffer.append("a.requester = :user ");
		}else{
			sqlBuffer.append("a.receiver = :user ");
		}
		
		sqlBuffer.append("AND a.approved = :approved ");
		sqlBuffer.append("AND a.reject = :reject ");
		
		Query query = em.createQuery(sqlBuffer.toString());
		if(requester != null){
			query.setParameter("user", requester);
		}else{
			query.setParameter("user", receiver);
		}
		query.setParameter("approved", approved);
		query.setParameter("reject", rejected);
		query.setFirstResult(start);
		query.setMaxResults(limits);
		
		query.setHint("org.hibernate.cacheable", true);
		
		return query.getResultList();
	}

	@Override
	public int countFriends(User user1, User user2, boolean approved) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT count(*) FROM Friends a WHERE a.approved = :approved AND ");
		sqlBuffer.append("a.requester = :requester AND a.receiver = :receiver ");
		
		Query query = em.createQuery(sqlBuffer.toString());
		query.setParameter("approved", approved);
		query.setParameter("requester", user1);
		query.setParameter("receiver", user2);
		query.setHint("org.hibernate.cacheable", true);
		
		int count1 = ((Number)query.getSingleResult()).intValue();
		
		StringBuffer sqlBuffer2 = new StringBuffer();
		sqlBuffer2.append("SELECT count(*) FROM Friends a WHERE a.approved = :approved AND ");
		sqlBuffer2.append("a.requester = :requester AND a.receiver = :receiver ");
		
		Query query2 = em.createQuery(sqlBuffer2.toString());
		query2.setParameter("approved", approved);
		query2.setParameter("requester", user2);
		query2.setParameter("receiver", user1);
		query2.setHint("org.hibernate.cacheable", true);
		
		int count2 = ((Number)query2.getSingleResult()).intValue();
		
		return count1 + count2;
	}

	@Override
	public void createLocation(CurrentLocation location) {
		em.persist(location);
	}

	@Override
	public void updateLocation(CurrentLocation location) {
		em.merge(location);
	}

	@Override
	public CurrentLocation getLocation(User user) {
		Query query = em.createQuery("FROM CurrentLocation a WHERE a.user = :user ");
		query.setParameter("user", user);
		query.setHint("org.hibernate.cacheable", true);
		
		List<CurrentLocation> locations = query.getResultList();
		if(locations.size() == 0){
			return null;
		}
		
		return locations.get(0);
	}

	@Override
	public List<CurrentLocation> listFriendsLocation(User user) {
		List<Friends> friends = findFriends(user, true, 0, 30);
		
		if(friends.size() == 0) {
			return new ArrayList<CurrentLocation>();
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("FROM CurrentLocation a WHERE a.user in (");
		
		for(int i=0; i < friends.size(); i++){
			sql.append(":user" + i);
			if(i < friends.size()-1){
				sql.append(",");
			}
		}
		sql.append(")");
		
		Query query = em.createQuery(sql.toString());
		for(int i=0; i < friends.size(); i++){
			Friends f = friends.get(i);
			User target = f.getRequester().equals(user) ? f.getReceiver() : f.getRequester();
			query.setParameter("user"+i, target);
		}
		query.setHint("org.hibernate.cacheable", true);
		
		return query.getResultList();
	}

	@Override
	public List<User> findUser(String phoneNum) {
		String tremed = phoneNum.replaceAll("-", "");
		
		Query query = em.createQuery("FROM User a WHERE a.phone like :phone ");
		query.setParameter("phone", "%" + tremed);
		query.setHint("org.hibernate.cacheable", true);
		return query.getResultList();
	}

	@Override
	public List<CompanyDelegate> findCompany(CompanySearchModel condition) {
		Query query = makeCompanyQuery(condition,false);
		List<Company> results = query.getResultList();
		
		List<CompanyDelegate> dels = new ArrayList<CompanyDelegate>();
		for(Company company : results){
			CompanyDelegate del = new CompanyDelegate(company);
			dels.add(del);
		}
		return dels;
	}

	@Override
	public int countCompany(CompanySearchModel condition) {
		Query query = makeCompanyQuery(condition, true);
		
		return ((Number)query.getSingleResult()).intValue();
	}
	
	private Query makeCompanyQuery(CompanySearchModel condition,boolean isCount){
		String prefix = "";
		if(isCount){
			prefix = "SELECT count(*) ";
		}
		
		StringBuffer hql = new StringBuffer(prefix + "FROM " + Company.class.getName() + " a WHERE a.active = :active ");
		
		if(condition.getKeyword() != null && !"".equals(condition.getKeyword())){
			hql.append("AND a.name like :keyword ");
		}
		
		
		if(condition.getCompanyType() != null){
			hql.append("AND a.companyType = :companyType ");
		}
			
		if(condition.getAssociationId() != null){
			hql.append("AND a.association = :association ");
		}
		
		if(!isCount){
			hql.append("Order by a.name asc ");
		}
		
		Query query = em.createQuery(hql.toString());
		
		query.setParameter("active", true);
	
		if(condition.getKeyword() != null && !"".equals(condition.getKeyword())){
			query.setParameter("keyword", "%"+condition.getKeyword() + "%");
		}
		
		if(condition.getCompanyType() != null){
			query.setParameter("companyType", CompanyType.valueOf(condition.getCompanyType()));
		}
		
		if(condition.getAssociationId() != null){
			Association assosiation = associationDao.load(condition.getAssociationId());
			query.setParameter("association", assosiation);
		}
			
		query.setHint("org.hibernate.cacheable", true);
		
		if(!isCount){
			query.setFirstResult(condition.getStart());
			query.setMaxResults(condition.getLimit());
		}
		
		return query;
	}

	@Override
	public Company loadCompany(String id) {
		try{
			Query query = em.createQuery("SELECT a from Company a WHERE a.id = :id");
			query.setParameter("id", id);
			return (Company)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public void createCompany(Company company) {
		company.setCreated(new Date());
		em.persist(company);
	}

	@Override
	public void updateCompany(Company company) {
		em.merge(company);
	}

	@Override
	public List<User> findUser(Company company) {
		Query query = em.createQuery("FROM User a WHERE a.active = :active AND a.company = :company");
		query.setParameter("active", true);
		query.setParameter("company", company);
		query.setHint("org.hibernate.cacheable", true);
		return query.getResultList();
	}
	
	

	@Override
	public List<User> findUser(Association association) {
		Query query = em.createQuery("FROM User a WHERE a.active = :active AND a.association = :association");
		query.setParameter("active", true);
		query.setParameter("association", association);
		query.setHint("org.hibernate.cacheable", true);
		return query.getResultList();
	}

	@Override
	public List<User> findUserByName(String name, UserType userType, int start,
			int limits) {
		StringBuffer sql = new StringBuffer("FROM User a WHERE a.active = :active ");
		if(name != null && name.length() > 0){
			sql.append("AND a.name like :keyword ");
		}
		if(userType != null){
			sql.append("AND a.userType = :userType ");
		}
		
		Query query = em.createQuery(sql.toString());
		query.setParameter("active", true);
		if(name != null && name.length() > 0){
			query.setParameter("keyword", "%" + name + "%");
		}
		if(userType != null){
			query.setParameter("userType", userType);
		}
		query.setHint("org.hibernate.cacheable", true);
		query.setFirstResult(start);
		query.setMaxResults(limits);
		
		return query.getResultList();
	}

	@Override
	public int countUser(String name, UserType userType) {
		StringBuffer sql = new StringBuffer("SELECT count(*) FROM User a WHERE a.active = :active ");
		if(name != null && name.length() > 0){
			sql.append("AND a.name like :keyword ");
		}
		if(userType != null){
			sql.append("AND a.userType = :userType ");
		}
		
		Query query = em.createQuery(sql.toString());
		query.setParameter("active", true);
		
		if(name != null && name.length() > 0){
			query.setParameter("keyword", "%" + name + "%");
		}
		if(userType != null){
			query.setParameter("userType", userType);
		}
		query.setHint("org.hibernate.cacheable", true);
		
		Number count = (Number)query.getSingleResult();
		
		return count.intValue();
	}

	@Override
	public List<Company> findCompany(Association association) {
		
		StringBuffer hql = new StringBuffer("FROM " + Company.class.getName() + " a WHERE a.active = :active AND a.association = :association");
		Query query = em.createQuery(hql.toString());
		query.setParameter("association", association);
		query.setHint("org.hibernate.cacheable", true);
		
		return query.getResultList();
	}
	
	
	
}
