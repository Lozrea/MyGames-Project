package models;

import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO de la plataforma */
@Data
@NoArgsConstructor
public class Platform {

  /** ID de la plataforma */
  private Long id;

  /** ID del juego asociado */
  private Long gameId;

  /** Nombre de la plataforma */
  private String name;

  /** Requerimientos mínimos del juego en la plataforma */
  private String requirementsMinimum;

  /** Requerimientos recomendados del juego en la plataforma */
  private String requirementsRecommended;

}
