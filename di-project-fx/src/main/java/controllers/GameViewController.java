package controllers;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import application.MainApp;
import controllers.utils.GameViewConstants;
import controllers.utils.NavigationPage;
import controllers.utils.components.Images;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import models.AppUser;
import models.Developer;
import models.Game;
import models.GameSimple;
import models.Genre;
import models.UserGame;
import models.UserGameResponse;
import models.feign.OpenFeignConstants;
import models.feign.client.UserGameClient;

/** Controlador con la vista del juego */
public class GameViewController {

  /** DateTimeFormatter para las fechas */
  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  /** Clase principal común */
  private MainApp mainApp;

  /** Usuario loggeado en la aplicación */
  private AppUser appUser;

  /** Asociación del juego con el usuario */
  private UserGame userGame;

  /** Cliente para asociaciones juego - usuario */
  private UserGameClient userGameClient;

  /** Página previa */
  private NavigationPage prevPage;

  /** Respuesta con los juegos asociados al usuario loggeado */
  private UserGameResponse userGameResponse;

  /** Juego actual */
  private Game game;

  /** Imagen del avatar */
  @FXML
  private ImageView avatarImage;

  /** Botón para modificar una reseña */
  @FXML
  private Button btnModReview;

  /** Descripción del juego */
  @FXML
  private TextArea description;

  /** FlowPane de imágenes del juego (Screenshots) */
  @FXML
  private HBox hboxImages;

  /** FlowPane de plataformas en las que está el juego disponible */
  @FXML
  private FlowPane flowPanePlatforms;

  /** FlowPane de etiquetas */
  @FXML
  private FlowPane flowPaneTags;

  /** Título del juego */
  @FXML
  private Label gameTitle;

  /** ImageView con el icono de una estrella para mostrar la puntuación */
  @FXML
  private ImageView imageViewStar;

  /** Imagen para mostrar si el juego está añadido en favoritos */
  @FXML
  private ImageView imgFavorite;

  /** Label para la configuración del username */
  @FXML
  private Label labelUsernameConfig;

  /** Label para añadir una reseña */
  @FXML
  private Label lblAddReview;

  /** Label para volver a la pestaña anterior */
  @FXML
  private Label lblBack;

  /** Label que indica la desarrolladora del juego */
  @FXML
  private Label lblDeveloper;

  /** Label con los géneros del juego */
  @FXML
  private Label lblGenre;

  /** Label con la puntuación personal del juego */
  @FXML
  private Label lblPersonalScore;

  /** Label con la fecha de salida */
  @FXML
  private Label lblReleaseDate;

  /** Label con el Status (Jugado / Pendiente) */
  @FXML
  private Label lblStatus;

  /** Label para añadir/quitar un juego a la biblioteca */
  @FXML
  private Label lblAddToLibrary;

  /** Puntuación de metacritic */
  @FXML
  private Label metacriticScore;

  /** BorderPane principal con toda la información */
  @FXML
  private BorderPane principal;

  /** TextField con el comentario del juego */
  @FXML
  private TextField txtCommentary;

  /**
   * Inicializa la vista para modificar una reseña
   * 
   * @param event Evento
   */
  @FXML
  void onBtnModReview(ActionEvent event) {
    if (btnModReview.isVisible() && userGame != null) {
      mainApp.initCommentaryView(userGame, this);
    }
  }

  /**
   * Acción al clickar la imagen de favorito. Sólo será visible cuando esté añadido a la biblioteca
   * 
   * @param event Evento
   */
  @FXML
  void onImgFavoriteClicked(MouseEvent event) {

    userGame.setFavorite(!userGame.getFavorite());
    userGameClient.saveUserGame(OpenFeignConstants.SECRET_KEY, userGame);

    imgFavorite
        .setImage(new Image(GameViewController.class
            .getResourceAsStream(userGame.getFavorite().equals(true) ? Images.FULL_HEART : Images.HOLLOW_HEART)));

    mainApp
        .getUserGameResponse()
        .getGames()
        .replaceAll(ug -> ug.getGame().getId().equals(userGame.getGame().getId()) ? userGame : ug);

  }

  /**
   * Acción al clickar el botón para añadir una nueva reseña
   * 
   * @param event Evento
   */
  @FXML
  void onLBlAddReviewClicked(MouseEvent event) {
    mainApp.initCommentaryView(userGame, this);
  }

  /**
   * Acción al clickar sobre el botón para añadir o quitar de la biblioteca
   * 
   * @param event Evento
   */
  @FXML
  void onLblAddToLibraryClicked(MouseEvent event) {

    if (userGame == null) {

      UserGame userGameToInsert = prepareUserGameToSave(game);
      userGame = userGameClient.saveUserGame(OpenFeignConstants.SECRET_KEY, userGameToInsert);
      mainApp.getUserGameResponse().getGames().add(userGame);

      lblAddToLibrary.setText(GameViewConstants.REMOVE_FROM_LIBRARY_TEXT);

      lblStatus.setVisible(true);
      lblStatus.setText(GameViewConstants.NOT_FINISHED_TEXT);

      imgFavorite.setVisible(true);
      imgFavorite.setImage(new Image(GameViewController.class.getResourceAsStream(Images.HOLLOW_HEART)));

    } else {
      userGameClient.deleteUserGame(OpenFeignConstants.SECRET_KEY, appUser.getId(), game.getId());
      mainApp.getUserGameResponse().getGames().remove(userGame);

      lblAddToLibrary.setText(GameViewConstants.ADD_TO_LIBRARY_TEXT);
      lblStatus.setVisible(false);
      lblAddReview.setVisible(false);
      imgFavorite.setVisible(false);
      btnModReview.setVisible(false);
      lblPersonalScore.setText("-");
      txtCommentary.setText("");

      userGame = null;
    }
  }

  /**
   * Acción al clickar sobre el botón de volver
   * 
   * @param event Evento
   */
  @FXML
  void onLblBackClicked(MouseEvent event) {

    switch (prevPage) {
    case NavigationPage.EXPLORE:
      mainApp.initExploreView();
      break;

    case NavigationPage.HOME:
      mainApp.initHomeView();
      break;

    case NavigationPage.LIBRARY:
      mainApp.initLibraryView();
      break;

    default:
      mainApp.initHomeView();
    }
  }

  /**
   * Acción al clickar sobre el botón de status
   * 
   * @param event Evento
   */
  @FXML
  void onLblStatusClicked(MouseEvent event) {

    if (lblStatus.isVisible()) {

      // Modificaciones del userGame
      userGame.setFinished(!userGame.getFinished());
      userGame.setPersonalRating(null);
      userGame.setCommentary(null);
      userGameClient.saveUserGame(OpenFeignConstants.SECRET_KEY, userGame);

      // Modificaciones sobre los lbls y botones afectados
      lblStatus
          .setText(lblStatus.getText().equals(GameViewConstants.NOT_FINISHED_TEXT) ? GameViewConstants.FINISHED_TEXT
              : GameViewConstants.NOT_FINISHED_TEXT);
      lblAddReview.setVisible(userGame.getFinished());
      btnModReview.setVisible(false);
      txtCommentary.setText("");
    }

    mainApp
        .getUserGameResponse()
        .getGames()
        .replaceAll(ug -> ug.getGame().getId().equals(userGame.getGame().getId()) ? userGame : ug);
  }

  /**
   * Acción para abrir el perfil del usuario
   * 
   * @param event Evento
   */
  @FXML
  void openUserView(MouseEvent event) {
    // TODO Vista de los datos del usuario
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
   * Getter - lblAddReview
   * 
   * @return Label
   */
  public Label getLblAddReview() {
    return lblAddReview;
  }

  /** Prepara todos los datos visibles referentes al usuario y a los juegos en la vista */
  public void setData() {

    // Datos del usuario
    labelUsernameConfig.setText(appUser.getUsername());
    Image backgroundImage = new Image(
        GameViewController.class.getResourceAsStream(String.format(Images.AVATAR, appUser.getAvatar())));
    avatarImage.setImage(backgroundImage);

    // Datos del juego
    if (game.getImageurl() != null) {
      BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
      principal
          .setBackground(new Background(new BackgroundImage(new Image(game.getImageurl()), BackgroundRepeat.NO_REPEAT,
              BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize)));
    }

    imageViewStar.setImage(new Image(GameViewController.class.getResourceAsStream(Images.STAR)));

    setLabels();
    chargeGameScreenshots();
  }

  /** Prepara los botones para ser usados en la vista */
  public void prepareUserGameButtons() {

    Optional<UserGame> optFoundUserGame = userGameResponse
        .getGames()
        .stream()
        .filter(ug -> ug.getGame().getId().equals(game.getId()))
        .findAny();

    userGame = optFoundUserGame.isPresent() ? optFoundUserGame.get() : null;

    // Si el juego está asociado ya previamente al usuario
    if (optFoundUserGame.isPresent()) {
      UserGame foundGame = optFoundUserGame.get();
      lblAddToLibrary.setText(GameViewConstants.REMOVE_FROM_LIBRARY_TEXT);

      // Se modifican los botones en función de los datos obtenidos
      imgFavorite
          .setImage(new Image(GameViewController.class
              .getResourceAsStream(foundGame.getFavorite().equals(true) ? Images.FULL_HEART : Images.HOLLOW_HEART)));

      if (foundGame.getFinished().equals(true)) {
        setButtonsForFinishedGame(foundGame);

        // Juego no finalizado, pero sí añadido a la biblioteca
      } else {
        lblStatus.setText(GameViewConstants.NOT_FINISHED_TEXT);
        lblAddReview.setVisible(false);
      }

      // Juego no añadido a la biblioteca
    } else {
      lblAddToLibrary.setText(GameViewConstants.ADD_TO_LIBRARY_TEXT);
      lblStatus.setVisible(false);
      lblAddReview.setVisible(false);
      imgFavorite.setVisible(false);
    }
  }

  /**
   * Setter - game
   * 
   * @param game Juego actual
   */
  public void setGame(Game game) {
    this.game = game;
  }

  /**
   * Setter - userGameResponse
   * 
   * @param userGameResponse Respuesta con los juegos asociados al usuario
   */
  public void setUserGameResponse(UserGameResponse userGameResponse) {
    this.userGameResponse = userGameResponse;
  }

  /**
   * Setter - prevPage
   * 
   * @param prevPage Página previa
   */
  public void setPrevPage(NavigationPage prevPage) {
    this.prevPage = prevPage;
  }

  /**
   * Setter - userGame
   * 
   * @param userGame Asociación del juego con el usuario
   */
  public void setUserGame(UserGame userGame) {
    this.userGame = userGame;
  }

  /**
   * Getter - Label con la puntuación personal
   * 
   * @return Label
   */
  public Label getLblPersonalScore() {
    return lblPersonalScore;
  }

  /**
   * Getter - La zona de escritura de comentarios
   * 
   * @return TextField
   */
  public TextField getTxtCommentary() {
    return txtCommentary;
  }

  /**
   * Getter - btnModReview
   * 
   * @return Botón para la modificación de reseñas
   */
  public Button getBtnModReview() {
    return btnModReview;
  }

  /**
   * Prepara un nuevo UserGame para ser insertado en el sistema
   * 
   * @param game Juego a asociar con el usuario
   * 
   * @return UserGame
   */
  private UserGame prepareUserGameToSave(Game game) {
    UserGame userGameToSave = new UserGame();
    GameSimple convertedGame = new GameSimple();

    convertedGame.setId(game.getId());

    userGameToSave.setGame(convertedGame);
    userGameToSave.setUserId(appUser.getId());
    return userGameToSave;
  }

  /** Settea todos los labels del juego */
  private void setLabels() {
    game.getTags().stream().limit(4).forEach(t -> flowPaneTags.getChildren().add(new Label(t.getName())));
    if (game.getTags().size() > 4) {
      flowPaneTags.getChildren().add(new Label("+"));
    }

    game.getPlatforms().forEach(p -> flowPanePlatforms.getChildren().add(new Label(p.getName())));

    gameTitle.setText(game.getName());

    if (game.getDescription() == null) {
      description.setVisible(false);
    } else {
      description.setText(game.getDescription());
    }

    metacriticScore.setText(game.getMetacriticRating() != null ? String.valueOf(game.getMetacriticRating()) : "-");
    lblReleaseDate.setText(game.getReleasedate() != null ? game.getReleasedate().format(DTF) : "");
    lblDeveloper
        .setText(game
            .getDevelopers()
            .stream()
            .map(Developer::getName)
            .reduce("", (d1, d2) -> d1.isBlank() ? d2 : d1 + " / " + d2));
    lblGenre
        .setText(
            game.getGenres().stream().map(Genre::getName).reduce("", (g1, g2) -> g1.isBlank() ? g2 : g1 + ", " + g2));
  }

  /** Carga los screenshots en el panel destinado a ello */
  private void chargeGameScreenshots() {

    hboxImages.setSpacing(30);

    hboxImages.getChildren().addAll(game.getScreenshots().stream().map(sc -> {
      ImageView imageView = new ImageView(new Image(sc.getImageUrl()));
      imageView.setFitWidth(300);
      imageView.setFitHeight(180);

      return imageView;
    }).toList());

  }

  /**
   * Establece los elementos para un juego terminado por el usuario
   * 
   * @param foundGame Juego encontrado
   */
  private void setButtonsForFinishedGame(UserGame foundGame) {

    lblStatus.setText(GameViewConstants.FINISHED_TEXT);
    lblAddReview.setVisible(foundGame.getCommentary() == null && foundGame.getPersonalRating() == null);
    lblPersonalScore
        .setText(foundGame.getPersonalRating() != null ? String.format("%.1f", foundGame.getPersonalRating()) : "-");
    txtCommentary.setText(foundGame.getCommentary() != null ? foundGame.getCommentary() : "");
    btnModReview.setVisible(foundGame.getCommentary() != null || foundGame.getPersonalRating() != null);
  }

}
