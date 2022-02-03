package imager.content;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.Gl;

public class NHPColor{
	public static Blending
		alphaKeeper = new Blending(Gl.srcAlpha, Gl.oneMinusSrcAlpha);
	
	public static Color
		ally = new Color(0, 0, 1, 0.15f), hostile = new Color(1, 0, 0, 0.15f),
	    ally2 = new Color(0.63f, 0.88f, 0.63f, 0.77f), hostile2 = new Color(1.0f, 0.44f, 0.41f, 0.77f),
	
		ally_copier = new Color(0, 0, 1, 0.15f), ally2_copier = new Color(0.63f, 0.88f, 0.63f, 0.77f), hostile2_copier = new Color(1.0f, 0.44f, 0.41f, 0.77f);
}










