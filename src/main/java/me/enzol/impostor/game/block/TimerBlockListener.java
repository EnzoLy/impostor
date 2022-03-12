package me.enzol.impostor.game.block;

import lombok.val;
import me.aleiv.core.paper.Core;
import me.enzol.impostor.ImpostorPlugin;
import me.enzol.impostor.profile.Profile;
import me.enzol.impostor.utilities.CC;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TimerBlockListener implements Listener {

  @EventHandler
  public void onInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();

    val profile = Profile.getProfile(player);

    if (!profile.isSetupMode()) return;

    Action action = event.getAction();

    if (action == Action.RIGHT_CLICK_BLOCK) {
      Block block = event.getClickedBlock();

      if (block.getType() != Material.NOTE_BLOCK) {
        return;
      }

      ImpostorPlugin.getInstance().getTimerBlockManager().addBlock(block);

      Core.getInstance()
          .getNoteBlockManager()
          .setBlockData(
              block, Core.getInstance().getNoteBlockManager().getNoteBlockData("banjo", 0, false));

      player.sendMessage(CC.translate("&aTimer block added!"));
    } else if (action == Action.LEFT_CLICK_BLOCK) {
      Block block = event.getClickedBlock();

      if (block.getType() != Material.NOTE_BLOCK) {
        return;
      }

      ImpostorPlugin.getInstance().getTimerBlockManager().removeBlock(block);

      player.sendMessage(CC.translate("&cTimer block removed!"));
    }
  }
}
