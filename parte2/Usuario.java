package parte2;

import java.util.ArrayList;
import java.util.List;

import parte2.LectoresEscritoresUsuario.BufferUsuario;

public class Usuario {
    
    String id, ip;
    String cliente;
    BufferUsuario info;
    public Usuario(String id, String ip, BufferUsuario info){
        this.id = id;
        this.ip = ip;
        this.info = info;
    }

    public BufferUsuario getInfo(){
        return this.info;
    }

    public void setClient(String s){
        this.cliente = s;
    }

    public String getCliente(){
        return this.cliente;
    }
    
    public String getId(){
        return this.id;
    }

    /*
 public Pelicula buscarPelicula(String titulo){
        for(Pelicula p: info){
            if(p.getTitulo().equals(titulo))
                return p;
        }
        return null;
    }

    public void addPelicula(Pelicula p){
        info.add(p);
    }
 */
   

}
