package com.ha.cjy.jyswitchbutton;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements JYSwitchButton.OnSwitchStateChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JYSwitchButton btnSwitch = (JYSwitchButton) findViewById(R.id.btnSwitch);
//        btnSwitch.changeState(true);
//        Log.i("state",btnSwitch.getDefaultState()+"");
//        btnSwitch.setOpenColor(Color.RED);
//        btnSwitch.setCloseColor(Color.GRAY);
//        btnSwitch.setCircleColor(Color.BLUE);
        btnSwitch.setListener(this);
    }

    @Override
    public void onSwitchStateChange(boolean isOpen) {
        Toast.makeText(this,"状态："+(isOpen?"开启":"关闭"),Toast.LENGTH_SHORT).show();
    }
}
