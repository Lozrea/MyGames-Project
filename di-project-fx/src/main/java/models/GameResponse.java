package models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Respuesta a una petición de juegos */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

  /** Número de juegos obtenidos en la consulta */
  private long count;

  /** Número de páginas de resultados */
  private int pages;

  /** Indica si hay más páginas de resultados en adelante */
  private Boolean next;

  /** Indica si hay más páginas de resultados hacia atrás */
  private Boolean previous;

  /** Página de juegos */
  private List<Game> results;
}
