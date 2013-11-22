package com.cgs.kkfh;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Amnart on 22/11/2556.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    TextView txt_name;
    TextView txt_phone;
    TextView txt_disease;
    Button btn_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_sign = (Button) findViewById(R.id.btnReg_signup);
        btn_sign.setOnClickListener(this);

        txt_name = (TextView)findViewById(R.id.etReg_name);
        txt_phone = (TextView)findViewById(R.id.etReg_phone);
        txt_disease = (TextView)findViewById(R.id.etReg_cd);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnReg_signup){

        }
    }
}
