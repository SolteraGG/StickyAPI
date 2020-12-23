/* 
 * Copyright (c) 2020 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information.
 */
package com.dumbdogdiner.stickyapi.bukkit.generator;

import java.util.Random;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * A class for generating empty void worlds
 */
public class VoidGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(
        @NotNull World world,
        Random random,
        int x,
        int z,
        BiomeGrid biome
    ) {
        return createChunkData(world);
    }
}
