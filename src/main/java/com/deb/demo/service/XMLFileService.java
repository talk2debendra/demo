package com.deb.demo.service;

import org.springframework.core.io.Resource;

public interface XMLFileService {

	public Resource readXML() throws Exception;
	
	
	public void readEmployeeXML();
	
}
