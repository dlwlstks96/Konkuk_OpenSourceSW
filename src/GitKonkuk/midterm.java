package GitKonkuk;

import java.io.IOException;

public class midterm {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		if(args[0].compareTo("-f")==0&&args[2].compareTo("-q")==0) {
			genSnippet a = new genSnippet();
			a.read(args[1]);
			a.func(args[3]);
		}

	}

}
