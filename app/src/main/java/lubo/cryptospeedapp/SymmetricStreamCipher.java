package lubo.cryptospeedapp;

import android.content.Context;

import java.security.InvalidKeyException;

import javax.crypto.Cipher;

/**
 * Created by Lubo on 11. 4. 2015.
 */

public class SymmetricStreamCipher extends SymmetricCipher
{
    SymmetricStreamCipher(String parCipherName, String parKeyName, int parKeySize, Context parAppContex)
    {
        super(parCipherName, parKeyName, parKeySize, parAppContex);
    }

    public void initEncryption()
    {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        inEncryptionMode = true;
    }

    public void initDecryption()
    {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        inEncryptionMode = false;
    }
}