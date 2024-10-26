/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.hooks;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryLanDetected;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import tk.wurst_client.WurstClient;
import tk.wurst_client.utils.MiscUtils;

public class ServerHook
{
	private static String currentServerIP = "127.0.0.1:25565";
	private static ServerListEntryNormal lastServer;
	
	public static void importServers(GuiMultiplayer guiMultiplayer)
	{
		JFileChooser fileChooser =
			new JFileChooser(WurstClient.INSTANCE.files.serverlistsDir)
			{
				@Override
				protected JDialog createDialog(Component parent)
					throws HeadlessException
				{
					JDialog dialog = super.createDialog(parent);
					dialog.setAlwaysOnTop(true);
					return dialog;
				}
			};
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
			"TXT files", "txt"));
		int action = fileChooser.showOpenDialog(FrameHook.getFrame());
		if(action == JFileChooser.APPROVE_OPTION)
			try
			{
				File file = fileChooser.getSelectedFile();
				BufferedReader load = new BufferedReader(new FileReader(file));
				int i = 0;
				for(String line = ""; (line = load.readLine()) != null;)
				{
					i++;
					guiMultiplayer.savedServerList
						.addServerData(new ServerData("Grief me #" + i, line));
					guiMultiplayer.savedServerList.saveServerList();
					guiMultiplayer.serverListSelector.setSelectedServer(-1);
					guiMultiplayer.serverListSelector
						.func_148195_a(guiMultiplayer.savedServerList);
				}
				load.close();
				guiMultiplayer.refreshServerList();
			}catch(IOException e)
			{
				e.printStackTrace();
				MiscUtils.simpleError(e, fileChooser);
			}
	}
	
	public static void exportServers(GuiMultiplayer guiMultiplayer)
	{
		JFileChooser fileChooser =
			new JFileChooser(WurstClient.INSTANCE.files.serverlistsDir)
			{
				@Override
				protected JDialog createDialog(Component parent)
					throws HeadlessException
				{
					JDialog dialog = super.createDialog(parent);
					dialog.setAlwaysOnTop(true);
					return dialog;
				}
			};
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
			"TXT files", "txt"));
		int action = fileChooser.showSaveDialog(FrameHook.getFrame());
		if(action == JFileChooser.APPROVE_OPTION)
			try
			{
				File file = fileChooser.getSelectedFile();
				if(!file.getName().endsWith(".txt"))
					file = new File(file.getPath() + ".txt");
				PrintWriter save = new PrintWriter(new FileWriter(file));
				for(int i = 0; i < guiMultiplayer.savedServerList
					.countServers(); i++)
					save.println(guiMultiplayer.savedServerList
						.getServerData(i).serverIP);
				save.close();
			}catch(IOException e)
			{
				e.printStackTrace();
				MiscUtils.simpleError(e, fileChooser);
			}
	}
	
	public static void joinLastServer(GuiMultiplayer guiMultiplayer)
	{
		if(lastServer == null)
			return;
		
		currentServerIP = lastServer.getServerData().serverIP;
		if(!currentServerIP.contains(":"))
			currentServerIP += ":25565";
		
		guiMultiplayer.connectToServer(lastServer.getServerData());
	}
	
	public static void reconnectToLastServer(GuiScreen prevScreen)
	{
		if(lastServer == null)
			return;
		
		currentServerIP = lastServer.getServerData().serverIP;
		if(!currentServerIP.contains(":"))
			currentServerIP += ":25565";
		
		Minecraft mc = Minecraft.getMinecraft();
		mc.displayGuiScreen(new GuiConnecting(prevScreen, mc, lastServer
			.getServerData()));
	}
	
	public static void updateLastServerFromServerlist(IGuiListEntry entry,
		GuiMultiplayer guiMultiplayer)
	{
		if(entry instanceof ServerListEntryNormal)
		{
			currentServerIP =
				((ServerListEntryNormal)entry).getServerData().serverIP;
			if(!currentServerIP.contains(":"))
				currentServerIP += ":25565";
			
			lastServer =
				(ServerListEntryNormal)(guiMultiplayer.serverListSelector
					.getSelectedServer() < 0 ? null
					: guiMultiplayer.serverListSelector
						.getListEntry(guiMultiplayer.serverListSelector
							.getSelectedServer()));
		}else if(entry instanceof ServerListEntryLanDetected)
		{
			currentServerIP =
				((ServerListEntryLanDetected)entry).getLanServer()
					.getServerIpPort();
			
			lastServer =
				new ServerListEntryNormal(guiMultiplayer, new ServerData(
					"LAN-Server", currentServerIP));
		}
	}
	
	public static void updateLastServerFromDirectConnect(
		GuiMultiplayer guiMultiplayer, ServerData serverData)
	{
		currentServerIP = serverData.serverIP;
		if(!currentServerIP.contains(":"))
			currentServerIP += ":25565";
		
		lastServer = new ServerListEntryNormal(guiMultiplayer, serverData);
	}
	
	public static boolean hasLastServer()
	{
		return lastServer != null;
	}
	
	public static void setCurrentIpToSingleplayer()
	{
		currentServerIP = "127.0.0.1:25565";
	}
	
	public static void setCurrentIpToLanServer(String port)
	{
		currentServerIP = "127.0.0.1:" + port;
	}
	
	public static String getCurrentServerIP()
	{
		return currentServerIP;
	}
	
	public static ServerData getLastServerData()
	{
		return lastServer.getServerData();
	}
}
