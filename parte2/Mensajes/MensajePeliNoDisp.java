package parte2.Mensajes;

public class MensajePeliNoDisp extends Mensaje {
    int tipo = 13;
    String titulo;
    String origen, destino;

    public MensajePeliNoDisp(String titulo){
        this.titulo = titulo;
    }

    public String getTituloPelicula(){
        return this.titulo;
    }

    public String toString(){
        return "La pelicula " + this.titulo + " no esta disponible puesto que ningun cliente conectado la tiene";
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
