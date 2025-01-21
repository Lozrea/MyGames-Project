package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.MainApp;
import controllers.utils.AlertUtils;
import controllers.utils.components.Components;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import models.AppUser;
import models.AppUserCreate;
import models.error.ForbiddenAccessException;
import models.error.GenericApiException;
import models.feign.OpenFeignConstants;
import models.feign.client.UserClient;

/** Controlador de la ventana de Login */
public class LoginController implements Initializable {

  /** Cliente para la obtención de datos de usuarios */
  private UserClient userClient;

  /** Aplicación principal */
  private MainApp mainApp;

  /** Spinner de carga */
  private ImageView spinner;
  
  /** BorderPane para el fondo de la pantalla */
  @FXML
  private BorderPane bkBorderPane;
  
  /**BorderPane para el fondo del formulario*/
  @FXML
  private BorderPane bkFormulario;

  /** Pane para el icono de la aplicación */
  @FXML
  private Pane paneIcon;

  /** TextField para el username */
  @FXML
  private TextField txtUsername;

  /** TextField para la contraseña */
  @FXML
  private PasswordField txtPassword;

  /** Label a pulsar si se ha olvidado la contraseña */
  @FXML
  private Label lblForgotPassword;

  /** Botón para iniciar sesión */
  @FXML
  private Button btnSignIn;

  /** Label mostrado si el login es incorrecto */
  @FXML
  private Label lblIncorrectSignIn;

  /** Label para acceder a la ventana de registro */
  @FXML
  private Label lblRegister;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    spinner = Components.getSpinner();
  }

  /**
   * Evento para manejar la contraseña olvidada
   * 
   * @param event Evento ocurrido
   */
  @FXML
  void handleForgotPasswordClicked(MouseEvent event) {
    mainApp.initRecoverPasswordView();
  }

  /**
   * Evento para manejar el click sobre el botón de registro
   * 
   * @param event Evento ocurrido
   */
  @FXML
  void handleRegisterClicked(MouseEvent event) {
    mainApp.initRegisterView();
  }

  /**
   * Evento para manejar el inicio de sesión
   * 
   * @param event Evento ocurrido
   */
  @FXML
  void handleSignIn(ActionEvent event) {

    btnSignIn.setText("");
    spinner.setVisible(true);

    try {
      // Se obtiene el usuario loggeado y se abre la ventana principal de la app
      AppUser user = getLoggedUser();
      mainApp.setAppUser(user);

      mainApp.initHomeView();

      // Control de errores por login incorrecto. Muestra el mensaje durante 3 segundos
    } catch (ForbiddenAccessException e) {
      lblIncorrectSignIn.setVisible(true);

      Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), ev -> lblIncorrectSignIn.setVisible(false)));
      timeline.setCycleCount(1);
      timeline.play();

      // Control para un error genérico
    } catch (GenericApiException e) {
      Alert alert = AlertUtils.getUnexpectedErrorAlert();
      alert.showAndWait();
    }

    btnSignIn.setText("Iniciar sesión");
    spinner.setVisible(false);
  }

  /**
   * Setter - UserClient
   * 
   * @param userClient Cliente de usuarios
   */
  public void setUserClient(UserClient userClient) {
    this.userClient = userClient;
  }

  /**
   * Setter - MainApp
   * 
   * @param mainApp Aplicación principal
   */
  public void setMainApp(MainApp mainApp) {
    this.mainApp = mainApp;
  }

  /**
   * Obtiene el usuario loggeado a partir de los campos username y password
   * 
   * @return AppUser
   */
  private AppUser getLoggedUser() {

    AppUserCreate userToLogin = new AppUserCreate();
    userToLogin.setUsername(txtUsername.getText());
    userToLogin.setPassword(txtPassword.getText());

    return userClient.login(OpenFeignConstants.SECRET_KEY, userToLogin);
  }

}
