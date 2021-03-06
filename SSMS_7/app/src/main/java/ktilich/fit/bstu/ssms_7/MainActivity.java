package ktilich.fit.bstu.ssms_7;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

  final String FILE_KEY_NAME = "SSMS7_key.txt";
  final String FILE_STRING_NAME = "SSMS7_STRING.txt";
  final String FILE_DIR = "SSMS_7";

  TextView tvOriginStr, tvEncryptStr;
  EditText edtStr;

  SecretKeySpec secretKeySpec = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ActivityCompat.requestPermissions(MainActivity.this,
        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
        1);

    ActivityCompat.requestPermissions(MainActivity.this,
        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
        1);

    tvOriginStr = findViewById(R.id.tvOriginalStr);
    tvEncryptStr = findViewById(R.id.tvEncryptStr);
    edtStr = findViewById(R.id.edtInputStr);

    try {
      SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
      sr.setSeed("any data used as random seed".getBytes());
      KeyGenerator kg = KeyGenerator.getInstance("AES");
      kg.init(128, sr);
      secretKeySpec = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
    } catch (Exception e) {
      Log.e("Crypto", "AES secret key spec error");
    }
    initKeyFile();
  }

  private void initKeyFile() {
    try {
      File directory = new File(Environment.getExternalStorageDirectory(), FILE_DIR);
      if (!directory.exists()) {
        directory.mkdirs();
      }

      File keyFile = new File(directory, FILE_KEY_NAME);
      if (!keyFile.exists()) {
        FileOutputStream fOut;

        /*String str = Base64.getEncoder().encodeToString(secretKeySpec.getEncoded());*/

        String str = new String(Base64.encode(secretKeySpec.getEncoded(), Base64.DEFAULT));

        /*return new String(Base64.encode(cipher.doFinal(inputByte), Base64.DEFAULT));*/

        fOut = new FileOutputStream(keyFile);
        fOut.write(str.getBytes());
        fOut.close();

        Log.i("Write key", str);
      } else {
        FileInputStream fin = new FileInputStream(keyFile);

        byte[] buffer = new byte[fin.available()];
        fin.read(buffer, 0, buffer.length);
        fin.close();

        String str = new String(buffer);
        /*Base64.getDecoder().decode(str);*/
        byte[] decodedKey = Base64.decode(str, Base64.DEFAULT);

        secretKeySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        Log.i("read key", str);
      }

    } catch (Exception ex) {
      Log.d("SSMS_7 save key", Objects.requireNonNull(ex.getMessage()));
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }


  public void onClickEncrypt(View view) {
    if (!edtStr.getText().toString().isEmpty()) {
      String originStr = edtStr.getText().toString();

      FileOutputStream fOut = null;
      ByteArrayOutputStream bOut = null;

      try {
        File directory = new File(Environment.getExternalStorageDirectory(), FILE_DIR);
        if (!directory.exists()) {
          directory.mkdirs();
        }

        fOut = new FileOutputStream(new File(directory, FILE_STRING_NAME));

        byte[] encodedBytes = null;

        String encryptStr = crypto(secretKeySpec, originStr);

        Log.d("SSMS_7 encryptStr", encryptStr);

        fOut.write(encryptStr.getBytes());
        fOut.close();

        tvOriginStr.setText(originStr);
        tvEncryptStr.setText(encryptStr);

      } catch (Exception ex) {
        Log.d("SSMS_7", Objects.requireNonNull(ex.getMessage()));
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
      }
    }
  }


  public String crypto(SecretKey key, String inString) {
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance("AES");

      byte[] inputByte = inString.getBytes(StandardCharsets.UTF_8);

      cipher.init(Cipher.ENCRYPT_MODE, key);
      return new String(Base64.encode(cipher.doFinal(inputByte), Base64.DEFAULT));
    } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeyException e) {
      Log.d("SSMS_7 crypto", e.getMessage());
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
      return "";
    }
  }

}

