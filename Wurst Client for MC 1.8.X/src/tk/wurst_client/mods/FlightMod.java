/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.AxisAlignedBB;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;

import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.navigator.settings.CheckboxSetting;
import tk.wurst_client.navigator.settings.SliderSetting;

@Info(category = Category.MOVEMENT,
	description = "Allows you to you fly.\n"
		+ "Bypasses NoCheat+ if YesCheat+ is enabled.\n"
		+ "Bypasses MAC if AntiMAC is enabled.",
	name = "Flight",
	tags = "FlyHack,fly hack,flying",
	tutorial = "Mods/Flight")
public class FlightMod extends Mod implements UpdateListener
{
	public float speed = 1F;
	
	public double flyHeight;
	private double startY;
	
	public final CheckboxSetting flightKickBypass = new CheckboxSetting(
		"Flight-Kick-Bypass", false);
	
	@Override
	public String getRenderName()
	{
		if(wurst.mods.yesCheatMod.isActive()
			|| wurst.mods.antiMacMod.isActive()
			|| !flightKickBypass.isChecked())
			return getName();
		
		return getName() + "[Kick: " + (flyHeight <= 300 ? "Safe" : "Unsafe")
			+ "]";
	}
	
	@Override
	public void initSettings()
	{
		settings.add(new SliderSetting("Speed", speed, 0.05, 5, 0.05,
			ValueDisplay.DECIMAL)
		{
			@Override
			public void update()
			{
				speed = (float)getValue();
			}
		});
		
		settings.add(flightKickBypass);
	}
	
	public void updateFlyHeight()
	{
		double h = 1;
		AxisAlignedBB box =
			mc.thePlayer.getEntityBoundingBox().expand(0.0625, 0.0625, 0.0625);
		for(flyHeight = 0; flyHeight < mc.thePlayer.posY; flyHeight += h)
		{
			AxisAlignedBB nextBox = box.offset(0, -flyHeight, 0);
			
			if(mc.theWorld.checkBlockCollision(nextBox))
			{
				if(h < 0.0625)
					break;
				
				flyHeight -= h;
				h /= 2;
			}
		}
	}
	
	public void goToGround()
	{
		if(flyHeight > 300)
			return;
		
		double minY = mc.thePlayer.posY - flyHeight;
		
		if(minY <= 0)
			return;
		
		for(double y = mc.thePlayer.posY; y > minY;)
		{
			y -= 8;
			if(y < minY)
				y = minY;
			
			C04PacketPlayerPosition packet =
				new C04PacketPlayerPosition(mc.thePlayer.posX, y,
					mc.thePlayer.posZ, true);
			mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
		
		for(double y = minY; y < mc.thePlayer.posY;)
		{
			y += 8;
			if(y > mc.thePlayer.posY)
				y = mc.thePlayer.posY;
			
			C04PacketPlayerPosition packet =
				new C04PacketPlayerPosition(mc.thePlayer.posX, y,
					mc.thePlayer.posZ, true);
			mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
	}
	
	@Override
	public NavigatorItem[] getSeeAlso()
	{
		return new NavigatorItem[]{wurst.mods.jetpackMod, wurst.mods.glideMod,
			wurst.mods.noFallMod, wurst.mods.yesCheatMod, wurst.mods.antiMacMod};
	}
	
	@Override
	public void onEnable()
	{
		if(wurst.mods.jetpackMod.isEnabled())
			wurst.mods.jetpackMod.setEnabled(false);
		
		if(wurst.mods.yesCheatMod.isActive()
			|| wurst.mods.antiMacMod.isActive())
		{
			double startX = mc.thePlayer.posX;
			startY = mc.thePlayer.posY;
			double startZ = mc.thePlayer.posZ;
			for(int i = 0; i < 4; i++)
			{
				mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
						startX, startY + 1.01, startZ, false));
				mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
						startX, startY, startZ, false));
			}
			mc.thePlayer.jump();
		}
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(wurst.mods.yesCheatMod.isActive())
		{
			if(!mc.thePlayer.onGround)
				if(mc.gameSettings.keyBindJump.pressed
					&& mc.thePlayer.posY < startY - 1)
					mc.thePlayer.motionY = 0.2;
				else
					mc.thePlayer.motionY = -0.02;
		}else if(wurst.mods.antiMacMod.isActive())
		{
			updateMS();
			if(!mc.thePlayer.onGround)
				if(mc.gameSettings.keyBindJump.pressed && hasTimePassedS(2))
				{
					mc.thePlayer.setPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + 8, mc.thePlayer.posZ);
					updateLastMS();
				}else if(mc.gameSettings.keyBindSneak.pressed)
					mc.thePlayer.motionY = -0.4;
				else
					mc.thePlayer.motionY = -0.02;
			mc.thePlayer.jumpMovementFactor = 0.04F;
		}else
		{
			updateMS();
			
			mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionY = 0;
			mc.thePlayer.motionZ = 0;
			mc.thePlayer.jumpMovementFactor = speed;
			
			if(mc.gameSettings.keyBindJump.pressed)
				mc.thePlayer.motionY += speed;
			if(mc.gameSettings.keyBindSneak.pressed)
				mc.thePlayer.motionY -= speed;
			
			if(flightKickBypass.isChecked())
			{
				updateFlyHeight();
				mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer(true));
				
				if(flyHeight <= 290 && hasTimePassedM(500) || flyHeight > 290
					&& hasTimePassedM(100))
				{
					goToGround();
					updateLastMS();
				}
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
	}
}
