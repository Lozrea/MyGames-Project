package controllers;

import application.MainApp;
import controllers.utils.NavigationPage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

  @FXML
  private ImageView LibraryIcon;

  @FXML
  private ImageView appIcon;

  @FXML
  private ImageView exploreIcon;

  @FXML
  private ImageView friendsIcon;

  @FXML
  private ImageView homeIcon;

  @FXML
  private Label labelExplore;

  @FXML
  private Label labelFriends;

  @FXML
  private Label labelHome;

  @FXML
  private Label labelLibrary;

  @FXML
  private Label labelSettings;

  @FXML
  private Label labelSupport;

  @FXML
  private ImageView settingsIcon;

  @FXML
  private ImageView supportIcon;

  @FXML
  private ImageView themeIcon;

  @FXML
  private StackPane toggleSwitchLayout;

  @FXML
  void appIconNavigationClicked(MouseEvent event) {
    mainApp.initHomeView();
  }

  @FXML
  void exploreNavigationClicked(MouseEvent event) {
    mainApp.initExploreView();
  }

  @FXML
  void friendsNavigationClicked(MouseEvent event) {

  }

  @FXML
  void homeNavigationClicked(MouseEvent event) {
    mainApp.initHomeView();
  }

  @FXML
  void libraryNavigationClicked(MouseEvent event) {
    mainApp.initLibraryView();
  }

  @FXML
  void settingsNavigationClicked(MouseEvent event) {

  }

  @FXML
  void supportNavigationClicked(MouseEvent event) {

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

    // TODO Cuando estén los iconos poner las rutas hacia cada uno :)
    /*
     * homeIcon.setImage(new Image(NavigationController.class.getResourceAsStream(""))); LibraryIcon.setImage(new
     * Image(NavigationController.class.getResourceAsStream(""))); exploreIcon.setImage(new
     * Image(NavigationController.class.getResourceAsStream(""))); friendsIcon.setImage(new
     * Image(NavigationController.class.getResourceAsStream(""))); settingsIcon.setImage(new
     * Image(NavigationController.class.getResourceAsStream(""))); supportIcon.setImage(new
     * Image(NavigationController.class.getResourceAsStream("")));
     * 
     * themeIcon .setImage( new Image(NavigationController.class.getResourceAsStream(ToggleSwitch.isOn() ? "opcionOn" :
     * "opcionOff")));
     */

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
