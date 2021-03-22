package GitKonkuk;

public class kuir {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	
		if(args!=null) {
			if(args[0].compareTo("c")==0) {
				makeCollection collect = new makeCollection();
				collect.readHtmlFile();
				collect.makeXmlFile();
			}else if(args[0].compareTo("k")==0) {
				makeKeyword keyword = new makeKeyword();
				keyword.kkmaKeyword();
				keyword.makeXmlFile();
			}
		}

	}

}
