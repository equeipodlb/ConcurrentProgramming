package parte2.Mensajes;

public class MensajeEmitirPeli extends Mensaje {
    int tipo = 7;
    String destino;
    String origen;
    int puerto;
    String pelicula;

    public MensajeEmitirPeli( String titulo, String destino, String origen, int puerto){
        this.pelicula = titulo;
        this.destino = destino;
        this.origen = origen;
        this.puerto = puerto;
    }


    public String toString(){
        return "El cliente " + destino + " esta solicitando la pelicula" + pelicula + "por el puerto " + puerto;
    }

    public int getPuerto(){
        return this.puerto;
    }

    public String getTituloPelicula(){
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
