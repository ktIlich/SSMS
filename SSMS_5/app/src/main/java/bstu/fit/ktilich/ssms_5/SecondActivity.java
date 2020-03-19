package bstu.fit.ktilich.ssms_5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

  WebView web;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_second);

    WebView browser = new WebView(this);
    setContentView(browser);
    Bundle bnd = getIntent().getExtras();
    if (bnd != null) {
      browser.loadUrl(bnd.getString("URL"));
    }
  }

}
