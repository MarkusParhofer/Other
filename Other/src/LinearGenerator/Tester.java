package LinearGenerator;

import java.util.Arrays;

public class Tester {
	public static void main(String[] args) {

		long firstSeed = 2814749763100L;// the seed is the first entry of the sequence of pseudo random numbers

		int numberOfPseudoRandomNumbers = 5;

		LinearCongruentialGeneratorAdj firstGenerator = new LinearCongruentialGeneratorAdj(numberOfPseudoRandomNumbers,
				firstSeed);

		long[] sequenceGeneratedByTheFirstObject = firstGenerator.getRandomNumberSequence();

		System.out.println("Simulation of " + numberOfPseudoRandomNumbers + " integers with seed " + firstSeed
				+ " : " + Arrays.toString(sequenceGeneratedByTheFirstObject));

		System.out.println();

		System.out.println("First four number of the random sequence, excluded the seed:");
		/*
		 * maybe the user is not interested to have all the sequence, but only in the
		 * first numbers
		 */
		for (int i = 0; i < numberOfPseudoRandomNumbers; i++) {
			System.out.println(firstGenerator.getNextInteger());
		}
		
		long secondSeed = (long)(Long.MAX_VALUE/2.0) ; // - 1;
		
		LinearCongruentialGeneratorAdj secondGenerator = new LinearCongruentialGeneratorAdj(10000000,
				secondSeed);

		long[] sequence2 = secondGenerator.getRandomNumberSequence();
		System.out.println();

		System.out.println("First four number of the random sequence, excluded the seed:");
		for (int i = 0; i < numberOfPseudoRandomNumbers; i++) {
			System.out.println(secondGenerator.getNextInteger());
		}
		
		System.out.println(sequence2[1] == Long.MAX_VALUE - 44); // mod = max, a = max-100, c=6, seed = max//2
		//System.out.println(sequence2[2] == Long.MAX_VALUE - 10594); // mod = max, a = max-100, c=6, seed = max-1
	}
}
