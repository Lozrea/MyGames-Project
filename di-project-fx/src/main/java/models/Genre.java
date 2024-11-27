package models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Género en el que se puede encapsular un videojuego */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre implements Serializable {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** ID */
  private Long id;

  /** Nombre del género */
  private String name;

}
