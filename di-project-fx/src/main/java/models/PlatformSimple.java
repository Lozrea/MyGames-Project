package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Dto simple para tratar plataformas */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformSimple {

  /** ID de la plataforma */
  private Long id;

  /** Nombre de la plataforma */
  private String name;

}
