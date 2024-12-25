package models.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enumerado de estados HTTP */
@Getter
@AllArgsConstructor
public enum HttpStatusEnum {

  /** HTTP Status 200 - OK */
  OK(200, "OK"),

  /** HTTP Status 201 - CREATED */
  CREATED(201, "Created"),

  /** HTTP Status 204 - NO CONTENT */
  NO_CONTENT(204, "No Content"),

  /** HTTP Status 400 - BAD REQUEST */
  BAD_REQUEST(400, "Bad Request"),

  /** HTTP Status 401 - UNAUTHORIZED */
  UNAUTHORIZED(401, "Unauthorized"),

  /** HTTP Status 403 - FORBIDDEN */
  FORBIDDEN(403, "Forbidden"),

  /** HTTP Status 404 - NOT FOUND */
  NOT_FOUND(404, "Not Found"),

  /** HTTP Status 500 - INTERNAL SERVER ERROR */
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