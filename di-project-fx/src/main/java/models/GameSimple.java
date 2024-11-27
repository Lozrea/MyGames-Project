package models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameSimple {

  /** ID del juego */
  private Long id;

  /** Nombre del juego */
  private String name;

  /** URL de la imagen del juego */
  private String imageurl;

  /** Nota de metacritic */
  private Integer metacriticRating;

}
