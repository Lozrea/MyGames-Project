package controllers;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import models.GameResponse;
import models.feign.constants.GameSearchParams;

/** Controlador de la paginación */
public class PaginationController {

  /** Respuesta con los juegos actuales mostrados en pantalla */
  private GameResponse currentGameResponse;

  /** Controlador para la vista de exploración */
  private ExploreViewController exploreViewController;

  /** Bloque HBox de paginación */
  @FXML
  private HBox hboxPagination;

  /** Establece la paginación actual a mostrar al usuario */
  public void setPagination() {

    int currentPage = (int) exploreViewController.getSearchParams().get(GameSearchParams.PAGE_NUMBER);

    // Primera página
    if (currentPage != 1) {
      hboxPagination.getChildren().add(generatePageNumber(1, false));
    }

    // Conjunto central de páginas, incluyendo puntos suspensivos si hay muchas páginas
    if (currentPage > 3) {
      hboxPagination.getChildren().add(new Label("..."));
    }

    if (currentGameResponse.isPrevious() && currentPage - 1 != 1) {
      hboxPagination.getChildren().add(generatePageNumber(currentPage - 1, false));
    }

    hboxPagination.getChildren().add(generatePageNumber(currentPage, true));

    if (currentGameResponse.isNext() && currentPage + 1 != currentGameResponse.getPages()) {
      hboxPagination.getChildren().add(generatePageNumber(currentPage + 1, false));
    }

    if (currentPage < currentGameResponse.getPages() - 2) {
      hboxPagination.getChildren().add(new Label("..."));
    }

    // Última página
    if (currentPage != currentGameResponse.getPages()) {
      hboxPagination.getChildren().add(generatePageNumber(currentGameResponse.getPages(), false));
    }
  }

  /**
   * Setter - currentGameResponse
   * 
   * @param currentGameResponse Respuesta con los juegos actualmente mostrados
   */
  public void setCurrentGameResponse(GameResponse currentGameResponse) {
    this.currentGameResponse = currentGameResponse;
  }

  /**
   * Setter - exploreViewController
   * 
   * @param exploreViewController Controlador para la vista de exploración
   */
  public void setExploreViewController(ExploreViewController exploreViewController) {
    this.exploreViewController = exploreViewController;
  }

  /**
   * Genera un Label para el número de página indicado. Distinguirá la página actual y no permitirá hacer click sobre
   * ella
   * 
   * @param pageNumber    Número de página
   * @param isCurrentPage Indica si la página es la actualmente mostrada
   * 
   * @return Label
   */
  private Label generatePageNumber(int pageNumber, boolean isCurrentPage) {

    Label pageLabel = new Label(String.valueOf(pageNumber));

    pageLabel.setTextFill(isCurrentPage ? Color.BLUE : Color.CADETBLUE);

    if (!isCurrentPage) {
      pageLabel.setCursor(Cursor.HAND);
      pageLabel.setOnMouseClicked(event -> {
        exploreViewController.getSearchParams().put(GameSearchParams.PAGE_NUMBER, pageNumber);
        exploreViewController.chargeData();
      });
    }

    return pageLabel;
  }

}
