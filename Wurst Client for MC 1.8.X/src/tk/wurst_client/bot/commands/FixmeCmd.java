/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.bot.commands;

import net.minecraft.client.Minecraft;

@Command.Info(help = "Fixes you if you get stuck in a menu.",
	name = "fixme",
	syntax = {})
public class FixmeCmd extends Command
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length != 0)
			syntaxError();
		if(Minecraft.getMinecraft().thePlayer == null)
			error("Not connected to any server.");
		Minecraft.getMinecraft().displayGuiScreen(null);
		System.out.println("Closed all open menus.");
	}
}
