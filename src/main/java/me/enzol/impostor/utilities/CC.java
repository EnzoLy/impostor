package me.enzol.impostor.utilities;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

@UtilityClass
public class CC {

  private final char COLOR_CHAR = ChatColor.COLOR_CHAR;

  public String translate(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public List<String> translate(List<String> messages) {
    return messages.stream().map(CC::translate).collect(Collectors.toList());
  }

  public String translateHexColorCodes(String startTag, String endTag, String message) {
    final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
    Matcher matcher = hexPattern.matcher(message);
    StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);

    while (matcher.find()) {
      String group = matcher.group(1);
      matcher.appendReplacement(
          buffer,
          COLOR_CHAR
              + "x"
              + COLOR_CHAR
              + group.charAt(0)
              + COLOR_CHAR
              + group.charAt(1)
              + COLOR_CHAR
              + group.charAt(2)
              + COLOR_CHAR
              + group.charAt(3)
              + COLOR_CHAR
              + group.charAt(4)
              + COLOR_CHAR
              + group.charAt(5));
    }
    return matcher.appendTail(buffer).toString();
  }

  public String prettyLocation(Location location) {
    return "X: "
        + location.getBlockX()
        + " Y: "
        + location.getBlockY()
        + " Z: "
        + location.getBlockZ();
  }

  public String prettyMaterial(Material material) {
    return StringUtils.capitalize(material.name().replace("_", " ").toLowerCase());
  }
}
