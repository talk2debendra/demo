package com.deb.demo.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.deb.demo.service.ProfileGeneratorService;

@Service
public class AndroidProfileGeneratorServiceImpl implements ProfileGeneratorService{

	@Autowired
	private Environment environment;

	@Override
	public Resource generateProfile(Map<String, String> nodeValue) throws Exception {
		try {

			FileInputStream fileInputStream = new FileInputStream(ResourceUtils.getFile(environment.getProperty("profile.android.file")));


			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(fileInputStream);
			XPath xPath = XPathFactory.newInstance().newXPath();

			updateNodeValue(xmlDocument,xPath,nodeValue);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDocument);
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			StreamResult result=new StreamResult(bos);
			transformer.transform(source, result);
			byte []array=bos.toByteArray();
			return new ByteArrayResource(array);			

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void updateNodeValue(Document doc, XPath xpath,Map<String,String> nodeValue) {
		nodeValue.forEach((k, v) -> {
			try {
				XPathExpression expression = xpath.compile(k);
				NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++){
					Node n=nodes.item(i);
					System.out.println(n.getNodeName());
					System.out.println(n.getNodeValue());
					n.setNodeValue(v);
					System.out.println(n.getNodeValue());
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}				
		});
	}


}
