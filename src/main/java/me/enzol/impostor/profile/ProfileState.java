package me.enzol.impostor.profile;

import org.apache.commons.lang.StringUtils;

public enum ProfileState {
  PLAYING,
  SPECTATING,
  ELIMINATED;

  public static ProfileState getByName(String name) {
    for (ProfileState state : values()) {
      if (state.name().equalsIgnoreCase(name)) {
        return state;
      }
    }

    return null;
  }

  public String getName() {
    return StringUtils.capitalize(name().toLowerCase());
  }
}
