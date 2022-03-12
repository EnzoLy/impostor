package me.enzol.impostor.profile;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
@Setter
public class Profile {

  private static final Map<UUID, Profile> profiles = Maps.newHashMap();

  private final UUID uuid;
  private ProfileState state = ProfileState.SPECTATING;
  private int votesReceived;
  private boolean setupMode;

  public static void load(UUID uniqueId) {
    profiles.put(uniqueId, new Profile(uniqueId));
  }

  public void resetVotes() {
    votesReceived = 0;
  }

  public static Profile getProfile(Player player) {
    return getProfile(player.getUniqueId());
  }

  public static Profile getProfile(UUID uuid) {
    return profiles.get(uuid);
  }

  public Player getPlayer() {
    return Bukkit.getPlayer(uuid);
  }

  public void save() {}
}
