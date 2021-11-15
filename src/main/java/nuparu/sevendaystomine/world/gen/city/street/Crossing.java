package nuparu.sevendaystomine.world.gen.city.street;

import net.minecraft.util.Direction;
import nuparu.sevendaystomine.world.gen.city.City;

import java.util.ArrayList;
import java.util.Iterator;

public class Crossing {
    private ArrayList<Street> streets = new ArrayList<Street>();

    private City city;

    public Crossing(City city) {
        this.setCity(city);
    }

    public void addStreet(Street street) {
        streets.add(street);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Street> getStreets() {
        return (ArrayList<Street>) streets.clone();
    }

    public int getStreetsCont() {
        return streets.size();
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Street getByyFacing(Direction facing, Street exclude) {
        for (Iterator<Street> iterator = streets.iterator(); iterator.hasNext();) {
            Street street = iterator.next();
            if(exclude != null && exclude == street)continue;
            if (street.facing == facing)
                return street;
        }
        return null;
    }
    public boolean isStreetInDirection(Direction facing, Street exclude) {
        for (Iterator<Street> iterator = streets.iterator(); iterator.hasNext();) {
            Street street = iterator.next();
            if(exclude != null && exclude == street)continue;
            if (street.facing == facing)
                return true;
        }
        return false;
    }

}
