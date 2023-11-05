package parte2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Receptor extends Thread {

    public Pelicula p;
    int port;
  
    
    public Receptor(int port){
        this.port = port;
       
    }

    public Pelicula getPelicula(){
        return this.p;
    }

    public void run(){
        try {
            Socket sc = new Socket(InetAddress.getLocalHost().getHostAddress(), port);
            ObjectOutputStream foutC = new ObjectOutputStream(sc.getOutputStream());
			ObjectInputStream finC = new ObjectInputStream(sc.getInputStream());

            this.p = (Pelicula)finC.readObject();

            foutC.writeObject("Pelicula " + p.getTitulo() + " enviada");
            foutC.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
