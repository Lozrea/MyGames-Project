package models.validation;

import java.util.regex.Pattern;

/** Clase de validaciones para el usuario */
public class AppUserValidation {

  /** Patrón para la validación de username - Entre 1 y 10 caracteres con letras minúscula, mayúscula, números, _ y - */
  private static final Pattern PATTERN_USERNAME = Pattern.compile("[\\w-]{1,20}");

  /** Patrón para la validación de una parte del nombre. Debe comenzar por mayúscula */
  private static final Pattern PATTERN_NAME = Pattern.compile("[\\p{IsLatin}\\s]{1,100}");

  /**
   * Patrón para la validación de contraseña - Entre 8 y 16 caracteres con al menos una letra minúscula, una mayúscula,
   * un número y un símbolo especial entre los dados en la expresión regular
   */
  private static final Pattern PATTERN_PASSWORD = Pattern
      .compile("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\.;,:\\/\\*&%$\\(\\)])[a-zA-Z\\d\\.;,:\\/\\*&%$\\(\\)]{8,16}");

  /** Patrón para la validación de email */
  private static final Pattern PATTERN_EMAIL = Pattern
      .compile("[a-zA-Z0-9][\\w\\.-]*@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)?\\.[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)?");

  /** Constructor privado para evitar inicialización de clase */
  private AppUserValidation() {
  }

  /**
   * Valida un Email
   * 
   * @param txtEmail Código postal
   * 
   * @return String - El email validado
   * 
   * @throws IllegalArgumentException En caso de no ser válido
   */
  public static String validateEmail(String txtEmail) {

    if (PATTERN_EMAIL.matcher(txtEmail).matches()) {
      return txtEmail;
    }

    throw new IllegalArgumentException();
  }

  /**
   * Valida un username. Debe constar de entre 1 y 20 caracteres y sólo puede contener letras minúsculas, mayúsculas,
   * números y caracteres -, _
   * 
   * @param txtUsername Nombre de usuario
   * 
   * @return String - El username validado
   * 
   * @throws IllegalArgumentException En caso de no ser válido
   */
  public static String validateUsername(String txtUsername) {

    if (PATTERN_USERNAME.matcher(txtUsername).matches()) {
      return txtUsername;
    }

    throw new IllegalArgumentException();
  }

  /**
   * Valida un nombre completo. El nombre debe componerse de letras y espacios, teniendo una longitud máxima de 100
   * caracteres
   * 
   * @param name Nombre
   * 
   * @return String - El nombre validado
   * 
   * @throws IllegalArgumentException En caso de no ser válido
   */
  public static String validateName(String name) {

    if (!PATTERN_NAME.matcher(name).matches()) {
      throw new IllegalArgumentException();
    }
    return name;
  }

  /**
   * Valida una contraseña. Debe constar de entre 8 y 16 caracteres y debe contener, al menos, 1 letra minúscula, 1
   * letra mayúscula, 1 número y 1 caracter especial de entre: .;,:/*&%$()
   * 
   * @param txtPassword Contraseña de usuario
   * 
   * @return String - La contraseña validada
   * 
   * @throws IllegalArgumentException En caso de no ser válido
   */
  public static String validatePassword(String txtPassword) {

    if (PATTERN_PASSWORD.matcher(txtPassword).matches()) {
      return txtPassword;
    }

    throw new IllegalArgumentException();
  }

}
