
package com.example.csc.helloworld2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by CSC on 2017. 4. 29..
 */

public class SignUpActivity extends AppCompatActivity {
    int resType;
    int resBodySize;
    int resImageSize;
    String resBody;
    String respond;
    private MyWebRequestReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText id_ET = (EditText)findViewById(R.id.id_ET);
        final EditText password_ET = (EditText)findViewById(R.id.password_ET);
        final EditText mail_ET = (EditText)findViewById(R.id.sign_up_mail_ET);
        final EditText department_ET = (EditText)findViewById(R.id.department_ET);
        final EditText phone_ET = (EditText)findViewById(R.id.phone_ET);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SEND);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        registerReceiver(receiver, filter);

        Button signUpBtn = (Button)findViewById(R.id.sign_up_btn); //sign up button click
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //저장할 데이터
                String code = "1";
                String id_text = id_ET.getText().toString();
                String password_text = password_ET.getText().toString();
                String mail_text = mail_ET.getText().toString();
                String department_text = department_ET.getText().toString();
                String phone_text = phone_ET.getText().toString();
                String body = id_text+"\n"+password_text+"\n"+mail_text+"\n"+phone_text+"\n"+department_text+"\n\0";
                String body_size = String.valueOf(body.length()+1);
                String image_size = "0";

                String common_send = code+"\n"+body_size+"\n"+image_size+"\n"+id_text+"\n";
                common_send = CommonSendComplete(common_send);
                String final_send = common_send + body;
                //final_send 수신****
                Intent it = new Intent(SignUpActivity.this, HelloIntentService.class);
                it. putExtra("request", final_send);
                startService(it);

                //response 송신*****
                //commonReceive(str);
                //성공하면 login page, 실패하면 에러 메시지 출력

            }
        });
    }

    public String CommonSendComplete(String common_send){
        while(common_send.length()<256){
            common_send = common_send+"\0";
        }
        return common_send;
    }

    public void CommonReceive(String common_receive){
        resType = Integer.valueOf(common_receive.split("\n")[0]);
        resBodySize = Integer.valueOf(common_receive.split("\n")[1]);
        resImageSize = Integer.valueOf(common_receive.split("\n")[2]);
        if(resBodySize!=0) {//signUp성공 - body 없음
            resBody = common_receive.split("\n")[3];
        }
    }

    public void onDestroy(){
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    public class MyWebRequestReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            respond = intent.getStringExtra("response");
            CommonReceive(respond);
            Log.e("restype은 뭘까",String.valueOf(resType));
            if(resType>0) {//만약에 성공하면
                Toast toast1 = Toast.makeText(SignUpActivity.this,"성공적으로 가입하셨습니다",Toast.LENGTH_SHORT);
                toast1.show();
                Intent getit = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(getit);
                finish();
            }else{//실패 -에러 메시지 출력
                Toast toast = Toast.makeText(SignUpActivity.this,resBody,Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
