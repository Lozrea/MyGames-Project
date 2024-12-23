package models.feign;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import feign.Response;
import feign.codec.ErrorDecoder;
import models.error.ApiError;
import models.error.ForbiddenAccessException;
import models.error.GenericApiException;
import models.error.ResourceNotFoundException;
import models.error.UnauthorizedAccessException;

/** Deserializador personalizado de errores para OpenFeign */
public class CustomErrorDecoder implements ErrorDecoder {

  /** Mapeador de clases */
  private final ObjectMapper objectMapper;

  /** Constructor */
  public CustomErrorDecoder() {
    this.objectMapper = new ObjectMapper();

    // Módulo para manejar fechas
    this.objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  @Override
  public Exception decode(String methodKey, Response response) {

    // Se deserializa el cuerpo de la respuesta si existe (ApiError)
    ApiError apiError = null;

    try {
      if (response.body() != null) {
        apiError = objectMapper.readValue(response.body().asInputStream(), ApiError.class);

      } else {
        apiError = new ApiError();
        apiError.setStatus(HttpStatusEnum.fromCode(response.status()));
        apiError.setMessage("Error desconocido sin cuerpo de respuesta.");
      }
    } catch (Exception e) {
      return new RuntimeException("Error al procesar la respuesta de error de la API: " + e.getMessage());
    }

    // Dependiendo del código de error, lanza una excepción personalizada
    switch (response.status()) {
    case 404:
      return new ResourceNotFoundException(apiError);

    case 401:
      return new UnauthorizedAccessException(apiError);

    case 403:
      return new ForbiddenAccessException(apiError);

    default:
      return new GenericApiException(apiError);
    }
  }

}
