package parte2.Mensajes;

import parte2.Pelicula;

public class MensajeEnviarPelicula extends Mensaje{


    int tipo = 2;
    String usrId;
    String origen, destino;
    public Pelicula pelicula;

    public MensajeEnviarPelicula(Pelicula p, String usrId){
        this.pelicula = p;
        this.usrId = usrId;
    }

    public Pelicula getPelicula(){
        return this.pelicula;
    }

    public String getUserId(){
        return this.usrId;
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
