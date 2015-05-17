package lubo.cryptospeedapp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    static final int SIZE = 51200000;
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
        return Arrays.equals(array1, array2);
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

    public static String readFile(String filename, Context context)
    {
        try {
            InputStream inputStream = context.openFileInput("filename");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                return stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(CommonAuxiliaryCode.TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(CommonAuxiliaryCode.TAG, "Can not read file: " + e.toString());
        }
        return null;
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
            Log.e("Exception", "Read AndroidKey Store failed in function getPrivateKeyEntry(String)" + ex.toString());
        }
        return null;
    }
}
