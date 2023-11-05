package parte2.LectoresEscritoresSem;

import parte2.Pelicula;

public class EscritorTablaUser extends Thread{

	InfoUser tablaInfoUser;
    private Pelicula p = null;
    private String usrId = null;
	private boolean borrar;
	
	public boolean getPeliculaRepetida(){
		return this.tablaInfoUser.getPeliculaRepetida();
	}

	public void changePeliculaRepetida(){
		this.tablaInfoUser.changePeliculaRepetida();
	}

	public EscritorTablaUser(InfoUser tablaInfoUser, Pelicula p, String usrId, boolean borrar) {
		this.tablaInfoUser = tablaInfoUser;
        this.p = p;
        this.usrId = usrId;
		this.borrar = borrar;
	}
	
	public void run() {
		
        tablaInfoUser.escribir(usrId, p, borrar);      
	
	}
	
}
