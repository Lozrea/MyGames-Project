package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO simple para tratar con usuarios. Creado para trabajar con amigos de usuarios */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserSimple {

  /** ID del usuario */
  private Long id;

  /** Nombre del usuario */
  private String username;

}
