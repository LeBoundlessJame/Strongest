#version 150

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulate;
uniform int Multiplier;

uniform vec2 InSize;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec3 color = texture(DiffuseSampler, texCoord).rgb;

    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    float invert = 1.0 - gray;

    fragColor = vec4(vec3(invert), 1.0);
}