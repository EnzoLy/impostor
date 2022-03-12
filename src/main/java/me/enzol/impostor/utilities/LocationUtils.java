package me.enzol.impostor.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@UtilityClass
public class LocationUtils {

  public String serialize(Location location) {
    return location.getWorld().getName()
        + ","
        + location.getX()
        + ","
        + location.getY()
        + ","
        + location.getZ()
        + ","
        + location.getYaw()
        + ","
        + location.getPitch();
  }

  public Location deSerialize(String serialized) {
    String[] split = serialized.split(",");
    return new Location(
        Bukkit.getWorld(split[0]),
        Double.parseDouble(split[1]),
        Double.parseDouble(split[2]),
        Double.parseDouble(split[3]),
        Float.parseFloat(split[4]),
        Float.parseFloat(split[5]));
  }
}
