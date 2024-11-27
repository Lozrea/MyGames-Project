package models.feign.client;

import java.util.List;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import models.Genre;

/** Cliente para los géneros */
public interface GenreClient {

  /**
   * Obtiene todos los géneros del sistema
   * 
   * @param secretKey Clave secreta para la comunicación
   * 
   * @return List(Genre)
   */
  @RequestLine("GET /genres?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  List<Genre> getGenres(@Param("secretKey") String secretKey);

}
