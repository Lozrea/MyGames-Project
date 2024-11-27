package models.feign.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import models.AppUser;
import models.AppUserCreate;

/** Cliente de usuarios */
public interface UserClient {

  /**
   * Obtiene un usuario dado su ID
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param id        ID del usuario
   * 
   * @return AppUser
   */
  @RequestLine("GET /users/id/{id}?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  AppUser getUserById(@Param("secretKey") String secretKey, @Param("id") Long id);

  /**
   * Obtiene un usuario dado su username
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param username  Username
   * 
   * @return AppUser
   */
  @RequestLine("GET /users/username/{username}?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  AppUser getUserByUsername(@Param("secretKey") String secretKey, @Param("username") String username);

  /**
   * Obtiene un usuario dado su email
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param email     Email del usuario
   * 
   * @return AppUser
   */
  @RequestLine("GET /users/email/{email}?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  AppUser getUserByEmail(@Param("secretKey") String secretKey, @Param("email") String email);

  /**
   * Realiza el login y devuelve el usuario loggeado
   * 
   * @param secretKey     Clave secreta para la comunicación
   * @param appUserCreate Entidad usuario con todos los parámetros
   * 
   * @return AppUser
   */
  @RequestLine("POST /users/login?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  AppUser login(@Param("secretKey") String secretKey, AppUserCreate appUserCreate);

  /**
   * Se solicita la recuperación de contraseña. Se devuelve el usuario en caso de haber sido cambiada
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param email     Email del usuario cuya contraseña va a ser cambiada
   * 
   * @return AppUser
   */
  @RequestLine("POST /users/recover-password?secretKey={secretKey}&email={email}")
  @Headers("Content-Type: application/json")
  AppUser recoverPassword(@Param("secretKey") String secretKey, @Param("email") String email);

  /**
   * Crea un nuevo usuario del sistema
   * 
   * @param secretKey     Clave secreta para la comunicación
   * @param appUserCreate Usuario a crear
   * 
   * @return AppUser - Usuario creado con su ID correspondiente
   */
  @RequestLine("POST /users?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  AppUser createUser(@Param("secretKey") String secretKey, AppUserCreate appUserCreate);

  /**
   * Actualiza un usuario del sistema
   * 
   * @param secretKey     Clave secreta para la comunicación
   * @param appUserCreate Usuario a actualizar
   * 
   * @return AppUser - Usuario modificado
   */
  @RequestLine("PUT /users?secretKey={secretKey}")
  @Headers("Content-Type: application/json")
  AppUser updateUser(@Param("secretKey") String secretKey, AppUserCreate appUserCreate);

  /**
   * Añade un nuevo amigo
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param userId    ID del usuario
   * @param friendId  ID del amigo
   */
  @RequestLine("PUT /users/add-friend/{userId}/{friendId}")
  @Headers("Content-Type: application/json")
  void addFriend(@Param("secretKey") String secretKey, @Param("userId") Long userId, @Param("friendId") Long friendId);

  /**
   * Confirma la solicitud de amistad de otra persona
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param userId    ID del usuario
   * @param friendId  ID de la persona que ha enviado la petición de amistad
   */
  @RequestLine("PUT /users/confirm-friend/{userId}/{friendId}")
  @Headers("Content-Type: application/json")
  void confirmFriend(@Param("secretKey") String secretKey, @Param("userId") Long userId,
      @Param("friendId") Long friendId);

  /**
   * Elimina la amistad entre dos usuarios del sistema
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param userId    ID del usuario
   * @param friendId  ID de la persona que ha enviado la petición de amistad
   */
  @RequestLine("PUT /users/remove-friend/{userId}/{friendId}")
  @Headers("Content-Type: application/json")
  void removeFriend(@Param("secretKey") String secretKey, @Param("userId") Long userId,
      @Param("friendId") Long friendId);

  /**
   * Elimina la amistad entre dos usuarios del sistema
   * 
   * @param secretKey Clave secreta para la comunicación
   * @param userId    ID del usuario
   * @param friendId  ID de la persona que ha enviado la petición de amistad
   */
  @RequestLine("DELETE /users/{appUserId}")
  @Headers("Content-Type: application/json")
  void deleteUser(@Param("appUserId") Long appUserId);
}
