/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.options.xray;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glDisable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tk.wurst_client.gui.GuiWurstSlot;
import tk.wurst_client.mods.XRayMod;

public class GuiXRayBlocksList extends GuiWurstSlot
{
	public GuiXRayBlocksList(Minecraft par1Minecraft, GuiScreen prevMenu)
	{
		super(par1Minecraft, prevMenu.width, prevMenu.height, 36,
			prevMenu.height - 56, 30);
		mc = par1Minecraft;
	}
	
	private int selectedSlot;
	private Minecraft mc;
	public static ArrayList<Block> blocks = new ArrayList<Block>();
	
	public static void sortBlocks()
	{
		blocks = XRayMod.xrayBlocks;
		Collections.sort(blocks, new Comparator<Block>()
		{
			@Override
			public int compare(Block o1, Block o2)
			{
				return o1.getLocalizedName().compareToIgnoreCase(
					o2.getLocalizedName());
			}
		});
		ArrayList<Block> newBlocks = new ArrayList<Block>();
		for(Block block : blocks)
			if(XRayMod.xrayBlocks.contains(block))
				newBlocks.add(block);
		for(Block block : blocks)
			if(!XRayMod.xrayBlocks.contains(block))
				newBlocks.add(block);
		blocks = newBlocks;
	}
	
	@Override
	protected boolean isSelected(int id)
	{
		return selectedSlot == id;
	}
	
	protected int getSelectedSlot()
	{
		return selectedSlot;
	}
	
	@Override
	protected int getSize()
	{
		return blocks.size();
	}
	
	@Override
	protected void elementClicked(int var1, boolean var2, int var3, int var4)
	{
		selectedSlot = var1;
	}
	
	@Override
	protected void drawBackground()
	{}
	
	@Override
	protected void drawSlot(int id, int x, int y, int var4, int var5, int var6)
	{
		Block block = blocks.get(id);
		ItemStack itemStack = new ItemStack(Item.getItemFromBlock(block));
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		if(itemStack.getItem() != null)
			try
			{
				Minecraft.getMinecraft().getRenderItem()
					.renderItemAndEffectIntoGUI(itemStack, x + 4, y + 4);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		else
			mc.fontRendererObj.drawString("?", x + 10, y + 9, 10526880);
		Minecraft
			.getMinecraft()
			.getRenderItem()
			.func_175030_a(Minecraft.getMinecraft().fontRendererObj, itemStack,
				x + 4, y + 4);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		glDisable(GL_LIGHTING);
		mc.fontRendererObj.drawString("Name: "
			+ (itemStack.getItem() == null ? block.getLocalizedName()
				: itemStack.getDisplayName()), x + 31, y + 3, 10526880);
		int blockID = Block.getIdFromBlock(block);
		mc.fontRendererObj.drawString("ID: " + blockID, x + 31, y + 15,
			10526880);
	}
}
