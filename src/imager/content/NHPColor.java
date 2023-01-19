package imager.content;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.Gl;
import mindustry.content.Items;
import mindustry.graphics.Pal;

public class NHPColor{
	public static Blending
		alphaKeeper = new Blending(Gl.srcAlpha, Gl.oneMinusSrcAlpha);
	
	public static Color
		ancient = Items.surgeAlloy.color.cpy().lerp(Pal.accent, 0.055f),
		deeperBlue = Color.valueOf("#778ff2"),
		lightSky = Color.valueOf("#8DB0FF"),
		lightSkyBack = lightSky.cpy().lerp(Color.white, 0.2f),
		lightSkyMiddle = lightSky.cpy().lerp(Color.white, 0.6f),
		lightSkyFront = lightSky.cpy().lerp(Color.white, 0.8f),
		darkEnrColor = Pal.sapBullet,
		thurmixRed = Color.valueOf("#FF9492"),
		thurmixRedLight = Color.valueOf("#FFCED0"),
		thurmixRedDark = thurmixRed.cpy().lerp(Color.black, 0.9f),
		darkEnr = darkEnrColor.cpy().lerp(Color.black, 0.85f),
		darkEnrFront = darkEnrColor.cpy().lerp(Color.white, 0.2f),
		trail = Color.lightGray.cpy().lerp(Color.gray, 0.65f),
		thermoPst = Color.valueOf("CFFF87").lerp(Color.white, 0.15f),
		
		ally = new Color(0, 0, 1, 0.15f), hostile = new Color(1, 0, 0, 0.15f),
	    ally2 = new Color(0.63f, 0.88f, 0.63f, 0.77f), hostile2 = new Color(1.0f, 0.44f, 0.41f, 0.77f),
	
		ally_copier = new Color(0, 0, 1, 0.15f), ally2_copier = new Color(0.63f, 0.88f, 0.63f, 0.77f), hostile2_copier = new Color(1.0f, 0.44f, 0.41f, 0.77f);
}










