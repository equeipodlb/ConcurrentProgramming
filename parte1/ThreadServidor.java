package parte1;

import java.io.*;
import java.net.Socket;

public class ThreadServidor extends Thread {

	Socket s;
	Mensaje miMensaje = new Mensaje("Hola CLIENTE");

	public ThreadServidor(Socket s) {
		this.s = s;
	}

	public void run() {
		System.out.println("Connected");
		ObjectInputStream finS;
		try {
			finS = new ObjectInputStream(s.getInputStream());// Creamos el canal de entrada del servidor
			Mensaje m = (Mensaje) finS.readObject(); // Leemos el mensaje que nos envía el cliente
			System.out.println("El Cliente quiere acceder al contenido del fichero " + m.toString());
			ObjectOutputStream foutS = new ObjectOutputStream(s.getOutputStream()); // Creamos el canal de salida del  servidor
			String line;
			BufferedReader in = new BufferedReader(new FileReader(m.toString()));
			line = in.readLine();
			Mensaje l = new Mensaje(line);
			while (line != null) {
				l = new Mensaje(line);
				foutS.writeObject(l);
				foutS.flush();
				line = in.readLine();
			}

			foutS.writeObject(l);
			foutS.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
