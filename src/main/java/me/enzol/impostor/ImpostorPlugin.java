package me.enzol.impostor;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import java.time.Duration;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import me.enzol.impostor.game.Game;
import me.enzol.impostor.game.GameState;
import me.enzol.impostor.game.block.TimerBlockListener;
import me.enzol.impostor.game.block.TimerBlockManager;
import me.enzol.impostor.game.commands.GameCommands;
import me.enzol.impostor.game.listeners.GameListeners;
import me.enzol.impostor.game.timer.commands.TimerCommand;
import me.enzol.impostor.profile.ProfileState;
import me.enzol.impostor.profile.commands.ProfileStateCommand;
import me.enzol.impostor.profile.listeners.ProfileListener;
import me.enzol.impostor.taks.PlayerVoiceTask;
import me.enzol.impostor.utilities.NegativeSpaces;
import me.enzol.impostor.utilities.ResourcePackManager;
import me.enzol.impostor.vote.VoteCommand;
import me.enzol.impostor.vote.VotesManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.jcedeno.libs.rapidinv.RapidInvManager;

@SpigotPlugin
@Getter
public class ImpostorPlugin extends JavaPlugin {

  @Getter private static ImpostorPlugin instance;
  private static final MiniMessage miniMessage = MiniMessage.get();

  private PaperCommandManager commandManager;
  private Game game;
  private TimerBlockManager timerBlockManager;
  private VotesManager votesManager;
  private ResourcePackManager resourcePackManager;

  @Override
  public void onEnable() {
    instance = this;

    saveDefaultConfig();
    loadManagers();

    registerListeners();
    registerCommands();
    registerTask();
  }

  @Override
  public void onDisable() {
    timerBlockManager.save();
  }

  private void registerTask() {
    Bukkit.getScheduler().runTaskTimer(this, new PlayerVoiceTask(), 0, 1L);
  }

  private void loadManagers() {
    RapidInvManager.register(this);
    NegativeSpaces.registerCodes();
    game = new Game(this);
    timerBlockManager = new TimerBlockManager(this);
    votesManager = new VotesManager();

    resourcePackManager = new ResourcePackManager(this);
    resourcePackManager.setResoucePackURL(
        "https://download.mc-packs.net/pack/f1ce0348b1943a4a2f33e602ee7f902fb2817485.zip");
    resourcePackManager.setResourcePackHash("f1ce0348b1943a4a2f33e602ee7f902fb2817485");
    resourcePackManager.setBypassPerm("rp.bypass");
  }

  private void registerListeners() {
    PluginManager pluginManager = Bukkit.getPluginManager();

    pluginManager.registerEvents(new GameListeners(this), this);
    pluginManager.registerEvents(new TimerBlockListener(), this);
    pluginManager.registerEvents(new ProfileListener(), this);
  }

  private void registerCommands() {
    commandManager = new PaperCommandManager(this);

    commandManager.registerCommand(new TimerCommand(this));
    commandManager.registerCommand(new VoteCommand());
    commandManager.registerCommand(new ProfileStateCommand());
    commandManager.registerCommand(new GameCommands(this));

    CommandContexts<BukkitCommandExecutionContext> commandContexts =
        getCommandManager().getCommandContexts();

    commandContexts.registerContext(
        GameState.class,
        c -> {
          String stateName = c.popFirstArg();

          GameState state = GameState.getByName(stateName.toUpperCase());

          if (state == null) {
            throw new InvalidCommandArgument("state " + stateName + " does not exist");
          }

          return state;
        });

    commandContexts.registerContext(
        GameMode.class,
        c -> {
          String modeName = c.popFirstArg();

          GameMode mode = GameMode.valueOf(modeName.toUpperCase());

          if (mode == null) {
            throw new InvalidCommandArgument("mode " + modeName + " does not exist");
          }

          return mode;
        });

    commandContexts.registerContext(
        ProfileState.class,
        c -> {
          String modeName = c.popFirstArg();

          ProfileState mode = ProfileState.getByName(modeName.toUpperCase());

          if (mode == null) {
            throw new InvalidCommandArgument("state " + modeName + " does not exist");
          }

          return mode;
        });
  }

  public void showTitle(
      Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    player.showTitle(
        Title.title(
            miniMessage.parse(title),
            miniMessage.parse(subtitle),
            Times.of(
                Duration.ofMillis(50 * fadeIn),
                Duration.ofMillis(50 * stay),
                Duration.ofMillis(50 * fadeIn))));
  }
}
