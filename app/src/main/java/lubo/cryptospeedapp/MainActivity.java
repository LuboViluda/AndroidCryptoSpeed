package lubo.cryptospeedapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.spongycastle.jce.provider.BouncyCastleProvider;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;




public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    static{
        // add Spongy Castle provider
        Security.addProvider(new BouncyCastleProvider());
    }

  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void MD5(View W)
    {
        HashFunction.testHash("MD5", CommonAuxiliaryCode.SIZE, CommonAuxiliaryCode.REPETITION);
    }

    public void SHA1(View W)
    {
        HashFunction.testHash("SHA-1", CommonAuxiliaryCode.SIZE, CommonAuxiliaryCode.REPETITION);
    }

    public void encDecRC4(View W) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException
    {
        // obsolete function and implementation, will be deleted soon
        //SymmetricStreamCipher RC4 = new SymmetricStreamCipher("RC4", "RC4", 128, getApplicationContext());
        //RC4.startSymmetricCipherTest();
    }

    public void encDecARC4(View w)
    {
        SymmetricStreamCipher RC4 = new SymmetricStreamCipher("ARC4", "ARC4", 128, getApplicationContext());
        RC4.startSymmetricCipherTest();
    }

    public void encDecDESede(View W) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException
    {
        SymmetricBlockCipher desed = new SymmetricBlockCipher("DESede/CBC/NoPadding", "DESede", 168, 8, getApplicationContext());
        desed.startSymmetricCipherTest();
    }

    public void encDecAES128(View W) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException
    {
        SymmetricBlockCipher AES128 = new SymmetricBlockCipher("AES/CBC/NoPadding", "AES", 128, 16, getApplicationContext());
        AES128.startSymmetricCipherTest();
    }

    public void encDecAES256(View W)
    {
        SymmetricBlockCipher AES256 = new SymmetricBlockCipher("AES/CBC/NoPadding", "AES", 256, 16, getApplicationContext());
        AES256.startSymmetricCipherTest();
    }

    public void signVerifySHA1RSA1024(View W)
    {
        SignatureRSA.test("SHA1withRSA", 1024, getApplicationContext());
    }

    public void signVerifySHA1RSA2048(View W)
    {
        SignatureRSA.test("SHA1withRSA", 2048, getApplicationContext());
    }

    public void signVerifySHA256RSA1024(View W)
    {
        SignatureRSA.test("SHA256withRSA", 1024, getApplicationContext());
    }

    public void signVerifyRSA(View W)
    {
        SignatureRSA.test("RSA", 1024, getApplicationContext());
    }

    public void signVerifySHA256RSA2048(View W)
    {

        SignatureRSA.test("SHA256withRSA", 2048, getApplicationContext());
    }

    public void signVerifyECprime192v1(View W)
    {
        SignatureEllipticCurves.testEC("prime192v1", getApplicationContext());
    }

    public void signVerifyECprime239v1(View W)
    {
        SignatureEllipticCurves.testEC("c2pnb163v1", getApplicationContext());
    }

    public void signVerifyECsecp192k1(View W)
    {
        SignatureEllipticCurves.testEC("secp192k1", getApplicationContext());
    }

    public void signVerifyECprime256v1(View W)
    {
        SignatureEllipticCurves.testEC("prime256v1", getApplicationContext());
    }

    public void supportedAlgorithms(View w)
    {
        Toast.makeText(getApplicationContext(), "Listing supported algorithms into file", Toast.LENGTH_SHORT).show();
        Log.i(CommonAuxiliaryCode.TAG, "Start listing supported algoritmhs into file");
        StringBuilder buffer = new StringBuilder();
        Provider[] providers = Security.getProviders();
        buffer.append("SUPPORTED ALGORITHMS !!!" + "\n");
        for (Provider provider : providers) {
            buffer.append("CRYPTO provider: " + provider.getName() + "\n");
            Set<Provider.Service> services = provider.getServices();
            for (Provider.Service service : services) {
                buffer.append("algorithm: " + service.getAlgorithm() + "\n");
            }
        }
        CommonAuxiliaryCode.writeToFile("supportedAlg.txt", buffer.toString());
    }

}

