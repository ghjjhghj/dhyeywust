/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.HashSet;
import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import tk.wurst_client.events.listeners.LeftClickListener;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.BlockUtils;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.BLOCKS,
	description = "Slower Nuker that bypasses any cheat prevention\n"
		+ "PlugIn. Not required on most NoCheat+ servers!",
	name = "NukerLegit",
	tags = "LegitNuker, nuker legit, legit nuker",
	tutorial = "Mods/NukerLegit")
public class NukerLegitMod extends Mod implements LeftClickListener,
	RenderListener, UpdateListener
{
	private static Block currentBlock;
	private float currentDamage;
	private EnumFacing side = EnumFacing.UP;
	private byte blockHitDelay = 0;
	private BlockPos pos;
	private boolean shouldRenderESP;
	private int oldSlot = -1;
	
	@Override
	public String getRenderName()
	{
		return wurst.mods.nukerMod.getRenderName() + "Legit";
	}
	
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.mods.nukerMod,
			wurst.mods.speedNukerMod, wurst.mods.tunnellerMod,
			wurst.mods.fastBreakMod, wurst.mods.autoMineMod};
	}
	
	@Override
	public void onEnable()
	{
		if(wurst.mods.nukerMod.isEnabled())
			wurst.mods.nukerMod.setEnabled(false);
		if(wurst.mods.speedNukerMod.isEnabled())
			wurst.mods.speedNukerMod.setEnabled(false);
		if(wurst.mods.tunnellerMod.isEnabled())
			wurst.mods.tunnellerMod.setEnabled(false);
		wurst.events.add(LeftClickListener.class, this);
		wurst.events.add(UpdateListener.class, this);
		wurst.events.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender()
	{
		if(blockHitDelay == 0 && shouldRenderESP)
			if(!mc.thePlayer.capabilities.isCreativeMode
				&& currentBlock.getPlayerRelativeBlockHardness(mc.thePlayer,
					mc.theWorld, pos) < 1)
				RenderUtils.nukerBox(pos, currentDamage);
			else
				RenderUtils.nukerBox(pos, 1);
	}
	
	@Override
	public void onUpdate()
	{
		shouldRenderESP = false;
		BlockPos newPos = find();
		if(newPos == null)
		{
			if(oldSlot != -1)
			{
				mc.thePlayer.inventory.currentItem = oldSlot;
				oldSlot = -1;
			}
			return;
		}
		if(pos == null || !pos.equals(newPos))
			currentDamage = 0;
		pos = newPos;
		currentBlock = mc.theWorld.getBlockState(pos).getBlock();
		if(blockHitDelay > 0)
		{
			blockHitDelay--;
			return;
		}
		BlockUtils.faceBlockClient(pos);
		if(currentDamage == 0)
		{
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
				Action.START_DESTROY_BLOCK, pos, side));
			if(wurst.mods.autoToolMod.isActive() && oldSlot == -1)
				oldSlot = mc.thePlayer.inventory.currentItem;
			if(mc.thePlayer.capabilities.isCreativeMode
				|| currentBlock.getPlayerRelativeBlockHardness(mc.thePlayer,
					mc.theWorld, pos) >= 1)
			{
				currentDamage = 0;
				shouldRenderESP = true;
				mc.thePlayer.swingItem();
				mc.playerController.onPlayerDestroyBlock(pos, side);
				blockHitDelay = (byte)4;
				return;
			}
		}
		if(wurst.mods.autoToolMod.isActive())
			AutoToolMod.setSlot(pos);
		mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
		shouldRenderESP = true;
		currentDamage +=
			currentBlock.getPlayerRelativeBlockHardness(mc.thePlayer,
				mc.theWorld, pos);
		mc.theWorld.sendBlockBreakProgress(mc.thePlayer.getEntityId(), pos,
			(int)(currentDamage * 10.0F) - 1);
		if(currentDamage >= 1)
		{
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
				Action.STOP_DESTROY_BLOCK, pos, side));
			mc.playerController.onPlayerDestroyBlock(pos, side);
			blockHitDelay = (byte)4;
			currentDamage = 0;
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(LeftClickListener.class, this);
		wurst.events.remove(UpdateListener.class, this);
		wurst.events.remove(RenderListener.class, this);
		if(oldSlot != -1)
		{
			mc.thePlayer.inventory.currentItem = oldSlot;
			oldSlot = -1;
		}
		currentDamage = 0;
		shouldRenderESP = false;
		NukerMod.id = 0;
		wurst.files.saveOptions();
	}
	
	@Override
	public void onLeftClick()
	{
		if(mc.objectMouseOver == null
			|| mc.objectMouseOver.getBlockPos() == null)
			return;
		if(wurst.mods.nukerMod.getMode() == 1
			&& mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos())
				.getBlock().getMaterial() != Material.air)
		{
			NukerMod.id =
				Block.getIdFromBlock(mc.theWorld.getBlockState(
					mc.objectMouseOver.getBlockPos()).getBlock());
			wurst.files.saveOptions();
		}
	}
	
	private BlockPos find()
	{
		LinkedList<BlockPos> queue = new LinkedList<BlockPos>();
		HashSet<BlockPos> alreadyProcessed = new HashSet<BlockPos>();
		queue.add(new BlockPos(mc.thePlayer));
		while(!queue.isEmpty())
		{
			BlockPos currentPos = queue.poll();
			if(alreadyProcessed.contains(currentPos))
				continue;
			alreadyProcessed.add(currentPos);
			if(BlockUtils.getPlayerBlockDistance(currentPos) > wurst.mods.nukerMod.yesCheatRange)
				continue;
			int currentID =
				Block.getIdFromBlock(mc.theWorld.getBlockState(currentPos)
					.getBlock());
			if(currentID != 0)
				switch(wurst.mods.nukerMod.getMode())
				{
					case 1:
						if(currentID == NukerMod.id)
							return currentPos;
						break;
					case 2:
						if(currentPos.getY() >= mc.thePlayer.posY)
							return currentPos;
						break;
					case 3:
						if(mc.theWorld
							.getBlockState(currentPos)
							.getBlock()
							.getPlayerRelativeBlockHardness(mc.thePlayer,
								mc.theWorld, currentPos) >= 1)
							return currentPos;
						break;
					default:
						return currentPos;
				}
			if(!mc.theWorld.getBlockState(currentPos).getBlock().getMaterial()
				.blocksMovement())
			{
				queue.add(currentPos.add(0, 0, -1));// north
				queue.add(currentPos.add(0, 0, 1));// south
				queue.add(currentPos.add(-1, 0, 0));// west
				queue.add(currentPos.add(1, 0, 0));// east
				queue.add(currentPos.add(0, -1, 0));// down
				queue.add(currentPos.add(0, 1, 0));// up
			}
		}
		return null;
		
	}
}
