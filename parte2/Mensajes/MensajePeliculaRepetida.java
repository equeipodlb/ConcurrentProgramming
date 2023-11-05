package parte2.Mensajes;

public class MensajePeliculaRepetida extends Mensaje{
    int tipo = 12;
    String titulo;
    String origen, destino;

    public MensajePeliculaRepetida( String titulo){
        this.titulo = titulo;
    }

    public String getTituloPelicula(){
        return this.titulo;
    }

    public String toString(){
        return "La pelicula " + this.titulo + " ya se habia enviado anteriormente";
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
