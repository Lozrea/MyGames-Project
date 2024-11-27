package models;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO de la clase UserGame */
@Data
@NoArgsConstructor
public class UserGame {

  /** Usuario */
  private Long userId;

  /** Juego */
  private GameSimple game;

  /** Fecha en la que se añadió el juego a la biblioteca del usuario */
  private LocalDate addedDate;

  /** Puntuación del juego puesta por el usuario */
  private Double personalRating;

  /** Comentario del usuario acerca del juego */
  private String commentary;

  /** Indica si el usuario ha marcado el juego como favorito */
  private Boolean favorite;

  /** Indica si el juego lo ha terminado */
  private Boolean finished;

  /** Fecha en la que se jugó al juego */
  private LocalDate playedDate;

}
