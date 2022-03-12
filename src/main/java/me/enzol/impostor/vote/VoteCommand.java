package me.enzol.impostor.vote;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import lombok.val;
import me.enzol.impostor.ImpostorPlugin;
import me.enzol.impostor.game.GameState;
import me.enzol.impostor.profile.Profile;
import me.enzol.impostor.profile.ProfileState;
import me.enzol.impostor.utilities.CC;
import org.bukkit.entity.Player;

@CommandAlias("vote")
public class VoteCommand extends BaseCommand {

  @Default
  public void onDefault(Player player) {

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
