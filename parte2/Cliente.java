package parte2;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;

import parte2.LectoresEscritoresUsuario.BufferUsuario;
import parte2.LectoresEscritoresUsuario.EscritorUser;
import parte2.LockBakery.LockBakery;
import parte2.LockBakery.MiEntero;
import parte2.Mensajes.MensajeDesconectar;
import parte2.Mensajes.MensajeEnviarPelicula;
import parte2.Mensajes.MensajeInfoDisp;
import parte2.Mensajes.MensajePedirPeli;

public class Cliente {

	//Funcion para leer un archivo input
	public static List<Pelicula> leerArchivo(String rutaArchivo) throws IOException {
		ArrayList<Pelicula> info = new ArrayList<Pelicula>();
		try{
			Scanner scanner = new Scanner(new File(rutaArchivo));
			List<String> datos = new ArrayList<String>(0);
			for (int i = 0; i < 6; ++i) {
				datos.add(null);
			}
			int i = 0;
			while (scanner.hasNextLine()) {
				String linea = scanner.nextLine();
				if (linea.isEmpty()) {
					ArrayList<String> aux = new ArrayList<String>(Arrays.asList(datos.get(3).split(",")));
					Pelicula pelicula = new Pelicula(datos.get(0), datos.get(1), Double.parseDouble(datos.get(2)), aux,datos.get(4), datos.get(5));
					info.add(pelicula);
					i = 0;
				} else {
					datos.set(i, linea.split(": ")[1]);
					i++;
				}
			}
			scanner.close();
			return info;
		}
		catch(Exception e){
			return null;
		}
	}
	//Funcion para mostrar el menu de opciones del usuario
	public static int mostrarMenu(LockBakery l, OyenteServidor th, Usuario usr,  Semaphore eUser, Semaphore mReadersUser,Semaphore mWritersUser) {
		
		Scanner scanner = new Scanner(System.in);
		int opcion = 0;
		boolean valido = false;
		while (!valido) {
			l.bakeryEnter(0);
			if (th.getRecibido()) {
				System.out.println("Se ha recibido la pelicula que has solicitado");
				th.setRecibido();
			}

			System.out.println("Seleccione una opción:");
			System.out.println("1. Suministrar información mediante fichero");
			System.out.println("2. Suministrar información mediante teclado");
			System.out.println("3. Pedir lista de informacion disponible");
			System.out.println("4. Pedir una pelicula");
			System.out.println("5. Salir");
			System.out.println("Opción: ");
			String input = scanner.nextLine();
			try {
				opcion = Integer.parseInt(input);
				if (opcion >= 1 && opcion <= 5) {
					valido = true;
				} else {
					System.out.println("Opción inválida. Intente nuevamente.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Opción inválida. Intente nuevamente.");
			}
			l.bakeryRelease(0);
		}
		

		return opcion;
	}

	//Funcion para leer una pelicula por teclado
	public static Pelicula leePeliculaConsola() {
		try{
		Scanner scanner = new Scanner(System.in);
	
		System.out.println("Introduce el título de la película:");
		String titulo = scanner.nextLine();
	
		System.out.println("Introduce la duración de la película en minutos:");
		int duracion = scanner.nextInt();
		int horas = duracion/60;
		int minutos = duracion%60;
		String d = horas + "h " + minutos + "m";
		scanner.nextLine(); // Consumimos el salto de línea pendiente
	
		System.out.println("Introduce la puntuación de la película (del 0 al 10):");
		double puntuacion = scanner.nextDouble();
		scanner.nextLine(); // Consumimos el salto de línea pendiente
	
		System.out.println("Introduce los actores principales separados por comas:");
		String actores = scanner.nextLine();
		String[] actoresPrincipales = actores.split(",");
		List<String> act = new ArrayList<String>();
		for(int i = 0; i < actoresPrincipales.length; ++i){
			act.add(actoresPrincipales[i]);
		}
	
		System.out.println("Introduce la fecha de lanzamiento de la película:");
		String fechaLanzamiento = scanner.nextLine();
	
		System.out.println("Introduce una breve sinopsis de la película:");
		String sinopsis = scanner.nextLine();
		return new Pelicula(titulo, d, puntuacion, act, fechaLanzamiento, sinopsis);
		}
		catch(Exception e){
			System.out.println("Introduce la pelicula en el formato que se indica por favor");
			
		}
		return leePeliculaConsola();
	}

	public static void ejecutarOp(Usuario usr, int op, Scanner keyboard, OyenteServidor th, String userId, LockBakery l, Semaphore eUser, Semaphore mReadersUser,Semaphore mWritersUser)
			throws IOException {
		boolean mostrar = true;
		switch (op) {
			case (1):// Mandar información al servidor mediante un fichero
				System.out.println("Introduzca el fichero con la información que tiene para poder compartirla con el resto de clientes");
				String file = keyboard.nextLine();
				String path = "parte2\\Input\\" + file;
				List<Pelicula> info = leerArchivo(path);
				if(info == null){
					l.bakeryEnter(0);
					System.out.println("El fichero que has escrito no se encuentra en la ruta del proyecto");
					l.bakeryRelease(0);
				}
				else{
					for (Pelicula p : info) {
						// Actualizamos la información del usuario para que concuerde con las peliculas que tiene
						EscritorUser thEscritorUser = new EscritorUser(usr.getInfo(),p);
                        thEscritorUser.start();
                        try {
							thEscritorUser.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						th.foutC.writeObject(new MensajeEnviarPelicula(p, userId));
						th.foutC.flush();
					}
				}
				break;

			case(2)://Mandar informacion al servidor leyendo desde el teclado
				l.bakeryEnter(0);
				Pelicula p = leePeliculaConsola();
				l.bakeryRelease(0);
				// Actualizamos la información del usuario para que concuerde con las peliculas que tiene
				EscritorUser thEscritorUser = new EscritorUser(usr.getInfo(),p);
                thEscritorUser.start();
				try {
					thEscritorUser.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				th.foutC.writeObject(new MensajeEnviarPelicula(p, userId));
				th.foutC.flush();
				break;

			case (3): // Pedir lista de información disponible
				th.foutC.writeObject(new MensajeInfoDisp(userId));
				th.foutC.flush();
				break;

			case (4): // Pedir una pelicula
				System.out.println("Introduzca el titulo de la pelicula que necesita");
				String titulo = keyboard.nextLine();
				th.foutC.writeObject(new MensajePedirPeli(titulo, th.getClientId(), userId));
				th.foutC.flush();
				break;
			case (5): //Desconexion
				th.foutC.writeObject(new MensajeDesconectar(userId, usr.getCliente()));
				th.foutC.flush();
				mostrar = false;
				break;
		}
		try {
			Thread.sleep(1000);

		} catch (Exception e) {
			
		}
		if(mostrar){
			op = mostrarMenu(l, th, usr, eUser, mReadersUser, mWritersUser);
			ejecutarOp(usr, op, keyboard, th, userId, l, eUser, mReadersUser, mWritersUser);
		}
		
	}

	public static void main(String[] args) throws IOException {

		LockBakery lockConsole = new LockBakery();
		lockConsole.reservarSitio(new MiEntero(0));
		int contBakery = 1;

		Scanner keyboard = new Scanner(System.in);
		System.out.println("Introduzca su nombre de usuario");
		String userId = keyboard.nextLine();

		Semaphore eUser = new Semaphore(1);
		Semaphore mReadersUser = new Semaphore(0);
		Semaphore mWritersUser = new Semaphore(0);
		Usuario usr = new Usuario(userId, InetAddress.getLocalHost().getHostAddress(), new BufferUsuario(eUser, mReadersUser, mWritersUser));
		
		
		try {
			Socket sc = new Socket(InetAddress.getLocalHost().getHostAddress(), 999);
			OyenteServidor th = new OyenteServidor(sc, usr, contBakery, lockConsole);
			contBakery++;
			th.start();
			try {
				Thread.sleep(100);

			} catch (Exception e) {
			}

			int op = mostrarMenu(lockConsole, th, usr, eUser, mReadersUser, mWritersUser);
			ejecutarOp(usr, op, keyboard, th, userId, lockConsole, eUser, mReadersUser, mWritersUser);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

