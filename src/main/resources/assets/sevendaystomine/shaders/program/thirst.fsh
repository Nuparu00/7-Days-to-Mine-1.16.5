#version 110

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform vec2 BlurDir;

uniform float Time;

uniform float Radius;

void main() {
    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    float totalAlpha = 0.0;
    float totalSamples = 0.0;
	
	float actualRadius = Radius;
	
    for(float r = -actualRadius; r <= actualRadius; r += 1.0) {
        vec4 sampleValue = texture2D(DiffuseSampler, texCoord + oneTexel * r * BlurDir);

		// Accumulate average alpha
        totalAlpha = totalAlpha + sampleValue.a;
        totalSamples = totalSamples + 1.0;

		// Accumulate smoothed blur
        float strength = (1.0 - abs(r / actualRadius));
        totalStrength = totalStrength + strength;
        blurred = blurred + sampleValue;
    }
    gl_FragColor = vec4(blurred.rgb / (actualRadius * 2.0 + 1.0), totalAlpha);
}
