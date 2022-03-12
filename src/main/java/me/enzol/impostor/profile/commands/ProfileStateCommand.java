package me.enzol.impostor.profile.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import lombok.val;
import me.enzol.impostor.profile.Profile;
import me.enzol.impostor.profile.ProfileState;
import me.enzol.impostor.utilities.CC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommandAlias("state")
@CommandPermission("impostor.profile.state")
public class ProfileStateCommand extends BaseCommand {

  @Default
  public void help(Player player) {
    player.sendMessage(CC.translate("&c&lState Help"));
    player.sendMessage(CC.translate("&7/state set <player> <state>"));
    player.sendMessage(CC.translate("&7/state info <player>"));
    player.sendMessage(CC.translate("&7/state list"));
    player.sendMessage(CC.translate("&7/state changestateall <state>"));
    player.sendMessage(CC.translate("&7/state setgamemodeall <state> <gamemode>"));
  }

  @Subcommand("set")
  public void set(Player player, OnlinePlayer onlinePlayer, ProfileState state) {
    Player target = onlinePlayer.getPlayer();

    Profile profile = Profile.getProfile(target);

    profile.setState(state);

    player.sendMessage(
        CC.translate(
            "&aYou have set &e" + target.getName() + "&a's state to &e" + state.getName()));
  }

  @Subcommand("info")
  public void info(Player player, OnlinePlayer onlinePlayer) {
    Player target = onlinePlayer.getPlayer();

    Profile profile = Profile.getProfile(target);

    player.sendMessage(
        CC.translate("&e" + target.getName() + "'s state is &e" + profile.getState().getName()));
  }

  @Subcommand("list")
  public void list(Player player, ProfileState state) {
    player.sendMessage(CC.translate("&c&lPlayer List with state " + state.getName() + ": "));
    Bukkit.getOnlinePlayers()
        .forEach(
            other -> {
              if (Profile.getProfile(other).getState() == state) {
                player.sendMessage(CC.translate("&e- &f" + other.getName()));
              }
            });
  }

  @Subcommand("changestateall")
  public void changestateAll(Player player, ProfileState state) {
    for (Player target : player.getWorld().getPlayers()) {
      Profile profile = Profile.getProfile(target);

      profile.setState(state);
    }

    player.sendMessage(
        CC.translate("&aYou have changed all player states to &e" + state.getName()));
  }

  @Subcommand("setgamemodeall")
  public void setGameModeAll(Player player, ProfileState state, GameMode gameMode) {
    for (Player target : Bukkit.getOnlinePlayers()) {
      Profile profile = Profile.getProfile(target);

      if (profile.getState() == state) {
        target.setGameMode(gameMode);
      }
    }

    player.sendMessage(
        CC.translate(
            "&aYou have set all players with state &e"
                + state.getName()
                + "&a to gamemode &e"
                + gameMode.name()));
  }

  @CommandAlias("givesnowballsspectator")
  public void giveSnowball(Player player) {
    for (Player target : Bukkit.getOnlinePlayers()) {
      val profile = Profile.getProfile(target);

      if (profile.getState() == ProfileState.SPECTATING) {
        for (int i = 1; i < 5; i++) {
          ItemStack itemStack = new ItemStack(Material.SNOWBALL);
          ItemMeta itemMeta = itemStack.getItemMeta();
          itemMeta.setCustomModelData(i);
          itemStack.setItemMeta(itemMeta);
          target.getInventory().addItem(itemStack);
        }
      }
    }

    player.sendMessage(CC.translate("&aYou have given snowballs to all spectators."));
  }

  @CommandAlias("givesnowballs")
  public void giveSnowball(Player player, OnlinePlayer onlinePlayer) {
    val target = onlinePlayer.getPlayer();

    for (int i = 1; i < 5; i++) {
      ItemStack itemStack = new ItemStack(Material.SNOWBALL);
      ItemMeta itemMeta = itemStack.getItemMeta();
      itemMeta.setCustomModelData(i);
      itemStack.setItemMeta(itemMeta);
      target.getInventory().addItem(itemStack);
    }

    player.sendMessage(CC.translate("&aYou have given snowballs to &e" + target.getName()));
  }
}
