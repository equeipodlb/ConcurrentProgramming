package parte2.Mensajes;

import parte2.Pelicula;

public class MensajePeliculaRecibida extends Mensaje {
    int tipo = 9;
    String destino;
    String origen;
    Pelicula pelicula;
    String userId;

    public MensajePeliculaRecibida(String origen, String destino, Pelicula p, String userId){
        this.origen = origen;
        this.destino = destino;
        this.pelicula = p;
        this.userId = userId;
    }

    public String getUserId(){
        return this.userId;
    }

    public String toString(){
        return "El cliente " + destino + " ha recibido la pelicula " + pelicula.getTitulo() + " del cliente "+ origen;
    }


    public Pelicula getPelicula(){
        return this.pelicula;
    }

    @Override
    public int getTipo() {
        // TODO Auto-generated method stub
        return this.tipo;
    }

    @Override
    public String getOrigen() {
        // TODO Auto-generated method stub
        return this.origen;
    }

    @Override
    public String getDestino() {
        // TODO Auto-generated method stub
        return this.destino;
    }
}
