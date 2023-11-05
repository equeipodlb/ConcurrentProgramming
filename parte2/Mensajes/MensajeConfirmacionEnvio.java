package parte2.Mensajes;

public class MensajeConfirmacionEnvio extends Mensaje{
    public int tipo = 3;
    public String destino;
    public String origen;

    public MensajeConfirmacionEnvio(){

    }


    public String toString(){
        return "El servidor ha recibido la información  correctamente";
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
