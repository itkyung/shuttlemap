package com.shuttlemap.web.common;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadOneCommand {
	private String localPath;
	
	/**
	 * @uml.property  name="anyFile"
	 * @uml.associationEnd  
	 */
	private MultipartFile anyFile;
	/**
	 * @uml.property  name="sourceCode"
	 */
	private byte[] sourceCode;

	/**
	 * @return
	 * @uml.property  name="anyFile"
	 */
	public MultipartFile getAnyFile() {
		return anyFile;
	}

	/**
	 * @param anyFile
	 * @uml.property  name="anyFile"
	 */
	public void setAnyFile(MultipartFile anyFile) {
		this.anyFile = anyFile;
	}

	/**
	 * @return
	 * @uml.property  name="sourceCode"
	 */
	public byte[] getSourceCode() {
		return sourceCode;
	}

	/**
	 * @param sourceCode
	 * @uml.property  name="sourceCode"
	 */
	public void setSourceCode(byte[] sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

}
