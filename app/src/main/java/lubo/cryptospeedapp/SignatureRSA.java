package lubo.cryptospeedapp;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

/**
 * Created by Lubo on 11. 4. 2015.
 */
public class SignatureRSA {

    public static void generateRSAkey(String alias, int keySize, Context appContext)
    {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 1);

        KeyPairGeneratorSpec.Builder builder = new KeyPairGeneratorSpec.Builder(appContext);
        builder.setAlias(alias);                        // set key alias
        try {
            builder.setKeyType("RSA");                          // set key type
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        builder.setKeySize(keySize);                        // set key size
        builder.setSubject(new X500Principal("CN=test"));   // set subject used for self-signed certificate of the generated key pair
        builder.setSerialNumber(BigInteger.ONE);            // set serial number used for the self-signed certificate of the generated key pair
        builder.setStartDate(start.getTime());              // set start of validity for the self-signed certificate of the generated key pair
        builder.setEndDate(end.getTime());                  // set end of validity used for the self-signed certificate of the generated key pair
        KeyPairGeneratorSpec spec = builder.build();
        // next two rows can throws exception NoSuchAlgorithmException|NoSuchProviderException|InvalidAlgorithmParameterException
        KeyPairGenerator kpGenerator = null;
        try {
            kpGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            kpGenerator.initialize(spec);
        } catch (NoSuchProviderException |InvalidAlgorithmParameterException |NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeyPair kp = kpGenerator.generateKeyPair();
        //
        Toast.makeText(appContext, "Key pair installed into app private keyStore successfully under alias: " + alias, Toast.LENGTH_LONG).show();
        Log.d(CommonAuxiliaryCode.TAG, "Public key and private key loaded with alias: " + alias);
    }

    public static void test(String RSAName, int keySize, Context appContex)
    {
        PublicKey publicKey = null;
        PrivateKey privateKey = null;

        KeyStore.PrivateKeyEntry privateKeyEntry = CommonAuxiliaryCode.getPrivateKeyEntry(CommonAuxiliaryCode.ALIAS);
        if(null == privateKeyEntry){
            generateRSAkey(CommonAuxiliaryCode.ALIAS, keySize, appContex);
            privateKeyEntry = CommonAuxiliaryCode.getPrivateKeyEntry(CommonAuxiliaryCode.ALIAS);
        }
        if(null == privateKeyEntry)
        {   // key generation error
            Log.e(CommonAuxiliaryCode.TAG, "Error loading and generation key pair failed");
        } else {
            publicKey = privateKeyEntry.getCertificate().getPublicKey();
            privateKey = privateKeyEntry.getPrivateKey();
        }

        java.security.Signature rsa = null;

        try {
            rsa = java.security.Signature.getInstance(RSAName);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        lubo.cryptospeedapp.Signature.testSignature(rsa, publicKey, privateKey, Integer.toString(keySize), appContex);
    }
}
