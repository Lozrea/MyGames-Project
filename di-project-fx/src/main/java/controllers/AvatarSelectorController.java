package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import controllers.utils.components.Images;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/** Controlador del selector de avatares */
public class AvatarSelectorController implements Initializable {

  /** Número total de avatares del sistema */
  private static final int TOTAL_AVATAR_QUANTITY = 6;

  /** Número de avatares por cada fila */
  private static final int AVATARS_PER_ROW = 4;

  /** Número de avatar seleccionado */
  private int avatarNumber;

  /** Controlador del registro */
  private RegisterController registerController;
  
  /** BorderPane fondo */
  @FXML
  private BorderPane background;

  /** GridPane con el selector de avatares */
  @FXML
  private GridPane gridSelector;

  /** Label para volver al registro */
  @FXML
  private Label lblGoBackToRegister;

  /**
   * Manejador del evento para volver al registro
   * 
   * @param event Evento ocurrido
   */
  @FXML
  void handleGoBackToRegister(MouseEvent event) {
    registerController.closeAvatarSelector();
  }

  /**
   * Maneja un click realizado sobre una imagen de avatar. Se marca en la ventana actual y se modifica en la original
   * 
   * @param imageViewSelected Imagen seleccionada
   * @param avatarNumber      Número de avatar seleccionado
   */
  void handleImageMouseClicked(ImageView imageViewSelected, int avatarNumber) {

    this.avatarNumber = avatarNumber;
    registerController.setAvatarViewImage(this.avatarNumber);
    // TODO hay que añadir aquí un borde por css a la imagen para saber que está marcada
    // Quitar la clase de selección de todas las imágenes
    gridSelector.getChildren().forEach(node -> {
        if (node instanceof ImageView) {
            node.getStyleClass().remove("avatar-selected");
        }
    });

    // Añadir la clase de selección a la imagen seleccionada
    imageViewSelected.getStyleClass().add("avatar-selected");

  }

  /**
   * Setter - registerController
   * 
   * @param registerController Controlador de la página de registro
   */
  public void setRegisterController(RegisterController registerController) {
    this.registerController = registerController;
  }

  /**
   * Establece el número de avatar actual
   * 
   * @param avatarNumber Número de avatar a establecer
   */
  public void setCurrentAvatar(int avatarNumber) {
    this.avatarNumber = avatarNumber;
    // TODO hay que añadir aquí un borde por css a la imagen para saber que está marcada
    // Quitar la clase de selección de todas las imágenes
    gridSelector.getChildren().forEach(node -> {
        if (node instanceof ImageView) {
            node.getStyleClass().remove("avatar-selected");
        }
    });

    // Añadir la clase de selección al avatar correspondiente
    gridSelector.getChildren().forEach(node -> {
        if (node instanceof ImageView && gridSelector.getChildren().indexOf(node) == avatarNumber) {
            node.getStyleClass().add("avatar-selected");
        }
    });
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // Se inicializa el grid de avatares
    for (int i = 0; i < TOTAL_AVATAR_QUANTITY; i++) {

      // Se crea cada ImageView
      Image image = new Image(AvatarSelectorController.class.getResourceAsStream(String.format(Images.AVATAR, i)));

      ImageView imageView = new ImageView(image);
      imageView.setFitHeight(100);
      imageView.setFitWidth(100);
      imageView.setPreserveRatio(true);
      imageView.setCursor(Cursor.HAND);

      // Evento de click
      final int currentAvatar = i;
      imageView.setOnMouseClicked(event -> handleImageMouseClicked(imageView, currentAvatar));

      // Se agrega al grid
      gridSelector.add(imageView, i % AVATARS_PER_ROW, i / AVATARS_PER_ROW);

    }

  }

}
