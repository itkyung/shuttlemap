package com.shuttlemap.web.identity;

import java.util.List;

import com.shuttlemap.web.entity.Association;

public interface IAssociationService {
	
	void saveAssociation(Association association);
	
	void saveAssociation(Association association,String userId,String password);
	
	List<Association> findAll();
	
	Association getDefault();
	
	void makeDefault();
	
	Association load(String id);
}
