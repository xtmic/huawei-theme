#version 320 es
precision mediump float;
uniform sampler2D sTexture;
uniform float uAlpha;
in vec2 vTexCoord;

out vec4 fragColor;

void main()
{
   vec2 uv =  vTexCoord;
   fragColor = texture(sTexture, uv);
   fragColor.a *= uAlpha;
}              