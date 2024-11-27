package controllers.utils;

import application.MainApp;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** Clase de utilidades para componentes */
public class ComponentUtils {

  /**
   * Obtiene un Spinner de carga que indica al usuario que se está esperando a que finalice una acción
   * 
   * @return ImageView
   */
  public static ImageView getSpinner() {

    Image image = new Image(MainApp.class.getResourceAsStream("/images/white-spinner.gif"));

    ImageView imageView = new ImageView();
    imageView.setImage(image);
    imageView.setFitWidth(60);
    imageView.setFitHeight(60);

    return imageView;
  }

}
