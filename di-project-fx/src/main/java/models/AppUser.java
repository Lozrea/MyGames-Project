package models;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO del usuario del sistema. DTO seguro puesto que no incorpora la contraseña en las peticiones */
@Data
@NoArgsConstructor
public class AppUser {

  /** ID del usuario */
  private Long id;

  /** Username del usuario */
  private String username;

  /** Nombre completo del usuario */
  private String name;

  /** Email del usuario */
  private String email;

  /** Avatar del usuario */
  private Integer avatar;

  /** Lista de IDs de los amigos del usuario */
  private List<Long> friendIds;

  /** Lista de usuarios esperando para aceptar la petición de amistad */
  private List<AppUserSimple> friendsToAccept;

}
