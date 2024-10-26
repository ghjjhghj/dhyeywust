/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.mods.RemoteViewMod;

@Info(help = "Toggles RemoteView or makes it target a specific entity.",
	name = "rv",
	syntax = {"[<Player>]"})
public class RvCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length == 0)
		{
			RemoteViewMod.onEnabledByCommand("");
			return;
		}else if(args.length == 1)
			RemoteViewMod.onEnabledByCommand(args[0]);
		else
			syntaxError("too many arguments.");
	}
}
