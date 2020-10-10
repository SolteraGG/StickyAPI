package com.dumbdogdiner.stickyapi.bukkit.generator;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

/**
 * A class for generating empty void worlds
 */
public class VoidGenerator extends ChunkGenerator {
    
    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        return createChunkData(world);
    }
}
