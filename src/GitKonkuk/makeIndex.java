package GitKonkuk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class makeIndex {
	
	String sLine = null;
	String xml = null;
	String keyWord[][] = new String[5][1000];
	HashMap<String, ArrayList<String>> indexMap = new HashMap<String, ArrayList<String>>();
	
	
	void makeIndexPost(String dir) {
		
		File f = new File(dir);
		try {
			BufferedReader inFile = new BufferedReader(new FileReader(f));
			try {
				while((sLine = inFile.readLine())!=null) {
					xml = sLine;
					}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String[] tmp = xml.split("body"); //1,3,5,7,9
		
		int xmlCnt = 0;
		
		for(int a = 1; a<10; a+=2) {
			String[] tmp2 = tmp[a].split("#");
			
			for(int b=0; b<tmp2.length; b++) {
				if(tmp2[b].contains(">")) {
					tmp2[b] = tmp2[b].replace(">", "");
				}
				if(tmp2[b].contains(":")) {
					String[] tmp3 = tmp2[b].split(":");
					keyWord[xmlCnt][b] = tmp3[0];
				}
			}
			xmlCnt++;
		}
		
		xmlCnt = 0;
		
		for(int a = 0; a<5; a++) {
			for(int b=0; b<keyWord[a].length; b++) {
				if(keyWord[a][b]!=null) {
					indexMap.put(keyWord[a][b], new ArrayList<String>());	
				}
			}
		}
		
		for(int a = 1; a<10; a+=2) {
			String[] tmp2 = tmp[a].split("#");
			
			for(int b=0; b<tmp2.length; b++) {
				if(tmp2[b].contains(">")) {
					tmp2[b] = tmp2[b].replace(">", "");
				}
				if(tmp2[b].contains(":")) { // : 기준으로 분리
					int cnt = 0;
					String[] tmp3 = tmp2[b].split(":");
					for(int c=0; c<5; c++) { // 모든 문서 조사해서 해당 단어 몇 번 나오는지
						for(int d=0; d<keyWord[c].length; d++) {
							if((keyWord[c][d]!=null)&&keyWord[c][d].equals(tmp3[0])) {
								cnt++;
							}
						}
						
					}
					
					String value = calculateWeight(Integer.parseInt(tmp3[1]), cnt, 5.0);
					if(!value.equals("0.0")) {
					}
					indexMap.get(tmp3[0]).add(Integer.toString(xmlCnt));
					indexMap.get(tmp3[0]).add(value);
				}
			}
			xmlCnt++;
		}
		
		try {
			FileOutputStream fileStream = new FileOutputStream(".\\index.post");
			
			try {
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
				
				objectOutputStream.writeObject(indexMap);
				objectOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			FileInputStream fileStream2 = new FileInputStream(".\\index.post");
			
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(fileStream2);
				
				try {
					Object object = objectInputStream.readObject();
					objectInputStream.close();
					
					@SuppressWarnings("rawtypes")
					HashMap hashMap = (HashMap)object;
					@SuppressWarnings("unchecked")
					Iterator<String> it = hashMap.keySet().iterator();
					
					while(it.hasNext()) {
						String key = it.next();
						@SuppressWarnings("rawtypes")
						ArrayList value = (ArrayList) hashMap.get(key);
						System.out.println(key+" → "+value);
					}
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	String calculateWeight(int tfx, int dfx, double n) {
		double tmp = tfx*(Math.log(n/dfx));
		tmp = tmp*100;
		tmp = (double)Math.round(tmp);
		tmp = ((double)Math.round(tmp)/100);
		String value = Double.toString(tmp);
		return value;
	}

}
