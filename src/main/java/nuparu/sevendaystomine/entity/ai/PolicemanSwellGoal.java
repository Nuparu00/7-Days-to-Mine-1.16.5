package nuparu.sevendaystomine.entity.ai;

import java.util.EnumSet;

import net.minecraft.entity.ai.goal.Goal;
import nuparu.sevendaystomine.entity.ZombiePolicemanEntity;

public class PolicemanSwellGoal extends Goal {
	private final ZombiePolicemanEntity policeman;

	public PolicemanSwellGoal(ZombiePolicemanEntity p_i1655_1_) {
		this.policeman = p_i1655_1_;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	public boolean canUse() {
		return this.policeman.getHealth() / this.policeman.getMaxHealth() <= 0.25f;
	}

	public void start() {
		this.policeman.getNavigation().stop();
	}

	public void stop() {
	}

	public void tick() {
		if (this.policeman.getHealth() / this.policeman.getMaxHealth() > 0.25f) {
			this.policeman.setSwellDir(-1);
		} else {
			this.policeman.setSwellDir(1);
		}
	}
}
