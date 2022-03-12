package me.enzol.impostor.game.block;

import com.google.common.collect.Lists;
import java.util.List;
import me.aleiv.core.paper.Core;
import me.enzol.impostor.ImpostorPlugin;
import me.enzol.impostor.utilities.LocationUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;

public class TimerBlockManager {

  private final List<Block> blocks = Lists.newArrayList();

  private final ImpostorPlugin plugin;

  public TimerBlockManager(ImpostorPlugin plugin) {
    this.plugin = plugin;

    plugin.getConfig().getStringList("timer-blocks").forEach(locationString -> {
      Location location = LocationUtils.deSerialize(locationString);

      if (location != null) {
        addBlock(location);
      }
    });
  }

  public void save(){
    plugin.getConfig().set("timer-blocks", blocks.stream().map(block -> LocationUtils.serialize(block.getLocation())).toArray());
    plugin.saveConfig();
  }

  public void addBlock(Location location) {
    blocks.add(location.getBlock());
  }

  public void addBlock(Block block) {
    blocks.add(block);
  }

  public void removeBlock(Location location) {
    blocks.remove(location.getBlock());
  }

  public void removeBlock(Block block) {
    blocks.remove(block);
  }

  public void onTick() {
    String timer = plugin.getGame().getGlobalTimer().getReadableTime();

    char[] chars = timer.toCharArray();

    int firstMinute = Integer.parseInt(String.valueOf(chars[0]));
    int secondMinute = Integer.parseInt(String.valueOf(chars[1]));

    int firstSecond = Integer.parseInt(String.valueOf(chars[3]));
    int secondSecond = Integer.parseInt(String.valueOf(chars[4]));

    if (!blocks.isEmpty() && blocks.size() >= 4) {
      Core.getInstance().getNoteBlockManager().setBlockData(blocks.get(0), changeNote(firstMinute));
      Core.getInstance()
          .getNoteBlockManager()
          .setBlockData(blocks.get(1), changeNote(secondMinute));

      Core.getInstance().getNoteBlockManager().setBlockData(blocks.get(2), changeNote(firstSecond));
      Core.getInstance()
          .getNoteBlockManager()
          .setBlockData(blocks.get(3), changeNote(secondSecond));
    }
  }

  public NoteBlock changeNote(int note) {
    return Core.getInstance().getNoteBlockManager().getNoteBlockData("banjo", note, false);
  }
}
