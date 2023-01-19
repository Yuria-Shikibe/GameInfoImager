package imager;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import imager.content.NHPColor;
import imager.content.NHPShaders;
import imager.expand.GII_EventListeners;
import imager.expand.GII_HUD;
import mindustry.Vars;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.Mods;
import mindustry.mod.Plugin;


public class GII_Plugin extends Plugin{
	public static Mods.LoadedMod MOD;
	public static final String SETTING_KEY = "nh-plugin-active";
	public static final String DRAW_UNIT_SIGN = "nh-plugin-sign";
	public static final String BUILDING_SIZE_FILTER = "nh-plugin-building-size-filter";
	public static final String UNIT_SIZE_FILTER = "nh-plugin-unit-size-filter";
	
	public static final String SHOW_ALLY = "nh-plugin-show-ally";
	public static final String SHOW_HIGHLIGHT = "nh-plugin-show-highlight";
	
	public static final String ALPHA = "nh-plugin-show-alpha";
	
	public static final String SHOW_WAVE_INFO = "nh-plugin-show-wave-info";
	public static final String SHOW_WAVE_DETAILS = "nh-plugin-show-wave-details";
	public static final String SHOW_WAVE_PER_LINE = "nh.ui.wave.unit-per-line";
	
	public static final String SHOW_UNIT_HEALTH_BAR = "nh.ui.wave.unit-health-bar";
	
	public static int buildingShowMinSize = 1;
	public static float unitShowMinSize = 1;
	
	public static boolean drawAlly, drawHighlight, showHealthBar;
	
	public GII_Plugin(){
		Events.on(ClientLoadEvent.class, e -> {
		
			Vars.ui.settings.game.checkPref(SHOW_UNIT_HEALTH_BAR, true);
			Vars.ui.settings.game.checkPref(SHOW_WAVE_INFO, true);
			Vars.ui.settings.game.checkPref(SHOW_WAVE_DETAILS, true);
			Vars.ui.settings.game.sliderPref(SHOW_WAVE_PER_LINE, 10, 4, 20, 1, i -> "*" + i);
			
			Vars.ui.settings.graphics.checkPref(SETTING_KEY, true);
			Vars.ui.settings.graphics.checkPref(DRAW_UNIT_SIGN, true);
			
			Vars.ui.settings.graphics.checkPref(SHOW_ALLY, true, c -> {
				drawAlly = c;
				
				if(drawAlly)NHPColor.ally.set(NHPColor.ally_copier);
				else NHPColor.ally.set(Color.clear);
				
				if(drawAlly && drawHighlight)NHPColor.ally2.set(NHPColor.ally2_copier);
				else NHPColor.ally2.set(Color.clear);
			});
			Vars.ui.settings.graphics.checkPref(SHOW_HIGHLIGHT, true, c -> {
				drawHighlight = c;
				
				if(drawHighlight)NHPColor.hostile2.set(NHPColor.hostile2_copier);
				else NHPColor.hostile2.set(Color.clear);
				
				if(drawAlly && drawHighlight)NHPColor.ally2.set(NHPColor.ally2_copier);
				else NHPColor.ally2.set(Color.clear);
			});
			
			Vars.ui.settings.graphics.sliderPref(ALPHA, 60, 0, 100, 5, i -> i + "%");
			Vars.ui.settings.graphics.sliderPref(BUILDING_SIZE_FILTER, 1, 1, Vars.maxBlockSize, i -> i + " [accent]"  + Core.bundle.get("unit.blocks"));
			Vars.ui.settings.graphics.sliderPref(UNIT_SIZE_FILTER, 0, 0, 20, i -> i + " [accent]" + Core.bundle.get("unit.blockssquared"));
		});
	}

	@Override
	public void init() {
		MOD = Vars.mods.getMod(getClass());
		
		NHPShaders.init();
		GII_EventListeners.load();
		GII_HUD.load();
		
		showHealthBar = Core.settings.getBool(SHOW_UNIT_HEALTH_BAR, true);
		drawAlly = Core.settings.getBool(SHOW_ALLY, true);
		drawHighlight = Core.settings.getBool(SHOW_HIGHLIGHT, true);
		
		if(drawAlly)NHPColor.ally.set(NHPColor.ally_copier);
		else NHPColor.ally.set(Color.clear);
		
		if(drawHighlight)NHPColor.hostile2.set(NHPColor.hostile2_copier);
		else NHPColor.hostile2.set(Color.clear);
		
		if(drawAlly && drawHighlight)NHPColor.ally2.set(NHPColor.ally2_copier);
		else NHPColor.ally2.set(Color.clear);
	}
}
