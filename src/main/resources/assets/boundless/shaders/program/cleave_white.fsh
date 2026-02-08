#version 150

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulate;
uniform int Multiplier;

uniform vec2 InSize;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec3 tex = texture(DiffuseSampler, texCoord).rgb;
    float brightness = max(tex.r, max(tex.g, tex.b)) * 1.2;

    float threshold = 0.75;
    float mask = step(threshold, brightness);

    vec3 white = vec3(1.0, 1.0, 1.0);
    vec3 black = vec3(0.0, 0.0, 0.0);

    vec3 finalColor = mix(black, white, mask);

    fragColor = vec4(finalColor, 0);
}