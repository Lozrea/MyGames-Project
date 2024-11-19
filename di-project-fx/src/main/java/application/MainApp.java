package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Aplicación principal */
public class MainApp extends Application {

  /** Panel principal */
  private Parent root;

  /**
   * Método main. Inicializa la aplicación
   * 
   * @param args Argumentos
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {

    try {

      // Configuración inicial previa
      primaryStage.setTitle("MyGames");
      // A falta del icono -> primaryStage.getIcons().add(new Image("file:/.....png"));
      primaryStage.setResizable(false);
      primaryStage.setMaximized(true);

      // Carga del login y muestra
      root = FXMLLoader.load(MainApp.class.getResource("/views/Login.fxml"));
      Scene scene = new Scene(root);

      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
