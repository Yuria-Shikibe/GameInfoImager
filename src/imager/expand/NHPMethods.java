package imager.expand;

import arc.struct.ObjectIntMap;
import mindustry.content.StatusEffects;
import mindustry.game.SpawnGroup;
import mindustry.gen.Payloadc;
import mindustry.type.UnitType;

import static mindustry.Vars.state;

public class NHPMethods{
	public static float maxWaveHealth = 0;
	public static float initialWaveShield = 0;
	public static ObjectIntMap<UnitType> bosses = new ObjectIntMap<>();
	public static ObjectIntMap<UnitType> payloadUnits = new ObjectIntMap<>();
	
	public static ObjectIntMap<UnitType> willSpawnNextWave(){
		ObjectIntMap<UnitType> toSpawn = new ObjectIntMap<>();
		
		maxWaveHealth = initialWaveShield = 0;
		bosses.clear();
		payloadUnits.clear();
		for(SpawnGroup group : state.rules.spawns){
			if(group.type == null) continue;
			
			int spawned = group.getSpawned(state.wave - 1);
			if(spawned == 0)continue;
			maxWaveHealth += group.type.health * spawned;
			initialWaveShield += group.getShield(state.wave - 1);
			if(group.effect == StatusEffects.boss)bosses.increment(group.type, spawned);
			if(group.payloads != null && group.type instanceof Payloadc){
				for(UnitType type : group.payloads){
					payloadUnits.increment(type, spawned);
				}
			}
			toSpawn.increment(group.type, spawned);
		}
		
		return toSpawn;
	}
}
