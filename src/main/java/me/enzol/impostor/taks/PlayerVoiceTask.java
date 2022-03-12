package me.enzol.impostor.taks;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerVoiceTask implements Runnable {

  private final List<UUID> pressing = Lists.newArrayList();

  @Override
  public void run() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (player.isBlocking()) {
        if (!pressing.contains(player.getUniqueId())) {
          pressing.add(player.getUniqueId());
          player.performCommand("squidvoice speaker true");
        }
      } else {
        if (pressing.contains(player.getUniqueId())) {
          pressing.remove(player.getUniqueId());
          player.performCommand("squidvoice speaker false");
        }
      }
    }
  }
}
