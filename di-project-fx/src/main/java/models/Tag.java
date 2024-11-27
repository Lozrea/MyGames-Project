package models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Etiqueta que define un juego */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag implements Serializable {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** ID de la etiqueta */
  private Long id;

  /** Nombre de la etiqueta */
  private String name;

}
