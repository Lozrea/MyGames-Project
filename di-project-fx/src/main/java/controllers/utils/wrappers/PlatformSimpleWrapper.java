package controllers.utils.wrappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import models.PlatformSimple;

/** Clase wrapper sobre PlatformSimple específica para el trabajo sobre Combo Box */
@Getter
@AllArgsConstructor
public class PlatformSimpleWrapper {

  /** Plataforma envuelta */
  private PlatformSimple platform;

  @Override
  public String toString() {
    return platform.getName();
  }

}
