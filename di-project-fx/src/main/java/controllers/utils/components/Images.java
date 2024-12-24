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

}
