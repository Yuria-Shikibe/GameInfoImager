package imager.expand;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.Tmp;
import imager.GII_Plugin;
import imager.expand.ui.IconNumTable;
import mindustry.Vars;
import mindustry.content.StatusEffects;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Iconc;
import mindustry.gen.Tex;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.world.Tile;

public class GII_HUD{
	public static Rect viewport = new Rect();
	public static ObjectIntMap<UnitType> willBeSpawn = new ObjectIntMap<>();
	
	public static Seq<Table> waveUpdated = new Seq<>();
	
	
	
	public static Group root = new WidgetGroup(){{
		visibility = () -> !Vars.state.isMenu() && Vars.ui.hudfrag.shown;
		touchablility = () -> Touchable.childrenOnly;
		setFillParent(true);
	}
		
		@Override
		public void act(float delta){
			super.act(delta);
			visible = Vars.ui.hudfrag.shown;
			toBack();
		}
	};
	
	public static void updateWaveShower(Table t){
		int perLine = Core.settings.getInt(GII_Plugin.SHOW_WAVE_PER_LINE) + 1;
		
		t.actions(Actions.fadeOut(0.4f), Actions.run(() -> {
			
			
			t.clearChildren();
			t.left();
			t.defaults().growX().fillY().pad(3).left();
			t.table(table -> {
				table.left();
				table.add(Core.bundle.format("nhp.ui.waves-info", Vars.state.wave + 1)).padRight(10);
				table.button(Icon.cancel, Styles.emptyi, () -> {
					t.actions(Actions.fadeOut(0.4f), Actions.touchable(Touchable.disabled));
				}).fill();
//				table.image().color(Color.lightGray).growX().height(3).pad(6);
			}).padBottom(6).row();
			if(willBeSpawn.size > 0){
				t.table(table -> {
					int i = 0;
					table.left();
					for(ObjectIntMap.Entry<UnitType> e : willBeSpawn.entries()){
						if(i % perLine == 0) table.row();
						table.add(new IconNumTable(e.key.fullIcon, e.value)).pad(1.5f).padRight(4).padLeft(4 * Mathf.num(i % perLine == 0));
						i++;
					}
				}).fill().padBottom(6).row();
				if(Core.settings.getBool(GII_Plugin.SHOW_WAVE_DETAILS)){
					t.add(Iconc.defense + " " + Core.bundle.format("nhp.ui.max-health", GII_Methods.maxWaveHealth)).row();
					t.add(Iconc.commandRally + " " + Core.bundle.format("nhp.ui.initial-shield", GII_Methods.initialWaveShield)).row();
					
					if(GII_Methods.bosses.size > 0){
						t.add(StatusEffects.boss.emoji() + " [lightgray]" + Core.bundle.get("status.boss.name") + ":").padBottom(6).row();
						t.table(table -> {
							int i = 0;
							table.left();
							for(ObjectIntMap.Entry<UnitType> e : GII_Methods.bosses){
								if(i % perLine == 0) table.row();
								table.add(new IconNumTable(e.key.fullIcon, e.value)).pad(1.5f).padRight(4).padLeft(4 * Mathf.num(i % perLine == 0));
								i++;
							}
						}).padBottom(6).row();
					}
					
					if(GII_Methods.payloadUnits.size > 0){
						t.add(Core.bundle.get("nhp.ui.payload") + ":").padBottom(6).row();
						t.table(table -> {
							int i = 0;
							table.left();
							for(ObjectIntMap.Entry<UnitType> e : GII_Methods.payloadUnits){
								if(i % perLine == 0) table.row();
								table.add(new IconNumTable(e.key.fullIcon, e.value)).pad(1.5f).padRight(4).padLeft(4 * Mathf.num(i % perLine == 0));
								i++;
							}
						});
					}
				}
			}else{
				t.add("@none").color(Color.lightGray).left().growX().fillY();
			}
			t.pack();
		}), Actions.visible(true), Actions.touchable(Touchable.enabled), Actions.fadeIn(0.4f));
	}
	
	public static void load(){
		Events.on(EventType.WaveEvent.class, et -> {
			willBeSpawn = GII_Methods.willSpawnNextWave();
			waveUpdated.each(GII_HUD::updateWaveShower);
		});
		
		Events.on(EventType.WorldLoadEvent.class, e -> {
			waveUpdated.each(t -> {
				t.clear();
				t.remove();
			});
			waveUpdated.clear();
			
			root.clear();
			root.remove();
			root.toBack();
			Vars.ui.hudGroup.addChildAt(0, root);
			
			willBeSpawn = GII_Methods.willSpawnNextWave();
			
			if(Vars.state.rules.waves && Vars.spawner.getSpawns().any()){
				for(Tile tile : Vars.spawner.getSpawns()){
					Table shower = new Table(Tex.buttonEdge3){{
						updateWaveShower(this);
						touchable = Touchable.enabled;
						update(() -> {
							Tmp.v1.set(Core.camera.project(tile.drawx(), tile.drawy()));
							setPosition(Tmp.v1.x - width / 2, Tmp.v1.y - height / 2);
							visible(() -> Core.settings.getBool(GII_Plugin.SHOW_WAVE_INFO));
							pack();
						});
					}};
					waveUpdated.add(shower);
					root.addChild(shower);
				}
			}
		});
	}
}
