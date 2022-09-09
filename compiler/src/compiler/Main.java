package compiler;

import lexico.*;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner("test_code");
		Token tk = null;
		
		do {
			tk = scanner.nextToken();
			if(tk!=null)
				System.out.println(tk);
			
		} while (tk != null);

	}

}
