package imager.expand.ui;

import arc.func.Intp;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import mindustry.core.UI;

public class IconNumTable extends Stack{
	public IconNumTable(TextureRegion region, int amount){
		
		add(new Table(o -> {
			o.left();
			o.add(new Image(region)).scaling(Scaling.fit).size(32f);
		}));
		
		add(new Table(t -> {
			t.left().bottom();
			t.add(amount > 1000 ? UI.formatAmount(amount) : amount + "");
			t.pack();
		}));
	}
	
	public IconNumTable(TextureRegion region, Intp amount){
		
		add(new Table(o -> {
			o.left();
			o.add(new Image(region)).scaling(Scaling.fit).size(32f);
		}));
		
		add(new Table(t -> {
			t.left().bottom();
			t.label(() -> amount.get() > 1000 ? UI.formatAmount(amount.get()) : amount.get() + "");
			t.pack();
		}));
	}
}
