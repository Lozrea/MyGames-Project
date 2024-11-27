package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.MainApp;
import controllers.utils.AlertUtils;
import controllers.utils.ComponentUtils;
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

  @FXML
  private Pane paneIcon;

  @FXML
  private TextField txtUsername;

  @FXML
  private PasswordField txtPassword;

  @FXML
  private Label lblForgotPassword;

  @FXML
  private Button btnSignIn;

  @FXML
  private Label lblIncorrectSignIn;

  @FXML
  private Label lblRegister;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    spinner = ComponentUtils.getSpinner();
  }

  @FXML
  void handleForgotPasswordClicked(MouseEvent event) {
    mainApp.initRecoverPasswordView();
  }

  @FXML
  void handleRegisterClicked(MouseEvent event) {
    mainApp.initRegisterView();
  }

  @FXML
  void handleSignIn(ActionEvent event) {

    btnSignIn.setText("");
    spinner.setVisible(true);

    try {
      // Se obtiene el usuario loggeado y se abre la ventana principal de la app
      AppUser user = getLoggedUser();
      mainApp.setAppUser(user);

      System.out.println(user.getName());
      // TODO -> Cuando esté lista HomeView, redirigir. Mientras, se deja el syso para testing->mainApp.initHomeView();

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
