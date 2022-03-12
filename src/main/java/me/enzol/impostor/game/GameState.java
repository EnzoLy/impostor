package me.enzol.impostor.game;

import org.apache.commons.lang.StringUtils;

public enum GameState {
  LOBBY,
  IN_GAME,
  VOTING;

  public static GameState getByName(String name) {
    for (GameState state : values()) {
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
