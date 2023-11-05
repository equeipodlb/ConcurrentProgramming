package parte2.MonitorUC;
import java.util.HashMap;
import java.util.Map;


public class ReaderUC extends Thread{
    private MonitorUC monitor;
	private HashMap<String, String> tablaUC;
	private String emisorCliente;
	private String emisorUsuario;
	
	public ReaderUC(MonitorUC monitor, HashMap<String, String> tablaUC, String emisorUsuario) {
		this.monitor = monitor;
		this.emisorUsuario = emisorUsuario;
		this.tablaUC = tablaUC;
	}
	
	public void run() {
		monitor.requestRead();
		this.emisorCliente = tablaUC.get(emisorUsuario);
		monitor.releaseRead();
	}

	public String getEmisorCliente(){
		return this.emisorCliente;
	}

}
