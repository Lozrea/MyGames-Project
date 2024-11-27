package models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO con los comentarios de un juego */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentaryUserGame {

  /** ID del usuario que ha hecho la reseña */
  private Long userId;

  /** Nombre del usuario que ha hecho la reseña */
  private String username;

  /** ID del juego */
  private Long gameId;

  /** Valoración del juego */
  private Double rating;

  /** Comentario o reseña */
  private String commentary;

  /** Fecha de juego */
  private LocalDate playedDate;

}
