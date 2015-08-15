package com.shuttlemap.web.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shuttlemap.web.entity.User;

@Service
public class FileService implements IFileService {

	@Value("${catalina.home}")
	private String shuttleHome;
	
	@Value("${uploadPath}")
	private String uploadPath;
	
	@Override
	public String saveFile(MultipartFile multipart, String prefix) throws Exception {
		
		String filePath = null;
		try{
			
			File orgDir = new File(shuttleHome + uploadPath + File.separator + prefix);
			if(!orgDir.exists()){
				orgDir.mkdirs();
			}
			
			String fileName = makeFileName();
			
			File orgFile = new File(orgDir,fileName);
			multipart.transferTo(orgFile);
			filePath = uploadPath + File.separator + prefix + File.separator + fileName;
			
		}catch(IOException e){
			throw e;
		}
			
		return filePath;
	}

	public String makeFileName(){
		UUID uid = UUID.randomUUID();
		return uid.toString();
	}
}
