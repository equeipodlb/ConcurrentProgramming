package parte2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;

import javafx.util.Pair;
import parte2.LectoresEscritoresSem.InfoUser;
import parte2.LockBakery.LockBakery;
import parte2.LockBakery.MiEntero;
import parte2.MonitorSocket.MonitorLockCond;
import parte2.MonitorUC.MonitorUC;

public class Servidor {


	public static void main(String[] args) {

		// Semaforos para leer y escribir la tabla que contiene la informacion de los
		// usuarios de forma segura
		Semaphore eTU = new Semaphore(1);
		Semaphore mReadersTU = new Semaphore(0);
		Semaphore mWritersTU = new Semaphore(0);
		MonitorLockCond monitor = new MonitorLockCond();//para tabla sockets
		MonitorUC monitorUC = new MonitorUC();//para tabla UserClient
		LockBakery lockPort = new LockBakery();//para el puerto de conexion entre emisor y receptor

		Semaphore semServerConsole = new Semaphore(1);//para la consola que comparten el servidor y los distintos oyentes clientes



		InfoUser tablaInfoUser = new InfoUser(eTU, mReadersTU, mWritersTU);
		HashMap<String, Pair<ObjectInputStream, ObjectOutputStream>> tablaInfoSockets = new HashMap<String, Pair<ObjectInputStream, ObjectOutputStream>>(0);
		HashMap<String,String> userClient = new HashMap<String,String>(0);
		ServerSocket ss;

		int numClientes = 0;
		MiEntero puerto = new MiEntero(500);


		try {
			ss = new ServerSocket(999);

			while (true) {
				Socket s = ss.accept();
				numClientes ++;
				OyenteCliente th = new OyenteCliente(numClientes, s, tablaInfoUser, mReadersTU, mWritersTU, eTU, tablaInfoSockets, monitor, userClient, monitorUC, puerto, lockPort, semServerConsole);
				th.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}