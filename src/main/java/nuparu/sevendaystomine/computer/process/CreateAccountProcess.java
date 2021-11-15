package nuparu.sevendaystomine.computer.process;

import net.minecraft.nbt.CompoundNBT;

public abstract class CreateAccountProcess extends TickingProcess {


	public String username = "";
	public String password = "";
	public String hint = "";

	protected boolean completed = false;

	public CreateAccountProcess() {
		super();
	}

	public CompoundNBT save(CompoundNBT nbt) {
		CompoundNBT nbt2 = super.save(nbt);
		nbt2.putString("username", username);
		nbt2.putString("password", password);
		nbt2.putString("hint", hint);
		nbt2.putBoolean("completed", completed);
		return nbt2;
	}

	public void readFromNBT(CompoundNBT nbt) {
		super.readFromNBT(nbt);
		this.username = nbt.getString("username");
		this.password = nbt.getString("password");
		this.hint = nbt.getString("hint");
		this.completed = nbt.getBoolean("completed");
	}

	@Override
	public void tick() {
		super.tick();
		if (computerTE.isRegistered()) {
			computerTE.killProcess(this);
			return;
		}
		if (completed) {
			if (!username.isEmpty()) {

				if (computerTE != null && !computerTE.isRegistered()) {
					computerTE.onAccountCreated(this);
				} else {
					computerTE.killProcess(this);
				}
			} else {
				computerTE.killProcess(this);
			}
		}

	}

}
