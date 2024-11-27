package models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Captura de pantalla tomada del juego */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Screenshot implements Serializable {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** ID */
  private Long id;

  private String imageUrl;

}
