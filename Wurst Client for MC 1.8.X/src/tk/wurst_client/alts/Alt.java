/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.alts;

public class Alt
{
	private String email;
	private String name;
	private String password;
	private boolean cracked;
	private boolean unchecked;
	private boolean starred;
	
	public Alt(String email, String password, String name)
	{
		this(email, password, name, false);
	}
	
	public Alt(String email, String password, String name, boolean starred)
	{
		this.email = email;
		this.starred = starred;
		
		if(password == null || password.isEmpty())
		{
			cracked = true;
			unchecked = false;
			
			this.name = email;
			this.password = null;
			
		}else
		{
			cracked = false;
			unchecked = name == null || name.isEmpty();
			
			this.name = name;
			this.password = password;
		}
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getNameOrEmail()
	{
		return unchecked ? email : name;
	}
	
	public String getPassword()
	{
		if(password == null || password.isEmpty())
		{
			cracked = true;
			return "";
		}else
			return password;
	}
	
	public boolean isCracked()
	{
		return cracked;
	}
	
	public boolean isStarred()
	{
		return starred;
	}
	
	public void setStarred(boolean starred)
	{
		this.starred = starred;
	}
	
	public boolean isUnchecked()
	{
		return unchecked;
	}
	
	public void setChecked(String name)
	{
		this.name = name;
		unchecked = false;
	}
}
