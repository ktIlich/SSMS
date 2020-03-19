package bstu.fit.ktilich.ssms_5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  ListView lstSites;
  ArrayList<String> sites;
  ArrayAdapter<String> sitesAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    lstSites = findViewById(R.id.lstSites);

    sites = new ArrayList<>();
    sites.add("https://www.google.com/");
    sites.add("https://yandex.by/");
    sites.add("https://metanit.com/java/android/4.6.php");
    sites.add("https://developer.android.com/");

    sitesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sites);
    lstSites.setAdapter(sitesAdapter);

    lstSites.setOnItemClickListener((parent, view, position, id) -> {
      Intent i = new Intent(getApplicationContext(), SecondActivity.class);
      i.putExtra("URL", sites.get(position));
      startActivity(i);

    });
  }
}
