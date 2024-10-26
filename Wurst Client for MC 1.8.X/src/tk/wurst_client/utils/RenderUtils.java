/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.utils;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;

public class RenderUtils
{
	/**
	 * Renders a box with any size and any color.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param color
	 */
	public static void box(double x, double y, double z, double x2, double y2,
		double z2, Color color)
	{
		x = x - Minecraft.getMinecraft().getRenderManager().renderPosX;
		y = y - Minecraft.getMinecraft().getRenderManager().renderPosY;
		z = z - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		x2 = x2 - Minecraft.getMinecraft().getRenderManager().renderPosX;
		y2 = y2 - Minecraft.getMinecraft().getRenderManager().renderPosY;
		z2 = z2 - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		RenderUtil.setColor(color);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x, y, z, x2, y2, z2));
		GL11.glColor4d(0, 0, 0, 0.5F);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x2, y2,
			z2), -1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}
	
	/**
	 * Renders a frame with any size and any color.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param color
	 */
	public static void frame(double x, double y, double z, double x2,
		double y2, double z2, Color color)
	{
		x = x - Minecraft.getMinecraft().getRenderManager().renderPosX;
		y = y - Minecraft.getMinecraft().getRenderManager().renderPosY;
		z = z - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		x2 = x2 - Minecraft.getMinecraft().getRenderManager().renderPosX;
		y2 = y2 - Minecraft.getMinecraft().getRenderManager().renderPosY;
		z2 = z2 - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		RenderUtil.setColor(color);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x2, y2,
			z2), -1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}
	
	/**
	 * Renders an ESP box with the size of a normal block at the specified
	 * coordinates.
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void blockESPBox(BlockPos blockPos)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(1.0F);
		GL11.glColor4d(0, 1, 0, 0.15F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glColor4d(0, 0, 0, 0.5F);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z,
			x + 1.0, y + 1.0, z + 1.0), -1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}
	
	public static void framelessBlockESP(BlockPos blockPos, Color color)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glColor4d(color.getRed() / 255, color.getGreen() / 255,
			color.getBlue() / 255, 0.15);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}
	
	public static void emptyBlockESPBox(BlockPos blockPos)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4d(0, 0, 0, 0.5F);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z,
			x + 1.0, y + 1.0, z + 1.0), -1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}
	
	public static int enemy = 0;
	public static int friend = 1;
	public static int other = 2;
	public static int target = 3;
	public static int team = 4;
	
	public static void entityESPBox(Entity entity, int mode)
	{
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		if(mode == 0)// Enemy
			GL11.glColor4d(
				1 - Minecraft.getMinecraft().thePlayer
					.getDistanceToEntity(entity) / 40,
				Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) / 40,
				0, 0.5F);
		else if(mode == 1)// Friend
			GL11.glColor4d(0, 0, 1, 0.5F);
		else if(mode == 2)// Other
			GL11.glColor4d(1, 1, 0, 0.5F);
		else if(mode == 3)// Target
			GL11.glColor4d(1, 0, 0, 0.5F);
		else if(mode == 4)// Team
			GL11.glColor4d(0, 1, 0, 0.5F);
		Minecraft.getMinecraft().getRenderManager();
		RenderGlobal.drawOutlinedBoundingBox(
			new AxisAlignedBB(
				entity.boundingBox.minX
					- 0.05
					- entity.posX
					+ (entity.posX - Minecraft.getMinecraft()
						.getRenderManager().renderPosX),
				entity.boundingBox.minY
					- entity.posY
					+ (entity.posY - Minecraft.getMinecraft()
						.getRenderManager().renderPosY),
				entity.boundingBox.minZ
					- 0.05
					- entity.posZ
					+ (entity.posZ - Minecraft.getMinecraft()
						.getRenderManager().renderPosZ),
				entity.boundingBox.maxX
					+ 0.05
					- entity.posX
					+ (entity.posX - Minecraft.getMinecraft()
						.getRenderManager().renderPosX),
				entity.boundingBox.maxY
					+ 0.1
					- entity.posY
					+ (entity.posY - Minecraft.getMinecraft()
						.getRenderManager().renderPosY),
				entity.boundingBox.maxZ
					+ 0.05
					- entity.posZ
					+ (entity.posZ - Minecraft.getMinecraft()
						.getRenderManager().renderPosZ)), -1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}
	
	public static void nukerBox(BlockPos blockPos, float damage)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(1.0F);
		GL11.glColor4d(damage, 1 - damage, 0, 0.15F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x + 0.5 - damage / 2, y + 0.5 - damage
			/ 2, z + 0.5 - damage / 2, x + 0.5 + damage / 2, y + 0.5 + damage
			/ 2, z + 0.5 + damage / 2));
		GL11.glColor4d(0, 0, 0, 0.5F);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x + 0.5 - damage
			/ 2, y + 0.5 - damage / 2, z + 0.5 - damage / 2, x + 0.5 + damage
			/ 2, y + 0.5 + damage / 2, z + 0.5 + damage / 2), -1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}
	
	public static void searchBox(BlockPos blockPos)
	{
		double x =
			blockPos.getX()
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			blockPos.getY()
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			blockPos.getZ()
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL_BLEND);
		GL11.glLineWidth(1.0F);
		float sinus =
			1F - MathHelper.abs(MathHelper.sin(Minecraft.getSystemTime()
				% 10000L / 10000.0F * (float)Math.PI * 4.0F) * 1F);
		GL11.glColor4d(1 - sinus, sinus, 0, 0.15);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glColor4d(0, 0, 0, 0.5);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z,
			x + 1.0, y + 1.0, z + 1.0), -1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL_BLEND);
	}
	
	public static void drawColorBox(AxisAlignedBB axisalignedbb)
	{
		Tessellator ts = Tessellator.getInstance();
		WorldRenderer wr = ts.getWorldRenderer();
		wr.startDrawingQuads();// Starts X.
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		ts.draw();
		wr.startDrawingQuads();
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		ts.draw();// Ends X.
		wr.startDrawingQuads();// Starts Y.
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		ts.draw();
		wr.startDrawingQuads();
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		ts.draw();// Ends Y.
		wr.startDrawingQuads();// Starts Z.
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		ts.draw();
		wr.startDrawingQuads();
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		wr.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		ts.draw();// Ends Z.
	}
	
	public static void tracerLine(Entity entity, int mode)
	{
		double x =
			entity.posX
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			entity.posY + entity.height / 2
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			entity.posZ
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(770, 771);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		if(mode == 0)// Enemy
			GL11.glColor4d(
				1 - Minecraft.getMinecraft().thePlayer
					.getDistanceToEntity(entity) / 40,
				Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) / 40,
				0, 0.5F);
		else if(mode == 1)// Friend
			GL11.glColor4d(0, 0, 1, 0.5F);
		else if(mode == 2)// Other
			GL11.glColor4d(1, 1, 0, 0.5F);
		else if(mode == 3)// Target
			GL11.glColor4d(1, 0, 0, 0.5F);
		else if(mode == 4)// Team
			GL11.glColor4d(0, 1, 0, 0.5F);
		glBegin(GL_LINES);
		{
			glVertex3d(0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0);
			glVertex3d(x, y, z);
		}
		glEnd();
		glEnable(GL11.GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void tracerLine(Entity entity, Color color)
	{
		double x =
			entity.posX
				- Minecraft.getMinecraft().getRenderManager().renderPosX;
		double y =
			entity.posY + entity.height / 2
				- Minecraft.getMinecraft().getRenderManager().renderPosY;
		double z =
			entity.posZ
				- Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(770, 771);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		RenderUtil.setColor(color);
		glBegin(GL_LINES);
		{
			glVertex3d(0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0);
			glVertex3d(x, y, z);
		}
		glEnd();
		glEnable(GL11.GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public static void tracerLine(int x, int y, int z, Color color)
	{
		x += 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosX;
		y += 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosY;
		z += 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosZ;
		glBlendFunc(770, 771);
		glEnable(GL_BLEND);
		glLineWidth(2.0F);
		glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		RenderUtil.setColor(color);
		glBegin(GL_LINES);
		{
			glVertex3d(0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0);
			glVertex3d(x, y, z);
		}
		glEnd();
		glEnable(GL11.GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
}
