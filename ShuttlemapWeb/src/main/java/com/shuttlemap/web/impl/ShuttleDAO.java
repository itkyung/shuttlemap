package com.shuttlemap.web.impl;

import java.util.Date;
import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.shuttlemap.web.IShuttleDAO;
import com.shuttlemap.web.entity.BookmarkShuttle;
import com.shuttlemap.web.entity.Company;
import com.shuttlemap.web.entity.Shuttle;
import com.shuttlemap.web.entity.ShuttleDrivingInfo;
import com.shuttlemap.web.entity.ShuttleRoute;
import com.shuttlemap.web.entity.User;

@Repository
public class ShuttleDAO implements IShuttleDAO {
	@PersistenceContext(type=PersistenceContextType.TRANSACTION)
	private EntityManager em;
	
	@Override
	public List<Shuttle> searchShuttle(String keyword, Company company, int start, int limit) {
		Query query = makeQuery(keyword, company, start, limit, false);
		
		return query.getResultList();
	}

	@Override
	public int countShuttle(String keyword,Company company) {
		Query query = makeQuery(keyword, company,0, 0, true);
		
		return ((Number)query.getSingleResult()).intValue();
	}

	
	private Query makeQuery(String keyword,Company company,int start,int limit,boolean countQuery){
		StringBuffer sql = new StringBuffer();
		
		if(countQuery){
			sql.append("SELECT count(*) ");
		}
		
		sql.append("FROM " + Shuttle.class.getName() + " a WHERE a.active = :active ");
		
		if(keyword != null){
			sql.append("AND (a.name like :name OR a.carNo like :carNo OR a.company.name like :companyName) ");
		}
		
		if(company != null) {
			sql.append("AND a.company = :company ");
		}
		
		Query query = em.createQuery(sql.toString());
		query.setParameter("active", true);
		if(keyword != null){
			query.setParameter("name", "%" + keyword + "%");
			query.setParameter("carNo", "%" + keyword + "%");
			query.setParameter("companyName", "%" + keyword + "%");
		}
		if(company != null) {
			query.setParameter("company", company);
		}
		if(!countQuery){
			query.setFirstResult(start);
			query.setMaxResults(limit);
		}
		query.setHint("org.hibernate.cacheable", true);
		
		return query;
	}
	
	
	@Override
	public void createShuttle(Shuttle shuttle) {
		shuttle.setCreated(new Date());
		shuttle.setUpdated(new Date());
		em.persist(shuttle);
	}

	@Override
	public void updateShuttle(Shuttle shuttle) {
		shuttle.setUpdated(new Date());
		em.merge(shuttle);
	}

	@Override
	public Shuttle loadShuttle(String id) {
		Query query = em.createQuery("FROM " + Shuttle.class.getName() + " a WHERE a.id = :id");
		query.setParameter("id", id);
		
		return (Shuttle)query.getSingleResult();
	}

	@Override
	public void addBookmark(User owner, Shuttle shuttle, String markName) {
		boolean exist = isAlreadyBookmakr(owner,shuttle);
		if(!exist){
			BookmarkShuttle bs = new BookmarkShuttle();
			bs.setActive(true);
			bs.setBookmarkName(markName);
			bs.setShuttle(shuttle);
			bs.setUser(owner);
			bs.setCreated(new Date());
			em.persist(bs);
		}
	}

	@Override
	public void removeBookmakr(String id) {
		Query query = em.createQuery("FROM " + BookmarkShuttle.class.getName() + " a WHERE a.id = :id");
		query.setParameter("id", id);
		
		BookmarkShuttle bs = (BookmarkShuttle)query.getSingleResult();
		em.remove(bs);
	}

	@Override
	public List<BookmarkShuttle> listBookmark(User owner) {
		Query query = em.createQuery("FROM " + BookmarkShuttle.class.getName() + " a WHERE a.active = :active");
		query.setParameter("active", true);
		query.setHint("org.hibernate.cacheable", true);
		
		return query.getResultList();
	}

	public boolean isAlreadyBookmakr(User owner, Shuttle shuttle){
		Query query = em.createQuery("SELECT count(*) FROM " + BookmarkShuttle.class.getName() + " a WHERE a.user = :user AND a.shuttle = :shuttle");
		query.setParameter("user", owner);
		query.setParameter("shuttle", shuttle);
		query.setHint("org.hibernate.cacheable", true);
		
		int count = ((Number)query.getSingleResult()).intValue();
		return count == 0 ? false : true;
	}

	@Override
	public List<Shuttle> findShuttle(Company company) {
		Query query = em.createQuery("FROM " + Shuttle.class.getName() + " a WHERE a.company = :company AND a.active = :active Order by a.name ASC");
		query.setParameter("company", company);
		query.setParameter("active", true);
		query.setHint("org.hibernate.cacheable", true);
		return query.getResultList();
	}

	@Override
	public void createRoute(ShuttleRoute route) {
		route.setCreated(new Date());
		em.persist(route);
	}

	@Override
	public void updateRoute(ShuttleRoute route) {
		em.merge(route);
	}

	@Override
	public List<Shuttle> findByDriver(User driver) {
		Query query = em.createQuery("FROM " + Shuttle.class.getName() + " a WHERE a.driver = :driver AND a.active = :active");
		query.setParameter("driver", driver);
		query.setParameter("active", true);
		query.setHint("org.hibernate.cacheable", true);
		return query.getResultList();
	}

	@Override
	public ShuttleDrivingInfo findCurrentDrivingInfo(Shuttle shuttle) {
		Query query = em.createQuery("FROM " + ShuttleDrivingInfo.class.getName() + " a WHERE a.shuttle = :shuttle AND a.active = :active");
		query.setParameter("shuttle", shuttle);
		query.setParameter("active", true);
		query.setHint("org.hibernate.cacheable", true);
		
		List<ShuttleDrivingInfo> infos = query.getResultList();
		return infos.size() == 0 ? null : infos.get(0);
	}

	@Override
	public void createDrivingInfo(ShuttleDrivingInfo info) {
		em.persist(info);
	}

	@Override
	public void updateDrivingInfo(ShuttleDrivingInfo info) {
		em.merge(info);
	}

	@Override
	public ShuttleRoute loadRoute(String id) {
		Query query = em.createQuery("FROM " + ShuttleRoute.class.getName() + " a WHERE a.id = :id");
		query.setParameter("id", id);
		
		return (ShuttleRoute)query.getSingleResult();
	}
	
	
}
