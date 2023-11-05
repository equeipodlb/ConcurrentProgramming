package parte2.Mensajes;

public class MensajeDesconectar extends Mensaje{
    
    public int tipo = 10;
    public String destino;
    public String origen;
    public String userId;
    public String clientId;

    public MensajeDesconectar(String userId, String clientId){
        this.userId = userId;
        this.clientId = clientId;
    }


    public String getUserId(){
        return this.userId;
    }

    public String getClientId(){
        return this.clientId;
    }

    public String toString(){
        return "El usuario " + userId + " se quiere desconectar mediante el cliente " + clientId;
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
