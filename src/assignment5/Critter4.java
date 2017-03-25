package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Timberlon Gray
 * tg22698
 * 16235
 * Raiyan Chowdhury
 * rac4444
 * 16235
 * Slip days used: <0>
 * Fall 2016
 */

public class Critter4 extends Critter {
	private int mating_season;
	
	public Critter4() {
		mating_season = 5;
	}
	/**
	 * Critter4 has a specified "mating season." It will reproduce every 5 turns, 
	 * provided it has sufficient energy to do so. If it is not time to reproduce,
	 * it will instead attempt to walk 20% of the time, as it does not like wasting energy.
	 */
	@Override
	public void doTimeStep() {
		if (mating_season == 0 && getEnergy() >= Params.min_reproduce_energy) {
			Critter4 child = new Critter4();
			reproduce(child, Critter.getRandomInt(8));
			mating_season = 5;
		} else {
			mating_season -= 1;
			int num = Critter.getRandomInt(50);
			if (num % 5 == 0)
				walk(Critter.getRandomInt(8));
		}
	}

	/**
	 * Because this critter does not like to use too much energy, it will only
	 * fight critters half the time, unless it confronts Algae which it will always fight.
	 * If it chooses not to fight a critter, it will try to walk away before a fight starts.
	 */
	@Override
	public boolean fight(String opponent) {
		int num = Critter.getRandomInt(100);
		if (num < 50 || opponent.equals("@")) {
			return true;
		}
		walk(Critter.getRandomInt(8));
		return false;
	}
	
	public String toString() {
		return "4";
	}
	
}
