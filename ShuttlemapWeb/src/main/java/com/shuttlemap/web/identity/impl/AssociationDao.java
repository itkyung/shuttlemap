package com.shuttlemap.web.identity.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.shuttlemap.web.entity.Association;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.identity.IAssociationDAO;

@Repository
public class AssociationDao implements IAssociationDAO {
	
	@PersistenceContext(type=PersistenceContextType.TRANSACTION)
	private EntityManager em;
	
	@Override
	public Association load(String id) {
		try{
			Query query = em.createQuery("SELECT a from Association a WHERE a.id = :id");
			query.setParameter("id", id);
			return (Association)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<Association> findAll() {
		Query query = em.createQuery("SELECT a from Association a");
		return query.getResultList();
	}

	@Override
	public void create(Association association) {
		association.setCreated(new Date());
		em.persist(association);
	}

	@Override
	public void update(Association association) {
		em.merge(association);
	}

	@Override
	public Association getDefault() {
		try{
			Query query = em.createQuery("SELECT a from Association a WHERE a.basicFlag = :basicFlag");
			query.setParameter("basicFlag", true);
			return (Association)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

}
