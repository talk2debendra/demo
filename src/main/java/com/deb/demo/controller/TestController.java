package com.deb.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deb.demo.service.ProfileGeneratorService;
import com.deb.demo.service.XMLFileService;

@RestController
public class TestController {

	@Autowired
	private Environment environment;
	
	@Autowired
	private XMLFileService xmlFileService;
	
	
	@Autowired
	@Qualifier("androidProfileService")
	private ProfileGeneratorService androidProfileGeneratorService;
	
	
	@Autowired
	@Qualifier("iosProfileService")
	private ProfileGeneratorService iosProfileGeneratorService;
	
	@GetMapping(produces="application/x-wifi-config")
	public ResponseEntity<Resource> testDownload() throws Exception{
		 return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"android.config\"")
	                .body(xmlFileService.readXML());
	}
	
	
	
	@GetMapping(value="/profile",produces="application/x-wifi-config")
	public ResponseEntity<Resource> download(@RequestParam String type) throws Exception{
		
		Map<String,String> xpathNodeValueMap =new HashMap<>();		
		if(environment.getProperty("profile.android.name").equalsIgnoreCase(type)){
			xpathNodeValueMap.put(environment.getProperty("profile.android.xpaths.username"),"debendra");
			xpathNodeValueMap.put(environment.getProperty("profile.android.xpaths.password"),"23d456htyu");
			xpathNodeValueMap.put(environment.getProperty("profile.android.xpaths.machinemanaged"),"false");
			
			
			
			return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"android.config\"")
	                .body(androidProfileGeneratorService.generateProfile(xpathNodeValueMap));
		}if(environment.getProperty("profile.ios.name").equalsIgnoreCase(type)){

			xpathNodeValueMap.put(environment.getProperty("profile.ios.xpaths.username"),"debendra");
			xpathNodeValueMap.put(environment.getProperty("profile.ios.xpaths.password"),"23d456htyu");
			
			return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ios.config\"")
	                .body(iosProfileGeneratorService.generateProfile(xpathNodeValueMap));
		}
		return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"android.config\"")
	                .body(xmlFileService.readXML());
	}
}
