package login;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import controllers.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import models.AppUser;
import models.AppUserCreate;
import models.error.ApiError;
import models.error.ForbiddenAccessException;
import models.feign.OpenFeignConstants;
import models.feign.client.UserClient;

/** Tests de Login */
@ExtendWith(ApplicationExtension.class)
class LoginTest {

  /** Usuario válido */
  private static final String VALID_USERNAME = "usuario_valido";
  private static final String VALID_PASSWORD = "Password123!";

  /** Usuario inválido */
  private static final String INVALID_USERNAME = "usuario_incorrecto";
  private static final String INVALID_PASSWORD = "password_erronea";

  /** Usuario no rellenado */
  private static final String NOT_FULFILLED_USERNAME = "";
  private static final String NOT_FULFILLED_PASSWORD = "";

  /** Loader de la vista */
  FXMLLoader loader;

  /** Cliente del usuario */
  @Mock
  private UserClient userClient;

  /**
   * Método que prepara los atributos necesarios para los test e inicia los mocks
   */
  @BeforeEach
  public void prepareAttributes() {

    MockitoAnnotations.openMocks(this);

    // Configurar respuestas del mock para usuarios
    AppUser validUser = new AppUser();
    validUser.setUsername(VALID_USERNAME);

    AppUserCreate validLogin = new AppUserCreate();
    validLogin.setUsername(VALID_USERNAME);
    validLogin.setPassword(VALID_PASSWORD);

    AppUserCreate invalidLogin = new AppUserCreate();
    invalidLogin.setUsername(INVALID_USERNAME);
    invalidLogin.setPassword(INVALID_PASSWORD);

    AppUserCreate notFulFilledLogin = new AppUserCreate();
    notFulFilledLogin.setUsername(NOT_FULFILLED_USERNAME);
    notFulFilledLogin.setPassword(NOT_FULFILLED_PASSWORD);
    
    // Simular error de autenticación
    Mockito.when(userClient.login(OpenFeignConstants.SECRET_KEY, invalidLogin))
        .thenThrow(new ForbiddenAccessException(new ApiError()));

    // Simular intento de inicio de sesión sin rellenar los campos
    Mockito.when(userClient.login(OpenFeignConstants.SECRET_KEY, notFulFilledLogin))
        .thenThrow(new ForbiddenAccessException(new ApiError()));

    // Obtener el controlador y configurar el cliente
    LoginController loginController = loader.getController();
    loginController.setUserClient(userClient);

  }

  /**
   * Inicia la ventana de login
   * 
   * @param stage Stage principal
   */
  @Start
  public void start(Stage stage) {
    try {
      loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
      Parent mainNode = loader.load();

      stage.setScene(new Scene(mainNode));
      stage.show();
      stage.toFront();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Método que cierra el estado del robot de pruebas
   * 
   * @throws Exception En caso de que ocurra un error
   */
  @AfterEach
  void releaseRobot() throws Exception {
    FxToolkit.hideStage();
    FxRobot fxRobot = new FxRobot();
    fxRobot.release(new KeyCode[] {});
    fxRobot.release(new MouseButton[] {});
  }

  /** Test que verifica que existe el botón de inicio de sesión */
  @Test
  void testVerifySignInButton() {
    FxAssert.verifyThat("#btnSignIn", LabeledMatchers.hasText("Iniciar sesion"));
  }

  /**
   * Test que verifica que al intentar iniciar sesión con campos vacíos se muestra
   * un mensaje de error.
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @Test
  void testClickSignInButtonWithEmptyFields() throws InterruptedException {
    FxRobot fxRobot = new FxRobot();
    fxRobot.clickOn("#btnSignIn");

    Thread.sleep(500);

    FxAssert.verifyThat("#lblIncorrectSignIn", NodeMatchers.isVisible());
    FxAssert.verifyThat("#lblIncorrectSignIn", LabeledMatchers.hasText("Username y/o contraseña incorrecto"));
  }

  /**
   * Test que verifica que se notifica cuando el login es incorrecto.
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @Test
  void testInvalidLogin() throws InterruptedException {
    FxRobot fxRobot = new FxRobot();

    fxRobot.clickOn("#txtUsername");
    fxRobot.write(INVALID_USERNAME);

    fxRobot.clickOn("#txtPassword");
    fxRobot.write(INVALID_PASSWORD);

    fxRobot.clickOn("#btnSignIn");

    Thread.sleep(1200);

    FxAssert.verifyThat("#lblIncorrectSignIn", NodeMatchers.isVisible());
    FxAssert.verifyThat("#lblIncorrectSignIn", LabeledMatchers.hasText("Username y/o contraseña incorrecto"));
  }

}
