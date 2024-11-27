package models;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO del juego */
@Data
@NoArgsConstructor
public class Game {

  /** ID del juego */
  private Long id;

  /** ID del juego en la API */
  private Long apiId;

  /** Nombre del juego */
  private String name;

  /** Descripción */
  private String description;

  /** Fecha de salida */
  private LocalDate releasedate;

  /** URL de la imagen del juego */
  private String imageurl;

  /** Indica si el juego ha sido creado por el usuario */
  private Boolean userCreated;

  /** Puntuación del juego (del público) */
  private Double rating;

  /** Puntuación del juego (metacritic) */
  private Integer metacriticRating;

  /** Géneros en los que está incluido el juego */
  private List<Genre> genres;

  /** Empresas implicadas en el desarrollo del juego */
  private List<Developer> developers;

  /** Tiendas en las que se puede encontrar el juego */
  private List<Store> stores;

  /** Etiquetas que definen el juego */
  private List<Tag> tags;

  /** Plataformas en las que está disponible el juego y requisitos para su funcionamiento */
  private List<Platform> platforms;

  /** Lista con capturas del juego in-game */
  private List<Screenshot> screenshots;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Game other = (Game) obj;
    return Objects.equals(apiId, other.apiId) && Objects.equals(description, other.description)
        && Objects.equals(developers, other.developers) && Objects.equals(genres, other.genres)
        && Objects.equals(imageurl, other.imageurl) && Objects.equals(metacriticRating, other.metacriticRating)
        && Objects.equals(name, other.name) && Objects.equals(platforms, other.platforms)
        && Objects.equals(rating, other.rating) && Objects.equals(releasedate, other.releasedate)
        && Objects.equals(screenshots, other.screenshots) && Objects.equals(stores, other.stores)
        && Objects.equals(tags, other.tags) && Objects.equals(userCreated, other.userCreated);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(apiId, description, developers, genres, imageurl, metacriticRating, name, platforms, rating, releasedate,
            screenshots, stores, tags, userCreated);
  }

}
