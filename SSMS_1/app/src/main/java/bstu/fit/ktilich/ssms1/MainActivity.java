package bstu.fit.ktilich.ssms1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  final private String LOGIN_SUC = "admin";
  final private String PASSWORD_SUC = "admin";
  public int inputCount, removeCount;
  int countAuthTry = 3;
  EditText edtLogin, edtPassword;
  Button btnLogin;
  int[] arr = {2, 1, 5};
  private String password = "";
  private String login;
  private TextWatcher watcher, removeWatcher;
  private boolean isUpdate = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    edtLogin = findViewById(R.id.edtLogin);
    edtPassword = findViewById(R.id.edtPassword);
    int a = arr.length;

    watcher = new TextWatcher() {

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (before > count) {
          /*Remove symbol*/
          password = password.substring(0, password.length() - (before - count));

        } else if (before < count) {
          /*Input symbol*/
          String strBefore = String.valueOf(s.charAt(s.length() - 1));
          if (!strBefore.equals("*")) {
            password += strBefore;
            String strAfter = s.toString().replace(strBefore, "*");
            edtPassword.setText(strAfter);
            edtPassword.setSelection(s.length());
          }
        }
        Log.d("TAG", start + " " + before + " " + count + " " + password);
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    };

    edtPassword.addTextChangedListener(watcher);
  }

  boolean authUser() {
    if (countAuthTry <= 1) {
      finish();
    }
    if (checkUserLogin(login) && checkUserPassword(password)) {
      return true;
    } else {
      countAuthTry--;
      return false;
    }
  }

  private boolean checkUserLogin(String login) {
    Log.d("TAG", String.valueOf(LOGIN_SUC.equals(login)));
    return LOGIN_SUC.equals(login);
  }

  private boolean checkUserPassword(String password) {
    if (!password.isEmpty()) {
      if (PASSWORD_SUC.equals(password)) {
        return true;
      } else {
        int _t = indexOfDifference(PASSWORD_SUC, password);
        Log.d("TAG", String.valueOf(_t));
        if (_t == 0) {
          return false;
        } else if (_t > 0 && ((PASSWORD_SUC.length() - password.length() == 1) || (password.length() - PASSWORD_SUC.length() == 1))) {
          if (!isUpdate) {
            countAuthTry = 5;
            Toast.makeText(this, "Количество попыток увеличено до 5", Toast.LENGTH_SHORT).show();
            isUpdate = true;
          }
          return false;
        }
      }
    }
    return false;
  }

  public void onClick_btn(View view) {
    login = edtLogin.getText().toString();
    if (authUser()) {
      edtLogin.setText("");
      edtPassword.setText("");
      password = "";
      login = "";
      Toast.makeText(this, "Success login", Toast.LENGTH_LONG).show();
    } else {
      Toast.makeText(this, "Неправильный логин или пароль\nКоличество оставшихся попыток " + countAuthTry, Toast.LENGTH_SHORT).show();
    }
  }

  public int indexOfDifference(CharSequence cs1, CharSequence cs2) {
    if (cs1 == cs2) {
      return -1;
    }
    if (cs1 == null || cs2 == null) {
      return 0;
    }
    int i;
    for (i = 0; i < cs1.length() && i < cs2.length(); ++i) {
      if (cs1.charAt(i) != cs2.charAt(i)) {
        break;
      }
    }
    if (i < cs2.length() || i < cs1.length()) {
      return i;
    }
    return -1;
  }
}

