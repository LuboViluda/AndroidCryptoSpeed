package lubo.cryptospeedapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

/**
 * Created by Lubo on 11. 4. 2015.
 */
public class Signature {
    // override standard common constant
    static final int SIG_SIZE = 20;
    static final int SIG_REPETITION = 50;

    // if elliptic curves are used keysize == ""
    // else is size of RSA key
    public static void testSignature(java.security.Signature sig, PublicKey publicKey, PrivateKey privateKey,  String keySize, Context appContext)
    {
        byte[] b1 =  new byte[SIG_SIZE];
        byte[] b2 = null;
        long startSign ,endSign, startVerify, endVerify;
        double[] signTime = new double[SIG_REPETITION];
        double[] verifyTime = new double[SIG_REPETITION];
        double sumSign = 0;
        double sumVerify = 0;
        StringBuilder buffer = new StringBuilder();
        CommonAuxiliaryCode.generateDummyBytes(b1);

        for(int i =0; i < SIG_REPETITION; i++)
        {
            try
            {
                sig.initSign(privateKey);
                startSign = System.nanoTime();
                sig.update(b1);
                b2 = sig.sign();
                endSign = System.nanoTime();

                boolean success;
                sig.initVerify(publicKey);
                startVerify = System.nanoTime();
                sig.update(b1);
                success = sig.verify(b2);
                endVerify = System.nanoTime();
                if (success)
                {
                    signTime[i] = (((double) endSign / 1000000.0) - ((double) startSign)/ 1000000.0);
                    sumSign += signTime[i];
                    verifyTime[i] =  (((double) endVerify / 1000000.0) - ((double) startVerify)/ 1000000.0);
                    sumVerify += verifyTime[i];
                    Log.i(CommonAuxiliaryCode.TAG, sig.getAlgorithm() + " attempt : " + i + " ended successful time sign: " + signTime[i] + " verify : " + verifyTime[i]);
                    buffer.append(signTime[i] + "," + verifyTime[i] + "\n");
                }
                else
                {   // shoudn't happen, skipp test
                    Log.e(CommonAuxiliaryCode.TAG, sig.getAlgorithm() + " plain text and plain text after enc/den differs !!!");
                    return;
                }
            }  catch (SignatureException |InvalidKeyException e)
            {
                e.printStackTrace();
            }
        }
        double encR =  sumSign / ((double) SIG_REPETITION);
        double decR =  sumVerify / ((double) SIG_REPETITION);
        buffer.append("Test " + sig.getAlgorithm() + " by provider: " + sig.getProvider() + " ended succesfully");
        buffer.append("\n Averange values: " + encR + "," + decR);
        Toast.makeText(appContext, "SIGN time: " + encR + " VERIFY time: " + decR, Toast.LENGTH_SHORT).show();
        CommonAuxiliaryCode.writeToFile(sig.getAlgorithm()+ "." + keySize + "." + SIG_SIZE + "x" + SIG_REPETITION  +".txt", buffer.toString());
    }
}
