package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.controlsfx.control.CheckComboBox;

import application.MainApp;
import controllers.utils.AlertUtils;
import controllers.utils.NavigationPage;
import controllers.utils.OrderingConstants;
import controllers.utils.components.Images;
import controllers.utils.wrappers.GenreWrapper;
import controllers.utils.wrappers.PlatformSimpleWrapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import models.AppUser;
import models.Game;
import models.GameResponse;
import models.GameSimple;
import models.Genre;
import models.PlatformSimple;
import models.UserGame;
import models.error.GenericApiException;
import models.error.ResourceNotFoundException;
import models.feign.OpenFeignConstants;
import models.feign.client.GameClient;
import models.feign.client.UserGameClient;
import models.feign.constants.GameSearchParams;
import views.Routes;

/** Controlador de la página de exploración */
public class ExploreViewController {

  /** Tamaño de la página a mostrar en la vista de exploración */
  private static final int EXPLORE_PAGE_SIZE = 12;

  /** Cantidad de columnas del grid de juegos */
  private static final int GAMES_COLUMN_QUANTITY = 3;

  /** Ancho de una imagen de un juego */
  private static final int IMAGE_WIDTH = 300;

  /** Altura de una imagen de un juego */
  private static final int IMAGE_HEIGHT = 180;

  /** Clase principal común */
  private MainApp mainApp;

  /** Usuario loggeado en la aplicación */
  private AppUser appUser;

  /** Cliente de juegos */
  private GameClient gameClient;

  /** Cliente de asociaciones juego - usuario */
  private UserGameClient userGameClient;

  /** Propiedades y filtros de búsqueda */
  private Map<String, Object> searchParams = new HashMap<>();

  /** ComboBox de los géneros */
  private CheckComboBox<GenreWrapper> cbGenre = new CheckComboBox<>();

  /** ComboBox de las plataformas */
  private CheckComboBox<PlatformSimpleWrapper> cbPlatform = new CheckComboBox<>();

  /** Atributo para el conteo del juego mostrado. Usado para el cálculo de la posición sobre el gridPane */
  private int currentGameCount = 0;

  /** IDs de los juegos mostrados. Pueden ser internos o externos */
  private List<Long> shownGamesIds;

  /** TimeLine para la búsqueda en la barra superior */
  private Timeline searchBarTimeLine;

  /** Respuesta con los juegos a mostrar actuales */
  private GameResponse currentGameResponse;

  /** Lista de géneros disponibles para el filtrado */
  private List<Genre> avaiableGenres;

  /** Lista de plataformas disponibles para el filtrado */
  private List<PlatformSimple> avaiablePlatforms;

  /** Imagen del avatar del usuario */
  @FXML
  private ImageView avatarImage;

  /** BorderPane que contiene la paginación */
  @FXML
  private BorderPane borderPanePagination;

  /** Botón para la creación del juego */
  @FXML
  private Button btnCreateGame;

  /** Combo Box para ordenar juegos */
  @FXML
  private ComboBox<String> cbOrder;

  /** Combo Box para indicar si el juego ha sido creado por la comunidad */
  @FXML
  private CheckBox cbCommunityCreated;

  /** GridPane con los juegos */
  @FXML
  private GridPane gridPaneGames;

  /** Label para redirigir a la configuración del usuario */
  @FXML
  private Label labelUsernameConfig;

  /** Imagen de la lupa en la barra de búsqueda */
  @FXML
  private ImageView magnifyingGlassIcon;

  /** Barra de búsqueda */
  @FXML
  private TextField searchBar;

  /** VBox con los géneros */
  @FXML
  private VBox vboxGenres;

  /** VBox con las plataformas */
  @FXML
  private VBox vboxPlatforms;

  /**
   * Evento de click para crear un nuevo juego. Abre una nueva ventana modal para ello
   * 
   * @param event Evento ocurrido
   */
  @FXML
  void onBtnCreateGameClicked(ActionEvent event) {
    mainApp.initCreateGameView();
  }

  /**
   * Evento de escritura sobre la barra de búsqueda
   * 
   * @param event Evento ocurrido
   */
  @FXML
  void onSearchBarWrite(KeyEvent event) {

    // Se para el Timeline por si estuviera funcionando y se relanza
    searchBarTimeLine.stop();

    if (searchBar.getText() != null && !searchBar.getText().isBlank()) {
      searchBarTimeLine.playFromStart();

    } else if (searchBar.getText() != null && searchBar.getText().isBlank()) {

      // Si el texto está en blanco, se elimina el parámetro de búsqueda y se vuelve a buscar
      if (searchParams.containsKey(GameSearchParams.GAME_NAME)) {
        searchParams.remove(GameSearchParams.GAME_NAME);
      }

      chargeData();
    }
  }

  /**
   * Evento al clickar sobre el combo box cbCommunityCreated
   * 
   * @param event Evento ocurrido
   */
  @FXML
  void onCbCommunityCreatedSelected(ActionEvent event) {

    searchParams.put(GameSearchParams.PAGE_NUMBER, 1);
    searchParams.put(GameSearchParams.USER_CREATED, cbCommunityCreated.isSelected());

    chargeData();
  }

  /**
   * Inicializa la vista para ver los datos del usuario loggeado
   * 
   * @param event Evento ocurrido
   */
  @FXML
  void openUserView(MouseEvent event) {
    // TODO Vista de los datos del usuario
  }

  /**
   * Evento ocurrido al modificar el orden de los elementos
   * 
   * @param event Evento ocurrido
   */
  @FXML
  void onOrderingChange(ActionEvent event) {

    searchParams.put(GameSearchParams.PAGE_NUMBER, 1);
    searchParams.put(GameSearchParams.ORDERING_FIELD, OrderingConstants.EXPLORE_ORDERING_MAP.get(cbOrder.getValue()));

    chargeData();
  }

  /** Carga de datos básicos en la vista - Datos de usuario y los combo box de filtrado */
  public void chargeBaseData() {

    // Datos del usuario
    labelUsernameConfig.setText(appUser.getUsername());
    avatarImage
        .setImage(
            new Image(HomeViewController.class.getResourceAsStream(String.format(Images.AVATAR, appUser.getAvatar()))));

    // Parámetros de búsqueda y componentes
    cbCommunityCreated.setSelected(false);
    searchParams.put(GameSearchParams.PAGE_SIZE, EXPLORE_PAGE_SIZE);
    searchParams.put(GameSearchParams.PAGE_NUMBER, 1);

    // Combobox
    initComboBoxes();
  }

  /** Carga los datos necesarios en la página */
  public void chargeData() {

    gridPaneGames.getChildren().clear();

    // Solicitud de juegos
    try {
      chargeGamesByParams();
      setPagination();

    } catch (ResourceNotFoundException e) {

      Label labelNotFoundGames = new Label("No se encontraron juegos para la búsqueda realizada");
      gridPaneGames.getChildren().add(labelNotFoundGames);

    } catch (GenericApiException | IOException e) {

      Label labelNotExpectedError = new Label("Ocurrió un error inesperado");
      gridPaneGames.getChildren().add(labelNotExpectedError);
    }

  }

  /** Establece el TimeLine necesario para la búsqueda por nombre de juego */
  public void setSearchBarTimeLine() {

    this.searchBarTimeLine = new Timeline(new KeyFrame(Duration.millis(700), ev -> {

      searchParams.put(GameSearchParams.PAGE_NUMBER, 1);
      searchParams.put(GameSearchParams.GAME_NAME, searchBar.getText());
      chargeData();
    }));
  }

  /**
   * Getter - Parámetros de búsqueda
   * 
   * @return Map(String, Object)
   */
  public Map<String, Object> getSearchParams() {
    return searchParams;
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
   * Getter - Barra de búsqueda
   * 
   * @return TextField
   */
  public TextField getSearchBar() {
    return searchBar;
  }

  /**
   * Getter - currentGameResponse
   * 
   * @return GameResponse
   */
  public GameResponse getCurrentGameResponse() {
    return currentGameResponse;
  }

  /** Inicializa los ComboBox de filtrado y ordenación */
  private void initComboBoxes() {

    vboxGenres.setSpacing(15);
    vboxPlatforms.setSpacing(15);

    vboxGenres.getChildren().add(cbGenre);
    vboxPlatforms.getChildren().add(cbPlatform);

    // Carga de datos
    cbGenre.getItems().addAll(avaiableGenres.stream().map(GenreWrapper::new).toList());
    cbPlatform.getItems().addAll(avaiablePlatforms.stream().map(PlatformSimpleWrapper::new).toList());
    cbOrder.getItems().addAll(OrderingConstants.EXPLORE_SHOWN_ORDERING);

    // Eventos por cambio del combobox
    setCheckComboBoxesActions();

  }

  /**
   * Carga los datos de juegos a partir de los parámetros establecidos
   * 
   * @throws ResourceNotFoundException En caso de no encontrar juegos
   */
  private void chargeGamesByParams() {

    // Carga juegos en función de si son creados por la comunidad
    currentGameResponse = cbCommunityCreated.isSelected()
        ? gameClient.getGamesFromDb(OpenFeignConstants.SECRET_KEY, searchParams)
        : gameClient.getGamesFromApi(OpenFeignConstants.SECRET_KEY, searchParams);

    // IDs de los juegos obteniendo UserGames para saber si el usuario los tiene añadidos (API ID / ID)
    try {
      shownGamesIds = getIdsFromUserGames();

    } catch (ResourceNotFoundException e) {
      shownGamesIds = new ArrayList<>();
    }

    // Se muestran los juegos y se añaden sus funcionalidades
    currentGameResponse.getResults().forEach(game -> {

      Node element = getElementToShowFromGame(game);
      gridPaneGames.add(element, currentGameCount % GAMES_COLUMN_QUANTITY, currentGameCount / GAMES_COLUMN_QUANTITY);

      // Puntuación de metacritic
      setMetacritic(game);

      // Se comprueba si se tiene el juego y se permite añadirlo
      setAddRemoveButtonForGame(shownGamesIds, game);

      element.setCursor(Cursor.HAND);
      element.setOnMouseClicked(event -> {

        if (cbCommunityCreated.isSelected()) {
          mainApp.initGameView(game.getId(), NavigationPage.EXPLORE);

        } else {
          mainApp.initGameView(game.getApiId(), game.getScreenshots(), NavigationPage.EXPLORE);
        }
      });
      currentGameCount++;
    });

    currentGameCount = 0;
  }

  /**
   * Establece las acciones a tomar por un cambio en los check combo box. Resetea las variables referentes a los juegos
   * mostrados hasta el momento
   */
  private void setCheckComboBoxesActions() {

    cbGenre.getCheckModel().getCheckedItems().addListener((ListChangeListener<GenreWrapper>) change -> {

      searchParams.put(GameSearchParams.PAGE_NUMBER, 1);
      searchParams
          .put(GameSearchParams.GENRE_IDS,
              cbGenre.getCheckModel().getCheckedItems().stream().map(gw -> gw.getGenre().getId()).toList());

      chargeData();
    });

    cbPlatform.getCheckModel().getCheckedItems().addListener((ListChangeListener<PlatformSimpleWrapper>) change -> {

      searchParams.put(GameSearchParams.PAGE_NUMBER, 1);
      searchParams
          .put(GameSearchParams.PLATFORM_IDS,
              cbPlatform.getCheckModel().getCheckedItems().stream().map(pw -> pw.getPlatform().getId()).toList());

      chargeData();
    });
  }

  /**
   * Obtiene los IDs de los juegos mostrados asociados con el usuario. En caso de ser juegos insertados por usuarios, se
   * buscará por ID. En caso de ser juegos externos ,se buscarán por ID Externo (API ID)
   * 
   * @return List(Long)
   */
  private List<Long> getIdsFromUserGames() {

    List<Long> ids;

    if (cbCommunityCreated.isSelected()) {
      List<Long> gameIds = currentGameResponse.getResults().stream().map(Game::getId).toList();

      ids = userGameClient
          .getUserGamesByGameIdIn(OpenFeignConstants.SECRET_KEY, appUser.getId(), gameIds)
          .getGames()
          .stream()
          .map(ug -> ug.getGame().getId())
          .toList();

    } else {
      List<Long> apiIds = currentGameResponse.getResults().stream().map(Game::getApiId).toList();

      ids = userGameClient
          .getUserGamesByGameApiIdIn(OpenFeignConstants.SECRET_KEY, appUser.getId(), apiIds)
          .getGames()
          .stream()
          .map(ug -> ug.getGame().getApiId())
          .toList();
    }

    return ids;
  }

  /**
   * Obtiene el nodo a mostrar en función del juego. Será su imagen principal en caso de existir una URL hacia ella, o
   * un Pane con el nombre del juego en caso contrario
   * 
   * @param game Juego a mostrar
   * 
   * @return Node
   */
  private Node getElementToShowFromGame(Game game) {
    Node element;

    // Establece la imagen o texto si tiene o no URL hacia una imagen
    if (game.getImageurl() != null) {

      ImageView image = new ImageView(new Image(game.getImageurl()));
      image.setFitHeight(IMAGE_HEIGHT);
      image.setFitWidth(IMAGE_WIDTH);

      element = image;

    } else {
      BorderPane pane = new BorderPane(new Label(game.getName()));
      pane.setPrefWidth(IMAGE_WIDTH);
      pane.setMinWidth(IMAGE_WIDTH);
      pane.setMaxWidth(IMAGE_WIDTH);

      pane.setPrefHeight(IMAGE_HEIGHT);
      pane.setMinHeight(IMAGE_HEIGHT);
      pane.setMaxHeight(IMAGE_HEIGHT);

      element = pane;
    }
    return element;
  }

  /**
   * Establece la puntuación de metacritic en el gridPane
   * 
   * @param game Juego
   */
  private void setMetacritic(Game game) {

    Label labelMetacritic = new Label(
        game.getMetacriticRating() != null ? String.valueOf(game.getMetacriticRating()) : "");

    labelMetacritic.setMinSize(40, 40);
    labelMetacritic.setAlignment(Pos.CENTER);

    BackgroundFill backgroundFill = new BackgroundFill(Color.GREY, new CornerRadii(10), new Insets(5));
    labelMetacritic.setBackground(new Background(backgroundFill));
    labelMetacritic.setTextFill(Color.WHITE);

    gridPaneGames
        .add(labelMetacritic, currentGameCount % GAMES_COLUMN_QUANTITY, currentGameCount / GAMES_COLUMN_QUANTITY);
    GridPane.setValignment(labelMetacritic, VPos.TOP);
    GridPane.setMargin(labelMetacritic, new Insets(10));

  }

  /**
   * Añade un botón para asociar o eliminar la asociación entre el usuario loggeado y el juego mostrado
   * 
   * @param ids  IDs de los juegos
   * @param game Juego mostrado
   */
  private void setAddRemoveButtonForGame(List<Long> ids, Game game) {

    Button addRemoveButton = new Button();
    addRemoveButton.setFont(Font.font("System", FontWeight.BOLD, 14));

    // Botón en caso de ser un juego de la comunidad. Actúa por ID directamente
    if (cbCommunityCreated.isSelected()) {
      addRemoveButton.setText(ids.contains(game.getId()) ? "-" : "+");
      setMouseClickEventToAddRemoveButton(game, addRemoveButton, cbCommunityCreated.isSelected());

      // Botón en caso de ser un juego externo. Se busca primero el juego internamente
    } else {
      addRemoveButton.setText(ids.contains(game.getApiId()) ? "-" : "+");
      setMouseClickEventToAddRemoveButton(game, addRemoveButton, cbCommunityCreated.isSelected());
    }

    gridPaneGames
        .add(addRemoveButton, currentGameCount % GAMES_COLUMN_QUANTITY, currentGameCount / GAMES_COLUMN_QUANTITY);
    GridPane.setHalignment(addRemoveButton, HPos.RIGHT);
    GridPane.setValignment(addRemoveButton, VPos.BOTTOM);
    GridPane.setMargin(addRemoveButton, new Insets(10));
  }

  /**
   * Añade el evento de click de ratón sobre el botón para añadir o eliminar asociaciones entre juegos y usuarios
   * 
   * @param game            Juego a asociar o eliminar su asociación
   * @param addRemoveButton Botón sobre el que añadir el evento
   * @param isInternalGame  Indica si el juego es interno o externo a la API
   */
  private void setMouseClickEventToAddRemoveButton(Game game, Button addRemoveButton, boolean isInternalGame) {

    addRemoveButton.setOnMouseClicked(event -> {
      Game gameToUse = game;

      if (!isInternalGame) {
        gameToUse = gameClient.getGameByApiId(OpenFeignConstants.SECRET_KEY, game.getApiId(), game.getScreenshots());
      }

      if (addRemoveButton.getText().equals("+")) {
        UserGame userGame = prepareUserGameToSave(gameToUse);
        UserGame savedUserGame = userGameClient.saveUserGame(OpenFeignConstants.SECRET_KEY, userGame);
        mainApp.getUserGameResponse().getGames().add(savedUserGame);
        mainApp.getUserGameResponse().setCount(mainApp.getUserGameResponse().getCount() + 1);

        Alert alert = AlertUtils.getAddedGameToLibraryAlert();
        alert.showAndWait();

        addRemoveButton.setText("-");

      } else {
        userGameClient.deleteUserGame(OpenFeignConstants.SECRET_KEY, appUser.getId(), gameToUse.getId());

        final Long gameId = gameToUse.getId();
        UserGame foundUserGame = mainApp
            .getUserGameResponse()
            .getGames()
            .stream()
            .filter(ug -> ug.getGame().getId() != gameId)
            .findFirst()
            .get();

        mainApp.getUserGameResponse().getGames().remove(foundUserGame);
        mainApp.getUserGameResponse().setCount(mainApp.getUserGameResponse().getCount() - 1);

        Alert alert = AlertUtils.getDeletedGameFromLibraryAlert();
        alert.showAndWait();

        addRemoveButton.setText("+");
      }

    });
  }

  /**
   * Prepara un nuevo UserGame para ser insertado en el sistema
   * 
   * @param game Juego a asociar con el usuario
   * 
   * @return UserGame
   */
  private UserGame prepareUserGameToSave(Game game) {
    UserGame userGame = new UserGame();
    GameSimple convertedGame = new GameSimple();

    convertedGame.setId(game.getId());

    userGame.setGame(convertedGame);
    userGame.setUserId(appUser.getId());
    return userGame;
  }

  /**
   * Establece la paginación
   * 
   * @throws IOException En caso de error durante el acceso a la vista
   */
  private void setPagination() throws IOException {

    FXMLLoader loader = new FXMLLoader();

    loader.setLocation(MainApp.class.getResource(Routes.EXPLORE_PAGINATION));
    Parent paginationLayout = (HBox) loader.load();

    ExplorePaginationController explorePaginationController = loader.getController();
    explorePaginationController.setCurrentGameResponse(currentGameResponse);
    explorePaginationController.setSearchController(this);

    explorePaginationController.setPagination();

    borderPanePagination.setCenter(paginationLayout);
    paginationLayout.setVisible(true);
  }

}
