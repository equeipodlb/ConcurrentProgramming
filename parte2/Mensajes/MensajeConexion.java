package parte2.Mensajes;

public class MensajeConexion extends Mensaje {

    int tipo = 0;
    String destino;
    String origen;
    String userId;

    public MensajeConexion(String userId){
        this.userId = userId; 
       
    }

    public String toString(){
        return "El usuario " + userId + " quiere conectarse";
    }


    public String getUserId(){
        return this.userId;
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
