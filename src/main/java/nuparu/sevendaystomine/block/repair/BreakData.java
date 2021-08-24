package nuparu.sevendaystomine.block.repair;

import java.io.Serializable;

import net.minecraft.nbt.CompoundNBT;

public class BreakData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long lastChange;
	float state;

	public BreakData(long lastChange, float state) {
		this.lastChange = lastChange;
		this.state = state;
	}

     public float getState(){
          return this.state;
     }
     public long getLastChange(){
          return this.lastChange;
     }
     
     public BreakData setState(float state) {
    	 this.state = state;
    	 return this;
     }
     
     public BreakData addState(float state) {
    	 this.state += state;
    	 return this;
     }
     
     public BreakData setLastChange(long lastChange) {
    	 this.lastChange = lastChange;
    	 return this;
     }
     
     public CompoundNBT save(CompoundNBT nbt) {
    	 nbt.putLong("lastChange", lastChange);
    	 nbt.putFloat("state", state);
    	 return nbt;
     }
     
     public BreakData read(CompoundNBT nbt) {
    	 lastChange = nbt.getLong("lastChange");
    	 state = nbt.getFloat("state");
    	 return this;
     }
     
     public static BreakData of(CompoundNBT nbt) {
    	 return new BreakData(nbt.getLong("lastChange"),nbt.getFloat("state"));
     }
}