package me.enzol.impostor.game.timer.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.enzol.impostor.ImpostorPlugin;
import me.enzol.impostor.game.timer.Timer;
import me.enzol.impostor.utilities.CC;
import me.enzol.impostor.utilities.time.TimeUtil;
import org.bukkit.entity.Player;

@CommandAlias("timer")
@CommandPermission("buildbattle.timer")
public class TimerCommand extends BaseCommand {

  private final ImpostorPlugin plugin;

  public TimerCommand(ImpostorPlugin plugin) {
    this.plugin = plugin;
  }

  @Default
  public void help(Player player) {
    player.sendMessage(CC.translate("&c&lTimer commands:"));

    player.sendMessage(
        CC.translate("&7/timer create <time> &7- &aCreates a timer with the time specified."));

    player.sendMessage(CC.translate("&7/timer add <time> &7- &aAdds time to the Timer"));

    player.sendMessage(
        CC.translate("&7/timer decrease <time> &7- &aDecreases time from the Timer"));

    player.sendMessage(
        CC.translate("&7/timer set <time> &7- &aSets the timer to the time specified."));

    player.sendMessage(CC.translate("&7/timer pause &7- &aPauses the Timer"));

    player.sendMessage(CC.translate("&7/timer resume &7- &aResumes the Timer"));

    player.sendMessage(CC.translate("&7/timer stop &7- &aStops the Timer"));

    player.sendMessage(CC.translate("&7/timer reset &7- &aResets the Timer"));
  }

  @Subcommand("add")
  public void add(Player player, String time) {

    long timeToAdd = TimeUtil.parseTime(time);

    if (timeToAdd == -1) {
      player.sendMessage(CC.translate("&cInvalid time format."));
      return;
    }

    plugin.getGame().getGlobalTimer().add(timeToAdd);

    player.sendMessage(
        CC.translate("&aAdded &e" + TimeUtil.millisToRoundedTime(timeToAdd) + "&a to the Timer"));
  }

  @Subcommand("decrease")
  public void decrease(Player player, String time) {

    long timeToDecrease = TimeUtil.parseTime(time);

    if (timeToDecrease == -1) {
      player.sendMessage(CC.translate("&cInvalid time format."));
      return;
    }

    if (timeToDecrease - plugin.getGame().getGlobalTimer().getTime() > 0) {
      player.sendMessage(
          CC.translate("&cYou can't decrease the timer by more than the current time."));
      return;
    }

    plugin.getGame().getGlobalTimer().decrease(timeToDecrease);

    player.sendMessage(
        CC.translate(
            "&aDecreased &e" + TimeUtil.millisToRoundedTime(timeToDecrease) + "&a from the Timer"));
  }

  @Subcommand("set")
  public void set(Player player, String time) {

    long timeToSet = TimeUtil.parseTime(time);

    if (timeToSet == -1) {
      player.sendMessage(CC.translate("&cInvalid time format."));
      return;
    }

    plugin.getGame().getGlobalTimer().set(System.currentTimeMillis() + timeToSet);

    player.sendMessage(
        CC.translate("&aSet the timer to &e" + TimeUtil.millisToRoundedTime(timeToSet) + "&a."));
  }

  @Subcommand("pause")
  public void pause(Player player) {
    plugin.getGame().getGlobalTimer().pause();
    player.sendMessage(CC.translate("&aPaused the Timer"));
  }

  @Subcommand("resume")
  public void resume(Player player) {
    plugin.getGame().getGlobalTimer().resume();
    player.sendMessage(CC.translate("&aResumed the Timer"));
  }

  @Subcommand("reset")
  public void reset(Player player) {
    plugin.getGame().getGlobalTimer().reset();
    player.sendMessage(CC.translate("&aTimer has been reset."));
  }

  @Subcommand("stop|cancel")
  public void stop(Player player) {
    plugin.getGame().getGlobalTimer().stop();
    player.sendMessage(CC.translate("&aStopped the Timer"));
  }

  @Subcommand("create")
  public void create(Player player, String time) {

    if (TimeUtil.parseTime(time) == -1) {
      player.sendMessage(CC.translate("&cInvalid time format."));
      return;
    }

    plugin.getGame().setGlobalTimer(new Timer(time));
    plugin.getGame().getGlobalTimer().start();
    player.sendMessage(
        CC.translate(
            "&aCreated a timer with &e"
                + TimeUtil.millisToRoundedTime(TimeUtil.parseTime(time))
                + "&a."));
  }
}
