package bstu.fit.ktilich.ssms_4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

  static final int REQUEST_PERMISSION_WRITE = 1001;
  final String FILE_NAME = "SSMS4.txt";
  final String FILE_DIR = "SSMS_4";

  TextView tvBreadcrumbs;
  ListView lstDisplay;

  String rootPath;

  ArrayList<String> explorerList;
  ArrayList<String> pathList;

  ArrayAdapter<String> expAdap;
  ArrayAdapter<String> pathAdap;

  boolean permissionGranted;

  SecretKeySpec sks = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    tvBreadcrumbs = findViewById(R.id.tvBreadcrumb);
    lstDisplay = findViewById(R.id.lvDisplay);

    explorerList = new ArrayList<>();
    pathList = new ArrayList<>();

    lstDisplay.setOnItemClickListener((parent, view, position, id) -> {
      File file = new File(pathList.get(position));

      if (file.isDirectory()) {
        if (file.canRead()) {
          getDir(pathList.get(position));
        } else {
          try {
            FileInputStream fin = null;
            fin = new FileInputStream(file);

            byte[] buffer = new byte[fin.available()];
            fin.read(buffer, 0, buffer.length);

            StringBuilder str = new StringBuilder();
            for (int i = 0; i < buffer.length; i++) {
              str.append((char) buffer[i]);
            }

            new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("[" + file.getName() + "]")
                .setMessage(str)
                .setPositiveButton("OK", null).show();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      } else {
        try {

          FileInputStream fin = null;
          ByteArrayInputStream bIn = null;
          fin = new FileInputStream(file);

          byte[] buffer = new byte[fin.available()];
          fin.read(buffer, 0, buffer.length);

          bIn = new ByteArrayInputStream(buffer);

          byte[] decodedBytes = null;
          try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            decodedBytes = c.doFinal(buffer);
          } catch (Exception e) {
            Log.e("Crypto", "AES decryption error");
          }

          Log.d( "Crypto", "[DECODED]:\n" + new String(decodedBytes) + "\n" );

          StringBuilder str = new StringBuilder();
          for (int i = 0; i < decodedBytes.length; i++) {
            str.append((char) decodedBytes[i]);
          }


          new AlertDialog.Builder(MainActivity.this)
              .setIcon(R.drawable.ic_launcher_background)
              .setTitle("[" + file.getName() + "]")
              .setMessage(str)
              .setPositiveButton("OK", null).show();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    if (checkPermissions()) {

      try {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed("any data used as random seed".getBytes());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, sr);
        sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
      } catch (Exception e) {
        Log.e("Crypto", "AES secret key spec error");
      }
      initFile();
    }


  }

  private void initFile() {
    FileOutputStream fOut = null;
    ByteArrayOutputStream bOut = null;

    try {
      File directory = new File(Environment.getExternalStorageDirectory(), FILE_DIR);
      if (!directory.exists()) {
        directory.mkdirs();
      }

      fOut = new FileOutputStream(new File(directory, FILE_NAME));

      String str = "KEK LOL CHEBUREK";
      byte[] encodedBytes = null;

      try {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, sks);
        encodedBytes = c.doFinal(str.getBytes());
      } catch (Exception e) {
        Log.e("Crypto", "RSA encryption error " + e.getMessage());
      }

      Log.d("Crypto", "[ENCODED]:\n" +
          Base64.encodeToString(encodedBytes, Base64.DEFAULT) + "\n");
      bOut = new ByteArrayOutputStream();
      bOut.write(encodedBytes);
      bOut.writeTo(fOut);


      rootPath = Environment.getExternalStorageDirectory().getPath();
      getDir(rootPath);
    } catch (Exception ex) {
      Log.d("SSMS_4", Objects.requireNonNull(ex.getMessage()));
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  private void getDir(String dirPath) {
    try {

      tvBreadcrumbs.setText("Location: " + dirPath);

      File f = new File(dirPath);
      File[] files = f.listFiles();

      if (!dirPath.equals(rootPath)) {
        explorerList.add(rootPath);
        pathList.add(rootPath);
        explorerList.add("../");
        pathList.add(f.getParent());
      }

      if (files != null) {
        for (File file : files) {
          if (!file.isHidden() && file.canRead()) {
            pathList.add(file.getPath());
            if (file.isDirectory()) {
              explorerList.add(file.getName() + "/");
            } else {
              explorerList.add(file.getName());
            }
          }
        }
      }
      /*
      File rootFolder = Environment.getExternalStorageDirectory();
      File[] filesArray = rootFolder.listFiles();
      Log.d("SSMS_4 1", "файлов: " + filesArray.length);
      for (File f: filesArray) {
        if (f.isDirectory()) Log.d("SSMS_4", "Folder: " + f);
        else if (f.isFile()) Log.d("SSMS_4", "File: " + f);
      }
      */

      expAdap = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, explorerList);
      lstDisplay.setAdapter(expAdap);
    } catch (Exception ex) {
      Log.d("SSMS_4 2", Objects.requireNonNull(ex.getMessage()));
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  private File getExternalPath(boolean mode) {
    if (mode) {
      return (new File(Environment.getExternalStorageDirectory(), FILE_NAME));
    } else {
      return (new File(super.getExternalFilesDir(null), FILE_NAME));
    }
  }

  private boolean checkPermissions() {

    if (!isExternalStorageReadable() || !isExternalStorageWriteable()) {
      Toast.makeText(this, "Внешнее хранилище не доступно", Toast.LENGTH_LONG).show();
      return false;
    }
    int permissionCheck =
        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_PERMISSION_WRITE);
      return false;
    }
    return true;
  }

  private boolean isExternalStorageWriteable() {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state);
  }

  private boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(
        state));
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == REQUEST_PERMISSION_WRITE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        permissionGranted = true;
        Toast.makeText(this, "Разрешения получены", Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, "Необходимо дать разрешения", Toast.LENGTH_LONG).show();
      }
    }
  }
}
