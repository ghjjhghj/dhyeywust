/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Allows you to see fake blocks in Prophunt.",
	name = "ProphuntESP",
	tags = "prophunt esp",
	tutorial = "Mods/ProphuntESP")
public class ProphuntEspMod extends Mod implements RenderListener
{
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.mods.playerEspMod,
			wurst.mods.mobEspMod, wurst.mods.tracersMod};
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender()
	{
		for(Object entity : mc.theWorld.loadedEntityList)
			if(entity instanceof EntityLiving && ((Entity)entity).isInvisible())
			{
				double x = ((Entity)entity).posX;
				double y = ((Entity)entity).posY;
				double z = ((Entity)entity).posZ;
				Color color;
				if(mc.thePlayer.getDistanceToEntity((Entity)entity) >= 0.5)
					color =
						new Color(1F, 0F, 0F, 0.5F - MathHelper.abs(MathHelper
							.sin(Minecraft.getSystemTime() % 1000L / 1000.0F
								* (float)Math.PI * 1.0F) * 0.3F));
				else
					color = new Color(0, 0, 0, 0);
				RenderUtils.box(x - 0.5, y - 0.1, z - 0.5, x + 0.5, y + 0.9,
					z + 0.5, color);
			}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(RenderListener.class, this);
	}
}
