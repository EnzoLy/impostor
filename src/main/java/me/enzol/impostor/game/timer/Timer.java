package me.enzol.impostor.game.timer;

import lombok.Getter;
import lombok.Setter;
import me.enzol.impostor.ImpostorPlugin;
import me.enzol.impostor.utilities.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
@Setter
public class Timer {

  private long endAt;
  private long currentTime;
  private boolean pause;
  private long pauseTime;
  private String timeDefault;
  private boolean announce;

  public Timer(String timeDefault) {
    endAt = System.currentTimeMillis() + TimeUtil.parseTime(timeDefault);
    pause = false;
    this.timeDefault = timeDefault;
  }

  public void add(long time) {
    endAt += time;
    currentTime += time;
  }

  public void decrease(long time) {
    endAt -= time;
    currentTime -= time;
  }

  public void set(long time) {
    endAt = time;
    currentTime = time;
  }

  public long getTime() {

    if (System.currentTimeMillis() == currentTime) {
      return 0;
    }

    if (System.currentTimeMillis() >= endAt) {
      if (!announce) {
        end();
        announce = true;
      }
      return 0;
    }

    if (pause) {
      return currentTime;
    }

    return currentTime = endAt - System.currentTimeMillis();
  }

  public boolean isEnded() {
    return getTime() == 0;
  }

  public String getReadableTime() {
    return TimeUtil.millisToTimer(getTime());
  }

  public void pause() {
    pause = true;
    pauseTime = System.currentTimeMillis();
  }

  public void resume() {
    long pauseEnd = System.currentTimeMillis() - pauseTime;

    endAt += pauseEnd;
    currentTime += pauseEnd;
    pause = false;
  }

  public void reset() {
    endAt = System.currentTimeMillis() + TimeUtil.parseTime(timeDefault);
  }

  public void start() {
    for (Player other : Bukkit.getOnlinePlayers()) {
      // other.playSound(other.getLocation(), "sfx.timer_start", 1, 1);
    }
  }

  public void stop() {
    ImpostorPlugin.getInstance().getGame().setGlobalTimer(null);
  }

  public void end() {
    for (Player other : Bukkit.getOnlinePlayers()) {
      other.playSound(other.getLocation(), "sfx.timer", 1, 1);
    }
  }
}
