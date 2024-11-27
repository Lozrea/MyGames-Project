package models.feign.client;

import java.util.Map;
import java.util.Set;

import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import models.Game;
import models.GameResponse;
import models.Screenshot;

/** Cliente para juegos */
public interface GameClient {

  /**
   * Obtiene un juego dado su ID
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param id        ID del juego
   * 
   * @return Game
   */
  @RequestLine("GET /games/id/{id}?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  Game getGameById(@Param("secretKey") String secretKey, @Param("id") Long id);

  /**
   * Obtiene un juego dado su ID en la API. En caso de no estar almacenado en la base de datos, se almacenará añadiendo
   * los screenshots dados en el cuerpo de la petición
   * 
   * @param secretKey   Clave secreta para la comunicación
   * @param apiId       ID del juego en la API
   * @param screenshots Capturas de pantalla
   * 
   * @return Game
   */
  @RequestLine("GET /games/api-id/{apiId}?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  Game getGameByApiId(@Param("secretKey") String secretKey, @Param("apiId") Long apiId, Set<Screenshot> screenshots);

  /**
   * Consulta todos los juegos cuyo ID de la API se encuentre entre los dados
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param apiIds    IDs de la API. Se escribirán como una cadena de caracteres con los IDs separados por comas
   * 
   * @return GameResponse
   */
  @RequestLine("GET /games/internal-api-ids?secretKey={secretKey}&apiIds={apiIds}")
  @Headers("Content-Type: application/json")
  GameResponse getGamesByApiIdIn(@Param("secretKey") String secretKey, @Param("apiIds") String apiIds);

  /**
   * Obtiene todos los juegos recomendados para un usuario basado en sus juegos asociados. En caso de no tener juegos
   * asociados, le recomendará otros juegos actuales con buenas valoraciones
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param userId    ID del usuario
   * 
   * @return GameResponse
   */
  @RequestLine("GET /games/recommended/{userId}?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  GameResponse getRecommendedGames(@Param("secretKey") String secretKey, @Param("userId") Long userId);

  /**
   * Obtiene todos los juegos de la base de datos filtrados por parámetros opcionales y paginados. Los parámetros
   * opcionales pueden ser:
   * <ul>
   * <li>gameName: Nombre o parte del nombre del juego (IgnoreCase)</li>
   * <li>genreIds: Cadena de caracteres con los IDs de los géneros, separados por coma</li>
   * <li>platformIds: Cadena de caracteres con los IDs de las plataformas, separados por coma</li>
   * <li>orderField: Campo de ordenación. Será: gameNameAsc, gameNameDesc, rating, releaseDate, metacritic. Por defecto
   * será releaseDate</li>
   * <li>page: Será 0 por defecto en caso de no ser especificado</li>
   * <li>size: Será 10 en caso de no ser especificado</li>
   * </ul>
   * 
   * @param secretKey      Clave secreta para la comunicación
   * @param optionalParams Parámetros opcionales para la consulta
   * 
   * @return GameResponse
   */
  @RequestLine("GET /games/internal?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  GameResponse getGamesFromDb(@Param("secretKey") String secretKey, @QueryMap Map<String, Object> optionalParams);

  /**
   * Obtiene todos los juegos de la base de datos filtrados por parámetros opcionales y paginados cuyo ID de la API no
   * se encuentre entre los aportados. Los parámetros opcionales pueden ser:
   * <ul>
   * <li>gameName: Nombre o parte del nombre del juego (IgnoreCase)</li>
   * <li>genreIds: Cadena de caracteres con los IDs de los géneros, separados por coma</li>
   * <li>platformIds: Cadena de caracteres con los IDs de las plataformas, separados por coma</li>
   * <li>orderField: Campo de ordenación. Será: gameNameAsc, gameNameDesc, rating, releaseDate, metacritic. Por defecto
   * será releaseDate</li>
   * <li>page: Será 0 por defecto en caso de no ser especificado</li>
   * <li>size: Será 10 en caso de no ser especificado</li>
   * </ul>
   * 
   * @param secretKey      Clave secreta para la comunicación
   * @param apiIdsNotIn    IDs de la API entre los que
   * @param optionalParams Parámetros opcionales para la consulta
   * 
   * @return GameResponse
   */
  @RequestLine("GET /games/internal-api-not-in?secretKey={secretKey}&apiIdsNotIn={apiIdsNotIn}")
  @Headers("Content-Type: application/json")
  GameResponse getGamesFromDbApiNotIn(@Param("secretKey") String secretKey, @Param("apiIdsNotIn") String apiIdsNotIn,
      @QueryMap Map<String, Object> optionalParams);

  /**
   * Obtiene todos los juegos de API filtrados por parámetros opcionales y paginados. Los parámetros opcionales pueden
   * ser:
   * <ul>
   * <li>gameName: Nombre o parte del nombre del juego (IgnoreCase)</li>
   * <li>genreIds: Cadena de caracteres con los IDs de los géneros, separados por coma</li>
   * <li>platformIds: Cadena de caracteres con los IDs de las plataformas, separados por coma</li>
   * <li>orderField: Campo de ordenación. Será: name, released, added, created, updated, rating, metacritic en caso de
   * ser orden ascendente, o con un prefijo - en caso de ser descendente. El valor por defecto será -released</li>
   * <li>page: Será 0 por defecto en caso de no ser especificado</li>
   * <li>size: Será 10 en caso de no ser especificado</li>
   * </ul>
   * 
   * @param secretKey      Clave secreta para la comunicación
   * @param optionalParams Parámetros opcionales para la consulta
   * 
   * @return GameResponse
   */
  @RequestLine("GET /games/external?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  GameResponse getGamesFromApi(@Param("secretKey") String secretKey, @QueryMap Map<String, Object> optionalParams);

  /**
   * Almacena un nuevo juego en el sistema
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param game      Juego a almacenar
   * 
   * @return Game
   */
  @RequestLine("POST /games?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  Game saveGame(@Param("secretKey") String secretKey, Game game);

  /**
   * Actualiza un juego del sistema
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param game      Juego a almacenar
   * 
   * @return Game
   */
  @RequestLine("PUT /games?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  Game updateGame(@Param("secretKey") String secretKey, Game game);

  /**
   * Elimina un juego del sistema
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param id        ID del juego a eliminar
   * 
   * @return Game
   */
  @RequestLine("DELETE /games/{id}?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  Game deleteGame(@Param("secretKey") String secretKey, @Param("id") Long id);

}
