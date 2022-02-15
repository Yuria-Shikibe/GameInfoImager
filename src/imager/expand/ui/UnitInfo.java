package imager.expand.ui;

import arc.Core;
import arc.func.Floatp;
import arc.func.Prov;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.IntMap;
import arc.struct.Seq;
import arc.util.Tmp;
import imager.GII_Plugin;
import imager.expand.GII_EventListeners;
import imager.expand.GII_HUD;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Iconc;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;

public class UnitInfo extends Table{
	public static final IntMap<Table> added = new IntMap<>();
	protected static int lastID = -1, lastSize;
	protected static final Seq<Unit> tmpSeq = new Seq<>();
	
	protected static final Vec2 pos = new Vec2();
	protected static final Group root = new WidgetGroup();
	
	protected static final float hideSclValue = 1.1f;
	
	protected static boolean hidden(){
		return Vars.renderer.getDisplayScale() <= hideSclValue;
	}
	
	public static final float SIZE_SCL = 6;
	
	public static final float DEFAULT_HEIGHT = 24f;
	
	public Unit unit;

	public static Table healthTable(Unit unit){
		return new Table(Styles.black3){{
			UnitHealthBar bar = new UnitHealthBar(
				unit, () -> Pal.lancerLaser, () -> Iconc.commandRally + " : " + (unit.shield() < 0 ? "SHIELD DOWNED" : (int)unit.shield()), unit::shield, () -> Math.max(unit.shield(), 100000)
			);
			bar.blinkable = true;
			bar.rootColor = Color.royal;
			bar.blinkColor = Pal.lancerLaser;
			bar.blinked = true;
			add(bar).grow().padBottom(4f).row();
			
			UnitHealthBar bar2 = new UnitHealthBar(
					unit, () -> unit.team.color, () -> Iconc.add + " : " + (unit.health() > 0 ? ((int)unit.health() + " / " + (int)unit.maxHealth()) : "Destroyed"), unit::healthf, () -> 1
			);
			
			bar2.blinkable = true;
			bar2.blinkColor = Pal.redderDust;
			bar2.blinked = true;
			
			add(bar2).grow();
			
			touchable = Touchable.enabled;
			
			hovered(() -> {
				toFront();
				setBackground(Styles.black8);
			});
			
			exited(() -> {
				setBackground(Styles.black3);
			});
			
			visible(() -> GII_Plugin.showHealthBar);
		}
			final Vec2 lastPosition = new Vec2();
			float lastSize = unit.hitSize;
			boolean shown = false;
			boolean fade = false;
			
			@Override
			public Element hit(float x, float y, boolean touchable){
				if(!shown)return null;
				return super.hit(x, y, touchable);
			}
			
			@Override
			public void act(float delta){
				margin(1.25f * Vars.renderer.getDisplayScale());

				if(unit.isValid()){
					lastPosition.set(unit).add(-unit.hitSize, unit.hitSize);
					lastSize = unit.hitSize;
				}else if(!fade){
					fade = true;
					actions(Actions.color(Pal.redderDust, 0.5f), Actions.delay(0.5f), Actions.fadeOut(0.85f), Actions.remove());
				}
				
				setSize(lastSize * 2f * Vars.renderer.getDisplayScale(), 14f * Vars.renderer.getDisplayScale());
				Tmp.v4.set(Core.camera.project(Tmp.v1.set(lastPosition)));
				setPosition(Tmp.v4.x, Tmp.v4.y);
				
				super.act(delta);
			}
			
			@Override
			public void draw(){
				if(fade || (shown = Mathf.dst2(Core.graphics.getWidth(), Core.graphics.getHeight()) > Mathf.dst2(Core.graphics.getWidth() / 2f, Core.graphics.getHeight() / 2f, x + width / 2f, y + height / 2f))) super.draw();
			}
		};
	}
	
	public static void init(){
		added.clear();
		root.clear();
		root.remove();
		GII_HUD.root.addChildAt(0, root);
	}
	
	public synchronized static void update(){
		if(Groups.unit.isEmpty())return;
		Groups.unit.copy(tmpSeq);
		tmpSeq.sortComparing(Unit::id);
		for(int i = tmpSeq.size - 1; i >= 0; i--){
			Unit unit = tmpSeq.get(i);
			if(unit.id == lastID)break;
			create(unit);
		}
		
		lastSize = Groups.unit.size();
	}
	
	public static void addBars(){
		init();
		
		Groups.unit.each(e -> !added.keys().toArray().contains(e.id()), UnitInfo::create);
	}
	
	public static void create(Unit unit){
		if(!GII_EventListeners.addUnit.get(unit))return;
		
		Table info = healthTable(unit);
		
		added.put(unit.id, info);
		lastID = unit.id;
		root.addChild(info);
	}
	
	public static class UnitHealthBar extends DelaySlideBar{
		public UnitHealthBar(Unit unit, Prov<Color> colorReal, Prov<CharSequence> info, Floatp valueGetter, Floatp maxValue){
			super(colorReal, info, valueGetter, maxValue);
			
			fontScale = b -> Mathf.clamp(b.getHeight() / Fonts.outline.getData().lineHeight * b.scaleY * 0.85f, 0.001f, b.scaleY);
		}
		
		public UnitHealthBar(Unit unit){
			this(unit, () -> unit.team.color, () -> (int)unit.health() + " / " + (int)unit.maxHealth(), unit::healthf, () -> 1);
		}
	}
}
