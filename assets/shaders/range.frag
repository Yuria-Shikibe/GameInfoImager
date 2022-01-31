#define HIGHP

#define THICK 5.8
#define LEN 8.0
#define SPACING 52.0

#define FALL 0.00225

#define STEP 2.5

#define ALLY vec4(0.63, 0.88, 0.63, 0.75)
#define HOSTILE vec4(1.0, 0.44, 0.41, 0.75)
#define BOTH vec4(0.995, 0.607, 0.209, 0.75)

uniform sampler2D u_texture;
uniform vec2 u_texsize;
uniform vec2 u_invsize;
uniform float u_time;
uniform float u_dp;
uniform vec2 u_offset;
uniform vec2 u_pos;
uniform float u_alpha;

varying vec2 v_texCoords;

void main(){
    vec2 T = v_texCoords.xy;
    vec2 coords = (T * u_texsize) + u_offset;

    float fall = 1.005 - distance(u_pos, coords) * FALL;

//    T += vec2(sin(coords.y / 3.0 + u_time / 20.0), sin(coords.x / 3.0 + u_time / 20.0)) / u_texsize;
//
    vec4 color = texture2D(u_texture, T);
    vec2 v = u_invsize;
//
    vec4 maxed =
        max(
            max(
                max(texture2D(u_texture, T + vec2(0.0, STEP) * v), texture2D(u_texture, T + vec2(0.0, -STEP) * v)
            ),
            texture2D(u_texture, T + vec2(STEP, 0.0) * v)
        ),
        texture2D(u_texture, T + vec2(-STEP, 0.0) * v)
    );

    if(color.a >= 0.001){
        if(color.r > 0.0 && color.b > 0.0)color = BOTH;
        else{
            if(color.r > 0.0)color = HOSTILE;
            else if(color.b > 0.0)color = ALLY;
        }

        color.a *= u_alpha * fall;
        color.a *= 1.0 + 0.6 * (step(mod((coords.x - coords.y) / u_dp - (LEN - THICK) / 2.0 + u_time / 4.0, SPACING), THICK));
    }

    if(texture2D(u_texture, T).a < 0.9 && color.a < 0.001){
        if(maxed.r > 0.0 && maxed.b > 0.0)maxed = BOTH;
        else{
            if(maxed.r > 0.0)maxed = HOSTILE;
            else if(maxed.b > 0.0)maxed = ALLY;
        }

        maxed.a *= fall * (u_alpha * 3 + 1.0) / 2.2;

        gl_FragColor = maxed;
    }else{
        gl_FragColor = color;
    }

    gl_FragColor *= 1.12 + fall / 6;
}
