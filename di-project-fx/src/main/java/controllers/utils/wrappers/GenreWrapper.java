package controllers.utils.wrappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import models.Genre;

/** Clase wrapper sobre Genre específica para el trabajo sobre Combo Box */
@Getter
@AllArgsConstructor
public class GenreWrapper {

  /** Plataforma envuelta */
  private Genre genre;

  @Override
  public String toString() {
    return genre.getName();
  }

}
