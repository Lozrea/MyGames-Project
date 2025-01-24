package controllers;

import application.MainApp;
import controllers.utils.NavigationPage;
import controllers.utils.components.ToggleSwitch;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/** Controlador de la vista de navegación */
public class NavigationController {

  /** Color de marcado de enlaces */
  private static final Color MARKED_LINK_COLOR = Color.rgb(254, 105, 39);

  /** Clase principal */
  private MainApp mainApp;

  /** Página actual */
  private NavigationPage currentPage;

  /** Toggle Switch */
  private StackPane toggleSwitch;

  /** Icono de la aplicación para la biblioteca */
  @FXML
  private ImageView LibraryIcon;

  /** Icono de la aplicación */
  @FXML
  private ImageView appIcon;

  /** Icono de la aplicación para la exploración */
  @FXML
  private ImageView exploreIcon;

  /** Icono de la aplicación para la vista de amigos */
  @FXML
  private ImageView friendsIcon;

  /** Icono de la aplicación para las recomendaciones */
  @FXML
  private ImageView homeIcon;

  /** Label de enlace para la vista de exploración */
  @FXML
  private Label labelExplore;

  /** Label de enlace para la vista de amigos */
  @FXML
  private Label labelFriends;

  /** Label de enlace para la vista de recomendaciones */
  @FXML
  private Label labelHome;

  /** Label de enlace para la vista de biblioteca */
  @FXML
  private Label labelLibrary;

  /** Label de enlace para la vista de ajustes */
  @FXML
  private Label labelSettings;

  /** Label de enlace para la vista de soporte */
  @FXML
  private Label labelSupport;

  /** Icono de la aplicación para los ajustes */
  @FXML
  private ImageView settingsIcon;

  /** Icono de la aplicación para el soporte */
  @FXML
  private ImageView supportIcon;

  /** Icono de la aplicación para el tema */
  @FXML
  private ImageView themeIcon;

  /** StackPane con el toggle switch para el tema */
  @FXML
  private StackPane toggleSwitchLayout;

  /**
   * Apertura de la vista de navegación
   * 
   * @param event Evento
   */
  @FXML
  void appIconNavigationClicked(MouseEvent event) {
    mainApp.initHomeView();
  }

  /**
   * Apertura de la vista de exploración
   * 
   * @param event Evento
   */
  @FXML
  void exploreNavigationClicked(MouseEvent event) {
    mainApp.initExploreView();
  }

  /**
   * Apertura de la vista de amigos
   * 
   * @param event Evento
   */
  @FXML
  void friendsNavigationClicked(MouseEvent event) {
    // TODO Enlace a la navegación de amigos
  }

  /**
   * Apertura de la vista de recomendaciones
   * 
   * @param event Evento
   */
  @FXML
  void homeNavigationClicked(MouseEvent event) {
    mainApp.initHomeView();
  }

  /**
   * Apertura de la vista de biblioteca
   * 
   * @param event Evento
   */
  @FXML
  void libraryNavigationClicked(MouseEvent event) {
    mainApp.initLibraryView();
  }

  /**
   * Apertura de la vista de ajustes
   * 
   * @param event Evento
   */
  @FXML
  void settingsNavigationClicked(MouseEvent event) {
    // TODO Enlace a la navegación de ajustes
  }

  /**
   * Apertura de la vista de soporte
   * 
   * @param event Evento
   */
  @FXML
  void supportNavigationClicked(MouseEvent event) {
    // TODO Enlace a la navegación del soporte
  }

  /**
   * Setter - MainApp
   * 
   * @param mainApp Aplicación principal
   */
  public void setMainApp(MainApp mainApp) {
    this.mainApp = mainApp;
  }

  /**
   * Setter - toggleSwitch
   * 
   * @param toggleSwitch Toggle Switch para el tema
   */
  public void setToggleSwitch(StackPane toggleSwitch) {

    this.toggleSwitch = toggleSwitch;
    toggleSwitchLayout.getChildren().add(this.toggleSwitch);
  }

  /** Settea todos los iconos */
  public void setIcons() {
    homeIcon.setImage(new Image(NavigationController.class.getResourceAsStream("/images/icons/Home icon.png")));
    LibraryIcon.setImage(new Image(NavigationController.class.getResourceAsStream("/images/icons/save-2.png")));
    exploreIcon.setImage(new Image(NavigationController.class.getResourceAsStream("/images/icons/eye.png")));
    friendsIcon.setImage(new Image(NavigationController.class.getResourceAsStream("/images/icons/people.png")));
    settingsIcon.setImage(new Image(NavigationController.class.getResourceAsStream("/images/icons/setting-2.png")));
    supportIcon.setImage(new Image(NavigationController.class.getResourceAsStream("/images/icons/message-question.png")));
    
    themeIcon.setImage(new Image(NavigationController.class.getResourceAsStream(ToggleSwitch.isOn() ? "/images/icons/sun.png" : "/images/icons/moon.png")));
  }

  /**
   * Setter - currentPage. Establece tanto el atributo como visualmente la página actual del usuario
   * 
   * @param currentPage Página actual en la que se encuentra el usuario
   */
  public void setCurrentPage(NavigationPage currentPage) {
    this.currentPage = currentPage;

    switch (this.currentPage) {

    case HOME:
      labelHome.setTextFill(MARKED_LINK_COLOR);
      break;

    case LIBRARY:
      labelLibrary.setTextFill(MARKED_LINK_COLOR);
      break;

    case EXPLORE:
      labelExplore.setTextFill(MARKED_LINK_COLOR);
      break;

    case FRIENDS:
      labelFriends.setTextFill(MARKED_LINK_COLOR);
      break;

    case SETTINGS:
      labelSettings.setTextFill(MARKED_LINK_COLOR);
      break;

    case SUPPORT:
      labelSupport.setTextFill(MARKED_LINK_COLOR);
      break;

    default:
      labelHome.setTextFill(MARKED_LINK_COLOR);
    }

  }

}
