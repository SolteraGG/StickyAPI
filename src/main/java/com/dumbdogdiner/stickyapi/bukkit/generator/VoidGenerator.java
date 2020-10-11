/* 
 *  StickyAPI - Utility methods, classes and potentially code-dupe-annihilating code for DDD plugins
 *  Copyright (C) 2020 DumbDogDiner <dumbdogdiner.com>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
