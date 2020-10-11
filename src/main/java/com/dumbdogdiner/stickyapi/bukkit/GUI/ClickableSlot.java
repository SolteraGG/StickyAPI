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

package com.dumbdogdiner.stickyapi.bukkit.GUI;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;

/**
 * This class is for creating new slots in an inventory GUI
*/
public class ClickableSlot {
    
    @Getter
    private ItemStack item;

    @Getter
    private int slot;

    @Getter
    private ItemMeta meta = item.getItemMeta();

    public ClickableSlot(Material pMaterial, String pName, int pAmount, int pSlot) {
        this.item = new ItemStack(pMaterial, pAmount);

        ItemMeta isM = item.getItemMeta();
        isM.setDisplayName(pName);
        item.setItemMeta(isM);

        this.slot = pSlot;
    }

    public void execute(Runnable r) {
        new Thread(r).start();
    }

    public void setName(String s) {
        ItemMeta isM = this.item.getItemMeta();
        isM.setDisplayName(s);
        item.setItemMeta(isM);
    }

    public String getName() {
        return item.getItemMeta().getDisplayName();
    }
}