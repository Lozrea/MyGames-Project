package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import application.MainApp;
import controllers.utils.OrderingConstants;
import controllers.utils.components.Images;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.AppUser;
import models.GameSimple;
import models.UserGame;
import models.UserGameResponse;
import models.error.GenericApiException;
import models.error.ResourceNotFoundException;
import models.feign.OpenFeignConstants;
import models.feign.client.UserGameClient;
import views.Routes;

/** Controlador de la página de exploración */
public class LibraryViewController {

  /** Tamaño de la página a mostrar en la vista de biblioteca */
  protected static final int LIBRARY_PAGE_SIZE = 16;

  /** Cantidad de columnas del grid de juegos */
  protected static final int GAMES_COLUMN_QUANTITY = 4;

  /** Ancho de una imagen de un juego */
  private static final int IMAGE_WIDTH = 300;

  /** Altura de una imagen de un juego */
  private static final int IMAGE_HEIGHT = 180;

  /** Clase principal común */
  private MainApp mainApp;

  /** Usuario loggeado en la aplicación */
  private AppUser appUser;

  /** Cliente de asociaciones juego - usuario */
  private UserGameClient userGameClient;

  /** Atributo para el conteo del juego mostrado. Usado para el cálculo de la posición sobre el gridPane */
  private int currentGameCount = 0;

  /** TimeLine para la búsqueda en la barra superior */
  private Timeline searchBarTimeLine;

  /** Número de página actual */
  private int currentPageNumber = 1;

  /** Respuesta con los juegos asociados al usuario loggeado */
  private UserGameResponse userGameResponse;

  /** Lista de juegos total */
  private List<GameSimple> allGamesList;

  /** Grupo de toggle buttons para los juegos acabados y no acabados */
  private ToggleGroup toggleGroupFiltering = new ToggleGroup();

  @FXML
  private ImageView avatarImage;

  @FXML
  private BorderPane borderPanePagination;

  @FXML
  private Button btnNewGame;

  @FXML
  private ComboBox<String> cbOrder;

  @FXML
  private GridPane gridPaneGames;

  @FXML
  private Label labelUsernameConfig;

  @FXML
  private ImageView magnifyingGlassIcon;

  @FXML
  private TextField searchBar;

  @FXML
  private ToggleButton tglbtnAll;

  @FXML
  private ToggleButton tglbtnFavorites;

  @FXML
  private ToggleButton tglbtnFinished;

  @FXML
  private ToggleButton tglbtnNotFinished;

  @FXML
  private HBox hboxPagination;

  @FXML
  void btnNewGameClicked(ActionEvent event) {
    // TODO Ventana de nuevo juego
  }

  @FXML
  void onSearchBarWrite(KeyEvent event) {

    // Se para el Timeline por si estuviera funcionando y se relanza
    searchBarTimeLine.stop();

    if (searchBar.getText() != null) {
      searchBarTimeLine.playFromStart();
    }
  }

  @FXML
  void openUserView(MouseEvent event) {
    // TODO Vista de los datos del usuario
  }

  @FXML
  void onOrderingChange(ActionEvent event) {
    commonFilteringEvent();
  }

  @FXML
  void tglbtnAllClicked(ActionEvent event) {
    commonFilteringEvent();
  }

  @FXML
  void tglbtnFavoritesClicked(ActionEvent event) {
    commonFilteringEvent();
  }

  @FXML
  void tglbtnFinishedClicked(ActionEvent event) {
    commonFilteringEvent();
  }

  @FXML
  void tglbtnNotFinishedClicked(ActionEvent event) {
    commonFilteringEvent();
  }

  /** Carga de datos básicos en la vista - Datos de usuario y el combo box de ordenado */
  public void chargeBaseData() {

    // Datos del usuario
    labelUsernameConfig.setText(appUser.getUsername());
    avatarImage
        .setImage(
            new Image(HomeViewController.class.getResourceAsStream(String.format(Images.AVATAR, appUser.getAvatar()))));

    // ToggleGroup
    toggleGroupFiltering.getToggles().add(tglbtnAll);
    toggleGroupFiltering.getToggles().add(tglbtnFinished);
    toggleGroupFiltering.getToggles().add(tglbtnNotFinished);
    toggleGroupFiltering.getToggles().add(tglbtnFavorites);

    // Campo de ordenación
    cbOrder.getItems().addAll(OrderingConstants.LIBRARY_SHOWN_ORDERING);

  }

  /** Carga los datos necesarios en la página */
  public void chargeData() {

    // Solicitud de juegos
    try {
      chargeGames();
      setPagination();

    } catch (ResourceNotFoundException e) {

      Label labelNotFoundGames = new Label("No se encontraron juegos");
      gridPaneGames.getChildren().add(labelNotFoundGames);

    } catch (GenericApiException | IOException e) {

      Label labelNotExpectedError = new Label("Ocurrió un error inesperado");
      gridPaneGames.getChildren().add(labelNotExpectedError);
    }
  }

  /** Filtra y ordena los juegos a mostrar */
  public void filterAndOrderGames() {

    // Filtrado de juegos por toggleButtons
    allGamesList = filterGamesByToggleButtons();

    // Filtrado de juegos por barra de búsqueda
    if (searchBar.getText() != null && !searchBar.getText().isBlank()) {

      allGamesList = allGamesList
          .stream()
          .filter(g -> g.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
          .toList();
    }

    // Ordenación de resultados
    allGamesList = orderGameList();

    List<GameSimple> gamesToShow;
    if (allGamesList.size() >= LIBRARY_PAGE_SIZE) {
      gamesToShow = allGamesList
          .subList((currentPageNumber - 1) * LIBRARY_PAGE_SIZE, currentPageNumber * LIBRARY_PAGE_SIZE);

    } else {
      gamesToShow = allGamesList;
    }

    // Mostrado por pantalla
    showGames(gamesToShow);
  }

  /** Establece el TimeLine necesario para la búsqueda por nombre de juego */
  public void setSearchBarTimeLine() {

    this.searchBarTimeLine = new Timeline(new KeyFrame(Duration.millis(700), ev -> commonFilteringEvent()));
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
   * Setter - currentPageNumber
   * 
   * @param currentPageNumber Página actual mostrada
   */
  public void setCurrentPageNumber(int currentPageNumber) {
    this.currentPageNumber = currentPageNumber;
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
   * Getter - Toggle Button para mostrar todos los juegos
   * 
   * @return ToggleButton
   */
  public ToggleButton getTglbtnAll() {
    return tglbtnAll;
  }

  /**
   * Carga los datos de juegos a partir de los parámetros establecidos
   * 
   * @throws ResourceNotFoundException En caso de no encontrar juegos
   */
  private void chargeGames() {

    // Carga juegos en función de si son creados por la comunidad
    userGameResponse = userGameClient.getUserGamesByUserId(OpenFeignConstants.SECRET_KEY, appUser.getId());

    // Se muestran los juegos y se añaden sus funcionalidades (Primera página de juegos)
    allGamesList = userGameResponse.getGames().stream().map(UserGame::getGame).toList();

    List<GameSimple> gamesToShow = userGameResponse.getGames().size() >= LIBRARY_PAGE_SIZE
        ? new ArrayList<>(allGamesList.subList(0, LIBRARY_PAGE_SIZE))
        : allGamesList;

    showGames(gamesToShow);

  }

  /**
   * Muestra la lista de juegos en el GridPane
   * 
   * @param gamesToShow Lista de juegos a mostrar
   */
  private void showGames(List<GameSimple> gamesToShow) {

    gridPaneGames.getChildren().clear();

    gamesToShow.forEach(game -> {

      Node element = getElementToShowFromGame(game);
      gridPaneGames.add(element, currentGameCount % GAMES_COLUMN_QUANTITY, currentGameCount / GAMES_COLUMN_QUANTITY);

      // Puntuación de metacritic
      setMetacritic(game);

      element.setCursor(Cursor.HAND);
      element.setOnMouseClicked(event -> mainApp.initGameView(game.getId()));

      currentGameCount++;

    });

    currentGameCount = 0;
  }

  /**
   * Obtiene el nodo a mostrar en función del juego. Será su imagen principal en caso de existir una URL hacia ella, o
   * un Pane con el nombre del juego en caso contrario
   * 
   * @param game Juego a mostrar
   * 
   * @return Node
   */
  private Node getElementToShowFromGame(GameSimple game) {
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
  private void setMetacritic(GameSimple game) {

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
   * Filtra los juegos a mostrar por los toggle buttons
   * 
   * @return List(GameSimple)
   */
  private List<GameSimple> filterGamesByToggleButtons() {

    return switch (((ToggleButton) toggleGroupFiltering.getSelectedToggle()).getText()) {

    case "Todos" -> userGameResponse.getGames().stream().map(UserGame::getGame).toList();

    case "Jugados" -> userGameResponse
        .getGames()
        .stream()
        .filter(ug -> ug.getFinished() != null && ug.getFinished())
        .map(UserGame::getGame)
        .toList();

    case "Pendientes" -> userGameResponse
        .getGames()
        .stream()
        .filter(ug -> ug.getFinished() == null || !ug.getFinished())
        .map(UserGame::getGame)
        .toList();

    case "Favoritos" -> userGameResponse
        .getGames()
        .stream()
        .filter(ug -> ug.getFinished() != null && ug.getFavorite())
        .map(UserGame::getGame)
        .toList();

    default -> userGameResponse.getGames().stream().map(UserGame::getGame).toList();
    };
  }

  /**
   * Ordena la lista actual de juegos
   * 
   * @return List(GameSimple)
   */
  private List<GameSimple> orderGameList() {

    String order = cbOrder.getSelectionModel().getSelectedItem();

    if (order != null) {
      return allGamesList.stream().sorted((g1, g2) ->

      switch (order) {

      case OrderingConstants.NAME_ASC -> g1.getName().compareTo(g2.getName());

      case OrderingConstants.NAME_DESC -> g2.getName().compareTo(g1.getName());

      case OrderingConstants.METACRITIC_DESC -> {

        Comparator<Integer> comparator = Comparator.nullsLast(Comparator.reverseOrder());
        yield comparator.compare(g1.getMetacriticRating(), g2.getMetacriticRating());
      }

      default -> g2.getMetacriticRating().compareTo(g1.getMetacriticRating());

      }).toList();

    } else {
      return allGamesList;
    }
  }

  /**
   * Establece la paginación
   * 
   * @throws IOException En caso de error durante el acceso a la vista
   */
  private void setPagination() throws IOException {

    FXMLLoader loader = new FXMLLoader();

    loader.setLocation(MainApp.class.getResource(Routes.LIBRARY_PAGINATION));
    Parent paginationLayout = (HBox) loader.load();

    LibraryPaginationController libraryPaginationController = loader.getController();
    libraryPaginationController.setPagesNumber(allGamesList.size() / LIBRARY_PAGE_SIZE + 1);
    libraryPaginationController.setCurrentPageNumber(currentPageNumber);
    libraryPaginationController.setLibraryViewController(this);

    libraryPaginationController.setPagination();

    borderPanePagination.setCenter(paginationLayout);
    paginationLayout.setVisible(true);
  }

  /** Evento general de filtrado y ordenación sobre los juegos mostrados */
  private void commonFilteringEvent() {

    try {
      currentPageNumber = 1;
      filterAndOrderGames();
      setPagination();

    } catch (IOException e) {
      Label labelNotExpectedError = new Label("Ocurrió un error inesperado");
      gridPaneGames.getChildren().add(labelNotExpectedError);
    }
  }

}
