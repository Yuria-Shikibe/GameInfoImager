#define HIGHP

#define FALL 0.0036

uniform sampler2D u_texture;
uniform vec2 u_texsize;
uniform vec2 u_offset;
uniform vec2 u_pos;
uniform vec2 u_mouse_offset;

varying vec2 v_texCoords;

void main(){
    vec2 T = v_texCoords.xy * u_texsize;
    vec2 coords = T + u_offset;

    vec4 color = texture2D(u_texture, v_texCoords);

    color.a *= 1.35 - distance(u_pos, T) * FALL;

    gl_FragColor = color;
}
