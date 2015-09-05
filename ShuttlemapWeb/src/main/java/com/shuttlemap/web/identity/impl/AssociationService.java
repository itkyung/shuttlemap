package com.shuttlemap.web.identity.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shuttlemap.web.Role;
import com.shuttlemap.web.common.CommonUtils;
import com.shuttlemap.web.entity.Association;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.identity.IAssociationDAO;
import com.shuttlemap.web.identity.IAssociationService;
import com.shuttlemap.web.identity.IUserDAO;
import com.sun.media.Log;

@Service
public class AssociationService implements IAssociationService {

	private Logger log = Logger.getLogger(AssociationService.class);
	
	@Autowired
	private IAssociationDAO associationDao;
	
	@Autowired
	private IUserDAO userDao;
	
	@Transactional
	@Override
	public void saveAssociation(Association association) {
		if(association.getId() == null) {
			associationDao.create(association);
		}else{
			associationDao.update(association);
		}

	}

	@Override
	public List<Association> findAll() {
		return associationDao.findAll();
	}

	@Override
	public Association getDefault() {
		return associationDao.getDefault();
	}
	

	@Transactional
	@Override
	public void makeDefault() {
		Association basic = new Association();
		basic.setName("본사협회");
		basic.setPresidentName("이성헌");
		
		basic.setBasicFlag(true);
		basic.setActive(true);
		
		associationDao.create(basic);
	}

	@Override
	public Association load(String id) {
		return associationDao.load(id);
	}

	@Transactional
	@Override
	public void saveAssociation(Association association, String loginId,
			String plainPassword) throws Exception {
		
		saveAssociation(association);
	
		if(loginId != null && loginId.length() > 0){
			User adminUser = userDao.findByLoginId(loginId);
			if(adminUser == null){
				adminUser = new User();
				adminUser.setLoginId(loginId);	
				if(plainPassword != null && plainPassword.length() > 0){
					adminUser.setPassword(new String(CommonUtils.md5(plainPassword)));
				}
				adminUser.setName(association.getName());
				adminUser.setActive(true);
				adminUser.setCreated(new Date());
				adminUser.setUpdated(new Date());
				adminUser.setAssociation(association);
				userDao.addRoleToUser(adminUser,Role.ASSOCIATION_ROLE);
				userDao.createUser(adminUser);
			}else{
				if(adminUser.getAssociation() != null && adminUser.getAssociation().getId().equals(association.getId())){
					adminUser.setAssociation(association);
					adminUser.setName(association.getName());
					if(plainPassword != null && plainPassword.length() > 0){
						adminUser.setPassword(new String(CommonUtils.md5(plainPassword)));
					}
					adminUser.setUpdated(new Date());
					userDao.updateUser(adminUser);
				} else {
					throw new Exception(loginId + "는 이미 등록된 아이디입니다.");
				}
			}
			
		}
		
	}

	

	
}
