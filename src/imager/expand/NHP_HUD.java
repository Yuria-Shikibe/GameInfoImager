package imager.expand;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Tmp;
import imager.NHPlugin;
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

public class NHP_HUD{
	public static Rect viewport = new Rect();
	public static ObjectIntMap<UnitType> willBeSpawn = new ObjectIntMap<>();
	
	public static Seq<Table> waveUpdated = new Seq<>();
	
	
	
	public static Table root = new Table(){{
		visibility = () -> !Vars.state.isMenu();
		touchablility = () -> Touchable.childrenOnly;
		setFillParent(true);
	}
		
		@Override
		public void act(float delta){
			super.act(delta);
			toBack();
		}
	};
	
	public static void updateWaveShower(Table t){
		int perLine = Core.settings.getInt(NHPlugin.SHOW_WAVE_PER_LINE) + 1;
		
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
				if(Core.settings.getBool(NHPlugin.SHOW_WAVE_DETAILS)){
					t.add(Iconc.defense + " " + Core.bundle.format("nhp.ui.max-health", NHPMethods.maxWaveHealth)).row();
					t.add(Iconc.commandRally + " " + Core.bundle.format("nhp.ui.initial-shield", NHPMethods.initialWaveShield)).row();
					
					if(NHPMethods.bosses.size > 0){
						t.add(StatusEffects.boss.emoji() + " [lightgray]" + Core.bundle.get("status.boss.name") + ":").padBottom(6).row();
						t.table(table -> {
							int i = 0;
							table.left();
							for(ObjectIntMap.Entry<UnitType> e : NHPMethods.bosses){
								if(i % perLine == 0) table.row();
								table.add(new IconNumTable(e.key.fullIcon, e.value)).pad(1.5f).padRight(4).padLeft(4 * Mathf.num(i % perLine == 0));
								i++;
							}
						}).padBottom(6).row();
					}
					
					if(NHPMethods.payloadUnits.size > 0){
						t.add(Core.bundle.get("nhp.ui.payload") + ":").padBottom(6).row();
						t.table(table -> {
							int i = 0;
							table.left();
							for(ObjectIntMap.Entry<UnitType> e : NHPMethods.payloadUnits){
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
			willBeSpawn = NHPMethods.willSpawnNextWave();
			waveUpdated.each(NHP_HUD::updateWaveShower);
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
			
			willBeSpawn = NHPMethods.willSpawnNextWave();
			
			if(Vars.state.rules.waves && Vars.spawner.getSpawns().any()){
				for(Tile tile : Vars.spawner.getSpawns()){
					Table shower = new Table(Tex.buttonEdge3){{
						updateWaveShower(this);
						touchable = Touchable.enabled;
						update(() -> {
							Tmp.v1.set(Core.camera.project(tile.drawx(), tile.drawy()));
							setPosition(Tmp.v1.x - width / 2, Tmp.v1.y - height / 2);
							visible(() -> Core.settings.getBool(NHPlugin.SHOW_WAVE_INFO));
							pack();
						});
					}};
					waveUpdated.add(shower);
					root.add(shower);
					Log.info("create");
				}
			}
		});
	}
}
