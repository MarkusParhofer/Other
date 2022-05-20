package LinearGenerator;

public class LinearCongruentialGeneratorAdj {

	// note: all the fields are private!
	private long[] randomNumbers;// array of long
	// upcasting necessary, the result of Math.pow(2, 48) is understood as an int
	private final long modulus =  Long.MAX_VALUE;//2814749767110L;
	private final long a =  Long.MAX_VALUE - 100;//Math.floorDiv(Long.MAX_VALUE, 2);//6553590L; // if I don't put L after the number, it will complain that is out of range
	private final long c = 6L;//11L; // automatic upcasting
	private long seed; // it will be the first entry of our pseudo random number list
	private int numberOfPseudoRandomNumbers;
	private int count = 1;

	// constructor
	public LinearCongruentialGeneratorAdj(int numberOfPseudoRandomNumbers, long seed) {
		this.numberOfPseudoRandomNumbers = numberOfPseudoRandomNumbers;
		if(seed > modulus) {
			seed %= modulus; // For convenience
		}
		this.seed = seed;
	}

	/*
	 * it generates the sequence of pseudo random numbers. It is void because the
	 * sequence is stored in the array, which is the field of the class. Private
	 * because it also gets called internally!
	 */
	private void generate() {
		// initialization! + 1 because the first one is the seed
		randomNumbers = new long[numberOfPseudoRandomNumbers + 1];
		randomNumbers[0] = seed; // the first entry is the seed: first number of the sequence
		// We assume modulus > a > c
		boolean closerToMa = modulus - a < a;
		for (int indexOfInteger = 0; indexOfInteger < numberOfPseudoRandomNumbers; indexOfInteger++) {
			long z = randomNumbers[indexOfInteger];
			long rest = c;
			long mult = a;
			if(closerToMa && modulus - z < z) {
				// For mult = mod - D1, z = mod - D2, 	D1,D2 < mod/2,  (as mod-D1-D2>0) it holds that:
				// (mult*z) % mod = ((mod-D1)(mod-D2)) % mod = ((mod-D1-D2)*mod + D1*D2) % mod = (D1*D2) % mod
				z = modulus - z;
				mult = modulus - mult;
			}
			else if(closerToMa || modulus - z < z) {
				
				if(!closerToMa) { // Swap variables if modulus - z < z
					z = a;
					mult = randomNumbers[indexOfInteger];
				}
				// For mult=mod-D with D<mod/2:
				// (mult*z) % mod = (mod*z - D*z) % mod = ((z - new z)*mod + (mod - D*lower)*new z + D*(lower*new z - z)) % mod 
				// = ((mod - D*lower)*new z + D*(lower*new z-z)) % mod
				long D = modulus - mult;
				long lower = Math.floorDiv(modulus, D);
				mult = modulus - D*lower;
				if(rest < modulus - ((lower - z % lower)*D) % modulus) {
					rest += ((lower - z % lower)*D) % modulus;
				} else {
					rest -= (modulus - ((lower - z % lower)*D) % modulus);
				}
				z = Math.floorDiv(z, lower) + 1; // doesn't matter if z%lower==0
			}
			
			// congruence = (z*mult + rest) % mod
			// idea:
			// Let T = ceil(mod/mult); F = T*mult - mod; P = floor(z/T); R = z%T;
			// Then (mult*z + rest)%mod = (mult*(T*P + R) + rest)%mod = (mult*T*P - F*P + F*P + mult*R + rest)%mod
			//							= (P*(mult*T - F) + F*P + mult*R + rest)%mod = (P*mod + F*P + mult*R + rest)%mod
			//							= (F*P + mult*R + rest)%mod     where mult*R < mod
			while(mult > 0 && z > Math.floorDiv(modulus, mult)) {
				int k = modulus % mult == 0 ? 0 : 1;
				// if mod%mult=0 --> floor(mod/mult)=ceil(mod/mult)
				
				if(rest < modulus - (z % (Math.floorDiv(modulus, mult) + k)) * mult) {
					rest += (z % (Math.floorDiv(modulus, mult) + k)) * mult;
				} else {
					rest -= (modulus - (z % (Math.floorDiv(modulus, mult) + k)) * mult);
				} // Is as (rest + z % (new z) * mult) % mod but assures no overflow
				
				z = Math.floorDiv(z, Math.floorDiv(modulus, mult) + k); // new z
				mult = (mult - modulus % mult) % mult;
				
				if(mult <= Math.floorDiv(Long.MAX_VALUE, z) || mult == 0) {
					break;
				} // As soon as mult*z <= Max --> break so that we use the % operator instead
			}
			
			if(rest < modulus - ((z * mult) % modulus)) {
				rest += (z * mult) % modulus;
			}
			else {
				rest -= (modulus - ((z * mult) % modulus));
			} // Is as (rest + z*mult) % mod but assures no overflow
			
			// rest is now congruence
			randomNumbers[indexOfInteger + 1] = rest;
		}
	}

	/*
	 * lazy initialization: the pseudo random sequence is generated only the first
	 * time one of the next two methods is called.
	 */
	
	/**
	 * getter method for the sequence of pseudo random natural numbers
	 *
	 * @return the sequence of pseudo random numbers
	 */
	public long[] getRandomNumberSequence() {
		// another use of encapsulation: we call the method generate() only once
		if (randomNumbers == null) {
			generate();
		}
		return randomNumbers.clone(); // returns the already generated sequence of numbers.
	}

	/**	
	 * @return the next number of the sequence of pseudo random numbers
	 */
	public long getNextInteger() {
		long[] sequence = getRandomNumberSequence();// it gets really generated only once
		return sequence[count++];
	}
	/**
	 * getter method for the modulus
	 *
	 * @return the modulus of the congruence that generates the pseudo random
	 *         numbers
	 */
	public long getModulus() {
		return modulus;
	}

	/**
	 * getter method for the length of the simulated sequence
	 *
	 * @return the length of the simulated sequence
	 */
	public int getNumberOfPseudoRandomNumbers() {
		return numberOfPseudoRandomNumbers;
	}
}
