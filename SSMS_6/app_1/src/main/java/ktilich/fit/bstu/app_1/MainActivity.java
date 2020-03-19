package ktilich.fit.bstu.app_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
      case R.id.btnTask_1: {
        Intent i = new Intent();
        i.setClassName("ktilich.fit.bstu.ssms_6", "ktilich.fit.bstu.ssms_6.PrivateActivity");
        startActivity(i);
        break;
      }
    }
  }
}
