package models.feign;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import models.feign.client.UserClient;

/** Factoría de clientes de OpenFeign */
public class FeignClientFactory {

  /** Constructor privado para evitar inicialización */
  private FeignClientFactory() {

  }

  /**
   * Crea un nuevo cliente para la obtención de datos de usuario a partir de la ruta base hacia el backend
   * 
   * @param baseUrl URL base del backend
   * 
   * @return UserClient
   */
  public static UserClient createUserClient(String baseUrl) {
    return Feign
        .builder()
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder())
        .decoder(new JacksonDecoder())
        .errorDecoder(new CustomErrorDecoder())
        .target(UserClient.class, baseUrl);
  }

}
