package practica1;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.IOException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


public class Controller {

        //atributos
        public static final String IP = "224.0.0.100";
        private String nombreUsuario;
        private Multicast multicast;

        @FXML
        private Label labelUsuario;

        @FXML
        private TextArea areaChat;

        @FXML
        private TextField textoEnviar;

        //CONSTRUCTOR
        public Controller() throws IOException{
            //nombre por defecto
            this.nombreUsuario="usuario";
            this.multicast=new Multicast();
        }

        //GETTERS/SETTERS

        public Multicast getMulticast(){
            return this.multicast;
        }

        //METODOS INTERACTIVOS (nivel grafico, mostrar info visual)

        //Obtenemos la fecha para escribir en el chat
        private String obtenerFechaActual() {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            return dateFormat.format(date);
        }

        //Ventana inicial para pedir el nombre de usuario
        public void nombreUsuario(){
            //creamos una ventana de dialogo para introducir texto
            TextInputDialog ventanaEmergente = new TextInputDialog("");
            //labels de informacion
            ventanaEmergente.setTitle("CHAT MCA");
            ventanaEmergente.setHeaderText(null);
            ventanaEmergente.setContentText("Usuario:\n");

            //establecemos el tema oscuro
            DialogPane dialogPane = ventanaEmergente.getDialogPane();
            dialogPane.getStylesheets().add("dark-theme.css");

            //Cambiamos la imagen de inicio
            Image image = new Image("https://assets.stickpng.com/images/580b57fcd9996e24bc43c543.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            ventanaEmergente.setGraphic(imageView);

            //Mostramos la ventana y esperamos a introducir el texto.
            //una vez introducido se almacena la info en las variables (atbts)
            Optional<String> resultado = ventanaEmergente.showAndWait();
            resultado.ifPresent(nombre -> {
                this.nombreUsuario = nombre;
                this.labelUsuario.setText(nombre);
            });
        }

        public void botonEnviarClick(){
            try {
                enviarMensaje();
            } catch(Exception exception){
                System.out.println(exception.getMessage());
            }
        }

        public void botonLimpiarChat(){
            this.areaChat.setText("");
        }

        //-----------------------------
        //METODOS DE INFORMACION (invocan al socket)

        //metodo para enviar el mensaje a nivel interfaz
        public void enviarMensaje() throws IOException {
            //si hay texto en el campo...
            if(!textoEnviar.getText().isBlank()){
                String fecha = obtenerFechaActual();
                //enviamos el mensaje al socket mediante la funcion de multicast
                this.multicast.enviarMensaje("["+fecha+"]"+"\n\t"+this.nombreUsuario + ":   "+this.textoEnviar.getText());
                textoEnviar.setText("");
            }else{
                this.textoEnviar.setText("Escribir mensaje aquí.");
            }

        }

        public void recibirMensaje(){
            try{
                //invocamos la funcion recibir mensaje que llama al socket
                String mensaje = this.multicast.recibirMensaje();
                //mostramos el mensaje en el chat
                this.areaChat.appendText(mensaje + "\n");
            } catch (SocketException exception){
                if(exception.getMessage().contains("Socket closed")) System.exit(0);
                System.out.println(exception.getMessage());
            } catch (IOException exception){
                System.out.println(exception.getMessage());
            }
        }

        public void cerrarConexion(){
            try {
                //cerramos la conexion
                this.multicast.cerrarMulticast();
            } catch (IOException exception){
                System.out.println(exception.getMessage());
            } catch (Exception exception){
                exception.printStackTrace();
            }
        }
}
