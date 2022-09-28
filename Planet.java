package list;

import list.Planet;

/**
 * A planet with immutable fields: name: String representing name of planet,
 * cannot be null distanceFromSun: planet's distance from its sun in AU, must be
 * positive dayLength: length of planet's day in Earth hours, must be positive
 * numberOfMoons: number of moons orbiting planet, must be non-negative
 *
 */
public class Planet {
	private final String name;
	private final double distanceFromSun;
	private final double dayLength;
	private final int numberOfMoons;

	public Planet(String n, double d, double l, int num) {
		if (n == null || d <= 0 || l <= 0 || num < 0)
			throw new IllegalArgumentException("Invalid arguments");
		name = n;
		distanceFromSun = d;
		dayLength = l;
		numberOfMoons = num;
	}

	public String getName() {
		return name;
	}

	public double getDistance() {
		return distanceFromSun;
	}

	public double getDayInHours() {
		return dayLength;
	}

	public int getMoons() {
		return numberOfMoons;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Planet))
			return false;
		Planet p = (Planet) o;
		return p.name.equals(this.name) && p.distanceFromSun == this.distanceFromSun && p.dayLength == this.dayLength
				&& p.numberOfMoons == this.numberOfMoons;
	}

}
