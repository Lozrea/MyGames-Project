package application;

import java.io.IOException;
import java.util.List;

import controllers.CreateGameViewController;
import controllers.ExploreViewController;
import controllers.HomeViewController;
import controllers.LibraryViewController;
import controllers.LoginController;
import controllers.NavigationController;
import controllers.RecoverPasswordController;
import controllers.RegisterController;
import controllers.utils.AlertUtils;
import controllers.utils.NavigationPage;
import controllers.utils.components.ToggleSwitch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.AppUser;
import models.Game;
import models.Genre;
import models.PlatformSimple;
import models.Screenshot;
import models.feign.FeignClientFactory;
import models.feign.OpenFeignConstants;
import models.feign.client.GameClient;
import models.feign.client.UserClient;
import models.feign.client.UserGameClient;
import models.feign.constants.GameSearchParams;
import views.Routes;

/** Aplicación principal */
public class MainApp extends Application {

  /** Stage principal */
  private Stage primaryStage;

  /** Cliente de usuarios */
  private UserClient userClient;

  /** Cliente de juegos */
  private GameClient gameClient;

  /** Cliente de relaciones usuario - juego */
  private UserGameClient userGameClient;

  /** Cliente loggeado / registrado de la aplicación */
  private AppUser appUser;

  /** Tamaño de la pantalla */
  private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

  /** Lista de plataformas disponibles en el sistema */
  private List<PlatformSimple> platforms;

  /** Lista de géneros disponibles en el sistema */
  private List<Genre> genres;

  /** Toggle Switch para la elección del tema claro / oscuro. Se mantendrá entre páginas */
  private StackPane toggleSwitch;

  /**
   * Método main. Inicializa la aplicación
   * 
   * @param args Argumentos
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {

    this.primaryStage = primaryStage;

    // Carga de los clientes principales
    userClient = FeignClientFactory.createUserClient(OpenFeignConstants.BASE_URL);
    gameClient = FeignClientFactory.createGameClient(OpenFeignConstants.BASE_URL);
    userGameClient = FeignClientFactory.createUserGameClient(OpenFeignConstants.BASE_URL);

    // Configuración inicial previa
    primaryStage.setTitle("MyGames");
    // TODO -> añadir icono a los resources -> primaryStage.getIcons().add(new Image("file:/.....png"));
    primaryStage.setResizable(false);

    // Carga del login
    initLoginView();
  }

  /** Carga la vista del Login en la ventana principal */
  public void initLoginView() {

    try {
      FXMLLoader loader = new FXMLLoader();

      loader.setLocation(MainApp.class.getResource(Routes.LOGIN));
      BorderPane loginLayout = (BorderPane) loader.load();

      // Controlador del login
      LoginController loginController = loader.getController();
      loginController.setUserClient(userClient);
      loginController.setMainApp(this);

      Scene scene = new Scene(loginLayout, screenSize.getWidth(), screenSize.getHeight());
      
      // Carga la hoja de estilos del Login
      scene.getStylesheets().add(getClass().getResource("/css/Login.css").toExternalForm());
      
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Carga la vista del Registro en la ventana principal */
  public void initRegisterView() {

    try {
      FXMLLoader loader = new FXMLLoader();

      loader.setLocation(MainApp.class.getResource(Routes.REGISTER));
      Parent registerLayout = (BorderPane) loader.load();

      // Controlador del registro
      RegisterController registerController = loader.getController();
      registerController.setUserClient(userClient);
      registerController.setMainApp(this);

      Scene scene = new Scene(registerLayout, screenSize.getWidth(), screenSize.getHeight());
      // Carga la hoja de estilos de la pantalla Registro
      scene.getStylesheets().add(getClass().getResource("/css/Register.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Carga la vista de la recuperación de contraseña */
  public void initRecoverPasswordView() {

    try {
      FXMLLoader loader = new FXMLLoader();

      loader.setLocation(MainApp.class.getResource(Routes.RECOVER_PASSWORD));
      Parent recoverPasswordLayout = (BorderPane) loader.load();

      // Controlador del registro
      RecoverPasswordController recoverPasswordController = loader.getController();
      recoverPasswordController.setUserClient(userClient);
      recoverPasswordController.setMainApp(this);

      Scene scene = new Scene(recoverPasswordLayout, screenSize.getWidth(), screenSize.getHeight());
      // Carga la hoja de estilos de la pantalla para recuperar la contraseña
      scene.getStylesheets().add(getClass().getResource("/css/RecoverPassword.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Carga la vista de la pantalla de inicio */
  public void initHomeView() {

    try {
      FXMLLoader loaderMain = new FXMLLoader();
      FXMLLoader loaderNav = new FXMLLoader();

      // ScrollPane para la biblioteca
      ScrollPane scrollLayout = new ScrollPane();
      scrollLayout.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

      // SplitPane interno
      SplitPane splitLayout = new SplitPane();
      scrollLayout.setContent(splitLayout);

      // Navegación y Home
      loaderNav.setLocation(MainApp.class.getResource(Routes.NAVIGATION));
      GridPane navigationLayout = (GridPane) loaderNav.load();

      navigationLayout.setAlignment(Pos.TOP_LEFT);

      loaderMain.setLocation(MainApp.class.getResource(Routes.HOME));
      BorderPane homeLayout = (BorderPane) loaderMain.load();

      splitLayout.getItems().addAll(navigationLayout, homeLayout);

      // Controladores
      initHomeViewControllers(loaderMain, loaderNav);

      Scene scene = new Scene(scrollLayout, screenSize.getWidth(), screenSize.getHeight());
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Carga la vista de la pantalla de exploración */
  public void initExploreView() {

    try {
      FXMLLoader loaderMain = new FXMLLoader();
      FXMLLoader loaderNav = new FXMLLoader();

      ScrollPane scrollLayout = initBaseExploreViews(loaderMain, loaderNav);

      // Controladores
      initExploreViewController(loaderMain, null);
      initNavigationController(loaderNav, NavigationPage.EXPLORE);

      Scene scene = new Scene(scrollLayout, screenSize.getWidth(), screenSize.getHeight());
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Carga la vista de la pantalla de la biblioteca */
  public void initLibraryView() {

    try {
      FXMLLoader loaderMain = new FXMLLoader();
      FXMLLoader loaderNav = new FXMLLoader();

      ScrollPane scrollLayout = initBaseLibraryViews(loaderMain, loaderNav);

      // Controladores
      initLibraryViewController(loaderMain);
      initNavigationController(loaderNav, NavigationPage.LIBRARY);

      Scene scene = new Scene(scrollLayout, screenSize.getWidth(), screenSize.getHeight());
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Carga la vista de la pantalla de exploración con una búsqueda preestablecida
   * 
   * @param searchText Texto de búsqueda
   */
  public void initExploreView(String searchText) {

    try {
      FXMLLoader loaderMain = new FXMLLoader();
      FXMLLoader loaderNav = new FXMLLoader();

      ScrollPane scrollLayout = initBaseExploreViews(loaderMain, loaderNav);

      // Controladores
      initExploreViewController(loaderMain, searchText);
      initNavigationController(loaderNav, NavigationPage.EXPLORE);

      Scene scene = new Scene(scrollLayout, screenSize.getWidth(), screenSize.getHeight());
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Inicializa la vista de un juego
   * 
   * @param id ID del juego
   */
  public void initGameView(Long id) {
    // TODO APERTURA DE LA VISTA JUEGO
  }

  /**
   * Inicializa la vista de un juego obtenido de forma externa a la API
   * 
   * @param externalId  ID externo a la API
   * @param screenshots Capturas de pantalla
   */
  public void initGameView(Long externalId, List<Screenshot> screenshots) {

    Game game = gameClient.getGameByApiId(OpenFeignConstants.SECRET_KEY, externalId, screenshots);
    initGameView(game.getId());
  }

  public void initCreateGameView() {

    try {
      // Stage
      Stage newStage = new Stage();

      newStage.initModality(Modality.APPLICATION_MODAL);
      newStage.setTitle("Creación de juego");

      // Carga del recurso y controlador
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(MainApp.class.getResource(Routes.CREATE_GAME));
      BorderPane createGameLayout = (BorderPane) loader.load();

      CreateGameViewController controller = loader.getController();
      controller.setAvaiableGenres(genres);
      controller.setAvaiablePlatforms(platforms);
      controller.setGameClient(gameClient);
      controller.setStage(newStage);

      // Inicialización
      controller.chargeBaseData();

      // Escena y muestra
      Scene scene = new Scene(createGameLayout);

      newStage.setScene(scene);
      newStage.show();

    } catch (IOException e) {
      AlertUtils.getUnexpectedErrorAlert().showAndWait();
    }
  }

  /**
   * Setter - AppUser
   * 
   * @param appUser Usuario loggeado en la aplicación
   */
  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }

  /**
   * Setter - plataformas disponibles
   * 
   * @param platforms Lista de plataformas a settear
   */
  public void setPlatforms(List<PlatformSimple> platforms) {
    this.platforms = platforms;
  }

  /**
   * Setter - géneros disponibles
   * 
   * @param genres Lista de géneros a settear
   */
  public void setGenres(List<Genre> genres) {
    this.genres = genres;
  }

  /**
   * Inicializa los controladores necesarios para la vista principal
   * 
   * @param loaderMain Loader del controlador para el home
   * @param loaderNav  Loader del controlador para la navegación
   */
  private void initHomeViewControllers(FXMLLoader loaderMain, FXMLLoader loaderNav) {

    // Carga del controlador home
    HomeViewController homeViewController = loaderMain.getController();
    homeViewController.setAppUser(appUser);
    homeViewController.setMainApp(this);
    homeViewController.setUserGameClient(userGameClient);
    homeViewController.setGameClient(gameClient);
    homeViewController.setSearchBarTimeLine();

    // Carga de datos
    if (platforms == null || genres == null || toggleSwitch == null) {
      homeViewController.stablishPlatformsAndGenres();

      // TODO A falta de tener los temas implementados
      toggleSwitch = ToggleSwitch.getToggleSwitch(false);
    }

    homeViewController.chargeData();

    initNavigationController(loaderNav, NavigationPage.HOME);
  }

  /**
   * Inicia las vistas base de exploración de juegos
   * 
   * @param loaderMain Cargador de la vista principal
   * @param loaderNav  Cargador de la vista de navegación
   * 
   * @return ScrollPane
   * 
   * @throws IOException En caso de error
   */
  private ScrollPane initBaseExploreViews(FXMLLoader loaderMain, FXMLLoader loaderNav) throws IOException {

    // ScrollPane
    ScrollPane scrollLayout = new ScrollPane();
    scrollLayout.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    // SplitPane interno
    SplitPane splitLayout = new SplitPane();
    scrollLayout.setContent(splitLayout);

    // Navegación y Home
    loaderNav.setLocation(MainApp.class.getResource(Routes.NAVIGATION));
    GridPane navigationLayout = (GridPane) loaderNav.load();

    navigationLayout.setAlignment(Pos.TOP_LEFT);

    loaderMain.setLocation(MainApp.class.getResource(Routes.EXPLORE));
    BorderPane exploreLayout = (BorderPane) loaderMain.load();

    splitLayout.getItems().addAll(navigationLayout, exploreLayout);
    return scrollLayout;
  }

  /**
   * Inicia las vistas base de la biblioteca
   * 
   * @param loaderMain Cargador de la vista principal
   * @param loaderNav  Cargador de la vista de navegación
   * 
   * @return ScrollPane
   * 
   * @throws IOException En caso de error
   */
  private ScrollPane initBaseLibraryViews(FXMLLoader loaderMain, FXMLLoader loaderNav) throws IOException {

    // ScrollPane
    ScrollPane scrollLayout = new ScrollPane();
    scrollLayout.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    // SplitPane interno
    SplitPane splitLayout = new SplitPane();
    scrollLayout.setContent(splitLayout);

    // Navegación y Home
    loaderNav.setLocation(MainApp.class.getResource(Routes.NAVIGATION));
    GridPane navigationLayout = (GridPane) loaderNav.load();

    navigationLayout.setAlignment(Pos.TOP_LEFT);

    loaderMain.setLocation(MainApp.class.getResource(Routes.LIBRARY));
    BorderPane libraryLayout = (BorderPane) loaderMain.load();

    splitLayout.getItems().addAll(navigationLayout, libraryLayout);
    return scrollLayout;
  }

  /**
   * Inicializa el controlador de la vista de exploración con una búsqueda preestablecida. En caso de ser una búsqueda
   * null, no se buscarán juegos por nombre
   * 
   * @param loaderMain Loader del controlador para el home
   * @param searchText Texto de búsqueda para la inicialización de la página
   */
  private void initExploreViewController(FXMLLoader loaderMain, String searchText) {

    // Carga del controlador home
    ExploreViewController exploreViewController = loaderMain.getController();
    exploreViewController.setAppUser(appUser);
    exploreViewController.setMainApp(this);
    exploreViewController.setUserGameClient(userGameClient);
    exploreViewController.setGameClient(gameClient);

    // Carga de géneros y plataformas
    exploreViewController.setAvaiableGenres(genres);
    exploreViewController.setAvaiablePlatforms(platforms);

    // Carga de eventos
    exploreViewController.setSearchBarTimeLine();

    // Parámetros
    exploreViewController.getSearchParams().put(GameSearchParams.USER_CREATED, false);

    // Carga de datos con nombre parcial o total del juego
    if (searchText != null && !searchText.isEmpty()) {
      exploreViewController.getSearchBar().setText(searchText);
      exploreViewController.getSearchParams().put(GameSearchParams.GAME_NAME, searchText);
    }

    exploreViewController.chargeBaseData();
    exploreViewController.chargeData();
  }

  /**
   * Inicializa el controlador de la navegación
   * 
   * @param loaderNav   Loader de la página
   * @param currentPage Página actual
   */
  private void initNavigationController(FXMLLoader loaderNav, NavigationPage currentPage) {

    NavigationController navigationController = loaderNav.getController();
    navigationController.setMainApp(this);
    navigationController.setIcons();
    navigationController.setToggleSwitch(toggleSwitch);
    navigationController.setCurrentPage(currentPage);
  }

  /**
   * Inicializa el controlador de la vista de la librería
   * 
   * @param loaderMain Loader del controlador para el home
   */
  private void initLibraryViewController(FXMLLoader loaderMain) {

    // Carga del controlador home
    LibraryViewController libraryViewController = loaderMain.getController();
    libraryViewController.setAppUser(appUser);
    libraryViewController.setMainApp(this);
    libraryViewController.setUserGameClient(userGameClient);
    libraryViewController.getTglbtnAll().setSelected(true);

    // Carga de eventos
    libraryViewController.setSearchBarTimeLine();

    libraryViewController.chargeBaseData();
    libraryViewController.chargeData();
  }

}
