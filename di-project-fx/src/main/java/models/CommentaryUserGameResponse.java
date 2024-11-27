package models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Respuesta a una petición sobre reseñas de un juego */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentaryUserGameResponse {

  /** Número de reseñas obtenidas en la consulta */
  private Long count;

  /** Número de páginas de resultados */
  private int pages;

  /** Página de reseñas */
  private List<CommentaryUserGame> results;

}
