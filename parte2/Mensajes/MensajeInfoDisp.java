package parte2.Mensajes;

public class MensajeInfoDisp extends Mensaje {
    int tipo = 4;
    String origen, destino, usrId;

    public MensajeInfoDisp(String usrId){
        this.usrId = usrId;
    }

   
    public String getUsrId(){
        return this.usrId;
    }

    public String toString(){
        return "El usuario " + usrId + " solicita conocer toda la informacion disponible en el sistema";
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
