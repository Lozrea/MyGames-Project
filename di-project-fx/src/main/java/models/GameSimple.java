package models;

import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO de un juego simple */
@Data
@NoArgsConstructor
public class GameSimple {

  /** ID del juego */
  private Long id;

  /** ID de la API del juego */
  private Long apiId;

  /** Nombre del juego */
  private String name;

  /** URL de la imagen del juego */
  private String imageurl;

  /** Nota de metacritic */
  private Integer metacriticRating;

}
