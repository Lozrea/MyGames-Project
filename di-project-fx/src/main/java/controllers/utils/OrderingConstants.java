package controllers.utils;

import java.util.List;
import java.util.Map;

/** Constantes de ordenación */
public class OrderingConstants {

  /** Constructor privado. Evita instanciación de clase */
  private OrderingConstants() {
  }

  /** Orden alfabético ascendente */
  public static final String NAME_ASC = "Orden alfabético A-Z";

  /** Orden alfabético descendente */
  public static final String NAME_DESC = "Orden alfabético Z-A";

  /** Orden por fecha de salida descendente / nuevos primero */
  public static final String RELEASED_DESC = "Nuevos";

  /** Orden por fecha de salida ascendente / más antiguos primero */
  public static final String RELEASED_ASC = "Más antiguos";

  /** Orden por nota de metacritic descendente */
  public static final String METACRITIC_DESC = "Metacritic";

  /** Lista de categorías de ordenación mostrada al usuario para la página de exploración */
  public static final List<String> EXPLORE_SHOWN_ORDERING = List
      .of(NAME_ASC, NAME_DESC, RELEASED_DESC, RELEASED_ASC, METACRITIC_DESC);

  /**
   * Mapa con las constantes de ordenación y su valor en la búsqueda a través del cliente para la página de exploración
   */
  public static final Map<String, String> EXPLORE_ORDERING_MAP = Map
      .of(NAME_ASC, "name", NAME_DESC, "-name", RELEASED_DESC, "-released", RELEASED_ASC, "released", METACRITIC_DESC,
          "-metacritic");

  /** Lista de categorías de ordenación mostrada al usuario para la página de biblioteca */
  public static final List<String> LIBRARY_SHOWN_ORDERING = List.of(NAME_ASC, NAME_DESC, METACRITIC_DESC);

}
