package com.dumbdogdiner.stickyapi.bukkit.util;

import com.dumbdogdiner.stickyapi.math.vector.Vector3;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Utilities for converting {@link Vector3}s into Bukkit's {@link Location} object.
 */
public final class LocationUtil {
	private LocationUtil() {}

	/**
	 * Convert the target {@link Vector3} into a Bukkit {@link Location}.
	 * @param world The world this location should be contained in
	 * @param vector The target vector
	 * @return A new {@link Location} in the target world.
	 */
	public static @NotNull Location toLocation(@NotNull World world, @NotNull Vector3 vector) {
		return new Location(world, vector.getX(), vector.getY(), vector.getZ());
	}

	/**
	 * Convert the target {@link Location} into a {@link Vector3}.
	 * @param location The target location
	 * @return A new {@link Location} in the target world.
	 */
	public static @NotNull Vector3 toVector3(@NotNull Location location) {
		return new Vector3(location.getX(), location.getY(), location.getZ());
	}
}
