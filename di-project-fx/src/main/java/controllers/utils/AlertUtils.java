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

}
