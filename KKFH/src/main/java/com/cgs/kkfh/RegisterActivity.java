package com.cgs.kkfh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        txt_name = (TextView) findViewById(R.id.etReg_name);
        txt_phone = (TextView) findViewById(R.id.etReg_phone);
        txt_disease = (TextView) findViewById(R.id.etReg_cd);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnReg_signup) {
            String name = txt_name.getText().toString();
            String phone = txt_phone.getText().toString();
            String disease = txt_disease.getText().toString();

            if (name.isEmpty() || phone.isEmpty() || disease.isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.toast1_RegActivity), Toast.LENGTH_LONG).show();
            } else {
                SQLiteControl db = new SQLiteControl(this);
                db.getWritableDatabase();
                if (db.insertData(name, phone, disease)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast2_RegActivity), Toast.LENGTH_LONG).show();
                    finish();
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast3_RegActivity), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
