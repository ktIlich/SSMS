package ktilich.fit.bstu.ssms_6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PrivateActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_private);
  }

  public void btnCLick(View view) {
    Intent i = new Intent(this, PrivateActivity.class);
    i.setClassName("ktilich.fit.bstu.app_1", "ktilich.fit.bstu.app_1.PrivateActivity");
    startActivity(i);
  }
}
