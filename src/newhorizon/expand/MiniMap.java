package newhorizon.expand;

import arc.scene.ui.layout.Table;
import mindustry.Vars;

public class MiniMap{
	public static Table minimapTable, minimap;
	public static void load(){
		minimapTable = Vars.ui.hudGroup.find("minimap/position");
		minimap = minimapTable.find("minimap");
		
	}
	
	
}
