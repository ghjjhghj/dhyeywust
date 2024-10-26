/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import tk.wurst_client.events.ChatOutputEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

@Cmd.Info(help = "Repairs the held item. Requires creative mode.",
	name = "repair",
	syntax = {})
public class RepairCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length > 0)
			syntaxError();
		
		// check for creative mode
		EntityPlayerSP player = mc.thePlayer;
		if(!player.capabilities.isCreativeMode)
			error("Creative mode only.");
		
		// validate item
		ItemStack item = player.inventory.getCurrentItem();
		if(item == null)
			error("You need an item in your hand.");
		if(!item.isItemStackDamageable())
			error("This item can't take damage.");
		if(!item.isItemDamaged())
			error("This item is not damaged.");
		
		// repair item
		item.setItemDamage(0);
		player.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(
			36 + player.inventory.currentItem, item));
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Repair Current Item";
	}
	
	@Override
	public void doPrimaryAction()
	{
		wurst.commands.onSentMessage(new ChatOutputEvent(".repair", true));
	}
}
