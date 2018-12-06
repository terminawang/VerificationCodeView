package com.finance.geex.verificationcodeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private VerificationCodeView verificationCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verificationCodeView = findViewById(R.id.verificationCodeView);


        //监听事件
        verificationCodeView.setOnInputEndCallBack(new VerificationCodeView.inputEndListener() {
            @Override
            public void input(String text) {

                //4个验证码输完之后的监听
                Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(String text) {

            }
        });

        //得到输入框的值
//        String text = verificationCodeView.getText();

        //清空输入框的值
//        verificationCodeView.setText("");


    }
}
