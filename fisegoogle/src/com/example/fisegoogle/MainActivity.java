package com.example.fisegoogle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

  Button btnActTwo;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btnActTwo = (Button) findViewById(R.id.btnActTwo);
    btnActTwo.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.btnActTwo:
    	Intent intent = new Intent(this, MapActivity.class);
    	startActivity(intent);
      // TODO Call second activity
      break;
    default:
      break;
    }
  }
}
