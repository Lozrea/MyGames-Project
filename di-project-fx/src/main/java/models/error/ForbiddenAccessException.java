package models.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Excepción de la API por acceso prohibido - Login incorrecto (403) */
@Getter
@Setter
@AllArgsConstructor
public class ForbiddenAccessException extends RuntimeException {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** Error de la API */
  private final ApiError apiError;
}