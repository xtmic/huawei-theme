#version 320 es
precision mediump float;

const float M_PI = 3.1415926;

uniform sampler2D sTexture;
uniform float uNormedTime;
uniform float uAlpha;

in vec2 vTexCoord;
in vec2 vPos;

out vec4 fragColor;

vec3 rgb2hsv(in vec3 c){
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));
    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

// Official HSV to RGB conversion
vec3 hsv2rgb(in vec3 c){
    vec3 rgb = clamp(abs(mod(c.x*6.0+vec3(0.0, 4.0, 2.0), 6.0)-3.0)-1.0, 0.0, 1.0);
    return c.z * mix(vec3(1.0), rgb, c.y);
}

float func0(float x){
    //    return 0.636403*x*x*x-2.239064*x*x+2.602661*x; //三次函数
    return sin(x*M_PI/2.0);
}

void main() {
    vec4 c = texture(sTexture, vTexCoord);

    if (uNormedTime < 1.0) {
        vec3 hsv = rgb2hsv(c.rgb);
        float f00 = func0(uNormedTime) - 1.0 + hsv.z;
        float vNew = max(0.0, f00);
        vec3 hsvNew = vec3(hsv.xy, vNew);
        fragColor = vec4(hsv2rgb(hsvNew), 1.0);
    } else {
        fragColor = c;
    }

    fragColor.a *= uAlpha;
}