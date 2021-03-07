package practica1;

import java.net.*;
import java.io.*;

public class Multicast {

    private static final int PUERTO = 6703;
    private static final String IP = "224.0.0.100";

    //Info de conexion
    private MulticastSocket socket;
    private InetAddress grupo;

    //Variabes recepcion
    private byte []bufferRecibir = new byte[1024];
    private DatagramPacket mensajeRecibir = new DatagramPacket(bufferRecibir, bufferRecibir.length);

    //Constructor
    public Multicast() throws IOException{
        this.socket=new MulticastSocket(PUERTO);
        this.grupo=InetAddress.getByName(IP);
        this.socket.joinGroup(grupo);
    }

    //Getters,setters
    public MulticastSocket getSocket() {
        return this.socket;
    }

    public void setSocket(MulticastSocket socket) {
        this.socket = socket;
    }

    public InetAddress getGrupo() {
        return this.grupo;
    }

    public void setGrupo(InetAddress grupo) {
        this.grupo = grupo;
    }

    //Metodos

    //metodo que se encarga de recibir los mensajes a nivel socket
    public String recibirMensaje() throws IOException{
        //recibimos el mensaje en bytes
        this.socket.receive(this.mensajeRecibir);
        //devolvemos el mensaje en string
        String salida=new String(this.mensajeRecibir.getData(),this.mensajeRecibir.getOffset(),this.mensajeRecibir.getLength());
        return salida;
    }

    //metodo para enviar mensajes a nivel socket
    public void enviarMensaje(String mensaje) throws IOException {
        //pasamos el mensaje a bytes
        byte[] mB = mensaje.getBytes();
        //creamos un paquete con la info del mensaje en bytes
        DatagramPacket mensajes = new DatagramPacket(mB, mB.length, this.grupo, PUERTO);
        //enviamos el mensaje al socket
        this.socket.send(mensajes);
    }

    public void cerrarMulticast() throws IOException{
        //si existe el socket...
        if(this.socket != null) {
            //salimos del grupo
            this.socket.leaveGroup(this.grupo);
            //cerramos del socket
            this.socket.close();
            System.out.println("Socket cerrado correctamente.");
        }else{
            System.out.println("Fallo en cerrarMulticast.");
        }
    }
}
