#version 150

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulate = vec4(1.0, 0.0, 0.0, 0.5f);
uniform int Multiplier;

in vec2 texCoord;

out vec4 fragColor;

void main() {

    fragColor = texture(DiffuseSampler, texCoord) * ColorModulate * float(Multiplier);

}