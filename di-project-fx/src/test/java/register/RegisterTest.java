package register;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

import controllers.RegisterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import models.AppUser;
import models.error.ApiError;
import models.error.ResourceNotFoundException;
import models.feign.OpenFeignConstants;
import models.feign.client.UserClient;

/** Tests de Registro */
@ExtendWith(ApplicationExtension.class)
class RegisterTest {

  /** Email en uso */
  private static final String EMAIL_IN_USE = "prueba@gmail.com";

  /** Username en uso */
  private static final String USERNAME_IN_USE = "prueba";

  /** Email libre */
  private static final String EMAIL_FREE = "pruebanoenuso@gmail.com";

  /** Username libre */
  private static final String USERNAME_FREE = "pruebanoenuso";

  /** Loader de la vista */
  FXMLLoader loader;

  /** Cliente del usuario */
  @Mock
  private UserClient userClient;

  /** Método que prepara los atributos necesarios para los test e inicia los mocks */
  @BeforeEach
  public void prepareAttributes() {

    MockitoAnnotations.openMocks(this);

    // Respuestas a las distintas llamadas
    Mockito.when(userClient.getUserByEmail(OpenFeignConstants.SECRET_KEY, EMAIL_IN_USE)).thenReturn(new AppUser());
    Mockito
        .when(userClient.getUserByEmail(OpenFeignConstants.SECRET_KEY, EMAIL_FREE))
        .thenThrow(new ResourceNotFoundException(new ApiError()));

    Mockito
        .when(userClient.getUserByUsername(OpenFeignConstants.SECRET_KEY, USERNAME_IN_USE))
        .thenReturn(new AppUser());

    Mockito
        .when(userClient.getUserByUsername(OpenFeignConstants.SECRET_KEY, USERNAME_FREE))
        .thenThrow(new ResourceNotFoundException(new ApiError()));

    // Obtener el controlador y configurar el cliente
    RegisterController registerController = loader.getController();
    registerController.setUserClient(userClient);

  }

  /**
   * Inicia la ventana de registro
   * 
   * @param stage Stage principal
   */
  @Start
  public void start(Stage stage) {
    try {
      loader = new FXMLLoader(getClass().getResource("/views/Register.fxml"));
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

  /** Test que verifica que existe el botón de registro */
  @Test
  void testVerifyRegisterButton() {
    FxAssert.verifyThat("#btnRegister", LabeledMatchers.hasText("Registrar"));
  }

  /**
   * Test que verifica que existe el botón de registro
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @Test
  void testClickRegisterButtonWithEmptyFields() throws InterruptedException {
    FxRobot fxRobot = new FxRobot();
    fxRobot.clickOn("#btnRegister");

    Thread.sleep(500);

    FxAssert.verifyThat("#lblRegisterError", NodeMatchers.isVisible());
    FxAssert
        .verifyThat("#lblRegisterError",
            LabeledMatchers.hasText("Por favor revise los campos antes de continuar con el registro"));
  }

  /**
   * Test que verifica que se notifica cuando el email está en uso
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @Test
  void testWriteEmailInUse() throws InterruptedException {

    FxRobot fxRobot = new FxRobot();

    fxRobot.clickOn("#txtEmail");
    fxRobot.write(EMAIL_IN_USE);

    Thread.sleep(1200);

    FxAssert.verifyThat("#lblUsedEmail", NodeMatchers.isVisible());
    FxAssert.verifyThat("#lblUsedEmail", LabeledMatchers.hasText(String.format("Email %s en uso", EMAIL_IN_USE)));
  }

  /**
   * Test que verifica que se notifica cuando el email está libre
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @Test
  void testWriteEmailFree() throws InterruptedException {

    FxRobot fxRobot = new FxRobot();

    fxRobot.clickOn("#txtEmail");
    fxRobot.write(EMAIL_FREE);

    Thread.sleep(1200);

    FxAssert.verifyThat("#lblUsedEmail", NodeMatchers.isVisible());
    FxAssert.verifyThat("#lblUsedEmail", LabeledMatchers.hasText(String.format("Email %s disponible", EMAIL_FREE)));
  }

  /**
   * Test que verifica que se notifica cuando el email no es válido
   * 
   * @param email Email a verificar
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @ParameterizedTest
  @ValueSource(strings = { "email", "email@", "email@email", "@email.com", "email@.com" })
  void testNotValidEmail(String email) throws InterruptedException {

    FxRobot fxRobot = new FxRobot();

    fxRobot.clickOn("#txtEmail");
    fxRobot.write(email);

    Thread.sleep(1200);

    FxAssert.verifyThat("#lblUsedEmail", NodeMatchers.isVisible());
    FxAssert.verifyThat("#lblUsedEmail", LabeledMatchers.hasText("El formato de email no es correcto"));
  }

  /**
   * Test que verifica que se notifica cuando el username está en uso
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @Test
  void testWriteUsernameInUse() throws InterruptedException {

    FxRobot fxRobot = new FxRobot();

    fxRobot.clickOn("#txtUsername");
    fxRobot.write(USERNAME_IN_USE);

    Thread.sleep(1200);

    FxAssert.verifyThat("#lblUsedUsername", NodeMatchers.isVisible());
    FxAssert
        .verifyThat("#lblUsedUsername",
            LabeledMatchers.hasText(String.format("Nombre de usuario %s en uso", USERNAME_IN_USE)));
  }

  /**
   * Test que verifica que se notifica cuando el email está libre
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @Test
  void testWriteUsernameFree() throws InterruptedException {

    FxRobot fxRobot = new FxRobot();

    fxRobot.clickOn("#txtUsername");
    fxRobot.write(USERNAME_FREE);

    Thread.sleep(1200);

    FxAssert.verifyThat("#lblUsedUsername", NodeMatchers.isVisible());
    FxAssert
        .verifyThat("#lblUsedUsername",
            LabeledMatchers.hasText(String.format("Nombre de usuario %s disponible", USERNAME_FREE)));
  }

  /**
   * Test que verifica que se notifica cuando el username no es válido
   * 
   * @param username Username a verificar
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @ParameterizedTest
  @ValueSource(strings = { "u.", "@@", "username!", "usernameusernameusername" })
  void testNotValidUsername(String username) throws InterruptedException {

    FxRobot fxRobot = new FxRobot();

    fxRobot.clickOn("#txtUsername");
    fxRobot.write(username);

    Thread.sleep(1200);

    FxAssert.verifyThat("#lblUsedUsername", NodeMatchers.isVisible());
    FxAssert
        .verifyThat("#lblUsedUsername",
            LabeledMatchers.hasText("Debe tener entre 1 y 20 caracteres (letras, números, _)"));
  }

  /**
   * Test que verifica que se notifica cuando el nombre no es válido
   * 
   * @param name Nombre a verificar
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @ParameterizedTest
  @ValueSource(strings = { "nombre.", ".nombre", "!nombre", "nombre !", "nombre3" })
  void testNotValidName(String name) throws InterruptedException {

    FxRobot fxRobot = new FxRobot();

    fxRobot.clickOn("#txtName");
    fxRobot.write(name);

    Thread.sleep(1200);

    FxAssert.verifyThat("#lblIncorrectName", NodeMatchers.isVisible());
  }

  /**
   * Test que verifica que se notifica cuando la contraseña no es válida
   * 
   * @param password Contraseña a verificar
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @ParameterizedTest
  @ValueSource(
      strings = { "passwor", "Passw0&", "Passw0r1Passw0r1Pass", "Password&", "0assword&", "0PASSWORD&", "0PASSword@" })
  void testNotValidPassword(String password) throws InterruptedException {

    FxRobot fxRobot = new FxRobot();

    fxRobot.clickOn("#txtPassword");
    fxRobot.write(password);

    Thread.sleep(1200);

    FxAssert.verifyThat("#lblIncorrectPassword", NodeMatchers.isVisible());
  }

  /**
   * Test que verifica que se notifica cuando las contraseñas no coinciden
   * 
   * @throws InterruptedException En caso de error durante la espera del hilo
   */
  @Test
  void testNotValidRepeatPassword() throws InterruptedException {

    FxRobot fxRobot = new FxRobot();

    fxRobot.clickOn("#txtPassword");
    fxRobot.write("&Password0");

    fxRobot.clickOn("#txtConfirmPassword");
    fxRobot.write("!Password");

    Thread.sleep(1200);

    FxAssert.verifyThat("#lblSamePasswords", NodeMatchers.isVisible());
  }

}
