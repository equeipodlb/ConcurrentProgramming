package parte2.Mensajes;

import parte2.Usuario;

public class MensajePedirPeli extends Mensaje {
    int tipo = 6;
    String destino;
    String origen;
    String usr;
    String pelicula;

    public MensajePedirPeli( String titulo, String origen, String userId){
        this.pelicula = titulo;
        this.origen = origen;
        this.usr = userId;
    }


    public String toString(){
        return "El usuario " + usr+ " esta solicitando la pelicula" + pelicula + "a traves del cliente" + origen;
    }


    public String getTituloPelicula(){
        return this.pelicula;
    }

    public String getUserId(){
        return this.usr;
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
