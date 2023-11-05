package parte2.Mensajes;

import java.io.Serializable;

import parte2.Pelicula;

public abstract class Mensaje implements Serializable{
    
    int tipo;
    String Origen,Destino;

    public abstract int getTipo();
    public abstract String getOrigen();
    public abstract String getDestino();
    public String getUserId(){return null;}
    public Pelicula getPelicula(){return null;}
    public String getTituloPelicula(){return null;}
    public String getClientId(){return null;}
    public int getPuerto(){return -1;}
}
