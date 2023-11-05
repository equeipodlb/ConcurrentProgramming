package parte1;

import java.io.*;
import java.net.*;

public class Servidor {

	public static void main(String[] args) {
			ServerSocket ss;
			try {
				ss = new ServerSocket(999);
				
				while(true) {
					Socket s = ss.accept();
					ThreadServidor th = new ThreadServidor(s);
					th.start();
				}
				
			} catch (Exception e) {e.printStackTrace();} 
			
	}

}
