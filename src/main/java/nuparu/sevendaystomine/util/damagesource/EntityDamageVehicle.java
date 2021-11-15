package nuparu.sevendaystomine.util.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class EntityDamageVehicle extends IndirectEntityDamageSource {
	public EntityDamageVehicle(String name, Entity transmitter, Entity indirectSource) {
		super(name, transmitter, indirectSource);
		this.bypassArmor();
	}

	@Override
	public ITextComponent getLocalizedDeathMessage(LivingEntity target) {

		Entity entity = getEntity();
		LivingEntity LivingEntity1 = null;
		if (entity instanceof LivingEntity) {
			LivingEntity1 = (LivingEntity) entity;
		}
		String s = "death.attack." + this.msgId;
		if (LivingEntity1 != null) {
			if (this.getDirectEntity() != null && getDirectEntity() instanceof LivingEntity
					&& getDirectEntity().hasCustomName()) {
				String s1 = s + ".vehicle";
				return new TranslationTextComponent(s1, target.getDisplayName(), LivingEntity1.getDisplayName(),
						getDirectEntity().getDisplayName());
			} else {
				return new TranslationTextComponent(s, target.getDisplayName(), LivingEntity1.getDisplayName());
			}
		} else {
			String s1 = s + ".unknown";
			return new TranslationTextComponent(s1, target.getDisplayName());

		}

	}

}