package nuparu.sevendaystomine.client.sound;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.LocatableSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class PositionedLoudSound extends LocatableSound {
	public static PositionedLoudSound create(ResourceLocation soundResource, float pitch, SoundCategory category) {
		return new PositionedLoudSound(soundResource, 0.25F, pitch, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F,
				0.0F, category);
	}

	public static PositionedLoudSound create(ResourceLocation soundResource, SoundCategory category) {
		return new PositionedLoudSound(soundResource, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F,
				0.0F, category);
	}

	public static PositionedLoudSound create(ResourceLocation soundResource, float xPosition, float yPosition,
			float zPosition, SoundCategory category) {
		return new PositionedLoudSound(soundResource, 4.0F, 1.0F, false, 0, ISound.AttenuationType.LINEAR, xPosition,
				yPosition, zPosition, category);
	}

	public PositionedLoudSound(ResourceLocation soundResource, float volume, float pitch, float xPosition,
			float yPosition, float zPosition, SoundCategory category) {
		this(soundResource, volume, pitch, false, 0, ISound.AttenuationType.LINEAR, xPosition, yPosition, zPosition,
				category);
	}

	public PositionedLoudSound(ResourceLocation soundResource, float volume, float pitch, boolean repeat,
			int repeatDelay, ISound.AttenuationType attenuationType, float xPosition, float yPosition, float zPosition,
			SoundCategory category) {
		super(soundResource, category);
		this.volume = volume;
		this.pitch = pitch;
		this.x = xPosition;
		this.y = yPosition;
		this.z = zPosition;
		this.looping = repeat;
		this.delay = repeatDelay;
		this.attenuation = attenuationType;
	}
}
