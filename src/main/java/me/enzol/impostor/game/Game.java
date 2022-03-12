package me.enzol.impostor.game;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.enzol.impostor.ImpostorPlugin;
import me.enzol.impostor.game.events.GameTickEvent;
import me.enzol.impostor.game.timer.Timer;
import me.enzol.impostor.utilities.CC;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@RequiredArgsConstructor
public class Game extends BukkitRunnable {

  private final ImpostorPlugin plugin;

  @Setter private Timer globalTimer;
  @Setter private GameState state = GameState.LOBBY;

  private long gameTime = 0;
  private final long startTime;
  private final BossBar timerBossBar;
  @Setter private int round = 1;
  @Setter private UUID impostor;
  @Setter private boolean mutedChat = false;
  @Setter private boolean allowDamage = false;

  public Game(ImpostorPlugin plugin) {
    this.plugin = plugin;
    this.startTime = System.currentTimeMillis();

    timerBossBar =
        Bukkit.createBossBar(
            new NamespacedKey(plugin, "TIMER"), "", BarColor.WHITE, BarStyle.SOLID);

    timerBossBar.setTitle("");
    timerBossBar.setProgress(1.0);
    timerBossBar.setVisible(true);
    start();
  }

  public void start() {
    this.runTaskTimerAsynchronously(plugin, 0L, 20L);
  }

  @Override
  public void run() {
    var new_time = (int) (Math.floor((System.currentTimeMillis() - startTime) / 1000.0));

    gameTime = new_time;

    if (globalTimer != null) {
      timerBossBar.setTitle(CC.translate("&f" + getGlobalTimer().getReadableTime()));
    } else {
      timerBossBar.setTitle("");
    }

    if (globalTimer != null && globalTimer.isEnded()) {
      globalTimer = null;
    }

    Bukkit.getPluginManager().callEvent(new GameTickEvent(new_time, true));
  }
}
