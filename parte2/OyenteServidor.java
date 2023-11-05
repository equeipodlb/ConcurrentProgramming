package parte2;

import java.io.*;
import java.net.*;

import parte2.LectoresEscritoresUsuario.EscritorUser;
import parte2.LectoresEscritoresUsuario.ReaderUsuario;
import parte2.LockBakery.LockBakery;
import parte2.LockBakery.MiEntero;
import parte2.Mensajes.*;

public class OyenteServidor extends Thread {
	Socket s;
	Usuario usr;
	ObjectOutputStream foutC;
	ObjectInputStream finC;
	int contBakery;
	LockBakery l;
	boolean recibido;
	String clientId;
	

	public String getClientId() {
		return this.clientId;
	}

	
	public OyenteServidor(Socket s, Usuario usr, int cont, LockBakery l) {
		this.s = s;
		this.contBakery = cont;
		this.usr = usr;
		this.l = l;
		this.recibido = false;
		l.reservarSitio(new MiEntero(contBakery));
		
	}

	public void setRecibido() {
		this.recibido = false;
	}

	public boolean getRecibido() {
		return this.recibido;
	}

	public void run() {
		try {
			// Comienza creando los canales de flujos y mandando el primer mensaje para
			// solitar conectarse al servidor
			foutC = new ObjectOutputStream(s.getOutputStream());
			foutC.writeObject(new MensajeConexion(usr.getId()));
			foutC.flush();
			finC = new ObjectInputStream(s.getInputStream());

			while (true) {// ahora se pone a la espera de mensajes
				Mensaje m = (Mensaje) finC.readObject();
				
				switch (m.getTipo()) {
					case (1): // Si el mensaje es de tipo confirmar conexion: MENSAJECONFCONEX
						l.bakeryEnter(contBakery);
						System.out.println(m.toString()); // se muestra que la conexión se ha realizado correctamente
						l.bakeryRelease(contBakery);
						this.clientId = m.getClientId();
						usr.setClient(clientId);
						break;
					case (3): //Mensaje que manda el servidor para confirmar que ha recibido la pelicula: MENSAJECONFCONEX
						l.bakeryEnter(contBakery);
						System.out.println(m.toString());
						l.bakeryRelease(contBakery);
						break;
					case (5)://El servidor me manda toda la informacion disponible: MENSAJECONFINFODISP
						l.bakeryEnter(contBakery);
						System.out.println(m.toString());
						l.bakeryRelease(contBakery);
						break;
					case (7): // El servidor me dice que tengo que emitir una pelicula a otro cliente: MENSAJEEMITIRPELI
						//lanzo un hilo emisor de la pelicula
						String receptor = m.getDestino();
						String titulo = m.getTituloPelicula();
						String emisor = m.getOrigen();
						int puerto = m.getPuerto();		
						//busco en la informacion de mi usuario la pelicula que me piden	
						ReaderUsuario thLectorUser = new ReaderUsuario(usr.getInfo(),titulo);
						thLectorUser.start();
						try {
							thLectorUser.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}		
						Emisor thEmisor = new Emisor(thLectorUser.getBuscada(), puerto);
						thEmisor.start();
						//Digo que estoy preparado para la emision
						foutC.writeObject(new MensajePreparadoEmision(emisor, receptor, titulo, puerto));
						foutC.flush();
						break;
					case (8)://El usuario que me va a enviar la pelicula que he solicitado ya esta preparado: MENSAJEPREPARADOEMISION
						//Lanzo un hilo receptor
						int puerto1 = m.getPuerto();
						Receptor thReceptor = new Receptor(puerto1);
						thReceptor.start();
						thReceptor.join();
						//Escribo en la informacion de mi usuario la pelicula que he recibido
						EscritorUser thEscritorUser2 = new EscritorUser(usr.getInfo(),thReceptor.getPelicula());
						thEscritorUser2.start();
						try {
							thEscritorUser2.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}		
	
						ReaderUsuario thLectorUser2 = new ReaderUsuario(usr.getInfo(),thReceptor.getPelicula().getTitulo());
						thLectorUser2.start();
						try {
							thLectorUser2.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}		
						l.bakeryEnter(contBakery);
						System.out.println(thLectorUser2.getBuscada().toString());
						l.bakeryRelease(contBakery);
						recibido = true;
						foutC.writeObject(new MensajePeliculaRecibida(m.getOrigen(), m.getDestino(),
								thReceptor.getPelicula(), usr.getId()));
						foutC.flush();
						break;
					case(11): //MENSAJECONFIRMARDESCONEXION
						l.bakeryEnter(contBakery);
						System.out.println(m.toString());
						l.bakeryRelease(contBakery);
						break;
					case(12): //MENSAJEPELICULAREPETIDA
						l.bakeryEnter(contBakery);
						System.out.println(m.toString());
						l.bakeryRelease(contBakery);
						break;
					case(13)://MENSAJEPELINODISP
						l.bakeryEnter(contBakery);
						System.out.println(m.toString());
						l.bakeryRelease(contBakery);
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
