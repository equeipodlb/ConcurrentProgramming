package parte2.MonitorSocket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javafx.util.Pair;

public class Reader extends Thread{
    private MonitorLockCond monitor;
	private HashMap<String, Pair<ObjectInputStream, ObjectOutputStream>> tablaInfoSockets;
	private ObjectOutputStream out = null;
	private String idCliente;
	
	public Reader(MonitorLockCond monitor, HashMap<String, Pair<ObjectInputStream, ObjectOutputStream>> tablaInfoSockets, String idCliente) {
		this.monitor = monitor;
		this.tablaInfoSockets = tablaInfoSockets;
		this.idCliente = idCliente;
	}
	
	public void run() {
		monitor.requestRead();
		this.out = tablaInfoSockets.get(idCliente).getValue();
		monitor.releaseRead();
	}

	public ObjectOutputStream getOut(){
		return this.out;
	}

}
