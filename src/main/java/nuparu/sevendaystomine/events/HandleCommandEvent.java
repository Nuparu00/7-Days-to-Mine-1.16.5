package nuparu.sevendaystomine.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import nuparu.sevendaystomine.computer.process.ShellProcess;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

@OnlyIn(Dist.CLIENT)
public class HandleCommandEvent extends Event {

	public TileEntityComputer computer;
	public ShellProcess process;
	public String command;
	public String output;
	public boolean override = false;

	public HandleCommandEvent(TileEntityComputer computer, ShellProcess process, String command) {
		this.computer = computer;
		this.command = command;
		this.process = process;
		this.output = this.command;
	}

}
