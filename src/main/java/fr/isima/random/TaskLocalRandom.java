/**
 * @author jH@CKTheRipper
 * @date 3/4/13
 */

package fr.isima.random;

import java.util.Random;
import fr.isima.util.ArithmeticMod;
import fr.isima.util.concurrent.RandomSafeRunnable;


public final class TaskLocalRandom {

    private static final long serialVersionUID = 0x58542182FF8F14B8L;
    
    private static final double m1 = 4294967087.0;
    private static final double m2 = 4294944443.0;
    private static final double a12 = 1403580.0;
    private static final double a13n = 810728.0;
    private static final double a21 = 527612.0;
    private static final double a23n = 1370589.0;
    private static final double norm = 2.328306549295727688e-10;
    
    private static double packageSeed[] = {12345, 12345, 12345,
            12345, 12345, 12345};

    private static final double A1p76[][] = {
        {82758667.0, 1871391091.0, 4127413238.0},
        {3672831523.0, 69195019.0, 1871391091.0},
        {3672091415.0, 3528743235.0, 69195019.0}
    };
    private static final double A2p76[][] = {
        {1511326704.0, 3759209742.0, 1610795712.0},
        {4292754251.0, 1511326704.0, 3889917532.0},
        {3859662829.0, 4292754251.0, 3708466080.0}
    };
    
    
    /** Current state of the stream. For optimization concerns, only
        Cg is conserved in this implementation (no Bg or Ig).
        We do not make use of nextSeed neither. */
    public double Cg[];

    private RandomSafeRunnable myRunnable_;

    /**
     * Constructs a new stream, initializes its seed <SPAN CLASS="MATH"><I>I</I><SUB>g</SUB></SPAN>,
     *    sets <SPAN CLASS="MATH"><I>B</I><SUB>g</SUB></SPAN> and <SPAN CLASS="MATH"><I>C</I><SUB>g</SUB></SPAN> equal to <SPAN CLASS="MATH"><I>I</I><SUB>g</SUB></SPAN>, and sets its antithetic switch
     *    to <TT>false</TT>.
     *    The seed <SPAN CLASS="MATH"><I>I</I><SUB>g</SUB></SPAN> is equal to the initial seed of the package given by
     *    {@link #setPackageSeed(long[]) setPackageSeed} if this is the first stream created,
     *    otherwise it is <SPAN CLASS="MATH"><I>Z</I></SPAN> steps ahead of that of the stream most recently
     *    created in this class.
     * 
     */
    public TaskLocalRandom(RandomSafeRunnable inMyRunnable) {

        myRunnable_ = inMyRunnable;

        this.Cg = new double[6];
        Cg = packageSeed.clone();

        // perform a jump ahead according to taskId
        this.jumpAhead(myRunnable_.getTaskId());
    }

    /** Jump Ahead of N substreams */
    private void jumpAhead(int pow) {

        /** A1^N temporary matrix */
        double A1_pN[][] = {
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0},
            {-810728.0, 1403580.0, 0.0}
        };

        /** A2^N temporary matrix */
        double A2_pN[][] = {
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0},
            {-1370589.0, 0.0, 527612.0}
        };


        ArithmeticMod.matPowModM(A1p76, A1_pN, m1, pow); // (A1^(2^76))^n mod m
        ArithmeticMod.matPowModM(A2p76, A2_pN, m2, pow); // (A2^(2^76))^n mod m

        ArithmeticMod.matVecModM_low(A1_pN, Cg, Cg, m1);
        ArithmeticMod.matVecModM_high(A2_pN, Cg, Cg, m2);

    }



    /**
        Sets the seed to be used by all the streams and substreams across the tasks
        USE WITH CAUTION  and prior to any call to TaskLocalRandom constructor
     */
    public static void setPackageSeed(long[] seed) {
        // Must use long because there is no unsigned int type.
        if (seed.length < 6)
            throw new IllegalArgumentException ("Seed must contain 6 values");
        if (seed[0] == 0 && seed[1] == 0 && seed[2] == 0)
            throw new IllegalArgumentException
                    ("The first 3 values must not be 0");
        if (seed[5] == 0 && seed[3] == 0 && seed[4] == 0)
            throw new IllegalArgumentException
                    ("The last 3 values must not be 0");
        final long m1 = 4294967087L;
        if (seed[0] >= m1 || seed[1] >= m1 || seed[2] >= m1)
            throw new IllegalArgumentException
                    ("The first 3 values must be less than " + m1);
        final long m2 = 4294944443L;
        if (seed[5] >= m2 || seed[3] >= m2 || seed[4] >= m2)
            throw new IllegalArgumentException
                    ("The last 3 values must be less than " + m2);
        for (int i = 0; i < 6;  ++i)
            packageSeed[i] = seed[i];
    }


    public double next () {
        return this.nextDouble();
    }

    /**
     * Conversion from initial double value to integer the way it is done in SSJ.
      * @return integer equivalent to the next random double in the stream
     */
    public int nextInt() {
        final int lowerBound = 0;
        final int upperBound = (int) Math.pow(2, 31) - 1;

        return lowerBound + (int)(nextDouble() * (upperBound - lowerBound + 1.0));
    }

    /**
     *
     * @return next random double in the stream
     */
    public double nextDouble() {
        int k;
        double p1, p2;
        
        // Component 1
        p1 = a12 * Cg[1] - a13n * Cg[0];
        k = (int) (p1 / m1);
        p1 -= k * m1;
        if (p1 < 0.0) {
            p1 += m1;
        }
        Cg[0] = Cg[1];
        Cg[1] = Cg[2];
        Cg[2] = p1;
        
        // Component 2
        p2 = a21 * Cg[5] - a23n * Cg[3];
        k = (int) (p2 / m2);
        p2 -= k * m2;
        if (p2 < 0.0) {
            p2 += m2;
        }
        Cg[3] = Cg[4];
        Cg[4] = Cg[5];
        Cg[5] = p2;
        
        // Combination
        return ((p1 > p2) ? (p1 - p2) * norm : (p1 - p2 + m1) * norm);
    }
}
