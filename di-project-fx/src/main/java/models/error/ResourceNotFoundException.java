package models.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Excepción lanzada por recurso no encontrado (404) */
@Getter
@Setter
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** Error de la API */
  private final ApiError apiError;

}