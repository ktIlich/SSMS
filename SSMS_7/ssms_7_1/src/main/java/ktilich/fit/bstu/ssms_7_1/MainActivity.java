package ktilich.fit.bstu.ssms_7_1;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

  final String FILE_KEY_NAME = "SSMS7_key.txt";
  final String FILE_STRING_NAME = "SSMS7_STRING.txt";
  final String FILE_DIR = "SSMS_7";

  TextView tvOriginStr, tvEncryptStr;
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


    initKeyFile();
  }

  private void initKeyFile() {
    try {
      File directory = new File(Environment.getExternalStorageDirectory(), FILE_DIR);
      if (!directory.exists()) {
        directory.mkdirs();
      }

      File keyFile = new File(directory, FILE_KEY_NAME);
      if (keyFile.exists()) {
        FileInputStream fin = new FileInputStream(keyFile);

        byte[] buffer = new byte[fin.available()];
        fin.read(buffer, 0, buffer.length);
        fin.close();

        String str = new String(buffer);

        byte[] decodedKey = Base64.decode(str, Base64.DEFAULT);

        secretKeySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        Log.i("read key", str);
      }

    } catch (Exception ex) {
      Log.d("SSMS_7 save key", Objects.requireNonNull(ex.getMessage()));
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }


  public void onClickMain_btn(View view) {
    try {
      File directory = new File(Environment.getExternalStorageDirectory(), FILE_DIR);

      FileInputStream fin = null;
      ByteArrayInputStream bIn = null;

      fin = new FileInputStream(new File(directory, FILE_STRING_NAME));

      byte[] buffer = new byte[fin.available()];
      fin.read(buffer, 0, buffer.length);

      String encryptStr = new String(buffer);
      String decryptStr = crypto(secretKeySpec, encryptStr);

      Log.d("Crypto", "[DECODED]:\n" + decryptStr + "\n");
      fin.close();

      tvOriginStr.setText(decryptStr);
      tvEncryptStr.setText(encryptStr);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String crypto(SecretKey key, String inString) {
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance("AES");

      byte[] inputByte = inString.getBytes(StandardCharsets.UTF_8);

      cipher.init(Cipher.DECRYPT_MODE, key);
      return new String(cipher.doFinal(Base64.decode(inputByte, Base64.DEFAULT)));
    } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeyException e) {
      Log.d("SSMS_7 crypto", e.getMessage());
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
      return "";
    }
  }
}
