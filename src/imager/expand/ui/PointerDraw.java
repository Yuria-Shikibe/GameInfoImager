package imager.expand.ui;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import imager.content.NHPColor;
import imager.expand.GII_EventListeners;
import imager.expand.GII_HUD;
import mindustry.Vars;
import mindustry.entities.Sized;
import mindustry.entities.abilities.ShieldArcAbility;
import mindustry.gen.Building;
import mindustry.gen.Iconc;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class PointerDraw{
	public static Sized last, cur;
	
	public static void update(){
		cur = GII_Vars.currentUnit == null ? GII_Vars.currentBuilding : GII_Vars.currentUnit;
		
		if(cur != null){
			if(cur instanceof Unit){
				Unit unit = (Unit)cur;
				
				if(cur != last){
					Table table = new Table(Styles.black5){{
						color.a = 0;
						//				margin(6f);
						
						ShieldArcAbility shield = null;
						for(int i = 0; i < unit.abilities().length; i++){
							if(unit.abilities[i] instanceof ShieldArcAbility){
								shield = (ShieldArcAbility)unit.abilities[i];
								break;
							}
						}
						ShieldArcAbility finalShield = shield;
						
						if(shield != null){
							UnitInfo.UnitHealthBar barArc = new UnitInfo.UnitHealthBar(() -> NHPColor.lightSkyFront, () -> Iconc.upOpen + " : " + (finalShield.data < 0 ? "ARC SHIELD DOWNED" : (int)finalShield.data), () -> finalShield.data, () -> finalShield.max);
							
							barArc.blinkable = true;
							barArc.rootColor = NHPColor.lightSky;
							barArc.blinkColor = NHPColor.lightSkyFront;
							barArc.blinked = true;
							add(barArc).grow().padBottom(1.5f).row();
						}
						
						UnitInfo.UnitHealthBar bar = new UnitInfo.UnitHealthBar(() -> Pal.lancerLaser, () -> Iconc.commandRally + " : " + (unit.shield() < 0 ? "SHIELD DOWNED" : (int)unit.shield()), unit::shield, () -> Math.max(unit.shield(), 100000));
						
						bar.blinkable = true;
						bar.rootColor = Color.royal;
						bar.blinkColor = Pal.lancerLaser;
						bar.blinked = true;
						add(bar).grow().padBottom(1.5f).row();
						
						UnitInfo.UnitHealthBar bar2 = new UnitInfo.UnitHealthBar(() -> unit.team.color, () -> Iconc.add + " : " + (unit.health() > 0 ? ((int)unit.health() + " / " + (int)unit.maxHealth()) : "Destroyed"), unit::healthf, () -> 1);
						
						bar2.blinkable = true;
						bar2.blinkColor = Pal.redderDust;
						bar2.blinked = true;
						
						add(bar2).grow().row();
						
//						add(Iconc.defense + "Armor: " + unit.armor);
						
						
						update(() -> {
							Vec2 pos = Core.input.mouseScreen(unit.x, unit.y + unit.hitSize / 1.15f + 8);
							setPosition(pos.x, pos.y, Align.top);
							setSize(Mathf.clamp(unit.hitSize() * 3.5f, 150f, 280f) * Vars.renderer.getDisplayScale(), (22 + 11 * Mathf.num(finalShield != null)) * Vars.renderer.getDisplayScale());
							margin(1.5f * Vars.renderer.getDisplayScale());
							
							if(unit != cur && !hasActions()){
								actions(Actions.fadeOut(0.25f), Actions.remove());
							}
						});
						
						touchable = Touchable.disabled;
					}};
					
					Vec2 pos = Core.input.mouseScreen(unit.x, unit.y + unit.hitSize / 1.15f + 8);
					table.setPosition(pos.x, pos.y, Align.top);
					GII_HUD.root.addChild(table);
					table.actions(Actions.fadeIn(0.125f));
				}
			}else{
				Building unit = (Building)cur;
				
				if(cur != last){
					Table table = new Table(Styles.black5){{
						color.a = 0;
						
						
						UnitInfo.UnitHealthBar bar2 = new UnitInfo.UnitHealthBar(() -> unit.team.color, () -> Iconc.add + " : " + (unit.health() > 0 ? ((int)unit.health() + " / " + (int)unit.maxHealth()) : "Destroyed"), () -> unit.healthf(), () -> 1);
						
						bar2.blinkable = true;
						bar2.blinkColor = Pal.redderDust;
						bar2.blinked = true;
						
						add(bar2).grow();
						
//						add(Iconc.defense + "Armor: " + unit.block.armor);
						
						update(() -> {
							Vec2 pos = Core.input.mouseScreen(unit.x, unit.y + unit.block.size * Vars.tilesize + 5);
							setPosition(pos.x, pos.y, Align.top);
							setSize(Mathf.clamp(unit.hitSize() * 3.5f, 150f, 250f) * Vars.renderer.getDisplayScale(), 11 * Vars.renderer.getDisplayScale());
							margin(1.5f * Vars.renderer.getDisplayScale());
							
							if(unit != cur && !hasActions()){
								actions(Actions.fadeOut(0.25f), Actions.remove());
							}
						});
						
						touchable = Touchable.disabled;
					}};
					
					Vec2 pos = Core.input.mouseScreen(unit.x, unit.y + unit.block.size * Vars.tilesize + 5);
					table.setPosition(pos.x, pos.y, Align.top);
					GII_HUD.root.addChild(table);
					table.actions(Actions.fadeIn(0.125f));
				}
			}
		}
		
		last = cur;
	}
	
	public static void draw(){
		if(cur != null){
			if(cur instanceof Unit){
				Unit unit = (Unit)cur;
				for(GII_EventListeners.DrawPair<Unit> drawer : GII_EventListeners.signDrawer){
					if(drawer.bool.get(unit)){
						drawer.drawer.get(unit);
						Draw.reset();
						break;
					}
				}
			}else{
				Building unit = (Building)cur;
				Drawf.square(unit.x, unit.y, (unit.block.size * Vars.tilesize) / (1.7f) + 1.5f, 45, unit.team.color);
			}
		}
	}
}
