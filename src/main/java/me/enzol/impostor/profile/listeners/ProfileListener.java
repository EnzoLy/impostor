package me.enzol.impostor.profile.listeners;

import me.enzol.impostor.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

  @EventHandler
  public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
    if (!event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) {
      return;
    }

    Profile.load(event.getUniqueId());
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.setJoinMessage(null);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();

    Profile profile = Profile.getProfile(player);

    profile.save();

    event.setQuitMessage(null);
  }
}
