package controllers.utils.components;

import javafx.animation.TranslateTransition;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/** Switch que funciona a modo de slider para los temas claro / oscuro. Será un único toggle */
public class ToggleSwitch {

  /** Constructor privado. Evita instancias de clase */
  private ToggleSwitch() {
  }

  /** Estado del toggle */
  private static boolean isOn = false;

  /** Rectángulo de fondo del toggle */
  private static Rectangle background;

  /** Slider / Pieza movible del toggle */
  private static Circle slider;

  /**
   * Obtiene un toggle switch
   * 
   * @param startingOn Indica si el Switch debe comenzar On (Tema claro) u Off (tema oscuro)
   * 
   * @return StackPane
   */
  public static StackPane getToggleSwitch(boolean startingOn) {

    isOn = startingOn;

    // Fondo del switch
    background = new Rectangle(40, 20);
    background.setArcWidth(20);
    background.setArcHeight(20);
    background.setFill(Color.LIGHTGRAY);

    // Pieza movible del toggle
    slider = new Circle(9);
    slider.setFill(Color.WHITE);
    slider.setCursor(Cursor.HAND);

    // Contenedor para el switch
    StackPane toggleSwitch = new StackPane(background, slider);
    toggleSwitch.setMinSize(40, 20);

    // Evento de clic del toggle
    slider.setOnMouseClicked(event -> {
      isOn = !isOn;
      updateToggle(isOn);
    });

    updateToggle(isOn);

    return toggleSwitch;
  }

  /**
   * Indica la posición del toggle actualmente
   * 
   * @return boolean - true (tema claro), false (tema oscuro)
   */
  public static boolean isOn() {
    return isOn;
  }

  /**
   * Actualiza el toggle
   * 
   * @param on Indica si el toggle está en on (tema claro) u off (tema oscuro)
   */
  private static void updateToggle(boolean on) {

    isOn = on;

    // Cambia el color del fondo según el estado
    background.setFill(isOn ? Color.LIGHTGRAY : Color.rgb(254, 105, 39));

    // Animar el círculo deslizante
    TranslateTransition transition = new TranslateTransition(Duration.millis(200), slider);
    transition.setToX(isOn ? 10 : -10);
    transition.play();
  }
}