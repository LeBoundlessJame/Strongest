#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
out vec4 fragColor;

void main()
{
    vec2 distFromCenter = texCoord - 0.5;
    float radius = length(distFromCenter);
    vec4 color = texture(DiffuseSampler, texCoord);
    float vignette = smoothstep(0.3, 0.8, radius);
    vec3 darkRed = vec3(0.4, 0.0, 0.0);
    vec3 desaturated = vec3(dot(color.rgb, vec3(0.3, 0.6, 0.10)));
    vec3 finalColor = mix(color.rgb, desaturated * 0.5 + darkRed * 0.5, vignette);

    fragColor = vec4(finalColor, color.a) * vec4(0.75, 0.0, 0.0, 1.0f);
}