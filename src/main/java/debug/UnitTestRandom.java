package debug;

import util.Random;

public class UnitTestRandom {

	public static void main(String[] args) {
		for (int i=0;i<100;i++) {
			System.out.println(Random.randomInt(1, 10));
		}
	}

}
