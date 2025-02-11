package controllers.utils.components;

/** Clase de constantes para el acceso a imágenes */
public class Images {

  /** Constructor privado. Evita instanciación de clase */
  private Images() {
  }

  /**
   * Ruta base hacia un avatar genérico. Debe sustituirse mediante el método format de String con el número del avatar a
   * usar
   */
  public static final String AVATAR = "/images/Avatares/%d.png";

  /** Icono de un corazón completo (Juego en favoritos) */
  public static final String FULL_HEART = "/images/icons/full-heart.png";

  /** Icono de un corazón vacío (Juego que no está en favoritos */
  public static final String HOLLOW_HEART = "/images/icons/hollow-heart.png";

  /** Icono de la parte derecha de una estrella (Puntuación) */
  public static final String HALF_RIGHT_STAR = "/images/icons/stars/half-right-star.png";

  /** Icono de la parte izquierda de una estrella (Puntuación) */
  public static final String HALF_LEFT_STAR = "/images/icons/stars/half-left-star.png";

  /** Icono simple de estrella para mostrar la puntuación */
  public static final String STAR = "/images/icons/stars/star.png";

}
