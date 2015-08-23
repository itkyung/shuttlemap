package com.shuttlemap.web.identity;

import java.util.List;

import com.shuttlemap.web.entity.Association;

public interface IAssociationDAO {
	Association load(String id);
	List<Association> findAll();
	void create(Association association);
	void update(Association association);
	Association getDefault();
}
