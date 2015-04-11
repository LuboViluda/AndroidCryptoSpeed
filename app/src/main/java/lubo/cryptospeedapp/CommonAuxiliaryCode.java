package lubo.cryptospeedapp;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Lubo on 11. 4. 2015.
 */
public class CommonAuxiliaryCode {
    // 10.24 MB, constant for symmetric ciphers to enc./denc.
    static final int SIZE = 10240000;
    static final int REPETITION = 50;
    static final String TAG = "lubo.cryptospeedapp";
    static final String ALIAS = "testKey";

    // generate dummy bytes
    public static void generateDummyBytes(byte[] array)
    {
        Random random = new Random();
        random.nextBytes(array);
    }

    public static boolean cmpByteArrayShowResult(byte[] array1, byte[] array2, String comparedAlg)
    {
        if (Arrays.equals(array1, array2))
        {
            //Toast.makeText(getApplicationContext(), "Test " + comparedAlg +   " succeed", Toast.LENGTH_SHORT).show();
            return true;
        } else
        {
            //Toast.makeText(getApplicationContext(), "Test " + comparedAlg + " failed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static void writeToFile(String filename, String data) {
        try {
            File outputFile = new File("/sdcard/" + filename);
            FileWriter fWriter;
            fWriter = new FileWriter(outputFile, true);
            fWriter.write(data);
            fWriter.flush();
            fWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static KeyStore.PrivateKeyEntry getPrivateKeyEntry(String alias)  {
        try
        {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            return (KeyStore.PrivateKeyEntry) ks.getEntry(alias, null);
        }
        catch(KeyStoreException |IOException |CertificateException |NoSuchAlgorithmException |UnrecoverableEntryException ex)
        {
            // print error
        }
        return null;
    }
}
