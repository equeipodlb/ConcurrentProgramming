package parte2.MonitorUC;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


public class WriterUC extends Thread {
    
	private MonitorUC monitor;
	private HashMap<String, String> tablaUC;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String idCliente;
	private String idUsuario;
	
	public WriterUC(MonitorUC monitor, HashMap<String, String> tablaUC,  String idCliente, String idUsuario) {
		this.monitor = monitor;
		this.tablaUC = tablaUC;
        this.idCliente = idCliente;
		this.idUsuario = idUsuario;
	}
	
	public void run() {
		monitor.requestWrite();
		tablaUC.put(idUsuario, idCliente);
		monitor.releaseWrite();
        System.out.println("Informacion añadida a la tabla Usuario-Cliente");
	}
	

}
