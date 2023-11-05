package parte2.MonitorSocket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javafx.util.Pair;

public class Writer extends Thread {
    
	private MonitorLockCond monitor;
	private HashMap<String, Pair<ObjectInputStream, ObjectOutputStream>> tablaInfoSockets;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int idCliente;
	
	public Writer(MonitorLockCond monitor, HashMap<String, Pair<ObjectInputStream, ObjectOutputStream>> tablaInfoSockets,  ObjectInputStream finS,  ObjectOutputStream foutS, int id) {
		this.monitor = monitor;
		this.tablaInfoSockets = tablaInfoSockets;
        this.in = finS;
        this.out = foutS;
        this.idCliente = id;
	}
	
	public void run() {
		monitor.requestWrite();
		tablaInfoSockets.put("Cliente" + idCliente, new Pair<ObjectInputStream,ObjectOutputStream>(in, out));
		monitor.releaseWrite();
        System.out.println("Canales de comunicación añadidos a la tabla");
	}
	

}
