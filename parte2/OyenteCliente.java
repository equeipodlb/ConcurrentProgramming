package parte2;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javafx.util.Pair;
import parte2.LectoresEscritoresSem.EscritorTablaUser;
import parte2.LectoresEscritoresSem.InfoUser;
import parte2.LectoresEscritoresSem.LectorTablaUser;
import parte2.LockBakery.LockBakery;
import parte2.LockBakery.MiEntero;
import parte2.Mensajes.*;
import parte2.MonitorSocket.*;
import parte2.MonitorSocket.Reader;
import parte2.MonitorSocket.Writer;
import parte2.MonitorUC.MonitorUC;
import parte2.MonitorUC.ReaderUC;
import parte2.MonitorUC.WriterUC;

public class OyenteCliente extends Thread {

    int id;
    Socket s;
    InfoUser tablaInfoUser;
    Semaphore mReadersTU, mWritersTU, eTU; // SEMAFOROS PARA ACTUALIZAR LA TABLA DE LA INFORMACION DE CADA USUARIO. SE LOS PASA EL SERVIDOR
    Semaphore semServerConsole; //Para compartir la consola con el resto de OyentesServidores y con el Servidor
    HashMap<String, Pair<ObjectInputStream, ObjectOutputStream>> tablaInfoSockets;//Para almacenar los flujos con cada cliente
    MonitorLockCond monitor; //Para actualizar tablaInfoSockets de forma segura
    MonitorUC monitorUC;//Para tabla UserClient
    HashMap<String,String> UserClient;//Relacion Usuario-Cliente
    MiEntero puertoConexionClientes;//Primer puerto a partir del 500 que todavia no se ha utilizado
    LockBakery lockPort; //lock del puerto

    //TABLA CORRESPONDENCIA ENTRE USUARIO-CLIENTE

    public OyenteCliente(int numCliente, Socket s, InfoUser tablaInfoUser, Semaphore mReadersTU, Semaphore mWritersTU, Semaphore eTU, HashMap<String, Pair<ObjectInputStream, ObjectOutputStream>> tablaInfoSockets, MonitorLockCond m,  HashMap<String,String> UserClient, MonitorUC monitorUC, MiEntero puertoConexionClientes, LockBakery lockPort, Semaphore semServerConsole) {
        this.s = s;
        this.tablaInfoUser = tablaInfoUser;
        this.eTU = eTU;
        this.mReadersTU = mReadersTU;
        this.mWritersTU = mWritersTU;
        this.tablaInfoSockets = tablaInfoSockets;
        this.monitor = m;
        this.id = numCliente;
        this.UserClient = UserClient;
        this.monitorUC = monitorUC;
        this.puertoConexionClientes = puertoConexionClientes;
        this.lockPort = lockPort;
        this.semServerConsole = semServerConsole;
        lockPort.reservarSitio(new MiEntero(id-1));
    }

    public void run() {
        try {
            ObjectInputStream finS = new ObjectInputStream(s.getInputStream());// Creamos el canal de entrada del servidor
            ObjectOutputStream foutS = new ObjectOutputStream(s.getOutputStream()); // Creamos el canal de salida del servidor

            while (true) {
                Mensaje m = (Mensaje) finS.readObject(); // Leemos el mensaje que nos envía el cliente

                switch (m.getTipo()) {
                    case (0)://Mensaje que nos envia un cliente en cuanto se establece la conexion: MENSAJECONEXION
                        //Metemos en la tabla de conexiones los canales que se han creado
                        Writer escritorTablaSocket = new Writer(monitor, tablaInfoSockets, finS, foutS, id);
                        escritorTablaSocket.start();
                        escritorTablaSocket.join();
                        //Actualizamos la tabla UC
                        WriterUC escritorUC = new WriterUC(monitorUC, UserClient, "Cliente"+ id,m.getUserId());
                        escritorUC.start();
                        escritorUC.join();
                        // Mostramos el mensaje que nos han enviado y para ello adquirimos la consola
                        semServerConsole.acquire();
                        System.out.println(m.toString());
                        semServerConsole.release();
                        // Mandamos el mensaje de confirmación de  la conexión
                        foutS.writeObject(new MensajeConfConex(m.getUserId(),"Cliente"+ id));
                        foutS.flush();
                        break;
                    case (2)://Mensaje que nos envia un cliente para informarnos de que tiene una pelicula: MENSAJEENVIARPELICULA
                        //Incluimos esta pelicula en la tabla del servidor que contiene la información sobre que película tiene cada usuario
                        EscritorTablaUser thEscritorTablaUser = new EscritorTablaUser(tablaInfoUser, m.getPelicula(),m.getUserId(), false);
                        thEscritorTablaUser.start();
                        thEscritorTablaUser.join();
                        if(thEscritorTablaUser.getPeliculaRepetida()){
                            thEscritorTablaUser.changePeliculaRepetida();
                            semServerConsole.acquire();
                            //Mostramos un mensaje diciendo que el usuario ya tenia la pelicula
                            System.out.println("El usuario " + m.getUserId() + " ya tiene la pelicula " + m.getPelicula().getTitulo());
                            semServerConsole.release();
                            //Enviamos un mensaje para que el cliente sepa que ha enviado una pelicula repetida
                            foutS.writeObject(new MensajePeliculaRepetida(m.getPelicula().getTitulo()));// Mandamos el mensaje de confirmación de la conexión
                            foutS.flush();
                        }
                        else{
                            semServerConsole.acquire();
                            //Mostramos el mensaje con la pelicula que hemos recibido
                            System.out.println("Se ha recibido la siguiente información: ");
                            System.out.println(m.getPelicula().toString());
                            semServerConsole.release();
                            //Enviamos un mensaje para que el cliente sepa que hemos recibido la pelicula
                            foutS.writeObject(new MensajeConfirmacionEnvio());// Mandamos el mensaje de confirmación de la conexión
                            foutS.flush();
                        }
                        break;
                    case (4): // Mensaje que nos envia un cliente para solicitar toda la información que tenemos disponible: MENSAJEINFODISP
                        //Iniciamos un hilo lector que construye un string con toda la informacion
                        LectorTablaUser thLectorTablaUser = new LectorTablaUser(tablaInfoUser);
                        thLectorTablaUser.start();
                        thLectorTablaUser.join();
                        // Mandamos el mensaje con toda la información que hay en el sistema
                        foutS.writeObject(new MensajeConfInfoDisp(thLectorTablaUser.getS()));
                        foutS.flush();
                        break;
                    case (6): //Mensaje que nos manda un cliente cuando este solicita una pelicula: MENSAJEPEDIRPELI
                        //Buscamos los usuarios que tienen la pelicula que se pide
                        String titulo = m.getTituloPelicula();
                        Set<String> candidatos = tablaInfoUser.buscaPelicula(titulo);
                        if(candidatos.isEmpty()){

                            //Si nadie tiene la pelicula, mandamos un mensaje al cliente para que lo sepa
                            foutS.writeObject(new MensajePeliNoDisp(titulo));
                        }
                        else if(candidatos.contains(m.getUserId())){
                            semServerConsole.acquire();
                            //Mostramos un mensaje diciendo que el usuario ya tenia la pelicula
                            System.out.println("El usuario " + m.getUserId() + " ya tiene la pelicula " + m.getTituloPelicula());
                            semServerConsole.release();
                            //Enviamos un mensaje para que el cliente sepa que ha enviado una pelicula repetida
                            foutS.writeObject(new MensajePeliculaRepetida(m.getTituloPelicula()));// Mandamos el mensaje de confirmación de la conexión
                            foutS.flush();
                        }
                        else{
                            //Elegimos un usuario que tenga la peli
                            String emisor = (String)candidatos.toArray()[0];
                            //Lanzamos un hilo lector de la tabla UC para obtener el cliente asociado al usuario que tiene la peli
                            ReaderUC readerUC = new ReaderUC(monitorUC, UserClient, emisor);
                            readerUC.start();
                            readerUC.join();
                            emisor = readerUC.getEmisorCliente();
                            //Lanzamos un hilo lector para obtener los canales del cliente mediante el cual se ha conectado el usuario que tiene la peli
                            Reader readerOut = new Reader(monitor, tablaInfoSockets, emisor);
                            readerOut.start();
                            readerOut.join();
                            ObjectOutputStream out = readerOut.getOut(); 
                            //Enviamos un mensaje al cliente que tiene la peli para que sepa qur tiene que enviarsela a otro cliente
                            out.writeObject(new MensajeEmitirPeli(titulo, m.getOrigen(), emisor, puertoConexionClientes.getValue()));
                            out.flush();
                            //Como vamos a realizar un envio, el puerto actual quedara utilizado, por eso lo incrementamos
                            lockPort.bakeryEnter(id-1);                            
                            puertoConexionClientes.sum(1);
                            lockPort.bakeryRelease(id-1);
                        }
                        break;
                    case (8): //Mensaje que nos envía un cliente que tiene que enviar una pelicula cuando este ya está preparado: MENSAJEPREPARADOEMISION
                        //Mostramos que el cliente ya está preparado
                        semServerConsole.acquire();
                        System.out.println(m.toString());
                        semServerConsole.release();
                        //Obtenemos el canal del que será el Cliente receptor de la pelicula
                        Reader readerOut = new Reader(monitor, tablaInfoSockets, m.getDestino());
                        readerOut.start();
                        readerOut.join();
                        //Reenviamos el mensaje al cliente que había solicitado la pelicula para que este lance el hilo receptor
                        ObjectOutputStream out = readerOut.getOut();
                        out.writeObject(m);
                        out.flush();
                        break;
                    case(9)://Mensaje que envia un cliente cuando recibe una pelicula que había solicitado: MENSAJEPELICULARECIBIDA
                        //Mostramos que el cliente ya ha recibido la pelicula que había solicitado
                        semServerConsole.acquire();
                        System.out.println(m.toString());
                        semServerConsole.release();
                        //Lanzamos un hilo escritor a la tabla de información del servidor para que quede constancia del cambio
                        EscritorTablaUser thEscritor1 = new EscritorTablaUser(tablaInfoUser, m.getPelicula(),m.getUserId(), false);
                        thEscritor1.start();
                        thEscritor1.join();
                        break;
                    case(10)://Mensaje que nos manda un cliente cuando se quieres desconectar: MENSAJEDESCONECTAR
                        semServerConsole.acquire();
                        System.out.println(m.toString());
                        semServerConsole.release();
                        //Borramos de la tabla de información del servidor la entrada asociada al cliente que se quiere desconectar
                        EscritorTablaUser thEscritorTablaUser1= new EscritorTablaUser(tablaInfoUser, m.getPelicula(),m.getUserId(), true);
                        thEscritorTablaUser1.start();
                        thEscritorTablaUser1.join();
                        //Mandamos un mensaje para confirmar la desconexion
                        foutS.writeObject(new MensajeConfirmarDesconexion(m.getUserId(), m.getClientId()));// Mandamos el mensaje de confirmación de la conexión
                        foutS.flush();
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
