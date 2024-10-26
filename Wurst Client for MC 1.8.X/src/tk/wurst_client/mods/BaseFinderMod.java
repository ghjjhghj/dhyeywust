/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Finds player bases by searching for man-made blocks.\n"
		+ "Good for finding faction bases.",
	name = "BaseFinder",
	tags = "base finder, factions",
	tutorial = "Mods/BaseFinder")
public class BaseFinderMod extends Mod implements UpdateListener,
	RenderListener
{
	public BaseFinderMod()
	{
		initBlocks();
	}
	
	private ArrayList<Block> naturalBlocks = new ArrayList<Block>();
	private ArrayList<BlockPos> matchingBlocks = new ArrayList<BlockPos>();
	private int range = 50;
	private int maxBlocks = 1024;
	private boolean shouldInform = true;
	
	@Override
	public void onEnable()
	{
		shouldInform = true;
		wurst.events.add(UpdateListener.class, this);
		wurst.events.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender()
	{
		for(BlockPos blockPos : matchingBlocks)
			RenderUtils.framelessBlockESP(blockPos, new Color(255, 0, 0));
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		if(hasTimePassedM(3000))
		{
			matchingBlocks.clear();
			for(int y = range; y >= -range; y--)
			{
				for(int x = range; x >= -range; x--)
				{
					for(int z = range; z >= -range; z--)
					{
						int posX = (int)(mc.thePlayer.posX + x);
						int posY = (int)(mc.thePlayer.posY + y);
						int posZ = (int)(mc.thePlayer.posZ + z);
						BlockPos pos = new BlockPos(posX, posY, posZ);
						if(!naturalBlocks.contains(mc.theWorld.getBlockState(
							pos).getBlock()))
							matchingBlocks.add(pos);
						if(matchingBlocks.size() >= maxBlocks)
							break;
					}
					if(matchingBlocks.size() >= maxBlocks)
						break;
				}
				if(matchingBlocks.size() >= maxBlocks)
					break;
			}
			if(matchingBlocks.size() >= maxBlocks && shouldInform)
			{
				wurst.chat.warning(getName() + " found �lA LOT�r of blocks.");
				wurst.chat
					.message("To prevent lag, it will only show the first "
						+ maxBlocks + " blocks.");
				shouldInform = false;
			}else if(matchingBlocks.size() < maxBlocks)
				shouldInform = true;
			updateLastMS();
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		wurst.events.remove(RenderListener.class, this);
	}
	
	private void initBlocks()
	{
		naturalBlocks.add(Block.getBlockFromName("air"));
		naturalBlocks.add(Block.getBlockFromName("stone"));
		naturalBlocks.add(Block.getBlockFromName("dirt"));
		naturalBlocks.add(Block.getBlockFromName("grass"));
		naturalBlocks.add(Block.getBlockFromName("gravel"));
		naturalBlocks.add(Block.getBlockFromName("sand"));
		naturalBlocks.add(Block.getBlockFromName("clay"));
		naturalBlocks.add(Block.getBlockFromName("sandstone"));
		naturalBlocks.add(Block.getBlockById(8));
		naturalBlocks.add(Block.getBlockById(9));
		naturalBlocks.add(Block.getBlockById(10));
		naturalBlocks.add(Block.getBlockById(11));
		naturalBlocks.add(Block.getBlockFromName("log"));
		naturalBlocks.add(Block.getBlockFromName("log2"));
		naturalBlocks.add(Block.getBlockFromName("leaves"));
		naturalBlocks.add(Block.getBlockFromName("leaves2"));
		naturalBlocks.add(Block.getBlockFromName("deadbush"));
		naturalBlocks.add(Block.getBlockFromName("iron_ore"));
		naturalBlocks.add(Block.getBlockFromName("coal_ore"));
		naturalBlocks.add(Block.getBlockFromName("gold_ore"));
		naturalBlocks.add(Block.getBlockFromName("diamond_ore"));
		naturalBlocks.add(Block.getBlockFromName("emerald_ore"));
		naturalBlocks.add(Block.getBlockFromName("redstone_ore"));
		naturalBlocks.add(Block.getBlockFromName("lapis_ore"));
		naturalBlocks.add(Block.getBlockFromName("bedrock"));
		naturalBlocks.add(Block.getBlockFromName("mob_spawner"));
		naturalBlocks.add(Block.getBlockFromName("mossy_cobblestone"));
		naturalBlocks.add(Block.getBlockFromName("tallgrass"));
		naturalBlocks.add(Block.getBlockFromName("yellow_flower"));
		naturalBlocks.add(Block.getBlockFromName("red_flower"));
		naturalBlocks.add(Block.getBlockFromName("cobweb"));
		naturalBlocks.add(Block.getBlockFromName("brown_mushroom"));
		naturalBlocks.add(Block.getBlockFromName("red_mushroom"));
		naturalBlocks.add(Block.getBlockFromName("snow_layer"));
		naturalBlocks.add(Block.getBlockFromName("vine"));
		naturalBlocks.add(Block.getBlockFromName("waterlily"));
		naturalBlocks.add(Block.getBlockFromName("double_plant"));
		naturalBlocks.add(Block.getBlockFromName("hardened_clay"));
		naturalBlocks.add(Block.getBlockFromName("red_sandstone"));
		naturalBlocks.add(Block.getBlockFromName("ice"));
		naturalBlocks.add(Block.getBlockFromName("quartz_ore"));
		naturalBlocks.add(Block.getBlockFromName("obsidian"));
		naturalBlocks.add(Block.getBlockFromName("monster_egg"));
		naturalBlocks.add(Block.getBlockFromName("red_mushroom_block"));
		naturalBlocks.add(Block.getBlockFromName("brown_mushroom_block"));
	}
}
