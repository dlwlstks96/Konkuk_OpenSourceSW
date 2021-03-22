package GitKonkuk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {
	
	String sLine = null;
	String[] title = new String[10000];
	String[][] body = new String[10000][10000];
	int titleCnt = 0;
	int bodyCnt1 = 0;
	int bodyCnt2 = 0;
	
	public void readHtmlFile() {
		//String dir = "C:\\Users\\s_dlwlstks96\\Desktop\\자바\\simpleIR\\data";
		String dir = "..\\data";

		File f = new File(dir);
		File[] items = f.listFiles();
		
		for(File file : items) {
			try {
				BufferedReader inFile = new BufferedReader(new FileReader(file));
				try {
					while((sLine = inFile.readLine())!=null) {
						if(sLine.contains("<title>")) {
							sLine = sLine.replace("<title>", "");
							sLine = sLine.replace("</title>", "");
							title[titleCnt] = sLine;
							titleCnt++;
						}else if(sLine.contains("<p>")) {
							sLine = sLine.replace("<p>", "");
							sLine = sLine.replace("</p>", "");
							body[bodyCnt1][bodyCnt2] = sLine;
							bodyCnt2++;
						}
						if(sLine.contains("</div>")) {
							bodyCnt1++;
							bodyCnt2 = 0;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void makeXmlFile() {
		int xmlCnt = 0;
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element eleDocs = doc.createElement("docs");
			doc.appendChild(eleDocs);
			
			for(int i = 0; i<titleCnt; i++) {
				Element eleDoc = doc.createElement("doc");
				eleDocs.appendChild(eleDoc);
				eleDoc.setAttribute("id", ""+xmlCnt+"");
				
				Element eleTitle = doc.createElement("title");
				eleDoc.appendChild(eleTitle);
				eleTitle.appendChild(doc.createTextNode(title[xmlCnt]));
				
				xmlCnt++;
				
				Element eleBody = doc.createElement("body");
				eleDoc.appendChild(eleBody);
				
				
				for (int a = 0; !(body[i][a] == null); a++) {
					eleBody.appendChild(doc.createTextNode(body[i][a]));
				}
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			
			try {
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				
				DOMSource source = new DOMSource(doc);
				try {
					StreamResult result = new StreamResult(new FileOutputStream(new File("C:\\Users\\s_dlwlstks96\\Desktop\\자바\\simpleIR\\src/collection.xml")));
					
					try {
						transformer.transform(source, result);
					} catch (TransformerException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

}
