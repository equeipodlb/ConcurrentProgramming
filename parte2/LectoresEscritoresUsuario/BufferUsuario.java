package parte2.LectoresEscritoresUsuario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import parte2.Pelicula;

public class BufferUsuario {

	List<Pelicula> pelis;
	Pelicula buscada;
	private Semaphore e, mReaders, mWriters; // semáforos para variables globales, lectores y escritores.
	private int dw, dr;// Representan respectivamente el numero de escritores y lectores en espera.
	private int nr, nw;// Representar respectivamente el numero de lectores y escritores que se están ejecutando

	public Pelicula getBuscada(){
		return this.buscada;
	}

	public BufferUsuario(Semaphore e, Semaphore mr, Semaphore mw) {

		this.pelis = new ArrayList<Pelicula>(0);
		this.e = e;
		mReaders = mr;
		mWriters = mw;
		dw = 0;
		dr = 0;
		nw = 0;
		nr = 0;
	}


	public void escribir(Pelicula p) {

		// TAKE LOCK WRITER

		try {
			e.acquire();
		} catch (InterruptedException e) {
		}
		;

		if (nr > 0 || nw > 0) {
			dw++;
			e.release();
			try {
				mWriters.acquire();
			} catch (InterruptedException e) {
			}
			;
		}
		nw++; // PASO DE TESTIGO, EL QUE ME DESPIERTA ME PASA EL SEMÁFORO, INSTRUCCIÓN SEGURA.
		e.release();

		// ESCRITURA

		pelis.add(p);
		

		// RELEASE LOCK WRITER

		try {
			e.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		nw--;
		if (dw > 0) {
			dw--;
			mWriters.release(); // PASO DE TESTIGO
		} else if (dr > 0) {
			dr--;
			mReaders.release();// PASO DE TESTIGO

		} else{
			e.release();
		}

	}


	
	
	public void leer(String titulo) {
		
		//TAKE LOCK WRITER
		
		try {
			e.acquire();  
	    } 
		catch (InterruptedException e) {};	
		
		if(nw > 0) {
			dr++;
			e.release();
			try {
				mReaders.acquire(); 
		    } 
			catch (InterruptedException e) {};	
		}
		nr ++; //PASO DE TESTIGO, EL QUE ME DESPIERTA ME PASA EL SEMÁFORO, INSTRUCCIÓN SEGURA.
		if(dr > 0) {
			dr--;
			mReaders.release(); //PASO DE TESTIGO
		}
		else e.release();
		
		
		//LECTURA
			
		for(Pelicula p: pelis){
            if(p.getTitulo().equals(titulo)){
                buscada = p;
				break;
			}
        }
		
		//RELEASE LOCK WRITER
		
		try {
			e.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		nr--;
		if(nr == 0 && dw > 0) {
			dw--;
			mWriters.release(); //PASO DE TESTIGO
		}
		else e.release();
		

	
	}
}
