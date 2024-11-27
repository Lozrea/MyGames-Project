package models;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de la entidad AppUser destinado a la creación y modificación de datos. Incorpora la contraseña para dichas
 * transacciones
 */
@Data
@NoArgsConstructor
public class AppUserCreate {

  /** ID del usuario */
  private Long id;

  /** Username del usuario */
  private String username;

  /** Contraseña del usuario */
  private String password;

  /** Nombre completo del usuario */
  private String name;

  /** Email del usuario */
  private String email;

  /** Avatar del usuario */
  private Integer avatar;
}
