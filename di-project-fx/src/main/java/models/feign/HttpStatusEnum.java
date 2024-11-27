package models.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enumerado de estados HTTP */
@Getter
@AllArgsConstructor
public enum HttpStatusEnum {

  OK(200, "OK"), CREATED(201, "Created"), NO_CONTENT(204, "No Content"), BAD_REQUEST(400, "Bad Request"),
  UNAUTHORIZED(401, "Unauthorized"), FORBIDDEN(403, "Forbidden"), NOT_FOUND(404, "Not Found"),
  INTERNAL_SERVER_ERROR(500, "Internal Server Error");

  /** Código del estado */
  private final int code;

  /** Nombre del estado */
  private final String statusName;

  /**
   * Obtiene el objeto enumerado a partir del código del estado
   * 
   * @param code Código del estado
   * 
   * @return HttpStatusEnum
   */
  public static HttpStatusEnum fromCode(int code) {
    for (HttpStatusEnum status : values()) {
      if (status.getCode() == code) {
        return status;
      }
    }
    throw new IllegalArgumentException("Unknown HTTP Status Code: " + code);
  }
}