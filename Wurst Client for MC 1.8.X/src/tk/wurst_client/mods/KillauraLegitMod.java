/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.entity.EntityLivingBase;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "Slower Killaura that bypasses any cheat prevention\n"
		+ "PlugIn. Not required on most NoCheat+ servers!",
	name = "KillauraLegit",
	tags = "LegitAura, killaura legit, kill aura legit, legit aura",
	tutorial = "Mods/KillauraLegit")
public class KillauraLegitMod extends Mod implements UpdateListener
{
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.special.targetSpf,
			wurst.mods.killauraMod, wurst.mods.multiAuraMod,
			wurst.mods.clickAuraMod, wurst.mods.triggerBotMod};
	}
	
	@Override
	public void onEnable()
	{
		// TODO: Clean up this mess!
		if(wurst.mods.killauraMod.isEnabled())
			wurst.mods.killauraMod.setEnabled(false);
		if(wurst.mods.multiAuraMod.isEnabled())
			wurst.mods.multiAuraMod.setEnabled(false);
		if(wurst.mods.clickAuraMod.isEnabled())
			wurst.mods.clickAuraMod.setEnabled(false);
		if(wurst.mods.tpAuraMod.isEnabled())
			wurst.mods.tpAuraMod.setEnabled(false);
		if(wurst.mods.triggerBotMod.isEnabled())
			wurst.mods.triggerBotMod.setEnabled(false);
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		EntityLivingBase en = EntityUtils.getClosestEntity(true, true);
		if(hasTimePassedS(wurst.mods.killauraMod.yesCheatSpeed))
			if(mc.thePlayer.getDistanceToEntity(en) <= wurst.mods.killauraMod.yesCheatRange)
			{
				if(wurst.mods.criticalsMod.isActive() && mc.thePlayer.onGround)
					mc.thePlayer.jump();
				if(EntityUtils.getDistanceFromMouse(en) > 55)
					EntityUtils.faceEntityClient(en);
				else
				{
					EntityUtils.faceEntityClient(en);
					mc.thePlayer.swingItem();
					mc.playerController.attackEntity(mc.thePlayer, en);
				}
				updateLastMS();
			}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
