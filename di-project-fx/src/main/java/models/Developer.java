package models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Desarrollador del juego */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Developer implements Serializable {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** ID */
  private Long id;

  /** Nombre de la empresa o persona desarrolladora */
  private String name;

  /** Ruta hacia la imagen representativa de la empresa (Logo) */
  private String image;
}
