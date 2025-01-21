package controllers;

import application.MainApp;
import controllers.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import models.error.GenericApiException;
import models.error.ResourceNotFoundException;
import models.feign.OpenFeignConstants;
import models.feign.client.UserClient;

/** Controlador para la vista de Recuperar Contraseña */
public class RecoverPasswordController {

  /** Aplicación principal */
  private MainApp mainApp;

  /** Cliente para la obtención de datos de usuarios */
  private UserClient userClient;

  /** Botón para recuperar la contraseña */
  @FXML
  private Button btnRecoverPassword;

  /** Label para volver al login */
  @FXML
  private Label lblBackToLogin;

  /** Label a mostrar que indica que se ha enviado el email de recuperación */
  @FXML
  private Label lblEmailSent;
  
  /** BorderPane para el fondo de la pantalla */
  @FXML
  private BorderPane bkBorderPane;
  
  /**BorderPane para el fondo del formulario*/
  @FXML
  private BorderPane bkFormulario;

  /** Pane con el icono de la aplicación */
  @FXML
  private Pane paneIcon;

  /** TextField para el email */
  @FXML
  private TextField txtEmail;

  /**
   * Evento para volver al Login
   * 
   * @param event Evento
   */
  @FXML
  void handleBackToLogin(MouseEvent event) {
    mainApp.initLoginView();
  }

  /**
   * Evento para manejar la recuperación de contraseña
   * 
   * @param event Evento
   */
  @FXML
  void handleRecoverPassword(ActionEvent event) {

    try {

      if (txtEmail.getText() != null && !txtEmail.getText().isBlank()) {
        userClient.recoverPassword(OpenFeignConstants.SECRET_KEY, txtEmail.getText());
        lblEmailSent.setVisible(true);
      }

    } catch (ResourceNotFoundException e) {
      // No se trata el error. No se informa al usuario de que el email es incorrecto
      lblEmailSent.setVisible(true);

      // Error general de la API
    } catch (GenericApiException e) {
      Alert alert = AlertUtils.getUnexpectedErrorAlert();
      alert.showAndWait();
    }

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

}
