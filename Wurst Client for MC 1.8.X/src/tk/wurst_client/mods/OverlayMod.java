/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Renders the Nuker animation when you mine a block.",
	name = "Overlay",
	tutorial = "Mods/Overlay")
public class OverlayMod extends Mod implements RenderListener
{
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.mods.nukerMod};
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender()
	{
		if(mc.objectMouseOver == null
			|| mc.objectMouseOver.typeOfHit != MovingObjectType.BLOCK)
			return;
		BlockPos pos = mc.objectMouseOver.getBlockPos();
		Block mouseOverBlock =
			mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos())
				.getBlock();
		if(Block.getIdFromBlock(mouseOverBlock) != 0)
			RenderUtils.nukerBox(pos, PlayerControllerMP.curBlockDamageMP);
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(RenderListener.class, this);
	}
}
