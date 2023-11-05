package parte2.Mensajes;

public class MensajeConfInfoDisp extends Mensaje{
    int tipo = 5;
    String origen, destino, info;

    public MensajeConfInfoDisp(String info){
        this.info = info;
    }

   
    public String getInfo(){
        return this.info;
    }

    public String toString(){
        return this.info;
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
