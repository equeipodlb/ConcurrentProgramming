package parte2;

import java.io.*;
import java.net.*;

import parte2.LockBakery.LockBakery;
import parte2.LockBakery.MiEntero;

public class Emisor extends Thread {

    Pelicula p;
    int port;
  
    public Emisor(Pelicula p, int port) {
        this.p = p;
        this.port = port;

    }

    public void run() {
        try {
            
            ServerSocket ss = new ServerSocket(port);
            Socket s = ss.accept();

            ObjectInputStream finS = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream foutS = new ObjectOutputStream(s.getOutputStream());

            foutS.writeObject(p);
            foutS.flush();

            finS.readObject().toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
