package com.shuttlemap.web.common;

import org.springframework.web.multipart.MultipartFile;

import com.shuttlemap.web.entity.User;

public interface IFileService {
	
	String saveFile(MultipartFile multipart,String prefix) throws Exception;
	
}
