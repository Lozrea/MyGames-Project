package models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Tienda donde se puede obtener el juego */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store implements Serializable {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** ID de la tienda */
  private Long id;

  /** Nombre de la tienda */
  private String name;
}
