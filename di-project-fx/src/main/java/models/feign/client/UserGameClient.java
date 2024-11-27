package models.feign.client;

import java.util.Map;

import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import models.CommentaryUserGameResponse;
import models.UserGame;
import models.UserGameResponse;

/** Cliente para objetos UserGame */
public interface UserGameClient {

  /**
   * Obtiene todos los UserGame dado el ID del usuario
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param id        ID del usuario
   * 
   * @return List(UserGame)
   */
  @RequestLine("GET /users-games/{userId}?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  UserGameResponse getUserGamesByUserId(@Param("secretKey") String secretKey, @Param("userId") Long userId);

  /**
   * Obtiene un objeto base que contiene los comentarios y valoraciones acerca de un juego concreto Los parámetros
   * opcionales pueden ser:
   * <ul>
   * <li>page: Será 0 por defecto en caso de no ser especificado</li>
   * <li>size: Será 10 en caso de no ser especificado</li>
   * </ul>
   * 
   * @param secretKey      Clave secreta para la comunicación
   * @param gameId         ID del juego
   * @param optionalParams Parámetros opcionales para la consulta
   * 
   * @return CommentaryUserGameResponse
   */
  @RequestLine("GET /users-games/game-id/{gameId}?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  CommentaryUserGameResponse getCommentariesForGame(@Param("secretKey") String secretKey, @Param("gameId") Long gameId,
      @QueryMap Map<String, Object> optionalParams);

  /**
   * Obtiene un objeto base que contiene las referencias a un juego de todos los usuarios especificados
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param gameId    ID del juego
   * @param userIds   IDS de los usuarios de los cuales se busca referencia. Debe ser una cadena de caracteres con los
   *                  IDs separados por coma
   * 
   * @return UserGameResponse
   */
  @RequestLine("GET /users-games/game-id-users/{gameId}?secretKey={secretKey}&gameId={gameId}&userIds={userIds}")
  @Headers("Content-Type: application/json")
  UserGameResponse getUserGamesByUserIdsForGame(@Param("secretKey") String secretKey, @Param("gameId") Long gameId,
      @Param("userIds") String userIds);

  /**
   * Obtiene un objeto base que contiene los juegos asociados a los usuarios especificados, filtrados mediante los
   * parámetros opcionales y paginados Los parámetros opcionales pueden ser:
   * <ul>
   * <li>gameName: Nombre o parte del nombre del juego (IgnoreCase)</li>
   * <li>genreIds: Cadena de caracteres con los IDs de los géneros, separados por coma</li>
   * <li>platformIds: Cadena de caracteres con los IDs de las plataformas, separados por coma</li>
   * <li>finished: true - false. Indica si el juego lo ha terminado el usuario</li>
   * <li>orderField: Campo de ordenación. Será: addedDate, personalRating, metacritic, gameNameAsc o gameNameDesc. Por
   * defecto será addedDate</li>
   * <li>page: Será 0 por defecto en caso de no ser especificado</li>
   * <li>size: Será 10 en caso de no ser especificado</li>
   * </ul>
   * 
   * @param secretKey      Clave secreta para la comunicación
   * @param userIds        IDS de los usuarios de los cuales se busca referencia. Debe ser una cadena de caracteres con
   *                       los IDs separados por coma
   * @param optionalParams Parámetros opcionales para la consulta
   * 
   * @return UserGameResponse
   */
  @RequestLine("GET /users-games/by-user-ids?secretKey={secretKey}&userIds={userIds}")
  @Headers("Content-Type: application/json")
  UserGameResponse getUserGamesByUsersIdsRecommended(@Param("secretKey") String secretKey,
      @Param("userIds") String userIds, @QueryMap Map<String, Object> optionalParams);

  /**
   * Obtiene todas las asociaciones del usuario dado con los juegos especificados
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param userId    ID del usuario
   * @param gameIds   IDs de los juegos. Será una cadena de caracteres con los IDs separados por coma
   * 
   * @return UserGameResponse
   */
  @RequestLine("GET /users-games/games-in?secretKey={secretKey}&userId={userId}&gameIdsIn={gameIdsIn}")
  @Headers("Content-Type: application/json")
  UserGameResponse getUserGamesByGameIdIn(@Param("secretKey") String secretKey, @Param("userId") Long userId,
      @Param("gameIds") String gameIdsIn);

  /**
   * Almacena o modifica una asociación entre usuario y juego. Se usará tanto para crear nuevas entidades como para
   * añadir comentarios, valoraciones u otros atributos de ésta
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param userGame  Entidad a guardar
   * 
   * @return UserGame
   */
  @RequestLine("POST /users-games?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  UserGame saveUserGame(@Param("secretKey") String secretKey, UserGame userGame);

  /**
   * Elimina una asociación entre un usuario y un juego
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param userId    ID del usuario
   * @param gameId    ID del juego
   */
  @RequestLine("DELETE /users-games/userId/{userId}/gameId/{gameId}?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  void deleteUserGame(@Param("secretKey") String secretKey, @Param("userId") Long userId, @Param("gameId") Long gameId);
}
