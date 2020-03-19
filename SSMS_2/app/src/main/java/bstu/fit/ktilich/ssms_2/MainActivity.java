package bstu.fit.ktilich.ssms_2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  ListView lvData;
  EditText edtCommand;
  Button btnSubmit;
  ArrayAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    lvData = findViewById(R.id.lvData);
    edtCommand = findViewById(R.id.edtCommand);
    btnSubmit = findViewById(R.id.btnSubmit);

  }

  public void onClick_btn(View view) {
    if (view.getId() == R.id.btnSubmit) {
      ShellExecutor executor = new ShellExecutor();
      String commaand = edtCommand.getText().toString();

      List<String> outputList = executor.executer(commaand);

      adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, outputList);
      lvData.setAdapter(adapter);
    }
  }

}
