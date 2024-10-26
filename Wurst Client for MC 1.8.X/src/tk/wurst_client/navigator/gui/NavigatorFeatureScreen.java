/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.navigator.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.util.RenderUtil;

import tk.wurst_client.WurstClient;
import tk.wurst_client.font.Fonts;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.navigator.PossibleKeybind;
import tk.wurst_client.navigator.settings.NavigatorSetting;
import tk.wurst_client.utils.MiscUtils;

public class NavigatorFeatureScreen extends NavigatorScreen
{
	private NavigatorItem item;
	private NavigatorMainScreen parent;
	private ButtonData activeButton;
	private GuiButton primaryButton;
	private int sliding = -1;
	private String text;
	private ArrayList<ButtonData> buttonDatas = new ArrayList<>();
	private ArrayList<SliderData> sliderDatas = new ArrayList<>();
	private ArrayList<CheckboxData> checkboxDatas = new ArrayList<>();
	
	public NavigatorFeatureScreen(NavigatorItem item, NavigatorMainScreen parent)
	{
		this.item = item;
		this.parent = parent;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(!button.enabled)
			return;
		
		WurstClient wurst = WurstClient.INSTANCE;
		switch(button.id)
		{
			case 0:
				item.doPrimaryAction();
				primaryButton.displayString = item.getPrimaryAction();
				break;
			case 1:
				MiscUtils.openLink("https://www.wurst-client.tk/wiki/"
					+ item.getTutorialPage());
				wurst.navigator.analytics.trackEvent("tutorial", "open",
					item.getName());
				break;
		}
		
		wurst.navigator.addPreference(item.getName());
		wurst.files.saveNavigatorData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onResize()
	{
		buttonDatas.clear();
		
		// primary button
		String primaryAction = item.getPrimaryAction();
		boolean hasPrimaryAction = !primaryAction.isEmpty();
		boolean hasTutorial = !item.getTutorialPage().isEmpty();
		if(hasPrimaryAction)
		{
			primaryButton =
				new GuiButton(0, width / 2 - 151, height - 65, hasTutorial
					? 149 : 302, 18, primaryAction);
			buttonList.add(primaryButton);
		}
		
		// tutorial button
		if(hasTutorial)
			buttonList.add(new GuiButton(1, width / 2
				+ (hasPrimaryAction ? 2 : -151), height - 65, hasPrimaryAction
				? 149 : 302, 20, "Tutorial"));
		
		// type
		text = "Type: " + item.getType();
		
		// description
		String description = item.getDescription();
		if(!description.isEmpty())
			text += "\n\nDescription:\n" + description;
		
		// area
		Rectangle area = new Rectangle(middleX - 154, 60, 308, height - 103);
		
		// settings
		ArrayList<NavigatorSetting> settings = item.getSettings();
		if(!settings.isEmpty())
		{
			text += "\n\nSettings:";
			sliderDatas.clear();
			checkboxDatas.clear();
			for(NavigatorSetting setting : settings)
				setting.addToFeatureScreen(this);
		}
		
		// keybinds
		ArrayList<PossibleKeybind> possibleKeybinds =
			item.getPossibleKeybinds();
		if(!possibleKeybinds.isEmpty())
		{
			// heading
			text += "\n\nKeybinds:";
			
			// add keybind button
			ButtonData addKeybindButton =
				new ButtonData(area.x + area.width - 16, area.y
					+ Fonts.segoe15.getStringHeight(text) - 8, 12, 8, "+",
					0x00ff00)
				{
					@Override
					public void press()
					{
						// add keybind
						mc.displayGuiScreen(new NavigatorNewKeybindScreen(
							possibleKeybinds, NavigatorFeatureScreen.this));
					}
				};
			buttonDatas.add(addKeybindButton);
			
			// keybind list
			HashMap<String, String> possibleKeybindsMap = new HashMap<>();
			for(PossibleKeybind possibleKeybind : possibleKeybinds)
				possibleKeybindsMap.put(possibleKeybind.getCommand(),
					possibleKeybind.getDescription());
			TreeMap<String, PossibleKeybind> existingKeybinds = new TreeMap<>();
			boolean noKeybindsSet = true;
			for(Entry<String, String> entry : WurstClient.INSTANCE.keybinds
				.entrySet())
			{
				String keybindDescription =
					possibleKeybindsMap.get(entry.getValue());
				if(keybindDescription != null)
				{
					if(noKeybindsSet)
						noKeybindsSet = false;
					text += "\n" + entry.getKey() + ": " + keybindDescription;
					existingKeybinds.put(entry.getKey(), new PossibleKeybind(
						entry.getValue(), keybindDescription));
				}
			}
			if(noKeybindsSet)
				text += "\nNone";
			else
			{
				// remove keybind button
				buttonDatas.add(new ButtonData(addKeybindButton.x,
					addKeybindButton.y, addKeybindButton.width,
					addKeybindButton.height, "-", 0xff0000)
				{
					@Override
					public void press()
					{
						// remove keybind
						mc.displayGuiScreen(new NavigatorRemoveKeybindScreen(
							existingKeybinds, NavigatorFeatureScreen.this));
					}
				});
				addKeybindButton.x -= 16;
			}
		}
		
		// see also
		NavigatorItem[] seeAlso = item.getSeeAlso();
		if(seeAlso.length != 0)
		{
			text += "\n\nSee also:\n";
			for(int i = 0; i < seeAlso.length; i++)
			{
				int y = 60 + getTextHeight() + 2;
				NavigatorItem seeAlsoItem = seeAlso[i];
				String name = seeAlsoItem.getName();
				text += "- " + name + (i == seeAlso.length - 1 ? "" : "\n");
				buttonDatas.add(new ButtonData(middleX - 148, y, Fonts.segoe15
					.getStringWidth(name) + 3, 8, "", 0x404040)
				{
					@Override
					public void press()
					{
						mc.displayGuiScreen(new NavigatorFeatureScreen(
							seeAlsoItem, parent));
					}
				});
			}
		}
		
		// text height
		setContentHeight(Fonts.segoe15.getStringHeight(text));
	}
	
	@Override
	protected void onKeyPress(char typedChar, int keyCode)
	{
		if(keyCode == 1)
		{
			parent.setExpanding(false);
			mc.displayGuiScreen(parent);
		}
	}
	
	@Override
	protected void onMouseClick(int x, int y, int button)
	{
		Rectangle area = new Rectangle(width / 2 - 154, 60, 308, height - 103);
		if(!area.contains(x, y))
			return;
		
		// buttons
		if(activeButton != null)
		{
			mc.getSoundHandler().playSound(
				PositionedSoundRecord.createPositionedSoundRecord(
					new ResourceLocation("gui.button.press"), 1.0F));
			activeButton.press();
			WurstClient wurst = WurstClient.INSTANCE;
			wurst.navigator.addPreference(item.getName());
			wurst.files.saveNavigatorData();
			return;
		}
		
		// sliders
		area.height = 12;
		for(int i = 0; i < sliderDatas.size(); i++)
		{
			area.y = sliderDatas.get(i).y + scroll;
			if(area.contains(x, y))
			{
				sliding = i;
				return;
			}
		}
		
		// checkboxes
		for(int i = 0; i < checkboxDatas.size(); i++)
		{
			CheckboxData checkboxData = checkboxDatas.get(i);
			area.y = checkboxData.y + scroll;
			if(area.contains(x, y))
			{
				checkboxData.checked = !checkboxData.checked;
				checkboxData.toggle();
				WurstClient wurst = WurstClient.INSTANCE;
				wurst.navigator.addPreference(item.getName());
				wurst.files.saveNavigatorData();
				return;
			}
		}
	}
	
	@Override
	protected void onMouseDrag(int x, int y, int button, long timeDragged)
	{
		if(button != 0)
			return;
		if(sliding != -1)
			sliderDatas.get(sliding).slideTo(x);
	}
	
	@Override
	protected void onMouseRelease(int x, int y, int button)
	{
		if(sliding != -1)
		{
			WurstClient wurst = WurstClient.INSTANCE;
			sliding = -1;
			
			wurst.navigator.addPreference(item.getName());
			wurst.files.saveNavigatorData();
		}
	}
	
	@Override
	protected void onUpdate()
	{	
		
	}
	
	@Override
	protected void onRender(int mouseX, int mouseY, float partialTicks)
	{
		// title bar
		drawCenteredString(Fonts.segoe22, item.getName(), middleX, 32, 0xffffff);
		glDisable(GL_TEXTURE_2D);
		
		// background
		int bgx1 = middleX - 154;
		int bgx2 = middleX + 154;
		int bgy1 = 60;
		int bgy2 = height - 43;
		
		// scissor box
		RenderUtil.scissorBox(bgx1, bgy1, bgx2, bgy2
			- (buttonList.isEmpty() ? 0 : 24));
		glEnable(GL_SCISSOR_TEST);
		
		// sliders
		for(SliderData sliderData : sliderDatas)
		{
			// rail
			int x1 = bgx1 + 2;
			int x2 = bgx2 - 2;
			int y1 = sliderData.y + scroll + 4;
			int y2 = y1 + 4;
			setColorToForeground();
			drawEngravedBox(x1, y1, x2, y2);
			
			// knob
			x1 = sliderData.x;
			x2 = x1 + 8;
			y1 -= 2;
			y2 += 2;
			float percentage = sliderData.percentage;
			glColor4f(percentage, 1F - percentage, 0F, 0.75F);
			drawBox(x1, y1, x2, y2);
			
			// value
			String value = sliderData.value;
			x1 = bgx2 - Fonts.segoe15.getStringWidth(value) - 2;
			y1 -= 12;
			drawString(Fonts.segoe15, value, x1, y1, 0xffffff);
			glDisable(GL_TEXTURE_2D);
		}
		
		// buttons
		activeButton = null;
		for(ButtonData buttonData : buttonDatas)
		{
			// positions
			int x1 = buttonData.x;
			int x2 = x1 + buttonData.width;
			int y1 = buttonData.y + scroll;
			int y2 = y1 + buttonData.height;
			
			// color
			float alpha;
			if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2)
			{
				alpha = 0.75F;
				activeButton = buttonData;
			}else
				alpha = 0.375F;
			float[] rgb = buttonData.color.getColorComponents(null);
			glColor4f(rgb[0], rgb[1], rgb[2], alpha);
			
			// button
			drawBox(x1, y1, x2, y2);
			
			// text
			drawCenteredString(Fonts.segoe18, buttonData.buttonText,
				(x1 + x2) / 2 - 1, y1 + (buttonData.height - 12) / 2 - 1,
				buttonData.textColor);
			glDisable(GL_TEXTURE_2D);
		}
		
		// checkboxes
		for(CheckboxData checkboxData : checkboxDatas)
		{
			// positions
			int x1 = bgx1 + 2;
			int x2 = x1 + 10;
			int y1 = checkboxData.y + scroll + 2;
			int y2 = y1 + 10;
			
			// hovering
			boolean hovering =
				mouseX >= x1 && mouseX <= bgx2 - 2 && mouseY >= y1
					&& mouseY <= y2;
			
			// box
			if(hovering)
				glColor4f(0.375F, 0.375F, 0.375F, 0.25F);
			else
				glColor4f(0.25F, 0.25F, 0.25F, 0.25F);
			drawBox(x1, y1, x2, y2);
			
			// check
			if(checkboxData.checked)
			{
				// check
				glColor4f(0F, 1F, 0F, hovering ? 0.75F : 0.375F);
				glBegin(GL_QUADS);
				{
					glVertex2i(x1 + 3, y1 + 5);
					glVertex2i(x1 + 4, y1 + 6);
					glVertex2i(x1 + 4, y1 + 8);
					glVertex2i(x1 + 2, y1 + 6);
					
					glVertex2i(x1 + 7, y1 + 2);
					glVertex2i(x1 + 8, y1 + 3);
					glVertex2i(x1 + 4, y1 + 6);
					glVertex2i(x1 + 4, y1 + 8);
				}
				glEnd();
				
				// shadow
				glColor4f(0.125F, 0.125F, 0.125F, hovering ? 0.75F : 0.375F);
				glBegin(GL_LINE_LOOP);
				{
					glVertex2i(x1 + 3, y1 + 5);
					glVertex2i(x1 + 4, y1 + 6);
					glVertex2i(x1 + 7, y1 + 2);
					glVertex2i(x1 + 8, y1 + 3);
					
					glVertex2i(x1 + 4, y1 + 8);
					glVertex2i(x1 + 2, y1 + 6);
				}
				glEnd();
				
			}
			
			// name
			x1 += 12;
			y1 -= 1;
			drawString(Fonts.segoe15, checkboxData.name, x1, y1, 0xffffff);
			glDisable(GL_TEXTURE_2D);
		}
		
		// text
		drawString(Fonts.segoe15, text, bgx1 + 2, bgy1 + scroll, 0xffffff);
		
		// scissor box
		glDisable(GL_SCISSOR_TEST);
		
		// buttons below scissor box
		for(int i = 0; i < buttonList.size(); i++)
		{
			GuiButton button = (GuiButton)buttonList.get(i);
			
			// positions
			int x1 = button.xPosition;
			int x2 = x1 + button.getButtonWidth();
			int y1 = button.yPosition;
			int y2 = y1 + 18;
			
			// color
			boolean hovering =
				mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
			if(item.isEnabled() && button.id == 0)
				if(item.isBlocked())
					glColor4f(hovering ? 1F : 0.875F, 0F, 0F, 0.25F);
				else
					glColor4f(0F, hovering ? 1F : 0.875F, 0F, 0.25F);
			else if(hovering)
				glColor4f(0.375F, 0.375F, 0.375F, 0.25F);
			else
				glColor4f(0.25F, 0.25F, 0.25F, 0.25F);
			
			// button
			glDisable(GL_TEXTURE_2D);
			drawBox(x1, y1, x2, y2);
			
			// text
			drawCenteredString(Fonts.segoe18, button.displayString,
				(x1 + x2) / 2, y1 + 2, 0xffffff);
		}
		
		// GL resets
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
	
	public NavigatorItem getItem()
	{
		return item;
	}
	
	public int getMiddleX()
	{
		return middleX;
	}
	
	public void addText(String text)
	{
		this.text += text;
	}
	
	public int getTextHeight()
	{
		return Fonts.segoe15.getStringHeight(text);
	}
	
	public void addButton(ButtonData button)
	{
		buttonDatas.add(button);
	}
	
	public void addSlider(SliderData slider)
	{
		sliderDatas.add(slider);
	}
	
	public void addCheckbox(CheckboxData checkbox)
	{
		checkboxDatas.add(checkbox);
	}
	
	public abstract class ButtonData extends Rectangle
	{
		public String buttonText;
		public Color color;
		public int textColor = 0xffffff;
		
		public ButtonData(int x, int y, int width, int height,
			String buttonText, int color)
		{
			super(x, y, width, height);
			this.buttonText = buttonText;
			this.color = new Color(color);
		}
		
		public abstract void press();
	}
	
	public class SliderData
	{
		public BasicSlider slider;
		public int x;
		public int y;
		public float percentage;
		public String value;
		
		public SliderData(BasicSlider slider, int y)
		{
			this.slider = slider;
			this.y = y;
			
			update();
		}
		
		private void update()
		{
			// display value
			switch(slider.getValueDisplay())
			{
				case DECIMAL:
					value = Double.toString(slider.getValue());
					break;
				case DEGREES:
					value = (int)slider.getValue() + "�";
					break;
				case INTEGER:
					value = Integer.toString((int)slider.getValue());
					break;
				case PERCENTAGE:
					value = slider.getValue() * 1e6 * 100D * 1e6 / 1e12 + "%";
					break;
				case NONE:
				default:
					value = "";
					break;
			}
			
			// percentage
			percentage =
				(float)((slider.getValue() - slider.getMinimumValue()) / (slider
					.getMaximumValue() - slider.getMinimumValue()));
			
			// x
			x = middleX - 154 + (int)(percentage * 298) + 1;
		}
		
		public void slideTo(int mouseX)
		{
			// percentage from mouse location (not the actual percentage!)
			float mousePercentage = (mouseX - (middleX - 150)) / 298F;
			if(mousePercentage > 1F)
				mousePercentage = 1F;
			else if(mousePercentage < 0F)
				mousePercentage = 0F;
			
			// update slider value
			slider.setValue((long)((slider.getMaximumValue() - slider
				.getMinimumValue()) * mousePercentage / slider.getIncrement())
				* 1e6 * slider.getIncrement() / 1e6 + slider.getMinimumValue());
			
			// update slider data
			update();
		}
	}
	
	public abstract class CheckboxData
	{
		public String name;
		public boolean checked;
		public int y;
		
		public CheckboxData(String name, boolean checked, int y)
		{
			this.name = name;
			this.checked = checked;
			this.y = y;
		}
		
		public abstract void toggle();
	}
}
