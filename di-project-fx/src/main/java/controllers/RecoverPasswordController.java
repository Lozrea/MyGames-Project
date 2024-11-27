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

  @FXML
  private Button btnRecoverPassword;

  @FXML
  private Label lblBackToLogin;

  @FXML
  private Label lblEmailSent;

  @FXML
  private Pane paneIcon;

  @FXML
  private TextField txtEmail;

  @FXML
  void handleBackToLogin(MouseEvent event) {
    mainApp.initLoginView();
  }

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
