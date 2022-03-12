package me.enzol.impostor.game.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import lombok.val;
import me.enzol.impostor.ImpostorPlugin;
import me.enzol.impostor.game.GameState;
import me.enzol.impostor.profile.Profile;
import me.enzol.impostor.profile.ProfileState;
import me.enzol.impostor.utilities.CC;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("impostor")
@CommandPermission("impostor.command.game")
public class GameCommands extends BaseCommand {

  private final ImpostorPlugin plugin;

  public GameCommands(ImpostorPlugin plugin) {
    this.plugin = plugin;
  }

  @Subcommand("setstate")
  public void setState(CommandSender player, GameState state) {
    val game = this.plugin.getGame();

    game.setState(state);

    player.sendMessage(CC.translate("&aGame state set to &e" + state.getName()));
  }

  @Subcommand("togglebossbar")
  public void toggleBossBar(CommandSender player) {
    val game = this.plugin.getGame();

    game.getTimerBossBar().setVisible(!game.getTimerBossBar().isVisible());

    player.sendMessage(
        CC.translate(
            "&aBoss bar is now "
                + (game.getTimerBossBar().isVisible() ? "&avisible" : "&chidden")));
  }

  @CommandAlias("resetvotes")
  public void resetVotes(CommandSender player) {
    val voteManager = this.plugin.getVotesManager();

    voteManager.resetVotes();

    player.sendMessage(CC.translate("&aVotos reseteados."));
  }

  @CommandAlias("infovotes")
  public void infoVotes(CommandSender player) {
    val voteManager = this.plugin.getVotesManager();

    player.sendMessage(CC.translate("&aVotos:"));
    for (Player votted : voteManager.getSortedVotes()) {
      player.sendMessage(
          CC.translate(
              "&e" + votted.getName() + " &7- &a" + voteManager.getVotes(votted.getUniqueId())));
    }
  }

  @CommandAlias("eliminatedmostvoted")
  public void kickMostVoted(CommandSender player) {
    val voteManager = this.plugin.getVotesManager();

    val mostVoted = voteManager.getMostMoted();
    if (mostVoted != null) {
      val profile = Profile.getProfile(mostVoted);
      Bukkit.broadcastMessage(
          CC.translate("&e" + profile.getPlayer().getName() + " &4ha sido eliminado."));

      profile.setState(ProfileState.ELIMINATED);

      for (Player other : Bukkit.getOnlinePlayers()) {
        if (mostVoted.getUniqueId().equals(other.getUniqueId())) continue;

        other.playSound(other.getLocation(), "sfx.elimination", 1, 1);

        plugin.showTitle(
            other,
            CC.translate("&e" + profile.getPlayer().getName()),
            "&4Ha sido eliminado",
            0,
            20,
            0);
      }

      plugin.showTitle(mostVoted, "&4Has sido eliminado", "", 0, 20, 0);
      mostVoted.setGameMode(GameMode.SPECTATOR);
    } else {
      player.sendMessage(CC.translate("&cNo players voted"));
    }
  }

  @CommandAlias("selectimpostor")
  @CommandCompletion("@players")
  public void selectImpostor(CommandSender player, OnlinePlayer onlinePlayer) {
    val game = this.plugin.getGame();
    val target = onlinePlayer.getPlayer();
    game.setImpostor(target.getUniqueId());

    player.sendMessage(
        CC.translate("&aHas seleccionado a &e" + target.getName() + "&a como impostor."));

    for (Player other : Bukkit.getOnlinePlayers()) {
      Profile profile = Profile.getProfile(other);

      if (profile.getState() != ProfileState.PLAYING) {
        continue;
      }

      if (game.getImpostor() == other.getUniqueId()) {
        plugin.showTitle(other, ChatColor.of("#fc3d03") + "Eres impostor", "", 0, 40, 0);
      } else {
        plugin.showTitle(other, ChatColor.of("#03fc6b") + "Eres inocente", "", 0, 40, 0);
      }
    }
  }

  @CommandAlias("removeimpostor")
  public void removeImpostor(CommandSender player) {
    val game = this.plugin.getGame();

    game.setImpostor(null);

    player.sendMessage(CC.translate("&cEl impostor ha sido removido."));
  }

  @CommandAlias("mutechat")
  public void muteChat(CommandSender player) {
    val game = this.plugin.getGame();

    game.setMutedChat(!game.isMutedChat());

    Bukkit.broadcastMessage(
        CC.translate("&aChat " + (game.isMutedChat() ? "&7muteado" : "&7desmuteado")));
  }

  @CommandAlias("setupmode")
  public void setupMode(Player player) {
    val profile = Profile.getProfile(player);

    profile.setSetupMode(!profile.isSetupMode());

    Bukkit.broadcastMessage(
        CC.translate("&aSetup mode " + (profile.isSetupMode() ? "&7activado" : "&7desactivado")));
  }

  @CommandAlias("allowdamage")
  public void allowDamage(Player player) {

    val game = this.plugin.getGame();

    game.setAllowDamage(!game.isAllowDamage());

    player.sendMessage(
        CC.translate("&aDamage " + (game.isAllowDamage() ? "&7activado" : "&7desactivado")));
  }
}
