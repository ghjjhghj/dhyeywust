/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.crash;

import java.util.concurrent.Callable;

import tk.wurst_client.WurstClient;

public class CrashSectionWurstVersion implements Callable
{
	@Override
	public String call()
	{
		return WurstClient.VERSION
			+ " (latest: "
			+ (WurstClient.INSTANCE.updater.getLatestVersion() == null
				? "unknown" : WurstClient.INSTANCE.updater.getLatestVersion())
			+ ")";
	}
}
