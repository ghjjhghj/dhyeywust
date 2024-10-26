/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.commands.Cmd.Info;

@Info(help = "Allows you to copy items that other people are holding\n"
	+ "or wearing. Requires creative mode.",
	name = "copyitem",
	syntax = {"<player> (hand|head|chest|legs|feet)"})
public class CopyItemCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length != 2)
			syntaxError();
		if(!mc.thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		
		// find item
		ItemStack item = null;
		for(Object entity : mc.theWorld.loadedEntityList)
			if(entity instanceof EntityOtherPlayerMP)
			{
				EntityOtherPlayerMP player = (EntityOtherPlayerMP)entity;
				if(player.getName().equalsIgnoreCase(args[0]))
				{
					switch(args[1].toLowerCase())
					{
						case "hand":
							item = player.inventory.getCurrentItem();
							break;
						case "head":
							item = player.inventory.armorItemInSlot(3);
							break;
						case "chest":
							item = player.inventory.armorItemInSlot(2);
							break;
						case "legs":
							item = player.inventory.armorItemInSlot(1);
							break;
						case "feet":
							item = player.inventory.armorItemInSlot(0);
							break;
						default:
							syntaxError();
							break;
					}
					break;
				}
			}
		if(item == null)
			error("Player \"" + args[0] + "\" could not be found.");
		
		// copy item
		for(int i = 0; i < 9; i++)
			if(mc.thePlayer.inventory.getStackInSlot(i) == null)
			{
				mc.thePlayer.sendQueue
					.addToSendQueue(new C10PacketCreativeInventoryAction(
						36 + i, item));
				wurst.chat.message("Item copied.");
				return;
			}
		error("Please clear a slot in your hotbar.");
	}
}
