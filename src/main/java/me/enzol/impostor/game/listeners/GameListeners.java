package me.enzol.impostor.game.listeners;

import lombok.val;
import me.enzol.impostor.ImpostorPlugin;
import me.enzol.impostor.game.GameState;
import me.enzol.impostor.game.events.GameTickEvent;
import me.enzol.impostor.profile.Profile;
import me.enzol.impostor.profile.ProfileState;
import me.enzol.impostor.utilities.CC;
import me.enzol.impostor.vote.VoteMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

public record GameListeners(ImpostorPlugin plugin) implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    val player = event.getPlayer();

    Bukkit.getBossBars().forEachRemaining(bar -> {
      if (bar.getPlayers().contains(player)) {
        bar.removePlayer(player);
      }
    });

    plugin.getGame().getTimerBossBar().addPlayer(player);

    player.teleport(player.getWorld().getSpawnLocation());
  }

  @EventHandler
  public void onGameTick(GameTickEvent event) {
    Bukkit.getScheduler()
        .runTask(
            plugin,
            () -> {
              if(plugin.getGame().getGlobalTimer() != null) {
                plugin.getTimerBlockManager().onTick();
              }
            });
  }

  @EventHandler
  public void onDamage(EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof Player)) {
      return;
    }

    val player = (Player) event.getDamager();

    if (player.hasPermission("buildbattle.admin")) {
      return;
    }

    event.setCancelled(true);
  }

  @EventHandler
  public void onDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player) {
      event.setCancelled(!plugin.getGame().isAllowDamage());
    }
  }

  @EventHandler
  public void onFood(FoodLevelChangeEvent event) {
    val player = (Player) event.getEntity();
    if (!player.hasPotionEffect(PotionEffectType.SATURATION) && !player.hasPotionEffect(
        PotionEffectType.HUNGER)) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onInteractEntity(PlayerInteractEntityEvent event) {
    Player player = event.getPlayer();

    if(event.getRightClicked() instanceof ArmorStand){
      return;
    }

    if (player.hasPermission("impostor.build")) {
      return;
    }

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onInteractEntity(PlayerInteractAtEntityEvent event) {
    Player player = event.getPlayer();

    if(event.getRightClicked() instanceof ArmorStand armorStand){

      if(armorStand.isSmall()){
        armorStand.addPassenger(player);
      }

      return;
    }

    if (player.hasPermission("impostor.build")) {
      return;
    }

    event.setCancelled(true);
  }

  @EventHandler
  public void onLeavesDecay(LeavesDecayEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event){
    Player player = event.getPlayer();
    Block block = event.getClickedBlock();

    if(block == null){
      return;
    }

    if(block.getType() == Material.WARPED_BUTTON){
      if (ImpostorPlugin.getInstance().getGame().getState() != GameState.VOTING) {
        player.sendMessage(CC.translate("&cNo puedes votar en este momento."));
        return;
      }

      val profile = Profile.getProfile(player);

      if(profile.getState() != ProfileState.PLAYING) {
        player.sendMessage(CC.translate("&cNo puedes votar en este momento."));
        return;
      }

      VoteMenu.openVoteMenu(player);
    }
  }

  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();

    if(ImpostorPlugin.getInstance().getGame().isMutedChat() && !player.hasPermission("impostor.chat.mute")){

      player.sendMessage(ChatColor.RED + "Chat is muted!");
      event.setCancelled(true);
    }

    event.setFormat(CC.translate("&a" + player.getName() + "&7:&f " + event.getMessage()));
  }
}
