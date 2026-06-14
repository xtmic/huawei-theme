#version 320 es
precision mediump float;
uniform float uAlpha;
in vec2 vTexCoord;

out vec4 fragColor;

void main()
{
   fragColor = vec4(0.0,0.0,0.0,uAlpha);
}              