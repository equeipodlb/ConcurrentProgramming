package parte2.LectoresEscritoresUsuario;

import parte2.Pelicula;

public class EscritorUser extends Thread{

	BufferUsuario pelis;
    private Pelicula p = null;

	public EscritorUser(BufferUsuario pelis, Pelicula p) {
		this.pelis = pelis;
        this.p = p;
  
	}
	
	public void run() {
		
        pelis.escribir(p);      
	
	}
	
}
