#version 150

uniform sampler2D DiffuseSampler;

uniform int Multiplier;

uniform vec2 InSize;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    float gray = 0.33 * (fragColor.r + fragColor.g + fragColor.b);
    fragColor = (texture(DiffuseSampler, texCoord)) * gray;
}