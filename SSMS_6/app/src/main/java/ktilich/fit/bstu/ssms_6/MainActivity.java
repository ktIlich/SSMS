package ktilich.fit.bstu.ssms_6;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ActivityCompat.requestPermissions(MainActivity.this,
        new String[]{Manifest.permission.CALL_PRIVILEGED},
        1);
  }

  public void btnCLick(View view) {
    switch (view.getId()) {
      case R.id.btnTask_2: {
        Intent i = new Intent();
        i.setClassName("ktilich.fit.bstu.app_1", "ktilich.fit.bstu.app_1.MainActivity");
        startActivity(i);
        break;
      }
      case R.id.btnTask_3: {
        Intent i = new Intent(this, PrivateActivity.class);
        startActivity(i);
        break;
      }

    }
  }
}
