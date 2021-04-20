package GitKonkuk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;



public class genSnippet {
	
	String sLine = null;
	String tmp[] = new String[5];
	int cntArr[] = {0,0,0,0,0};
	int cnt = 0;
	
	//파일 읽어오기
	public void read(String a) throws IOException {
		String dir = a;
		File f = new File(dir);
		try {
			BufferedReader inFile = new BufferedReader(new FileReader(f));
			try {
				while((sLine = inFile.readLine())!=null) {
						tmp[cnt++] = sLine;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//System.out.println(tmp[0]);
	}
	
	//input과 입력 키워드 분석 함수
	public void func(String str) {
		String[] tmp2 = str.split(" "); //입력 키워드 스페이스로 파싱
		for(int i=0; i<5; i++) {
			if(tmp[i]!=null) {
				for(int j=0; j<tmp[i].length(); j++) {
					for(int k=0; k<tmp2.length; k++) {
						if(tmp[i].contains(tmp2[k])) { //해당 줄에 입력 키워드가 포함되어 있다면
							cntArr[i]++; //해당 줄의 카운트 1
						}
					}
				}	
			}
		}
		
		for(int i=0; i<cntArr.length; i++) { //카운트 수를 비교하기 위함
			for(int j=0; j<cntArr.length; j++) {
				if(cntArr[i]<cntArr[j]) {
					int k = cntArr[i];
					cntArr[i] = cntArr[j];
					cntArr[j] = k; //내림차순으로 순서 변경
					
				}
			}
		}
		
		for(int i=0; i<cntArr.length; i++) {
			System.out.println(tmp[i]);
		}
		
		
	}


}
