package controllers;

import java.util.HashMap;

import application.MainApp;
import controllers.utils.components.Images;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import models.AppUser;
import models.Game;
import models.GameResponse;
import models.UserGameResponse;
import models.feign.FeignClientFactory;
import models.feign.OpenFeignConstants;
import models.feign.client.GameClient;
import models.feign.client.GenreClient;
import models.feign.client.PlatformClient;
import models.feign.client.UserGameClient;

/** Controlador de la vista principal */
public class HomeViewController {

  /** Clase principal común */
  private MainApp mainApp;

  /** Usuario loggeado en la aplicación */
  private AppUser appUser;

  /** Cliente para asociaciones juego - usuario */
  private UserGameClient userGameClient;

  /** Cliente de juegos */
  private GameClient gameClient;

  /** Respuesta del cliente de juegos con los juegos recomendados para el usuario loggeado */
  private GameResponse recommendedGames;

  /** Índice de la imagen actual */
  private int currentMainGameIndex = 0;

  /** TimeLine para la búsqueda en la barra superior */
  private Timeline searchBarTimeLine;

  @FXML
  private ImageView avatarImage;

  @FXML
  private HBox hBoxRecommendedGames;

  @FXML
  private HBox hBoxRecommendedGamesByFriends;

  @FXML
  private Label labelUsernameConfig;

  @FXML
  private ImageView magnifyingGlassIcon;

  @FXML
  private AnchorPane mainDescription;

  @FXML
  private SplitPane mainGame;

  @FXML
  private ImageView mainImage;

  @FXML
  private ImageView mainSecondImage;

  @FXML
  private ImageView mainThirdImage;

  @FXML
  private TextField searchBar;

  @FXML
  private TextArea txtDescription;

  @FXML
  void onSearchBarWrite(KeyEvent event) {

    // Se para el Timeline por si estuviera funcionando y se relanza
    searchBarTimeLine.stop();

    if (searchBar.getText() != null && !searchBar.getText().isBlank()) {
      searchBarTimeLine.playFromStart();
    }
  }

  @FXML
  void openMainGame(MouseEvent event) {
    Game currentGame = recommendedGames.getResults().get(currentMainGameIndex);

    mainApp.initGameView(currentGame.getApiId(), currentGame.getScreenshots());
  }

  @FXML
  void openUserView(MouseEvent event) {
    // TODO Vista de los datos del usuario
  }

  /**
   * Método consumido la primera vez que se construye la vista principal. Carga las plataformas y los géneros
   * disponibles
   */
  public void stablishPlatformsAndGenres() {

    PlatformClient platformClient = FeignClientFactory.createPlatformClient(OpenFeignConstants.BASE_URL);
    GenreClient genreClient = FeignClientFactory.createGenreClient(OpenFeignConstants.BASE_URL);

    mainApp.setPlatforms(platformClient.getPlatforms(OpenFeignConstants.SECRET_KEY));
    mainApp.setGenres(genreClient.getGenres(OpenFeignConstants.SECRET_KEY));
  }

  /** Carga todos los datos necesarios a mostrar al usuario */
  public void chargeData() {

    // Se cargan los datos del usuario
    labelUsernameConfig.setText(appUser.getUsername());
    avatarImage
        .setImage(
            new Image(HomeViewController.class.getResourceAsStream(String.format(Images.AVATAR, appUser.getAvatar()))));

    hBoxRecommendedGamesByFriends.setSpacing(20);
    hBoxRecommendedGamesByFriends.setPadding(new Insets(10));

    txtDescription.setEditable(false);

    // Se cargan los juegos recomendados
    chargeRecommendedGamesByFriends();
    chargeRecommendedGames();

    // Da comienzo al Timeline cíclico para mostrar el juego principal
    startMainGameTimeline();

  }

  /**
   * Setter - MainApp
   * 
   * @param mainApp Clase principal
   */
  public void setMainApp(MainApp mainApp) {
    this.mainApp = mainApp;
  }

  /**
   * Setter - Usuario loggeado
   * 
   * @param appUser Usuario loggeado
   */
  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }

  /**
   * Setter - Cliente de asociaciones juego - usuario
   * 
   * @param userGameClient Cliente de asociaciones juego - usuario
   */
  public void setUserGameClient(UserGameClient userGameClient) {
    this.userGameClient = userGameClient;
  }

  /**
   * Setter - Cliente de juegos
   * 
   * @param gameClient Cliente de juegos
   */
  public void setGameClient(GameClient gameClient) {
    this.gameClient = gameClient;
  }

  /** Establece el TimeLine necesario para la búsqueda por nombre de juego */
  public void setSearchBarTimeLine() {
    this.searchBarTimeLine = new Timeline(
        new KeyFrame(Duration.millis(700), ev -> mainApp.initExploreView(searchBar.getText())));
  }

  /** Carga los juegos recomendados por amigos */
  private void chargeRecommendedGamesByFriends() {

    // Se establecen los juegos si hay amigos
    if (!appUser.getFriendIds().isEmpty()) {

      UserGameResponse recommendedByFriends = userGameClient
          .getUserGamesByUsersIdsRecommended(OpenFeignConstants.SECRET_KEY,
              appUser.getFriendIds().stream().map(String::valueOf).reduce("", (f1, f2) -> String.join(",", f1, f2)),
              new HashMap<>());

      recommendedByFriends.getGames().forEach(g -> {

        // Apertura de la imagen y setteo en el HBox
        if (g.getGame().getImageurl() != null) {
          ImageView imageView = new ImageView(new Image(g.getGame().getImageurl()));
          imageView.setFitWidth(500);
          imageView.setFitHeight(250);

          imageView.setCursor(Cursor.HAND);

          // Redirección al clickar sobre la imagen
          imageView.setOnMouseClicked(event -> mainApp.initGameView(g.getGame().getId()));

          hBoxRecommendedGamesByFriends.getChildren().add(imageView);
        }
      });
    }
  }

  /** Carga los juegos recomendados al usuario en función de sus últimos juegos almacenados */
  private void chargeRecommendedGames() {

    recommendedGames = gameClient.getRecommendedGames(OpenFeignConstants.SECRET_KEY, appUser.getId());

    recommendedGames.getResults().forEach(g -> {

      // Apertura de la imagen y setteo en el HBox
      if (g.getImageurl() != null) {
        ImageView imageView = new ImageView(new Image(g.getImageurl()));
        imageView.setFitWidth(500);
        imageView.setFitHeight(250);

        imageView.setCursor(Cursor.HAND);

        // Redirección al clickar sobre la imagen
        imageView.setOnMouseClicked(event -> mainApp.initGameView(g.getApiId(), g.getScreenshots()));

        hBoxRecommendedGames.getChildren().add(imageView);
      }
    });
  }

  /** Establece el juego principal a mostrar en la pestaña de recomendaciones */
  private void startMainGameTimeline() {

    showGameWithIndex();

    // Se carga cada cierto tiempo periódico el juego principal
    Timeline mainGameTimeline = new Timeline(new KeyFrame(Duration.millis(7000), ev -> {

      // Nuevo índice del juego
      currentMainGameIndex = (currentMainGameIndex + 1) % recommendedGames.getResults().size();
      showGameWithIndex();
    }));

    mainGameTimeline.setCycleCount(Animation.INDEFINITE);
    mainGameTimeline.play();
  }

  /** Muestra un juego en la pestaña principal */
  private void showGameWithIndex() {

    Game currentGame = recommendedGames.getResults().get(currentMainGameIndex);

    // Imagen principal y descripción
    mainImage.setImage(new Image(currentGame.getImageurl()));
    txtDescription.setText(currentGame.getDescription() != null ? currentGame.getDescription() : "Sin descripción");

    // Screenshots secundarios
    if (currentGame.getScreenshots().size() > 1) {
      mainSecondImage.setImage(new Image(currentGame.getScreenshots().get(0).getImageUrl()));
      mainSecondImage.setImage(new Image(currentGame.getScreenshots().get(1).getImageUrl()));

    } else if (currentGame.getScreenshots().size() == 1) {
      mainSecondImage.setImage(new Image(currentGame.getScreenshots().get(0).getImageUrl()));
    }
  }

}
