package com.deb.demo.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import com.deb.demo.service.XMLFileService;

@Service
public class XMLFileServiceImpl implements XMLFileService{

	@Autowired
	private Environment environment;

	@Override
	public Resource readXML() throws Exception {

		try {

			FileInputStream fileInputStream = new FileInputStream(ResourceUtils.getFile(environment.getProperty("profile.android.file")));


			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(fileInputStream);
			XPath xPath = XPathFactory.newInstance().newXPath();

			System.out.println(getNodeValue(xmlDocument,xPath,""));

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

	private String getNodeValue(Document doc, XPath xpath,String exprssion) {
		String name=null;
		try {
			//create XPathExpression object
			XPathExpression expr =xpath.compile("/MgmtTree/Node/Node/Node[2]/Node[1]/Node[3][NodeName='Username']/Value/text()");
			name = (String) expr.evaluate(doc, XPathConstants.STRING);
			System.out.println(name);
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++){
				Node n=nodes.item(i);
				System.out.println(n.getNodeName());
				System.out.println(n.getNodeValue());
				n.setNodeValue("Debendra");

				System.out.println(n.getNodeValue());
			}
			name = (String) expr.evaluate(doc, XPathConstants.STRING);
			System.out.println(name);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return name;
	}








	@Override
	public void readEmployeeXML() {
		try{
			FileInputStream fileInputStream = new FileInputStream(ResourceUtils.getFile(environment.getProperty("employee.file")));


			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(fileInputStream);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "/Employees";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);


			System.out.println(nodeList.getLength());

			System.out.println(getEmployeeNameById(xmlDocument,xPath,4));

			List<String> names = getEmployeeNameWithAge(xmlDocument,xPath, 30);
			System.out.println("Employees with 'age>30' are:" + Arrays.toString(names.toArray()));


			List<String> femaleEmps = getFemaleEmployeesName(xmlDocument,xPath);
			System.out.println("Female Employees names are:" +Arrays.toString(femaleEmps.toArray()));


		} catch (ParserConfigurationException | SAXException | IOException  | XPathExpressionException e ) {
			e.printStackTrace();
		}
	}

	private String getEmployeeNameById(Document doc, XPath xpath, int id) {
		String name = null;
		try {
			XPathExpression expr =xpath.compile("/Employees/Employee[@id='" + id + "']/name/text()");
			name = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return name;
	}


	private List<String> getEmployeeNameWithAge(Document doc, XPath xpath, int age) {
		List<String> list = new ArrayList<>();
		try {
			XPathExpression expr =
					xpath.compile("/Employees/Employee[age>" + age + "]/name/text()");
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++)
				list.add(nodes.item(i).getNodeValue());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return list;
	}

	private List<String> getFemaleEmployeesName(Document doc, XPath xpath) {
		List<String> list = new ArrayList<>();
		try {
			//create XPathExpression object
			XPathExpression expr =xpath.compile("/Employees/Employee[gender='Female']/name/text()");
			//evaluate expression result on XML document
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++)
				list.add(nodes.item(i).getNodeValue());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return list;
	}

}
