package nuparu.sevendaystomine.util.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class EntityDamageShot extends IndirectEntityDamageSource {
	public EntityDamageShot(String name, Entity transmitter, Entity indirectSource) {
		super(name, transmitter, indirectSource);
		this.setProjectile();
	}

	@Override
	public ITextComponent getLocalizedDeathMessage(LivingEntity target) {

		Entity entity = (Entity) this.getEntity();
		LivingEntity LivingEntity1 = null;
		if (entity instanceof LivingEntity) {
			LivingEntity1 = (LivingEntity) entity;
		}
		String s = "death.attack." + this.msgId;
		if (LivingEntity1 != null) {
			ItemStack stack = LivingEntity1.getMainHandItem();
			if (stack != null && !stack.isEmpty()) {
				String s1 = s + ".item";
				return new TranslationTextComponent(s1, target.getDisplayName(), LivingEntity1.getDisplayName(),
						stack.getDisplayName());
			}
			else {
				return new TranslationTextComponent(s, target.getDisplayName(), LivingEntity1.getDisplayName());
			}
		} else {
			String s1 = s + ".unknown";
			return new TranslationTextComponent(s1, target.getDisplayName());

		}

	}

}