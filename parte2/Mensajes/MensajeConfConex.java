package parte2.Mensajes;

public class MensajeConfConex extends Mensaje {

    public int tipo = 1;
    public String destino;
    public String origen;
    public String userId;
    public String clientId;

    public MensajeConfConex(String userId, String clientId){
        this.userId = userId;
        this.clientId = clientId;
    }


    public String getClientId(){
        return this.clientId;
    }

    public String toString(){
        return "El usuario " + userId + " se ha conectado al servidor correctamente mediante el cliente " + clientId;
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
