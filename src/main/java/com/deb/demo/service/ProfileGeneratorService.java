package com.deb.demo.service;

import java.util.Map;

import org.springframework.core.io.Resource;

public interface ProfileGeneratorService {

	public Resource generateProfile(Map<String,String> nodeValue) throws Exception;
}
