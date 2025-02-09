package controllers;

import java.util.List;

import application.MainApp;
import controllers.utils.components.Images;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.UserGame;
import models.feign.OpenFeignConstants;
import models.feign.client.UserGameClient;

/** Controlador para la vista de comentarios */
public class CommentaryViewController {

  /** Stage de la vista */
  private Stage stage;

  /** UserGame de la vista */
  private UserGame userGame;

  /** Controlador de la vista del controlador */
  private GameViewController gameViewController;

  /** Cliente de asociación entre el juego y el usuario */
  private UserGameClient userGameClient;

  /** Puntuación personal seleccionada sobre las imágenes */
  private Double personalRatingSelected;

  /** Clase principal */
  private MainApp mainApp;

  /** lista con las imágenes de estrellas para la puntuación */
  private List<ImageView> stars;

  /** Botón para cancelar la acción */
  @FXML
  private Button btnCancel;

  /** Botón para guardar la reseña */
  @FXML
  private Button btnSave;

  /** ImageView para media estrella */
  @FXML
  private ImageView img05Star;

  /** ImageView para una estrella y media */
  @FXML
  private ImageView img15Star;

  /** ImageView para una estrella */
  @FXML
  private ImageView img1Star;

  /** ImageView para dos estrellas y media */
  @FXML
  private ImageView img25Star;

  /** ImageView para dos estrellas */
  @FXML
  private ImageView img2Star;

  /** ImageView para tres estrellas y media */
  @FXML
  private ImageView img35Star;

  /** ImageView para tres estrellas */
  @FXML
  private ImageView img3Star;

  /** ImageView para cuatro estrellas y media */
  @FXML
  private ImageView img45Star;

  /** ImageView para cuatro estrellas */
  @FXML
  private ImageView img4Star;

  /** ImageView para cinco estrellas */
  @FXML
  private ImageView img5Star;

  /** Label para la puntuación */
  @FXML
  private Label lblScore;

  /** Texto con la reseña */
  @FXML
  private TextArea txtReview;

  /**
   * Botón para cancelar la acción
   * 
   * @param event Evento
   */
  @FXML
  void onBtnCancelClicked(ActionEvent event) {
    this.stage.close();
  }

  /**
   * Almacena una reseña sobre el juego
   * 
   * @param event Evento
   */
  @FXML
  void onBtnSaveClicked(ActionEvent event) {

    userGame.setPersonalRating(personalRatingSelected);
    userGame.setCommentary(txtReview.getText() != null && !txtReview.getText().isBlank() ? txtReview.getText() : "");

    if (personalRatingSelected != null || !txtReview.getText().isBlank()) {
      UserGame savedUserGame = userGameClient.saveUserGame(OpenFeignConstants.SECRET_KEY, userGame);
      gameViewController.setUserGame(savedUserGame);
      gameViewController
          .getLblPersonalScore()
          .setText(personalRatingSelected != null ? Double.toString(personalRatingSelected) : "-");
      gameViewController.getTxtCommentary().setText(txtReview.getText());

      mainApp
          .getUserGameResponse()
          .getGames()
          .replaceAll(ug -> ug.getGame().getId().equals(savedUserGame.getGame().getId()) ? savedUserGame : ug);
    }

    this.stage.close();

  }

  /**
   * Selección de una puntuación de 0.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg05StarClicked(MouseEvent event) {
    personalRatingSelected = 0.5;
    showStars(personalRatingSelected);
  }

  /**
   * Entrada del ratón en la zona de 0.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg05StarEntered(MouseEvent event) {
    showStars(0.5);
  }

  /**
   * Salida del ratón de la zona de 0.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg05StarExited(MouseEvent event) {
    showStars(personalRatingSelected);
  }

  /**
   * Selección de una puntuación de 1.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg15StarClicked(MouseEvent event) {
    personalRatingSelected = 1.5;
    showStars(personalRatingSelected);
  }

  /**
   * Entrada del ratón en la zona de 1.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg15StarEntered(MouseEvent event) {
    showStars(1.5);
  }

  /**
   * Salida del ratón de la zona de 1.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg15StarExited(MouseEvent event) {
    showStars(personalRatingSelected);
  }

  /**
   * Selección de una puntuación de 1.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg1StarClicked(MouseEvent event) {
    personalRatingSelected = 1.0;
    showStars(personalRatingSelected);
  }

  /**
   * Entrada del ratón en la zona de 1.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg1StarEntered(MouseEvent event) {
    showStars(1.0);
  }

  /**
   * Salida del ratón de la zona de 1.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg1StarExited(MouseEvent event) {
    showStars(personalRatingSelected);
  }

  /**
   * Selección de una puntuación de 2.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg25StarClicked(MouseEvent event) {
    personalRatingSelected = 2.5;
    showStars(personalRatingSelected);
  }

  /**
   * Entrada del ratón en la zona de 2.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg25StarEntered(MouseEvent event) {
    showStars(2.5);
  }

  /**
   * Salida del ratón de la zona de 2.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg25StarExited(MouseEvent event) {
    showStars(personalRatingSelected);
  }

  /**
   * Selección de una puntuación de 2.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg2StarClicked(MouseEvent event) {
    personalRatingSelected = 2.0;
    showStars(personalRatingSelected);
  }

  /**
   * Entrada del ratón en la zona de 2.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg2StarEntered(MouseEvent event) {
    showStars(2.0);
  }

  /**
   * Salida del ratón de la zona de 2.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg2StarExited(MouseEvent event) {
    showStars(personalRatingSelected);
  }

  /**
   * Selección de una puntuación de 3.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg35StarClicked(MouseEvent event) {
    personalRatingSelected = 3.5;
    showStars(personalRatingSelected);
  }

  /**
   * Entrada del ratón en la zona de 3.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg35StarEntered(MouseEvent event) {
    showStars(3.5);
  }

  /**
   * Salida del ratón de la zona de 3.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg35StarExited(MouseEvent event) {
    showStars(personalRatingSelected);
  }

  /**
   * Selección de una puntuación de 3.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg3StarClicked(MouseEvent event) {
    personalRatingSelected = 3.0;
    showStars(personalRatingSelected);
  }

  /**
   * Entrada del ratón en la zona de 3.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg3StarEntered(MouseEvent event) {
    showStars(3.0);
  }

  /**
   * Salida del ratón de la zona de 3.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg3StarExited(MouseEvent event) {
    showStars(personalRatingSelected);
  }

  /**
   * Selección de una puntuación de 4.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg45StarClicked(MouseEvent event) {
    personalRatingSelected = 4.5;
    showStars(personalRatingSelected);
  }

  /**
   * Entrada del ratón en la zona de 4.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg45StarEntered(MouseEvent event) {
    showStars(4.5);
  }

  /**
   * Salida del ratón de la zona de 4.5 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg45StarExited(MouseEvent event) {
    showStars(personalRatingSelected);
  }

  /**
   * Selección de una puntuación de 4.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg4StarClicked(MouseEvent event) {
    personalRatingSelected = 4.0;
    showStars(personalRatingSelected);
  }

  /**
   * Entrada del ratón en la zona de 4.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg4StarEntered(MouseEvent event) {
    showStars(4.0);
  }

  /**
   * Salida del ratón de la zona de 4.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg4StarExited(MouseEvent event) {
    showStars(personalRatingSelected);
  }

  /**
   * Selección de una puntuación de 5.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg5StarClicked(MouseEvent event) {
    personalRatingSelected = 5.0;
    showStars(personalRatingSelected);
  }

  /**
   * Selección de una puntuación de 5.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg5StarEntered(MouseEvent event) {
    showStars(5.0);
  }

  /**
   * Salida del ratón de la zona de 5.0 estrellas
   * 
   * @param event Evento
   */
  @FXML
  void onImg5StarExited(MouseEvent event) {
    showStars(personalRatingSelected);
  }

  /**
   * Getter - userGame
   * 
   * @return userGame
   */
  public UserGame getUserGame() {
    return userGame;
  }

  /**
   * Setter - userGame
   * 
   * @param userGame Asociación juego - usuario
   */
  public void setUserGame(UserGame userGame) {
    this.userGame = userGame;
  }

  /**
   * Setter - stage
   * 
   * @param stage Stage de la vista
   */
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /**
   * Setter - gameViewController
   * 
   * @param gameViewController Controlador de la vista del juego
   */
  public void setGameViewController(GameViewController gameViewController) {
    this.gameViewController = gameViewController;
  }

  /**
   * Setter - userGameClient
   * 
   * @param userGameClient Cliente para asociaciones entre usuarios y juegos
   */
  public void setUserGameClient(UserGameClient userGameClient) {
    this.userGameClient = userGameClient;
  }

  /**
   * Setter - mainApp
   * 
   * @param mainApp Clase principal
   */
  public void setMainApp(MainApp mainApp) {
    this.mainApp = mainApp;
  }

  /** Carga la información inicial */
  public void chargeData() {

    stars = List
        .of(img05Star, img15Star, img25Star, img35Star, img45Star, img1Star, img2Star, img3Star, img4Star, img5Star);

    List<ImageView> leftHalfStars = List.of(img05Star, img15Star, img25Star, img35Star, img45Star);
    List<ImageView> rightHalfStars = List.of(img1Star, img2Star, img3Star, img4Star, img5Star);

    // Carga de datos sobre variables
    lblScore.setText(String.format("¿Cómo fue tu experiencia con %s?", userGame.getGame().getName()));
    personalRatingSelected = userGame.getPersonalRating();

    // Carga de imágenes
    leftHalfStars
        .forEach(
            iv -> iv.setImage(new Image(CommentaryViewController.class.getResourceAsStream(Images.HALF_LEFT_STAR))));

    rightHalfStars
        .forEach(
            iv -> iv.setImage(new Image(CommentaryViewController.class.getResourceAsStream(Images.HALF_RIGHT_STAR))));

    // Muestra de datos en la vista
    if (userGame.getPersonalRating() == null) {
      stars.forEach(iv -> iv.setOpacity(1));

    } else {
      showStars(personalRatingSelected);
      txtReview.setText(userGame.getCommentary());
    }
  }

  /**
   * Muestra las estrellas de la puntuación indicada
   * 
   * @param personalRating Puntuación dada
   */
  private void showStars(Double personalRating) {

    if (personalRating != null) {
      stars.forEach(iv -> iv.setOpacity(0));

      switch (Double.toString(personalRating)) {
      case "5.0":
        img5Star.setOpacity(1);

      case "4.5":
        img45Star.setOpacity(1);

      case "4.0":
        img4Star.setOpacity(1);

      case "3.5":
        img35Star.setOpacity(1);

      case "3.0":
        img3Star.setOpacity(1);

      case "2.5":
        img25Star.setOpacity(1);

      case "2.0":
        img2Star.setOpacity(1);

      case "1.5":
        img15Star.setOpacity(1);

      case "1.0":
        img1Star.setOpacity(1);

      default:
        img05Star.setOpacity(1);
      }

    }
  }

}
