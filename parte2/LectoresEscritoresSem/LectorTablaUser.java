package parte2.LectoresEscritoresSem;

public class LectorTablaUser extends Thread {

    InfoUser tablaInfoUser;
    String s;

	public LectorTablaUser(InfoUser tablaInfoUser) {
		this.tablaInfoUser = tablaInfoUser;
	}
	
	public void run() {
		this.s = tablaInfoUser.leer();
	}

    public String getS(){
        return this.s;
    }
	
}
