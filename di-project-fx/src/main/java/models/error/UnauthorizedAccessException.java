package models.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Excepción lanzada por acceso no autorizado - Falta de secretKey (401) */
@Getter
@Setter
@AllArgsConstructor
public class UnauthorizedAccessException extends RuntimeException {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** Error de la API */
  private final ApiError apiError;

}