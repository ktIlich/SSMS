package bstu.fit.ktilich.ssms_3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

  private final String FILE_NAME = "FileLab.txt";
  private final String STRING_SEPARATOR = ":";

  EditText edtTranslate, edtFword, edtSword;
  Button btnSave, btnLoad, btnTranslate;
  ListView lst;
  ArrayAdapter<String> lstAdapter;

  ArrayList<String> strArr;
  File labFile;

  boolean isTranslate = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    init();
  }

  private void init() {
    edtFword = findViewById(R.id.edtFirstWord);
    edtSword = findViewById(R.id.edtSecondWord);
    edtTranslate = findViewById(R.id.edtTranslateWord);

    btnLoad = findViewById(R.id.btnLoad);
    btnSave = findViewById(R.id.btnSave);
    btnTranslate = findViewById(R.id.btnTranslate);

    lst = findViewById(R.id.lvData);

    strArr = new ArrayList<>();

    lstAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strArr);
    lst.setAdapter(lstAdapter);

    labFile = new File(super.getFilesDir(), FILE_NAME);
    if (labFile.exists()) {
      loadFromFile();
    } else {
      try {
        labFile.createNewFile();
        Toast.makeText(this, "Файл создан", Toast.LENGTH_SHORT).show();
      } catch (Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d("Lab_3", Objects.requireNonNull(e.getMessage()));
      }
    }
  }

  public void onClick_btn(View view) {
    switch (view.getId()) {
      case R.id.btnSave: {
        saveToFile();
        break;
      }
      case R.id.btnLoad: {
        loadFromFile();
        break;
      }
      case R.id.btnTranslate: {
        translate();
        break;
      }
    }
  }

  private void translate() {
    if (!isTranslate) {
      findViewById(R.id.btnLoad).setVisibility(View.GONE);
      edtTranslate.setVisibility(View.VISIBLE);
      isTranslate = true;

      TextWatcher txtWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
          if (before < count) {
            if (strArr.isEmpty()) {
              loadFromFile();
              if (strArr.isEmpty()) {
                Log.d("Lab_3", "Файл пуст");
                Toast.makeText(MainActivity.this, "Файл пуст, поиск невохможен", Toast.LENGTH_SHORT)
                    .show();
              } else {
                for (String str : strArr) {
                  if (str.contains(s)) {
                    lst.setSelection(lstAdapter.getPosition(str));
                    lst.requestFocusFromTouch();
                  }
                }
              }
            } else {
              for (String str : strArr) {
                if (str.contains(s)) {
                  lst.setSelection(lstAdapter.getPosition(str));
                  lst.requestFocusFromTouch();
                }
              }
            }
          }
        }

        @Override public void afterTextChanged(Editable s) {

        }
      };

      edtTranslate.addTextChangedListener(txtWatcher);
    } else {
      findViewById(R.id.btnLoad).setVisibility(View.VISIBLE);
      edtTranslate.setVisibility(View.GONE);
      isTranslate = false;
      edtTranslate.setText("");
    }
  }

  private void loadFromFile() {
    try {
      InputStream inputStream = openFileInput(FILE_NAME);

      if (inputStream != null) {
        InputStreamReader isReader = new InputStreamReader(inputStream);
        BufferedReader bufReader = new BufferedReader(isReader);

        if (!strArr.isEmpty()) {
          strArr.clear();
        }
        String _str;

        while ((_str = bufReader.readLine()) != null) {
          strArr.add(_str);
        }

        isReader.close();
        lstAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strArr);
        lst.setAdapter(lstAdapter);
      }
    } catch (Exception e) {
      Log.d("Lab_3", Objects.requireNonNull(e.getMessage()));
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  private void saveToFile() {
    if (!edtSword.getText().toString().isEmpty() && !edtFword.getText().toString().isEmpty()) {
      FileOutputStream fos = null;
      try {
        fos = openFileOutput(FILE_NAME, MODE_APPEND);
        fos.write((edtFword.getText().toString()
            + STRING_SEPARATOR
            + edtSword.getText().toString()
            + "\n").getBytes());
        strArr.add(
            edtFword.getText().toString() + STRING_SEPARATOR + edtSword.getText().toString());
        lstAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        Log.d("Lab_3", "Файл сохранен");
        edtSword.setText("");
        edtFword.setText("");
        edtFword.requestFocus();
      } catch (Exception ex) {
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d("Lab_3", Objects.requireNonNull(ex.getMessage()));
      } finally {
        try {
          if (fos != null) {
            fos.close();
          }
        } catch (Exception ex) {
          Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
          Log.d("Lab_3", Objects.requireNonNull(ex.getMessage()));
        }
      }
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
      if (event.getAction() == KeyEvent.ACTION_DOWN && event.isLongPress()) {
        FileOutputStream fos = null;
        try {
          fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
          fos.write(("").getBytes());
          strArr.clear();
          lstAdapter.notifyDataSetChanged();
          Toast.makeText(this, "Файл очищен", Toast.LENGTH_SHORT).show();
          Log.d("Lab_3", "Файл очишшен");
        } catch (Exception ex) {
          Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
          Log.d("Lab_3", Objects.requireNonNull(ex.getMessage()));
        } finally {
          try {
            if (fos != null) {
              fos.close();
            }
          } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Lab_3", Objects.requireNonNull(ex.getMessage()));
          }
        }
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }
}
