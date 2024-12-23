package models.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import models.feign.client.GameClient;
import models.feign.client.GenreClient;
import models.feign.client.PlatformClient;
import models.feign.client.UserClient;
import models.feign.client.UserGameClient;

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

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    return Feign
        .builder()
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder(objectMapper))
        .decoder(new JacksonDecoder(objectMapper))
        .errorDecoder(new CustomErrorDecoder())
        .target(UserClient.class, baseUrl);
  }

  /**
   * Crea un nuevo cliente para la obtención de datos de plataformas a partir de la ruta base hacia el backend
   * 
   * @param baseUrl URL base del backend
   * 
   * @return PlatformClient
   */
  public static PlatformClient createPlatformClient(String baseUrl) {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    return Feign
        .builder()
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder(objectMapper))
        .decoder(new JacksonDecoder(objectMapper))
        .errorDecoder(new CustomErrorDecoder())
        .target(PlatformClient.class, baseUrl);
  }

  /**
   * Crea un nuevo cliente para la obtención de datos de géneros a partir de la ruta base hacia el backend
   * 
   * @param baseUrl URL base del backend
   * 
   * @return GenreClient
   */
  public static GenreClient createGenreClient(String baseUrl) {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    return Feign
        .builder()
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder(objectMapper))
        .decoder(new JacksonDecoder(objectMapper))
        .errorDecoder(new CustomErrorDecoder())
        .target(GenreClient.class, baseUrl);
  }

  /**
   * Crea un nuevo cliente para la obtención de datos de juegos a partir de la ruta base hacia el backend
   * 
   * @param baseUrl URL base del backend
   * 
   * @return GameClient
   */
  public static GameClient createGameClient(String baseUrl) {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    return Feign
        .builder()
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder(objectMapper))
        .decoder(new JacksonDecoder(objectMapper))
        .errorDecoder(new CustomErrorDecoder())
        .target(GameClient.class, baseUrl);
  }

  /**
   * Crea un nuevo cliente para la obtención de relaciones entre usuarios y su colección de juegos a partir de la ruta
   * base hacia el backend
   * 
   * @param baseUrl URL base del backend
   * 
   * @return UserGameClient
   */
  public static UserGameClient createUserGameClient(String baseUrl) {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    return Feign
        .builder()
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder(objectMapper))
        .decoder(new JacksonDecoder(objectMapper))
        .errorDecoder(new CustomErrorDecoder())
        .target(UserGameClient.class, baseUrl);
  }

}
