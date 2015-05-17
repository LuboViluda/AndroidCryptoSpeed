package lubo.cryptospeedapp;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Lubo on 11. 4. 2015.
 */
public class HashFunction {
    public static void testHash(String algorithmName, int testFileSize, int repetitions)
    {
        byte[] b1 = new byte[testFileSize];
        byte[] b2 = null;
        CommonAuxiliaryCode.generateDummyBytes(b1);

        long startHash ,endHash;
        double[] hashTime = new double[repetitions];
        double hashSum = 0;
        StringBuilder buffer = new StringBuilder();
        MessageDigest md = null;

        Log.i(CommonAuxiliaryCode.TAG, algorithmName + " test start with file size: " + testFileSize + " bytes x times: " + repetitions);
        for(int i = 0; i < repetitions; i++)
        {

            try {
                md = MessageDigest.getInstance(algorithmName);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            startHash = System.nanoTime();
            md.update(b1);
            b2 = md.digest();
            endHash = System.nanoTime();
            if(b2 != null)
            {
                hashTime[i] = (((double) endHash / 1000000.0) - ((double) startHash) / 1000000.0);
                hashSum += hashTime[i];
                Log.i(CommonAuxiliaryCode.TAG, algorithmName + " attempt : " + i + " ended successful time : " + hashTime[i]);
                buffer.append(hashTime[i] + ",");
                b2 = null;
            }
        }
        double hashAvr = hashSum / ((double) repetitions);
        Log.i(CommonAuxiliaryCode.TAG, "Test " + algorithmName + " finished, avrg. time : " + hashAvr);
        Log.i(CommonAuxiliaryCode.TAG, "Test " + algorithmName + " by provider: " + md.getProvider()
                                        + "with file size: " + testFileSize + " bytes x times: " + repetitions);
        Log.i(CommonAuxiliaryCode.TAG, "ended successfully Average values: " + hashAvr);
        CommonAuxiliaryCode.writeToFile(algorithmName + "." + testFileSize + "x" + repetitions + ".csv", buffer.toString());
    }
}
