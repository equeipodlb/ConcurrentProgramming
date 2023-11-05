package parte1;

import java.io.*;
import java.net.*;

public class Cliente {

	public static void main(String[] args) {
		Mensaje miMensaje = new Mensaje("C:\\Users\\Usuario\\Desktop\\Mis cosas\\Kelogger\\dat.txt");
		try {
			Socket sc = new Socket(InetAddress.getLocalHost().getHostAddress(),999);
			ObjectOutputStream foutC = new ObjectOutputStream(sc.getOutputStream());
			foutC.writeObject(miMensaje);
			foutC.flush();
			ObjectInputStream finC = new ObjectInputStream(sc.getInputStream());
			Mensaje m = (Mensaje) finC.readObject();
			System.out.println(m.toString());
			
		} catch (UnknownHostException e) {e.printStackTrace();} 
		  catch (IOException e) {e.printStackTrace();} 
		  catch (ClassNotFoundException e) {e.printStackTrace();}

	}

}
