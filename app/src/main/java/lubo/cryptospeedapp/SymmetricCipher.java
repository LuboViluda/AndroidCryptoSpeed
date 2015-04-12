package lubo.cryptospeedapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


/**
 * Created by Lubo on 11. 4. 2015.
 */

public abstract class SymmetricCipher
{
    protected String cipherName;
    protected String keyName;
    protected int keySize;
    protected Cipher cipher;
    protected SecretKey secretKey;
    protected boolean inEncryptionMode;
    protected Context appContex;

    public String getCipherName()
    {
        return cipherName;
    }

    public String getKeyName()
    {
        return keyName;
    }

    public int getKeySize()
    {
        return keySize;
    }

    public Cipher getCipher()
    {
        return cipher;
    }

    public SecretKey getSecretKey()
    {
        return secretKey;
    }

    public boolean getInEncryptionMode()
    {
        return inEncryptionMode;
    }

    SymmetricCipher(String parCipherName, String parKeyName, int parKeySize, Context parAppConntext)
    {
        cipherName = parCipherName;
        keyName = parKeyName;
        keySize = parKeySize;
        appContex = parAppConntext;

        try
        {
            cipher = Cipher.getInstance(cipherName);
            secretKey = generateKey(parKeySize, keyName);
        }
        catch (NoSuchAlgorithmException |NoSuchPaddingException ex)
        {
            ex.printStackTrace();
        }
    }

    public void startSymmetricCipherTest()
    {
        // b1 plain text, b2 cipher text, b3 plain text to compare
        byte[] b1 = new byte[CommonAuxiliaryCode.SIZE];
        byte[] b2 = null;
        byte[] b3 = null;
        long startEncryption ,endEncryption, startDecryption, endDecryption;
        double[] encTime = new double[50];
        double[] decTime = new double[50];
        double sumEncryption, sumDecryption;
        sumDecryption = sumEncryption = 0;
        // buffer for log written into device sd card
        StringBuilder bufferEncryption = new StringBuilder();
        StringBuilder bufferDecryption = new StringBuilder();
        String cipherName = this.getKeyName() + "-" + this.getKeySize();

        CommonAuxiliaryCode.generateDummyBytes(b1);
        Log.i(CommonAuxiliaryCode.TAG, cipherName + "test start");
        for(int i = 0; i < CommonAuxiliaryCode.REPETITION; i++)
        {
            this.initEncryption();
            startEncryption = System.nanoTime();
            b2 = this.encrypt(b1);
            endEncryption = System.nanoTime();

            this.initDecryption();
            startDecryption = System.nanoTime();
            b3 = this.decrypt(b2);
            endDecryption = System.nanoTime();

            if(CommonAuxiliaryCode.cmpByteArrayShowResult(b1, b3, cipherName + " attempt: " + i))
            {
                encTime[i] = (((double) endEncryption / 1000000.0) - ((double) startEncryption)/ 1000000.0);
                sumEncryption += encTime[i];
                decTime[i] =  (((double) endDecryption/ 1000000.0) - ((double) startDecryption)/ 1000000.0);
                sumDecryption += decTime[i];
                Log.i(CommonAuxiliaryCode.TAG, cipherName + " attempt : " + i + " ended successful time enc: " + encTime[i] + " dec : " + decTime[i]);
                // builds CSV buffers
                bufferDecryption.append(encTime[i] + ",");
                bufferEncryption.append(decTime[i] + ",");
                b3[0] = 0;
            }
            else
            {   // shoudn't happen, skipp test
                Log.e(CommonAuxiliaryCode.TAG, cipherName + " plain text and plain text after enc/den differs !!!");
                return;
            }
        }
        double encR =  sumEncryption / ((double) CommonAuxiliaryCode.REPETITION);
        double decR =  sumDecryption / ((double) CommonAuxiliaryCode.REPETITION);
        Log.i(CommonAuxiliaryCode.TAG, "Test " + cipherName + " by provider: " + this.getCipher().getProvider() + " ended succesfully");
        Log.i(CommonAuxiliaryCode.TAG ,"Average values: " + encR + "," + decR);
        Toast.makeText(appContex, "ENC time: " + encR + " DEC time: " + decR, Toast.LENGTH_SHORT).show();
        CommonAuxiliaryCode.writeToFile(cipherName + "." + "D" + "." + CommonAuxiliaryCode.SIZE + "x" + CommonAuxiliaryCode.REPETITION  +".csv",
                                        bufferDecryption.toString());
        CommonAuxiliaryCode.writeToFile(cipherName + "." + "E" + "." + CommonAuxiliaryCode.SIZE + "x" + CommonAuxiliaryCode.REPETITION  +".csv",
                                        bufferEncryption.toString());
    }

    public byte[] encrypt(byte[] input)
    {
        if(!inEncryptionMode)
        {
            Toast.makeText(appContex, "No in encryption mode ! encryption skipped", Toast.LENGTH_SHORT).show();
        }
        byte[] output = null;
        try
        {
            output = cipher.doFinal(input);
        } catch (IllegalBlockSizeException |BadPaddingException ex)
        {
            ex.printStackTrace();
        }
        return output;
    }

    public byte[] decrypt(byte[] input)
    {
        if(inEncryptionMode)
        {
            Toast.makeText(appContex, "No in decryption mode ! decryption skipped", Toast.LENGTH_SHORT).show();
        }
        byte [] output = null;
        try
        {
            output = cipher.doFinal(input);
        } catch (IllegalBlockSizeException|BadPaddingException ex)
        {
            ex.printStackTrace();
        }
        return output;
    }

    private static SecretKey generateKey(int keyLength, String algorithm) throws NoSuchAlgorithmException
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(keyLength, secureRandom);
        return keyGenerator.generateKey();
    }

    abstract public void initEncryption();
    abstract public void initDecryption();
}