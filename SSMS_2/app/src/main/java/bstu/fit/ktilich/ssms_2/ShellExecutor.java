package bstu.fit.ktilich.ssms_2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ShellExecutor {

  public List<String> executer(String command) {


    Process p;
    ArrayList<String> arrayList = new ArrayList<>();
    try {
      p = Runtime.getRuntime().exec(command);
      p.waitFor();
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line = "";
      while ((line = reader.readLine()) != null) {
        arrayList.add(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return arrayList;
  }

}