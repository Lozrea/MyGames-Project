package controllers.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/** Clase de utilidades para alertas */
public class AlertUtils {

  /** Constructor privado para evitar inicialización */
  private AlertUtils() {
  }

  /**
   * Genera una alerta preparada para indicar al usuario que ocurrió un error inesperado
   * 
   * @return Alert
   */
  public static Alert getUnexpectedErrorAlert() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Error inesperado");
    alert
        .setContentText(
            """
              Ocurrió un error inesperado en la aplicación.
              Por favor, vuelva a intentarlo y, en caso de que el problema persista, contacte con nosotros para poder darle una solución lo antes posible.
              """);
    return alert;
  }

  /**
   * Genera una alerta preparada para indicar al usuario que se ha podido insertar correctamente un nuevo juego en la
   * API
   * 
   * @return Alert
   */
  public static Alert getInsertedGameAlert() {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Juego creado");
    alert.setHeaderText("Se ha añadido correctamente el juego en el sistema");
    return alert;
  }

  /**
   * Genera una alerta preparada para indicar al usuario que se ha añadido un nuevo juego a la biblioteca
   * 
   * @return Alert
   */
  public static Alert getAddedGameToLibraryAlert() {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Juego añadido a la biblioteca");
    alert.setHeaderText("Se ha añadido un nuevo juego a la biblioteca");

    return alert;
  }

  /**
   * Genera una alerta preparada para indicar al usuario que se ha eliminado un juego de la biblioteca
   * 
   * @return Alert
   */
  public static Alert getDeletedGameFromLibraryAlert() {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Juego eliminado de la biblioteca");
    alert.setHeaderText("Juego eliminado de la biblioteca");

    return alert;
  }

}
