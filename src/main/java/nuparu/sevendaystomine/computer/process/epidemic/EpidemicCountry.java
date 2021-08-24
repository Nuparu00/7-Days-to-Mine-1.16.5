package nuparu.sevendaystomine.computer.process.epidemic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.SevenDaysToMine;

public class EpidemicCountry {
	String name;
	ResourceLocation texture;

	public long population;
	// In thousands km sq
	public int area;
	public long infected;
	public long dead = 0;
	public int airports;
	public int ports;
	public boolean quarantine;
	public boolean martialLaw;

	public List<EpidemicCountry> adjacent = new ArrayList<EpidemicCountry>();
	public List<EpidemicCountry> naval = new ArrayList<EpidemicCountry>();
	public List<EpidemicCountry> air = new ArrayList<EpidemicCountry>();

	public List<String> adjacentTemp = new ArrayList<String>();
	public List<String> navalTemp = new ArrayList<String>();
	public List<String> airTemp = new ArrayList<String>();

	EpidemicProcess parent;

	protected EpidemicCountry(EpidemicProcess parent) {
		this.parent = parent;
	}

	public EpidemicCountry(String name, EpidemicProcess parent) {
		this(parent);
		this.name = name;
		this.texture = new ResourceLocation(SevenDaysToMine.MODID,
				"textures/backgrounds/epidemic/" + name.toLowerCase() + ".png");
	}

	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putString("name", name);
		nbt.putInt("area", area);
		nbt.putLong("population", population);
		nbt.putLong("infected", infected);
		nbt.putLong("dead", dead);
		nbt.putInt("ports", ports);
		nbt.putInt("airports", airports);
		nbt.putBoolean("quarantine", quarantine);
		nbt.putBoolean("martialLaw", martialLaw);

		ListNBT adjacents = new ListNBT();
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(adjacent)) {
			adjacents.add(StringNBT.valueOf(country.name));
		}

		nbt.put("adjacent", adjacents);

		ListNBT airs = new ListNBT();
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(air)) {
			airs.add(StringNBT.valueOf(country.name));
		}

		nbt.put("air", airs);

		ListNBT navals = new ListNBT();
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(naval)) {
			navals.add(StringNBT.valueOf(country.name));
		}

		nbt.put("naval", navals);

		return nbt;
	}

	public void readFromNBT(CompoundNBT nbt) {
		this.adjacent.clear();
		this.adjacentTemp.clear();

		this.air.clear();
		this.airTemp.clear();

		this.naval.clear();
		this.navalTemp.clear();

		name = nbt.getString("name");
		area = nbt.getInt("area");
		population = nbt.getLong("population");
		infected = nbt.getLong("infected");
		dead = nbt.getLong("dead");
		ports = nbt.getInt("ports");
		airports = nbt.getInt("airports");
		quarantine = nbt.getBoolean("quarantine");
		martialLaw = nbt.getBoolean("martialLaw");

		ListNBT adjacents = nbt.getList("adjacent", Constants.NBT.TAG_STRING);
		Iterator<INBT> it = adjacents.iterator();
		while (it.hasNext()) {
			INBT base = it.next();
			if (base instanceof StringNBT) {
				this.adjacentTemp.add(((StringNBT) base).getAsString());
			}
		}

		ListNBT airs = nbt.getList("air", Constants.NBT.TAG_STRING);
		it = airs.iterator();
		while (it.hasNext()) {
			INBT base = it.next();
			if (base instanceof StringNBT) {
				this.airTemp.add(((StringNBT) base).getAsString());
			}
		}

		ListNBT navals = nbt.getList("naval", Constants.NBT.TAG_STRING);
		it = navals.iterator();
		while (it.hasNext()) {
			INBT base = it.next();
			if (base instanceof StringNBT) {
				this.navalTemp.add(((StringNBT) base).getAsString());
			}
		}

		this.texture = new ResourceLocation(SevenDaysToMine.MODID,
				"textures/backgrounds/epidemic/" + name.toLowerCase() + ".png");
	}

	public void addLandAdjacency(EpidemicCountry country) {
		addLandAdjacency(country, true);
	}

	public void addNavalConnection(EpidemicCountry country) {
		addNavalConnection(country, true);
	}

	public void addAirConnection(EpidemicCountry country) {
		addAirConnection(country, true);
	}

	public void addLandAdjacency(EpidemicCountry country, boolean bothDirections) {
		this.adjacent.add(country);
		if (bothDirections) {
			country.adjacent.add(this);
		}
	}

	public void addNavalConnection(EpidemicCountry country, boolean bothDirections) {
		if (this.ports == 0 || country.ports == 0)
			return;
		this.naval.add(country);
		if (bothDirections) {
			country.naval.add(this);
		}
	}

	public void addAirConnection(EpidemicCountry country, boolean bothDirections) {
		if (this.airports == 0 || country.airports == 0)
			return;
		this.air.add(country);
		if (bothDirections) {
			country.air.add(this);
		}
	}

	/*
	 * Called when at least a one day has passed
	 */
	public void update(double delta) {
		if (infected > 0) {
			if (!parent.discovered) {
				if (ThreadLocalRandom.current().nextLong(Math.max(1, getHealthy())) == 0) {
					parent.discovered = true;
					parent.sync();
				}
			}
			if (getHealthy() > 0) {
				long deltaInfected = (long) (infected * (0.2) * (1 + (getDensity() / 100d)) * delta
						* parent.getInfectivity());
				if (deltaInfected == 0 && this.parent.getRand().nextInt(20) == 0) {
					deltaInfected = 1;
				}
				if (deltaInfected > getHealthy()) {
					deltaInfected = getHealthy();
				}
				infected += deltaInfected;

			}
			if (getAlive() > 0) {
				double d = infected / 2d * parent.getLethality() * delta
						* (this.parent.getRand().nextFloat() * 0.75 + 0.5);
				long deltaDead = (long) (d);
				if (deltaDead == 0 && this.parent.getRand().nextInt(20) == 0) {
					deltaDead = (long) (d * this.parent.getRand().nextFloat() * 1000);
				}
				if (deltaDead > infected) {
					deltaDead = infected;
				}
				dead += deltaDead;
				infected -= deltaDead;

			}

			if (!quarantine) {
				for (EpidemicCountry destination : new ArrayList<EpidemicCountry>(adjacent)) {
					if (destination.quarantine || destination.getHealthy() == 0)
						continue;
					if ((5 * this.parent.getRand().nextFloat() * parent.getGlobalPopulation()) / 500 <= infected) {
						destination.infect(1);
					}
				}

				for (EpidemicCountry destination : new ArrayList<EpidemicCountry>(air)) {
					if (destination.quarantine || destination.getHealthy() == 0)
						continue;
					if ((50 * this.parent.getRand().nextFloat() * parent.getGlobalPopulation())
							/ airports <= infected) {
						destination.infect(1);
					}
				}

				for (EpidemicCountry destination : new ArrayList<EpidemicCountry>(naval)) {
					if (destination.quarantine || destination.getHealthy() == 0)
						continue;
					if ((50 * this.parent.getRand().nextFloat() * parent.getGlobalPopulation())
							/ ports <= infected) {
						destination.infect(1);
					}
				}
			}
		}
	}

	public void infect(int delta) {
		if (infected == 0 && delta > 0) {
			parent.addPoints(2);
		}
		infected += delta;
	}

	public boolean isInfected() {
		return infected > 0;
	}

	public long getHealthy() {
		return population - (infected + dead);
	}

	public long getAlive() {
		return population - dead;
	}

	public double getDensity() {
		return getAlive() / (double) (area * 1000);
	}

	public static EpidemicCountry fromNBT(CompoundNBT nbt, EpidemicProcess parent) {
		EpidemicCountry country = new EpidemicCountry(parent);
		country.readFromNBT(nbt);
		return country;
	}

}