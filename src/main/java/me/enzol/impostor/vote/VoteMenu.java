package me.enzol.impostor.vote;

import me.enzol.impostor.ImpostorPlugin;
import me.enzol.impostor.profile.Profile;
import me.enzol.impostor.profile.ProfileState;
import me.enzol.impostor.utilities.ItemBuilder;
import me.enzol.impostor.utilities.NegativeSpaces;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.jcedeno.libs.rapidinv.RapidInv;

public class VoteMenu {

  private static final ImpostorPlugin plugin = ImpostorPlugin.getInstance();

  public static void openVoteMenu(Player player) {
    RapidInv votesInv = new RapidInv(9 * 5, ChatColor.WHITE + NegativeSpaces.get(-8) + '\u3403');

    int slot = 10;

    for (Player other : Bukkit.getOnlinePlayers()) {
      Profile profile = Profile.getProfile(other.getUniqueId());

      if (profile.getState() != ProfileState.PLAYING) continue;

      ItemBuilder head = new ItemBuilder(Material.PLAYER_HEAD);

      head.name("&a" + other.getName());
      head.owner(other);

      head.lore("");
      head.lore("&fVotos: &6" + profile.getVotesReceived());
      head.lore("");
      head.lore("&7Click para votar a &a" + other.getName());
      head.lore("");

      votesInv.setItem(
          slot++,
          head.build(),
          event -> {
            Player clicked = (Player) event.getWhoClicked();

            plugin.getVotesManager().vote(clicked, other);

            clicked.closeInventory();
          });

      if (slot == 17) {
        slot = 19;
      } else if (slot == 26) {
        slot = 28;
      } else if (slot == 35) {
        slot = 37;
      }
    }

    votesInv.open(player);
  }
}
