package models.error;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.feign.HttpStatusEnum;

/** Clase de error general para la API */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError implements Serializable {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** Status que devuelve el error */
  private HttpStatusEnum status;

  /** Fecha y hora en la que ocurre el error */
  @JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime dateTime = LocalDateTime.now();

  /** Mensaje del error */
  private String message;

  /** Detalles del error */
  private String details;

}
