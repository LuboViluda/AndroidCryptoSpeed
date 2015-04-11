package lubo.cryptospeedapp;

import android.content.Context;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Lubo on 11. 4. 2015.
 */
public class SymmetricBlockCipher extends SymmetricCipher
{
    protected int ivLength;
    private IvParameterSpec ivSpec;

    public IvParameterSpec getIvSpec()
    {
        return ivSpec;
    }

    public int getIvLength()
    {
        return ivLength;
    }

    SymmetricBlockCipher(String parCipherName, String parKeyName, int parKeySize, int parIvLength, Context parAppContex)
    {
        super(parCipherName, parKeyName, parKeySize, parAppContex);

        ivLength = parIvLength;
        ivSpec = new IvParameterSpec(SecureRandom.getSeed(ivLength));
    }

    public void initEncryption()
    {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        inEncryptionMode = true;
    }

    public void initDecryption()
    {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        inEncryptionMode = false;
    }
}