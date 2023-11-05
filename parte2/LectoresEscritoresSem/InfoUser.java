package parte2.LectoresEscritoresSem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javafx.util.Pair;
import parte2.Pelicula;

public class InfoUser {

	HashMap<String, Pair<Boolean, List<Pelicula>>> tablaInfoUser;

	private Semaphore e, mReaders, mWriters; // semáforos para variables globales, lectores y escritores.
	private int dw, dr;// Representan respectivamente el numero de escritores y lectores en espera.
	private int nr, nw;// Representar respectivamente el numero de lectores y escritores que se están ejecutando
	boolean peliculaRepetida = false;

	public boolean getPeliculaRepetida(){
		return this.peliculaRepetida;
	}

	public void changePeliculaRepetida(){
		this.peliculaRepetida = !this.peliculaRepetida;
	}

	public InfoUser(Semaphore e, Semaphore mr, Semaphore mw) {

		this.tablaInfoUser = new HashMap<String, Pair<Boolean, List<Pelicula>>>(0);
		this.e = e;
		mReaders = mr;
		mWriters = mw;
		dw = 0;
		dr = 0;
		nw = 0;
		nr = 0;
	}

	public  Set<String> buscaPelicula( String title) {
		Set<String> result = new HashSet<>();
	
		for (Map.Entry<String, Pair<Boolean, List<Pelicula>>> entry : this.tablaInfoUser.entrySet()) {
			List<Pelicula> peliculas = entry.getValue().getValue();
	
			for (Pelicula pelicula : peliculas) {
				if (pelicula.getTitulo().equals(title)) {
					result.add(entry.getKey());
					break;
				}
			}
		}
	
		return result;
	}

	public String toString() {
		String s = "";
		for (Map.Entry<String, Pair<Boolean, List<Pelicula>>> entry : tablaInfoUser.entrySet()) {
			String key = entry.getKey();
			Pair<Boolean, List<Pelicula>> value = entry.getValue();
			if(value.getKey()){
				s = s + key + ": Connected ";
			}
			else{
				s = s + key + ": Disconnected ";
			}
			for(Pelicula p: value.getValue()){
				s = s + ", " + p.getTitulo();
			}
			s = s + "\n";
		}
		

		return s;

	}

	public void escribir(String usrId, Pelicula p, boolean borrar) {

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

		if(borrar){
			tablaInfoUser.remove(usrId);
		}
		else{
			if(tablaInfoUser.containsKey(usrId)){

				Pair<Boolean, List<Pelicula>> aux = tablaInfoUser.get(usrId);
				for(Pelicula pAux: aux.getValue()){
					if(pAux.getTitulo().equals(p.getTitulo()))
						this.peliculaRepetida = true;
				}

				if(!peliculaRepetida){
					aux.getValue().add(p);
					tablaInfoUser.remove(usrId);
					tablaInfoUser.put(usrId, aux);
				}
			}
			else{
				List<Pelicula> aux = new ArrayList<>();
				aux.add(p);
				tablaInfoUser.put(usrId, new Pair<Boolean, List<Pelicula>>(true, aux));
			}
		}
		
		

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


	
	
	public String leer() {
		
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
			
		String s = this.toString();
		
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
		

	
		return s;
	}
}
