package parte2.LectoresEscritoresUsuario;

import parte2.Pelicula;

public class ReaderUsuario extends Thread {

    BufferUsuario pelis;
	String titulo;

	public ReaderUsuario(BufferUsuario pelis, String titulo) {
		this.pelis = pelis;
		this.titulo = titulo;
	}
	
	public Pelicula getBuscada(){
		return pelis.getBuscada();
	}

	public void run() {
		pelis.leer(titulo);
	}
}
