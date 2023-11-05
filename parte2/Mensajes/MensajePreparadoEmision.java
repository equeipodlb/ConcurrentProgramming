package parte2.Mensajes;

public class MensajePreparadoEmision extends Mensaje{
    int tipo = 8;
    String destino;
    String origen;
    String pelicula;
    int puerto;

    public MensajePreparadoEmision(String origen, String destino, String titulo, int puerto){
        this.origen = origen;
        this.destino = destino;
        this.pelicula = titulo;
        this.puerto = puerto;
    }

    public int getPuerto(){
        return puerto;
    }

    public String toString(){
        return "El cliente " + origen + " esta preparado para enviar la pelicula " + pelicula + " al cliente "+ destino + "por el puerto" + puerto;
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
