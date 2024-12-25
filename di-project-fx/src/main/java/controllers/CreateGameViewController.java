package controllers;

import java.util.List;

import org.controlsfx.control.CheckComboBox;

import controllers.utils.AlertUtils;
import controllers.utils.wrappers.GenreWrapper;
import controllers.utils.wrappers.PlatformSimpleWrapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Game;
import models.Genre;
import models.Platform;
import models.PlatformSimple;
import models.error.GenericApiException;
import models.feign.OpenFeignConstants;
import models.feign.client.GameClient;

/** Controlador de la ventana modal para la creación de un nuevo juego */
public class CreateGameViewController {

  /** Stage definido de la vista */
  private Stage stage;

  /** Cliente de juegos */
  private GameClient gameClient;

  /** Lista de géneros disponibles para el filtrado */
  private List<Genre> avaiableGenres;

  /** Lista de plataformas disponibles para el filtrado */
  private List<PlatformSimple> avaiablePlatforms;

  /** ComboBox de los géneros */
  private CheckComboBox<GenreWrapper> cbGenre = new CheckComboBox<>();

  /** ComboBox de las plataformas */
  private CheckComboBox<PlatformSimpleWrapper> cbPlatform = new CheckComboBox<>();

  /** Botón para cancelar la creación */
  @FXML
  private Button btnCancel;

  /** Botón para crear el juego */
  @FXML
  private Button btnCreate;

  /** HBox que contendrá el CheckComboBox de géneros */
  @FXML
  private HBox hboxGenres;

  /** HBox que contendrá el CheckComboBox de plataformas */
  @FXML
  private HBox hboxPlatforms;

  /** Label a mostrar cuando hay campos incorrectos en el formulario */
  @FXML
  private Label lblIncorrectFields;

  /** Selector de fecha de creación del juego */
  @FXML
  private DatePicker releaseDatePicker;

  /** TextArea para la descripción del juego */
  @FXML
  private TextArea txtDescription;

  /** TextField para el nombre del juego */
  @FXML
  private TextField txtName;

  /**
   * Evento para cancelar la creación del juego y volver a la ventana anterior
   * 
   * @param event Evento ocurrido
   */
  @FXML
  void onBtnCancelClicked(ActionEvent event) {
    stage.close();
  }

  /**
   * Evento para crear el juego
   * 
   * @param event Evento ocurrido
   */
  @FXML
  void onBtnCreateClicked(ActionEvent event) {

    if (checkForm()) {
      Game game = getGameFromFields();

      try {
        gameClient.saveGame(OpenFeignConstants.SECRET_KEY, game);

        Alert insertedGameAlert = AlertUtils.getInsertedGameAlert();
        insertedGameAlert.showAndWait();

        stage.close();

      } catch (GenericApiException e) {
        Alert unexpectedErrorAlert = AlertUtils.getUnexpectedErrorAlert();
        unexpectedErrorAlert.showAndWait();
      }

    } else {
      lblIncorrectFields.setVisible(true);
      new Timeline(new KeyFrame(Duration.millis(3000), ev -> lblIncorrectFields.setVisible(false))).playFromStart();
    }

  }

  /** Carga de datos básicos en la vista - Datos de los combo box */
  public void chargeBaseData() {

    // Carga de CheckComboBox
    hboxGenres.getChildren().add(cbGenre);
    hboxPlatforms.getChildren().add(cbPlatform);

    cbGenre.getItems().addAll(avaiableGenres.stream().map(GenreWrapper::new).toList());
    cbPlatform.getItems().addAll(avaiablePlatforms.stream().map(PlatformSimpleWrapper::new).toList());

    // Propiedades de vista
    cbGenre.setMaxHeight(40);
    cbGenre.setPrefHeight(40);
    cbGenre.setPrefWidth(700);

    cbPlatform.setMaxHeight(40);
    cbPlatform.setPrefHeight(40);
    cbPlatform.setPrefWidth(700);

  }

  /**
   * Setter - Stage
   * 
   * @param stage Stage
   */
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /**
   * Setter - Cliente de juegos
   * 
   * @param gameClient Cliente de juegos
   */
  public void setGameClient(GameClient gameClient) {
    this.gameClient = gameClient;
  }

  /**
   * Setter - avaiableGenres
   * 
   * @param avaiableGenres Géneros disponibles para la búsqueda
   */
  public void setAvaiableGenres(List<Genre> avaiableGenres) {
    this.avaiableGenres = avaiableGenres;
  }

  /**
   * Setter - avaiablePlatforms
   * 
   * @param avaiablePlatforms Plataformas disponibles para la búsqueda
   */
  public void setAvaiablePlatforms(List<PlatformSimple> avaiablePlatforms) {
    this.avaiablePlatforms = avaiablePlatforms;
  }

  /**
   * Checkea el formulario y verifica que todos los campos hayan sido rellenados
   * 
   * @return boolean
   */
  private boolean checkForm() {

    return !txtName.getText().isBlank() && !txtDescription.getText().isBlank() && releaseDatePicker != null
        && !cbGenre.getCheckModel().getCheckedItems().isEmpty()
        && !cbPlatform.getCheckModel().getCheckedItems().isEmpty();
  }

  /**
   * Obtiene un juego a partir de los campos rellenados por el usuario
   * 
   * @return Game
   */
  private Game getGameFromFields() {
    // Plataformas y géneros
    List<Platform> platforms = cbPlatform.getCheckModel().getCheckedItems().stream().map(pw -> {

      Platform platform = new Platform();
      platform.setId(pw.getPlatform().getId());
      platform.setName(pw.getPlatform().getName());

      return platform;
    }).toList();

    List<Genre> genres = cbGenre.getCheckModel().getCheckedItems().stream().map(gw -> gw.getGenre()).toList();

    // Construcción del juego e inserción
    Game game = new Game();
    game.setName(txtName.getText());
    game.setDescription(txtDescription.getText());
    game.setPlatforms(platforms);
    game.setGenres(genres);
    return game;
  }

}
