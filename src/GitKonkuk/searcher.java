package GitKonkuk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class searcher {
<<<<<<< HEAD
   
   String[] kwrdList = new String[1000];
   int queryCnt = 0;
   String[] keyList = new String[1000];
   int keyCnt = 0;
   @SuppressWarnings("rawtypes")
   ArrayList[] valueList = new ArrayList[1000];
   double querySim[] = new double[5];
   double cosineSim[] = new double[5];
   double doubleArr[] = new double[5];
   String sLine = null;
   String collectXml = null;
   String[] title = new String[100];
   int titleCnt = 0;
   
   public void kkmaQuery(String query) {
      KeywordExtractor ke = new KeywordExtractor();
      KeywordList kl = ke.extractKeyword(query, true);
      for(int i=0; i<kl.size(); i++) {
         Keyword kwrd = kl.get(i);
         //System.out.println(kwrd.getString()+"\t"+kwrd.getCnt());
         kwrdList[i] = kwrd.getString();
         //queryCnt++;
      }
   }
   
   @SuppressWarnings("rawtypes")
   public void readPost(String dir) {
      
      try {
         FileInputStream fileStream = new FileInputStream(dir);
         try {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
            try {
               Object object = objectInputStream.readObject();
               objectInputStream.close();
               
               HashMap hashMap = (HashMap)object;
               @SuppressWarnings("unchecked")
               Iterator<String> it = hashMap.keySet().iterator();
               
               while(it.hasNext()) {
                  keyList[keyCnt] = it.next();
                  valueList[keyCnt] = (ArrayList)hashMap.get(keyList[keyCnt]);
                  //System.out.println(valueList[keyCnt].get(1));
                  keyCnt++;
                  
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
      
      CalcSim();
      
   }

   public void InnerProduct(){
      for(int i=0; i<kwrdList.length; i++) {
         if(kwrdList[i]!=null) {
            for(int j=0; j<keyList.length; j++) {
               if(keyList[j]!=null) {
                  if(kwrdList[i].equals(keyList[j])) {
                     for(int k=0; k<valueList[j].size(); k+=2) {
                        if(valueList[k]!=null) {
                           querySim[(Integer.parseInt(String.valueOf(valueList[j].get(k))))] += Double.parseDouble(String.valueOf(valueList[j].get(k+1)));   
                        }
                     }
                  }   
               }
            }   
         }
      }

      for(int i=0; i<querySim.length; i++) {
         System.out.println(querySim[i]);   
      }
   }

   
   
   public void CalcSim() {

      InnerProduct();
      
      System.out.println("\n=======6주차 실습 출력 결과========");

      
      for(int i=0; i<kwrdList.length; i++) {
          if(kwrdList[i]!=null) {
             for(int j=0; j<keyList.length; j++) {
                if(keyList[j]!=null) {
                   if(kwrdList[i].equals(keyList[j])) {
                      for(int k=0; k<valueList[j].size(); k+=2) {
                         if(valueList[k]!=null) {
                            doubleArr[(Integer.parseInt(String.valueOf(valueList[j].get(k))))] += Math.pow(Double.parseDouble(String.valueOf(valueList[j].get(k+1))), 2);   
                         }
                      }
                   }   
                }
             }   
          }
       }
      
      for(int i=0; i<5; i++) {
          cosineSim[i] = (querySim[i])/(2*Math.sqrt(doubleArr[i]));
      }

      printResult2();
      
      
      
   }
   
   //기존 Calcsim()에서 출력 기능 분리 , 5주차 실습의 유사도 출력
   public void printResult1() {
   	
       System.out.println("===============");
       
       File f = new File(".//collection.xml");
       try {
          BufferedReader inFile = new BufferedReader(new FileReader(f));
          try {
             while((sLine = inFile.readLine())!=null){
                collectXml = sLine;
             }
          } catch (IOException e) {
             e.printStackTrace();
          }
       } catch (FileNotFoundException e) {
          e.printStackTrace();
       }
       
       String[] tmp = collectXml.split("body");
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
       
       String[] rank = new String[5];
       
       for(int i=0; i<rank.length; i++) {
          rank[i] = title[i];
       }
       
       /*for(int i=0; i<rank.length; i++) {
          for(int j=i; j<rank.length; j++) {
             if(rank[i].compareTo(rank[j])==-1) {
                String s = rank[i];
                rank[i] = rank[j];
                rank[j] = s;
             }
          }
       }*/
       
       for(int i=0; i<rank.length; i++) {
          for(int j=0; j<rank.length; j++) {
             if(querySim[i]>querySim[j]) {
                double k = querySim[i];
                querySim[i] = querySim[j];
                querySim[j] = k;
                String s = title[i];
                title[i] = title[j];
                title[j] = s;
             }
          }
       }
       
       for(int i=0; i<rank.length; i++) {
          rank[i] = title[i];
       }
       
       for(int i=0; i<3; i++) {
          if(rank[i]!=null) {
             System.out.println(rank[i]);   
          }
       }
 	  
   }
   
   //코사인 유사도 기반 출력
   public void printResult2() {
	   	
       System.out.println("===============");
       
       File f = new File(".//collection.xml");
       try {
          BufferedReader inFile = new BufferedReader(new FileReader(f));
          try {
             while((sLine = inFile.readLine())!=null){
                collectXml = sLine;
             }
          } catch (IOException e) {
             e.printStackTrace();
          }
       } catch (FileNotFoundException e) {
          e.printStackTrace();
       }
       
       String[] tmp = collectXml.split("body");
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
       
       String[] rank = new String[5];
       
       for(int i=0; i<rank.length; i++) {
          rank[i] = title[i];
       }
       
       /*for(int i=0; i<rank.length; i++) {
          for(int j=i; j<rank.length; j++) {
             if(rank[i].compareTo(rank[j])==-1) {
                String s = rank[i];
                rank[i] = rank[j];
                rank[j] = s;
             }
          }
       }*/
       
       for(int i=0; i<rank.length; i++) {
          for(int j=0; j<rank.length; j++) {
        	  if(cosineSim[i]!=Double.NaN&&cosineSim[j]!=Double.NaN) {
                  if(cosineSim[i]>cosineSim[j]) {
                      double k = cosineSim[i];
                      cosineSim[i] = cosineSim[j];
                      cosineSim[j] = k;
                      String s = title[i];
                      title[i] = title[j];
                      title[j] = s;
                   }  
        	  }
          }
       }
       
       for(int i=0; i<rank.length; i++) {
          rank[i] = title[i];
       }
       
       //isNaN 메소드로 NaN 값 출력 제외
       for(int i=0; i<3; i++) {
          if(rank[i]!=null&&(!Double.isNaN(cosineSim[i]))) {
             System.out.println(rank[i]);   
          }
       } 	  
   }

}
