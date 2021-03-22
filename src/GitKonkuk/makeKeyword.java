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

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeKeyword {
	
	String sLine = null;
	String collectXml = null;
	String[] title = new String[10000];
	String[] body = new String[10000];
	int titleCnt = 0;
	int bodyCnt = 0;
	
	void kkmaKeyword() {
		
		//String dir = "C:\\Users\\s_dlwlstks96\\Desktop\\자바\\simpleIR\\src\\collection.xml";
		String dir = ".\\collection.xml";
		File f = new File(dir);
		try {
			BufferedReader inFile = new BufferedReader(new FileReader(f));
			try {
				while((sLine = inFile.readLine())!=null) {
					collectXml = sLine;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		KeywordExtractor ke = new KeywordExtractor();
		
		
		String[] tmp = collectXml.split("body"); //1,3,5,7,9
		
		for(int i=0; i<tmp.length; i+=2) {
			if(!(tmp[i]==null)) {
				if(tmp[i].contains("<title>")) {
					int findTitleIndex = tmp[i].indexOf("<title>");
					int findTitleIndex2 = tmp[i].indexOf("</title>");
					
					title[titleCnt] = tmp[i].substring(findTitleIndex + 11, findTitleIndex2);
					titleCnt++;
				}			
			}
		}
		
		KeywordList kl = null;
		for(int i = 1; i<tmp.length; i+=2) {
			kl = ke.extractKeyword(tmp[i], true);
			
			for(int j = 0; j < kl.size(); j++) {
				Keyword kwrd = kl.get(j);
				if(j!=0) {
					body[bodyCnt] = body[bodyCnt] + kwrd.getString() + ":" + kwrd.getCnt() + "#";
				}else if(j==0) {
					body[bodyCnt] = kwrd.getString() + ":" + kwrd.getCnt() + "#";	
				}
			}
			bodyCnt++;
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
				
				
				eleBody.appendChild(doc.createTextNode(body[i]));
				
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			
			try {
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				
				DOMSource source = new DOMSource(doc);
				try {
					StreamResult result = new StreamResult(new FileOutputStream(new File("C:\\Users\\s_dlwlstks96\\Desktop\\자바\\simpleIR\\src\\index.xml")));
					
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
