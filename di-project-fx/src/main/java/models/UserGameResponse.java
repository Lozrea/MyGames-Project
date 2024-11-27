package models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Clase de respuesta a una petición de consulta de objetos de la entidad UserGame */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGameResponse {

  /** Número de elementos que se han encontrado en la búsqueda */
  private long count;

  /** Número de páginas disponibles con resultados */
  private int pageNumber;

  /** Página de resultados */
  private List<UserGame> games;

}
