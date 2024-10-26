/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MISC,
	description = "Blocks damage from catching on fire.\n"
		+ "Does NOT block damage from standing inside of fire.\n"
		+ "Requires a full hunger bar.",
	name = "AntiFire",
	noCheatCompatible = false,
	tags = "anti fire, AntiBurn, anti burn, NoFire, no fire",
	tutorial = "Mods/AntiFire")
public class AntiFireMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(!mc.thePlayer.capabilities.isCreativeMode && mc.thePlayer.onGround
			&& mc.thePlayer.isBurning())
			for(int i = 0; i < 100; i++)
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
