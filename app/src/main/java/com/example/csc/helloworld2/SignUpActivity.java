
package com.example.csc.helloworld2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by CSC on 2017. 4. 29..
 */

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText id_ET = (EditText)findViewById(R.id.id_ET);
        final EditText password_ET = (EditText)findViewById(R.id.password_ET);
        final EditText name_ET = (EditText)findViewById(R.id.name_ET);
        final EditText department_ET = (EditText)findViewById(R.id.department_ET);
        final EditText phone_ET = (EditText)findViewById(R.id.phone_ET);

        Button signUpButton = (Button)findViewById(R.id.sign_up_btn); //sign up button click
        signUpButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String id_text = id_ET.getText().toString();
                String password_text = password_ET.getText().toString();
                String name_text = name_ET.getText().toString();
                String department_text = department_ET.getText().toString();
                String phone_text = phone_ET.getText().toString();


                //DB에 저장해야함
                Intent it = new Intent(SignUpActivity.this, LoginActivity.class);
                it.putExtra("id",id_text);
                it.putExtra("password",password_text);
                it.putExtra("name",name_text);
                it.putExtra("department",department_text);
                it.putExtra("phone",phone_text);

                startActivity(it);
                finish();
            }
        });
    }
}
