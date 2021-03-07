package practica1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controlador;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //indicamos donde esta el fichero con la interfaz grafica al programa
        FXMLLoader loader = new FXMLLoader(getClass().getResource("window.fxml"));
        //la cargamos
        Parent root=loader.load();
        //obtenemos el controlador de la interfaz
        this.controlador=loader.getController();
        //ejecutamos el metodo que nos permite introducir un nombre de usuario
        this.controlador.nombreUsuario();

        //cargamos la ventana principal (la del chat, window.fxml)
        primaryStage.setTitle("App mensajería - María Caseiro");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.getScene().getStylesheets().add("dark-theme.css");
        primaryStage.show();

        //Creacion de hilos de recepcion
        Thread hilo = new Thread( () -> {
            while (!this.controlador.getMulticast().getSocket().isClosed())
                this.controlador.recibirMensaje();
        });
        hilo.start();
    }

    @Override
    public void stop(){
        this.controlador.cerrarConexion();
        System.out.println("Controlador cerrado con éxito.");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
