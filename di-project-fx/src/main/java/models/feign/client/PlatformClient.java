package models.feign.client;

import java.util.List;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import models.PlatformSimple;

/** Cliente de plataformas */
public interface PlatformClient {

  /**
   * Obtiene todas las plataformas del sistema
   * 
   * @param secretKey Clave secreta para la comunicación
   * 
   * @return List(PlatformSimple)
   */
  @RequestLine("GET /platforms?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  List<PlatformSimple> getPlatforms(@Param("secretKey") String secretKey);

}
