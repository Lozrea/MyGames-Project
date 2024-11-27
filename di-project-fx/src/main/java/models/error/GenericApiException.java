package models.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Excepción genérica de la API */
@Getter
@Setter
@AllArgsConstructor
public class GenericApiException extends RuntimeException {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** Error de la API */
  private final ApiError apiError;
}