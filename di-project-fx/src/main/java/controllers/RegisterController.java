package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.MainApp;
import controllers.utils.AlertUtils;
import controllers.utils.components.Components;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.AppUser;
import models.AppUserCreate;
import models.error.GenericApiException;
import models.error.ResourceNotFoundException;
import models.feign.OpenFeignConstants;
import models.feign.client.UserClient;
import models.validation.AppUserValidation;

/** Controlador del registro */
public class RegisterController implements Initializable {

  /** Ruta base hacia los avatares */
  private static final String AVATAR_BASE_PATH = "/images/Avatares/%d.png";

  /** Color usado para formularios con campo incorrecto */
  private static final Color INCORRECT_FIELD_COLOR = Color.color(0.75, 0, 0);

  /** Color usado para formularios con campo correcto */
  private static final Color CORRECT_FIELD_COLOR = Color.color(0, 0.35, 0);

  /** Cliente para la obtención de datos de usuarios */
  private UserClient userClient;

  /** Aplicación principal */
  private MainApp mainApp;

  /** Buscador de un usuario dado el email con delay */
  private Timeline delayTimelineEmail;

  /** Buscador de un usuario dado el nombre de usuario con delay */
  private Timeline delayTimelineUsername;

  /** Comprobador de contraseñas con delay */
  private Timeline delayTimelinePassword;

  /** Comprobador del nombre con delay */
  private Timeline delayTimelineName;

  /** Número de avatar elegido por el usuario */
  private int avatarNumber = 0;

  /** Stage del selector de imágenes */
  private Stage imageSelectorStage;

  /** Spinner de carga */
  private ImageView spinner;

  @FXML
  private Button btnRegister;

  @FXML
  private ImageView imageSelector;

  @FXML
  private Label lblAlreadyHasAccount;

  @FXML
  private Label lblIncorrectName;

  @FXML
  private Label lblIncorrectPassword;

  @FXML
  private Label lblRegisterError;

  @FXML
  private Label lblSamePasswords;

  @FXML
  private Label lblUsedEmail;

  @FXML
  private Label lblUsedUsername;

  @FXML
  private Pane paneIcon;

  @FXML
  private PasswordField txtConfirmPassword;

  @FXML
  private TextField txtEmail;

  @FXML
  private TextField txtName;

  @FXML
  private PasswordField txtPassword;

  @FXML
  private TextField txtUsername;

  /** Constructor. Establece las acciones con delay */
  public RegisterController() {

    // Configuración de eventos con delay para los campos
    delayTimelineEmail = generateDelayTimeLineEmail();
    delayTimelineUsername = generateDelayTimeLineUsername();
    delayTimelineName = generateDelaytimelineName();
    delayTimelinePassword = generateDelayTimelinePassword();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // Se establecen las imágenes
    Image image = new Image(RegisterController.class.getResourceAsStream(String.format(AVATAR_BASE_PATH, 0)));
    imageSelector.setImage(image);

    spinner = Components.getSpinner();
  }

  @FXML
  void handleImageSelectorClicked(MouseEvent event) {

    imageSelectorStage = new Stage();
    imageSelectorStage.setWidth(650);
    imageSelectorStage.setHeight(750);
    imageSelectorStage.setTitle("Selector de Avatar");
    imageSelectorStage.initModality(Modality.APPLICATION_MODAL);

    try {
      FXMLLoader loader = new FXMLLoader();

      loader.setLocation(MainApp.class.getResource("/views/AvatarSelector.fxml"));
      BorderPane avatarSelectorLayout = (BorderPane) loader.load();

      // Controlador del login
      AvatarSelectorController avatarSelectorController = loader.getController();
      avatarSelectorController.setCurrentAvatar(avatarNumber);
      avatarSelectorController.setRegisterController(this);

      Scene scene = new Scene(avatarSelectorLayout);
      imageSelectorStage.setScene(scene);
      imageSelectorStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @FXML
  void handleRegister(ActionEvent event) {

    btnRegister.setText("");
    btnRegister.setGraphic(spinner);

    try {
      // Se obtiene el usuario registrado y se abre la ventana principal de la app
      AppUser user = getRegisteredUser();
      mainApp.setAppUser(user);

      mainApp.initHomeView();

      // Error de validación de usuario
    } catch (IllegalArgumentException e) {

      lblRegisterError.setVisible(true);

      new Timeline(new KeyFrame(Duration.seconds(3), ev -> lblRegisterError.setVisible(false))).playFromStart();

      // Error general de la API
    } catch (GenericApiException e) {
      Alert alert = AlertUtils.getUnexpectedErrorAlert();
      alert.showAndWait();
    }

    btnRegister.setText("Registrar");
    btnRegister.setGraphic(null);
  }

  @FXML
  void handleTxtEmailKeyPressed(KeyEvent event) {

    // Se para el Timeline por si estuviera funcionando y se relanza
    delayTimelineEmail.stop();

    if (txtEmail.getText() != null && !txtEmail.getText().isBlank()) {
      delayTimelineEmail.playFromStart();
    }
  }

  @FXML
  void handleTxtUsernameKeyPressed(KeyEvent event) {

    // Se para el Timeline por si estuviera funcionando y se relanza
    delayTimelineUsername.stop();

    if (txtUsername.getText() != null && !txtUsername.getText().isBlank()) {
      delayTimelineUsername.playFromStart();
    }
  }

  @FXML
  void handleTxtNameKeyPressed(KeyEvent event) {

    // Se para el Timeline por si estuviera funcionando y se relanza
    delayTimelineName.stop();

    if (txtName.getText() != null && !txtName.getText().isBlank()) {
      delayTimelineName.playFromStart();
    }
  }

  @FXML
  void lblAlreadyHasAccountClicked(MouseEvent event) {
    mainApp.initLoginView();
  }

  @FXML
  void handleTxtPasswordKeyPressed(KeyEvent event) {

    // Se para el Timeline por si estuviera funcionando y se relanza
    delayTimelinePassword.stop();

    if (txtPassword.getText() != null && !txtPassword.getText().isBlank()) {
      delayTimelinePassword.playFromStart();
    }
  }

  /**
   * Establece la imagen del avatar elegida por el usuario en la vista
   * 
   * @param number Número de avatar
   */
  public void setAvatarViewImage(int number) {

    avatarNumber = number;
    imageSelector
        .setImage(new Image(RegisterController.class.getResourceAsStream(String.format(AVATAR_BASE_PATH, number))));
  }

  /** Cierra el stage del selector de avatar */
  public void closeAvatarSelector() {
    imageSelectorStage.close();
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
   * Genera el Timeline con delay para la escritura del campo email
   * 
   * @return Timeline
   */
  private Timeline generateDelayTimeLineEmail() {
    return new Timeline(new KeyFrame(Duration.millis(700), ev -> {

      // Se informa al usuario en función de si el email está o no disponible
      try {
        AppUserValidation.validateEmail(txtEmail.getText());

        userClient.getUserByEmail(OpenFeignConstants.SECRET_KEY, txtEmail.getText());
        lblUsedEmail.setText(String.format("Email %s en uso", txtEmail.getText()));
        lblUsedEmail.setTextFill(INCORRECT_FIELD_COLOR);

        // Error de formato en el email
      } catch (IllegalArgumentException e) {
        lblUsedEmail.setText("El formato de email no es correcto");
        lblUsedEmail.setTextFill(INCORRECT_FIELD_COLOR);

        // No hay coincidencia de email
      } catch (ResourceNotFoundException e) {
        lblUsedEmail.setText(String.format("Email %s disponible", txtEmail.getText()));
        lblUsedEmail.setTextFill(CORRECT_FIELD_COLOR);
      }

      lblUsedEmail.setVisible(true);
    }));
  }

  /**
   * Genera el Timeline con delay para la escritura del campo username
   * 
   * @return Timeline
   */
  private Timeline generateDelayTimeLineUsername() {
    return new Timeline(new KeyFrame(Duration.millis(700), ev -> {

      // Se informa al usuario en función de si el nombre de usuario está o no disponible
      try {
        AppUserValidation.validateUsername(txtUsername.getText());

        userClient.getUserByUsername(OpenFeignConstants.SECRET_KEY, txtUsername.getText());
        lblUsedUsername.setText(String.format("Nombre de usuario %s en uso", txtUsername.getText()));
        lblUsedUsername.setTextFill(INCORRECT_FIELD_COLOR);

        // Formato incorrecto
      } catch (IllegalArgumentException e) {
        lblUsedUsername.setText("Debe tener entre 1 y 20 caracteres (letras, números, _)");
        lblUsedUsername.setTextFill(INCORRECT_FIELD_COLOR);

        // No hay coincidencia de username
      } catch (ResourceNotFoundException e) {
        lblUsedUsername.setText(String.format("Nombre de usuario %s disponible", txtUsername.getText()));
        lblUsedUsername.setTextFill(CORRECT_FIELD_COLOR);
      }

      lblUsedUsername.setVisible(true);
    }));
  }

  /**
   * Genera el timeline con delay para la escritura del campo nombre
   * 
   * @return Timeline
   */
  private Timeline generateDelaytimelineName() {

    return new Timeline(new KeyFrame(Duration.millis(700), ev -> {

      try {
        AppUserValidation.validateName(txtName.getText());
        lblIncorrectName.setVisible(false);

      } catch (IllegalArgumentException e) {
        lblIncorrectName.setVisible(true);
      }

    }));
  }

  /**
   * Genera el Timeline con delay para la escritura de los campos contraseña
   * 
   * @return Timeline
   */
  private Timeline generateDelayTimelinePassword() {

    return new Timeline(new KeyFrame(Duration.millis(700), ev -> {
      String password = txtPassword.getText();
      String confirmPassword = txtConfirmPassword.getText();

      try {
        AppUserValidation.validatePassword(password);
        lblIncorrectPassword.setVisible(false);
        lblSamePasswords.setVisible(!password.equals(confirmPassword));

      } catch (IllegalArgumentException e) {
        lblIncorrectPassword.setVisible(true);
      }
    }));
  }

  /**
   * Obtiene el usuario registrado a partir de los campos establecidos
   * 
   * @return AppUser
   */
  private AppUser getRegisteredUser() {

    AppUserCreate userToLogin = new AppUserCreate();
    userToLogin.setAvatar(avatarNumber);
    userToLogin.setUsername(AppUserValidation.validateUsername(txtUsername.getText()));
    userToLogin.setName(AppUserValidation.validateName(txtName.getText()));
    userToLogin.setPassword(AppUserValidation.validatePassword(txtPassword.getText()));
    userToLogin.setEmail(AppUserValidation.validateEmail(txtEmail.getText()));

    return userClient.createUser(OpenFeignConstants.SECRET_KEY, userToLogin);
  }

}
