import java.math.BigInteger;

public class BBP {

	public static void main(String[] args) {
		for (long i = 0; true; i += 25) {
			String s = BBP.pi(i);
			System.out.println(i + "\t" + s);
		}
	}

	final static int D = 32; // working precision
	final static int SHIFT = (4 * D);
	final static BigInteger M = BigInteger.ONE.shiftLeft(4*D);
	final static BigInteger MASK = M.subtract(BigInteger.ONE);
	final static String padding = String.format("%0" + (D-7) + "d", 0);
	
	/**
	 * fast modular exponentiation
	 * 
	 * @param base
	 * @param exp
	 * @param mod
	 * @return
	 */
	static BigInteger modpow(long base, long exp, long mod) {
	    if (exp == 0) return BigInteger.ONE;

	    long h = ~(~0L >>> 1);
	    while ((exp & h) == 0)
	    	h >>>= 1;
	    
	    BigInteger result = BigInteger.valueOf(base);

	    while ((h >>>= 1) != 0) {
	        result = result.multiply(result).mod(BigInteger.valueOf(mod));
	        if ((exp & h) != 0)
	        	result = result.multiply(BigInteger.valueOf(base)).mod(BigInteger.valueOf(mod));
	    }
	    return result;
	}

	static BigInteger sigma(long j, long n) {
		BigInteger s = BigInteger.ZERO;
		for (long k = 0; k <= n; ++k) {
			long r = 8 * k + j;
			s = s.add(modpow(16, n - k, r).shiftLeft(SHIFT).divide(BigInteger.valueOf(r)).and(MASK));
		}

		BigInteger t, newt = BigInteger.ZERO;
		long k = n + 1;

		do {
			t = newt;
			BigInteger xp = M.shiftRight((int) ((k-n)*4));
			newt = t.add(xp.divide(BigInteger.valueOf(8 * k + j)));
			++k;
		} while (t != newt);

		return s.add(t);
	}

	/**
	 * calculates 25 hexadecimal digits of pi starting at position n.
	 * 
	 * @param n
	 * @return
	 */
	static String pi(long n) {
		--n;
		BigInteger x = (BigInteger.valueOf(4).multiply(sigma(1, n))
				.subtract(BigInteger.valueOf(2).multiply(sigma(4, n)))
				.subtract(sigma(5, n)).subtract(sigma(6, n))).and(MASK);
		
		String s = padding+x.toString(16);
		return s.substring(s.length()-D, s.length()-7);
	}
}
