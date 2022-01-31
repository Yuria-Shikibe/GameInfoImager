package newhorizon.content;

import arc.Core;
import arc.files.Fi;
import arc.graphics.gl.Shader;
import arc.scene.ui.layout.Scl;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.graphics.Shaders;
import mindustry.mod.Mods;
import newhorizon.NHPlugin;

public class NHPShaders{
	public static Shader range, fader;
	
	public static void init(){
		range = new ModShader("screenspace", "range"){
			@Override
			public void apply(){
				setUniformf("u_dp", Scl.scl(1f));
				setUniformf("u_time", Time.time / Scl.scl(1f));
				setUniformf("u_offset",
						Core.camera.position.x - Core.camera.width / 2,
						Core.camera.position.y - Core.camera.height / 2);
				setUniformf("u_texsize", Core.camera.width, Core.camera.height);
				setUniformf("u_invsize", 1f / Core.camera.width, 1f / Core.camera.height);
				setUniformf("u_alpha", Core.settings.getInt(NHPlugin.ALPHA, 60) / 100f);
				
				setUniformf("u_pos", Core.camera.position);
//				else setUniformf("u_pos", Core.input.mouse().sub(Core.camera.width / 2f, Core.camera.height / 2));
			}
		};
		
		fader = new ModShader("screenspace", "fader"){
			@Override
			public void apply(){
				setUniformf("u_offset",
						Core.camera.position.x - Core.camera.width / 2,
						Core.camera.position.y - Core.camera.height / 2);
				setUniformf("u_texsize", Core.camera.width, Core.camera.height);
				
				setUniformf("u_mouse_offset", Core.graphics.getWidth() - Core.camera.width, Core.graphics.getHeight() - Core.camera.height);
				
				setUniformf("u_pos", Core.input.mouse().div(Tmp.v1.set(Core.graphics.getWidth(), Core.graphics.getHeight())).scl(Core.camera.width, Core.camera.height));
			}
		};
	}
	
	public static class ModShader extends Shader{
		public ModShader(String vert, String frag){
			super(getShaderFi(vert + ".vert"), getShaderFi(frag + ".frag"));
		}
	}
	
	public static Fi getShaderFi(String file){
		Mods.LoadedMod mod = NHPlugin.MOD;
		
		Fi shaders = mod.root.child("shaders");
		if(shaders.exists()){
			if(shaders.child(file).exists())return shaders.child(file);
		}
		
		return Shaders.getShaderFi(file);
	}
}
