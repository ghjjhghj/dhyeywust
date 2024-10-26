/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.special;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

public class SpfManager
{
	private final TreeMap<String, Spf> features = new TreeMap<String, Spf>(
		new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2)
			{
				return o1.compareToIgnoreCase(o2);
			}
		});
	
	public final BookHackSpf bookHackSpf = new BookHackSpf();
	public final ChangelogSpf changelogSpf = new ChangelogSpf();
	public final ServerFinderSpf serverFinderSpf = new ServerFinderSpf();
	public final SessionStealerSpf sessionStealerSpf = new SessionStealerSpf();
	public final TargetSpf targetSpf = new TargetSpf();
	
	public SpfManager()
	{
		try
		{
			for(Field field : SpfManager.class.getFields())
				if(field.getName().endsWith("Spf"))
				{
					Spf spf = (Spf)field.get(this);
					features.put(spf.getName(), spf);
				}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Spf getFeatureByName(String name)
	{
		return features.get(name);
	}
	
	public Collection<Spf> getAllFeatures()
	{
		return features.values();
	}
	
	public int countFeatures()
	{
		return features.size();
	}
}
