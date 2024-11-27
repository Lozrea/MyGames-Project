package application;

import java.io.IOException;

import controllers.LoginController;
import controllers.RecoverPasswordController;
import controllers.RegisterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.AppUser;
import models.feign.FeignClientFactory;
import models.feign.OpenFeignConstants;
import models.feign.client.UserClient;

/** Aplicación principal */
public class MainApp extends Application {

  /** Stage principal */
  private Stage primaryStage;

  /** Cliente de usuarios */
  private UserClient userClient;

  /** Cliente loggeado / registrado de la aplicación */
  private AppUser appUser;

  /** Tamaño de la pantalla */
  private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

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

    this.primaryStage = primaryStage;
    userClient = FeignClientFactory.createUserClient(OpenFeignConstants.BASE_URL);

    // Configuración inicial previa
    primaryStage.setTitle("MyGames");
    // TODO -> añadir icono a los resources -> primaryStage.getIcons().add(new Image("file:/.....png"));
    primaryStage.setResizable(false);

    // Carga del login
    initLoginView();
  }

  /** Carga la vista del Login en la ventana principal */
  public void initLoginView() {

    try {
      FXMLLoader loader = new FXMLLoader();

      loader.setLocation(MainApp.class.getResource("/views/Login.fxml"));
      BorderPane loginLayout = (BorderPane) loader.load();

      // Controlador del login
      LoginController loginController = loader.getController();
      loginController.setUserClient(userClient);
      loginController.setMainApp(this);

      Scene scene = new Scene(loginLayout, screenSize.getWidth(), screenSize.getHeight());
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Carga la vista del Registro en la ventana principal */
  public void initRegisterView() {

    try {
      FXMLLoader loader = new FXMLLoader();

      loader.setLocation(MainApp.class.getResource("/views/Register.fxml"));
      Parent registerLayout = (BorderPane) loader.load();

      // Controlador del registro
      RegisterController registerController = loader.getController();
      registerController.setUserClient(userClient);
      registerController.setMainApp(this);

      Scene scene = new Scene(registerLayout, screenSize.getWidth(), screenSize.getHeight());
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void initRecoverPasswordView() {

    try {
      FXMLLoader loader = new FXMLLoader();

      loader.setLocation(MainApp.class.getResource("/views/RecoverPassword.fxml"));
      Parent recoverPasswordLayout = (BorderPane) loader.load();

      // Controlador del registro
      RecoverPasswordController recoverPasswordController = loader.getController();
      recoverPasswordController.setUserClient(userClient);
      recoverPasswordController.setMainApp(this);

      Scene scene = new Scene(recoverPasswordLayout, screenSize.getWidth(), screenSize.getHeight());
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Carga la vista de la pantalla de inicio */
  public void initHomeView() {

    try {
      FXMLLoader loader = new FXMLLoader();

      loader.setLocation(MainApp.class.getResource("/views/Home.fxml"));
      Parent homeLayout = (BorderPane) loader.load();

      // Controlador de la ventana Home
      // TODO -> Añadir controlador al Home cuando exista y pasar appUser

      Scene scene = new Scene(homeLayout);
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Setter - AppUser
   * 
   * @param appUser Usuario loggeado en la aplicación
   */
  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }

}
