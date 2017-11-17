# JYSwitchButton
### <p>滑动开关按钮</p>[我的文章：自定义view-滑动开关](http://blog.csdn.net/ha_cjy/article/details/78562121)
<img src="https://github.com/hacjy/JYSwitchButton/blob/master/JYSwitchButton/snapshot/JYSwitchButton.gif" alt="效果图"/>

1、支持自定义属性：

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- JYSwitchButton的自定义属性
     openColor：开启状态的颜色
     closeColor：关闭状态的颜色
     circleColor：滑动圆形图标的颜色
     openText：开启状态的文本
     closeText：关闭状态的文本
     openTextColor：开启状态的文本颜色
     closeTextColor：关闭状态的文本颜色
     android:textSize：字体大小
     -->
    <declare-styleable name="switchbutton">
        <attr name="openColor" format="integer"></attr>
        <attr name="closeColor" format="integer"></attr>
        <attr name="circleColor" format="integer"></attr>
        <attr name="openText" format="string"></attr>
        <attr name="closeText" format="string"></attr>
        <attr name="openTextColor" format="integer"></attr>
        <attr name="closeTextColor" format="integer"></attr>
        <attr name="textSize" format="dimension"></attr>
    </declare-styleable>
</resources>
```

2、提供的方法有：

```
 * 支持自定义颜色值setOpenColor/setCloseColor/setCircleColor;
 * 支持设置偏移量setOffset;
 * 支持设置初始状态changeState;
 * 支持获取默认状态getDefaultState;
 * 支持开启/关闭状态的监听setListener(OnSwitchStateChangeListener);
 * 支持设置滑动圆形图标的边距setCirclePadding;
 * 支持设置开启/关闭文本和颜色setOpenText、setCloseText、setOpenTextColor、setCloseTextColor
 * 支持设置文本字体大小setTextSize
```

3、使用步骤：

1. 布局文件

   ```
   <?xml version="1.0" encoding="utf-8"?>
   <LinearLayout
       xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:app="http://schemas.android.com/apk/res-auto"
       xmlns:tools="http://schemas.android.com/tools"
       xmlns:jy="http://schemas.android.com/apk/res/com.ha.cjy.jyswitchbutton"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       android:orientation="vertical">

       <com.ha.cjy.jyswitchbutton.JYSwitchButton
           android:id="@+id/btnSwitch"
           android:layout_marginTop="40dp"
           android:layout_marginLeft="40dp"
           android:layout_width="60dp"
           android:layout_height="wrap_content"
           jy:textSize="40sp"
           jy:openColor="@color/colorAccent"
           jy:closeColor="@color/colorPrimary"
           jy:circleColor="@color/colorWhite"
           jy:openText="开"
           jy:closeText="关"
           jy:openTextColor="@color/colorWhite"
           jy:closeTextColor="@color/colorWhite"/>

   </LinearLayout>
   ```

   ​

2. Activity代码

   ```
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
           //滑动开关控件
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
   ```

   ​

