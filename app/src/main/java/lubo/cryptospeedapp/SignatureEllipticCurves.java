package lubo.cryptospeedapp;

import android.content.Context;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

/**
 * Created by Lubo on 11. 4. 2015.
 */
public class SignatureEllipticCurves extends Signature{

    public static void testEC(String ecName, Context appContex){
        ECGenParameterSpec ecGenSpec = new ECGenParameterSpec(ecName); //"prime192v1"
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("ECDSA", "SC");
            kpg.initialize(ecGenSpec, new SecureRandom());
        } catch (NoSuchAlgorithmException |InvalidAlgorithmParameterException |NoSuchProviderException e) {
            e.printStackTrace();
        }
        KeyPair pair = kpg.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        java.security.Signature ec = null;
        try {
            ec = java.security.Signature.getInstance("ECDSA");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        lubo.cryptospeedapp.Signature.testSignature(ec, publicKey, privateKey, ecName, appContex);
    }
}
