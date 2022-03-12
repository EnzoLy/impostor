package me.enzol.impostor.vote;

import com.google.common.collect.Maps;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import me.enzol.impostor.profile.Profile;
import me.enzol.impostor.utilities.CC;
import org.bukkit.entity.Player;

public class VotesManager {

  @Getter private final Map<UUID, UUID> votes = Maps.newHashMap();

  public void vote(Player voter, Player voted) {
    vote(voter.getUniqueId(), voted.getUniqueId());

    Profile profile = Profile.getProfile(voted.getUniqueId());

    profile.setVotesReceived(profile.getVotesReceived() + 1);

    voter.sendMessage(CC.translate("&aHas votado a &e" + voted.getName() + "&a."));
  }

  public void vote(UUID voter, UUID voted) {
    if(votes.containsKey(voter)){
      Profile profile = Profile.getProfile(voted);

      profile.setVotesReceived(profile.getVotesReceived() - 1);

      votes.remove(voter);
    }
    votes.put(voter, voted);
  }

  public void resetVotes() {
    votes.clear();
  }

  public int getVotes(UUID uuid) {
    int votesCount = 0;
    for (UUID u : votes.values()) {
      if (u.equals(uuid)) {
        votesCount++;
      }
    }
    return votesCount;
  }

  public Player getMostMoted() {
    List<Profile> profiles =
        votes.values().stream()
            .map(Profile::getProfile)
            .sorted(Comparator.comparing(Profile::getVotesReceived))
            .collect(Collectors.toList());

    Profile mostMoted = profiles.get(0);

    if (mostMoted.getVotesReceived() == 0) {
      return null;
    }

    return mostMoted.getPlayer();
  }

  public List<Player> getSortedVotes() {
    return votes.values().stream()
        .map(Profile::getProfile)
        .sorted(Comparator.comparing(Profile::getVotesReceived))
        .map(Profile::getPlayer)
        .collect(Collectors.toList());
  }
}
