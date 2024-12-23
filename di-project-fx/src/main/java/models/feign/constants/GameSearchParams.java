package models.feign.constants;

/** Clase de constantes para las búsquedas de juegos */
public class GameSearchParams {

  /** Constructor privado. Evita instanciación de clase */
  private GameSearchParams() {
  }

  /** Nombre total o parcial del juego, ignorándo mayúsculas y minúsculas */
  public static final String GAME_NAME = "gameName";

  /** IDs de los géneros */
  public static final String GENRE_IDS = "genreIds";

  /** IDs de las plataformas */
  public static final String PLATFORM_IDS = "platformIds";

  /** Indica si los juegos son creados o no por usuarios del sistema */
  public static final String USER_CREATED = "userCreated";

  /** Campo de ordenación */
  public static final String ORDERING_FIELD = "orderField";

  /** Número de página a mostrar */
  public static final String PAGE_NUMBER = "page";

  /** Tamaño de la página a mostrar */
  public static final String PAGE_SIZE = "pageSize";

}
