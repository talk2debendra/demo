package com.deb.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deb.demo.service.XMLFileService;

@RestController
public class TestController {

	@Autowired
	private XMLFileService xmlFileService;
	
	@GetMapping(produces="application/x-wifi-config")
	public ResponseEntity<Resource> test() throws Exception{
		 return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"android.config\"")
	                .body(xmlFileService.readXML());
	}
}
