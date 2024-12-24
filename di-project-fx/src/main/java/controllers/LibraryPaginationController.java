package controllers;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/** Controlador de la paginación */
public class LibraryPaginationController {

  /** Tamaño de la página a mostrar en la vista de biblioteca */
  protected static final int LIBRARY_PAGE_SIZE = 16;

  /** Respuesta con los juegos actuales mostrados en pantalla */
  private int pagesNumber;

  /** Número actual de página */
  private int currentPage;

  /** Controlador para la vista de exploración */
  private LibraryViewController controller;

  /** Bloque HBox de paginación */
  @FXML
  private HBox hboxPagination;

  /** Establece la paginación actual a mostrar al usuario */
  public void setPagination() {

    // Primera página
    if (currentPage != 1) {
      hboxPagination.getChildren().add(generatePageNumber(1, false));
    }

    // Conjunto central de páginas, incluyendo puntos suspensivos si hay muchas páginas
    if (currentPage > 3) {
      hboxPagination.getChildren().add(new Label("..."));
    }

    if (currentPage >= 3 && currentPage - 1 != 1) {
      hboxPagination.getChildren().add(generatePageNumber(currentPage - 1, false));
    }

    hboxPagination.getChildren().add(generatePageNumber(currentPage, true));

    if (currentPage < pagesNumber && currentPage + 1 != pagesNumber) {
      hboxPagination.getChildren().add(generatePageNumber(currentPage + 1, false));
    }

    if (currentPage < pagesNumber - 2) {
      hboxPagination.getChildren().add(new Label("..."));
    }

    // Última página
    if (currentPage != pagesNumber) {
      hboxPagination.getChildren().add(generatePageNumber(pagesNumber, false));
    }
  }

  /**
   * Setter - pagesNumber
   * 
   * @param pagesNumber Número de págiinas disponibles
   */
  public void setPagesNumber(int pagesNumber) {
    this.pagesNumber = pagesNumber;
  }

  /**
   * Setter - currentPageNumber
   * 
   * @param currentPageNumber Número de página mostrada
   */
  public void setCurrentPageNumber(int currentPageNumber) {
    this.currentPage = currentPageNumber;
  }

  /**
   * Setter - exploreViewController
   * 
   * @param controller Controlador para la vista de búsqueda
   */
  public void setLibraryViewController(LibraryViewController controller) {
    this.controller = controller;
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
        controller.setCurrentPageNumber(pageNumber);
        controller.filterAndOrderGames();
      });
    }

    return pageLabel;
  }

}
