/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.ArrayList;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.BlockPos;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;

import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.SliderSetting;

@Info(category = Category.MOVEMENT,
	description = "Allows you to step up full blocks.",
	name = "Step",
	tutorial = "Mods/Step")
public class StepMod extends Mod implements UpdateListener
{
	public float height = 1F;
	
	@Override
	public void initSettings()
	{
		settings.add(new SliderSetting("Height", height, 1, 100, 1,
			ValueDisplay.INTEGER)
		{
			@Override
			public void update()
			{
				height = (float)getValue();
			}
		});
	}
	
	@Override
	public void onEnable()
	{
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(wurst.mods.yesCheatMod.isActive())
		{
			mc.thePlayer.stepHeight = 0.5F;
			if(mc.thePlayer.onGround
				&& !mc.thePlayer.isOnLadder()
				&& (mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F)
				&& canStep() && !mc.thePlayer.movementInput.jump
				&& mc.thePlayer.isCollidedHorizontally)
			{
				mc.getNetHandler().addToSendQueue(
					new C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + 0.42D, mc.thePlayer.posZ,
						mc.thePlayer.onGround));
				mc.getNetHandler().addToSendQueue(
					new C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + 0.753D, mc.thePlayer.posZ,
						mc.thePlayer.onGround));
				mc.thePlayer.setPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + 1D, mc.thePlayer.posZ);
			}
		}else
			mc.thePlayer.stepHeight = isEnabled() ? height : 0.5F;
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		mc.thePlayer.stepHeight = 0.5F;
	}
	
	private boolean canStep()
	{
		ArrayList<BlockPos> collisionBlocks = new ArrayList<>();
		
		EntityPlayerSP player = mc.thePlayer;
		BlockPos pos1 =
			new BlockPos(player.getEntityBoundingBox().minX - 0.001D,
				player.getEntityBoundingBox().minY - 0.001D,
				player.getEntityBoundingBox().minZ - 0.001D);
		BlockPos pos2 =
			new BlockPos(player.getEntityBoundingBox().maxX + 0.001D,
				player.getEntityBoundingBox().maxY + 0.001D,
				player.getEntityBoundingBox().maxZ + 0.001D);
		
		if(player.worldObj.isAreaLoaded(pos1, pos2))
			for(int x = pos1.getX(); x <= pos2.getX(); x++)
				for(int y = pos1.getY(); y <= pos2.getY(); y++)
					for(int z = pos1.getZ(); z <= pos2.getZ(); z++)
						if(y > player.posY - 1.0D && y <= player.posY)
							collisionBlocks.add(new BlockPos(x, y, z));
		
		BlockPos belowPlayerPos =
			new BlockPos(player.posX, player.posY - 1.0D, player.posZ);
		for(BlockPos collisionBlock : collisionBlocks)
			if(!(player.worldObj.getBlockState(collisionBlock.add(0, 1, 0))
				.getBlock() instanceof BlockFenceGate))
				if(player.worldObj
					.getBlockState(collisionBlock.add(0, 1, 0))
					.getBlock()
					.getCollisionBoundingBox(mc.theWorld, belowPlayerPos,
						mc.theWorld.getBlockState(collisionBlock)) != null)
					return false;
		
		return true;
	}
}
