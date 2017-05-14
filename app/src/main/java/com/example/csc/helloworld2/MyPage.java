package com.example.csc.helloworld2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class MyPage extends AppCompatActivity  {
    private MyWebRequestReceiver receiver;
    String code;
    String id_text;
    String password_text;
    String mail_text;
    String department_text;
    String phone_text;
    String body_size;
    String image_size;
    String common_send;
    String body;
    String final_send;

    String respond;
    int resType;
    int resBodySize;
    int resImageSize;
    String resBody;

    TextView id_TV;
    EditText password_ET;
    EditText mail_ET;
    EditText department_ET;
    EditText phone_ET;

    final Intent sock = new Intent(MyPage.this, HelloIntentService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SEND);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        registerReceiver(receiver, filter);

        Intent getIt = getIntent();
        id_text = getIt.getExtras().getString("id");

        //데이터를 뿌려주기 위한 request
        dataRequestDown();

        Button applyBtn = (Button) findViewById(R.id.apply_btn); //sign up button click
        applyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dataRequestUp();
            }
        });
    }

    public void dataRequestDown(){//userInfo를 다운로드 하기 위한 request
        code = "7";
        body = id_text+"\n\0";
        body_size =String.valueOf(body.length()+1);
        image_size ="0";
        common_send = code+"\n"+body_size+"\n"+image_size+"\n" +id_text+"\n";
        common_send = CommonSendComplete(common_send);
        final_send = common_send+body;
        //final send 송신(user info download를 위한 송신)
        sock. putExtra("request", final_send);
        startService(sock);
    }

    public void dataRequestUp(){//myPage 정보를 upload 할 때 쓰는 request
        code = "6";
        password_text = password_ET.getText().toString();
        mail_text = mail_ET.getText().toString();
        department_text = department_ET.getText().toString();
        phone_text = phone_ET.getText().toString();
        body = id_text+"\n"+password_text+"\n"+mail_text+"\n"+phone_text+"\n"+department_text+"\n\0";
        body_size =String.valueOf(body.length()+1);
        image_size ="0";
        common_send = code+"\n"+body_size+"\n"+image_size+"\n" +id_text+"\n";
        common_send = CommonSendComplete(common_send);
        final_send = common_send + body;

        //final send 송신(user info upload를 위한 송신)
        sock.putExtra("request",final_send);
        startService(sock);
    }

    public String CommonSendComplete(String common_send){//헤더를 완성하는 메소드(256byte로 맞추기)
        while(common_send.length()<256){
            common_send = common_send+"\0";
        }
        return common_send;
    }

    public void CommonReceive(String common_receive){//헤더랑 body 구분, 헤더 파싱
        resType = Integer.valueOf(common_receive.split("\n")[0]);
        resBodySize = Integer.valueOf(common_receive.split("\n")[1]);
        resImageSize = Integer.valueOf(common_receive.split("\n")[2]);
        if(resBodySize!=0) {//mypage upload  body 없음, download body 있음
            if(resType==7) dataRecieve(common_receive); //download가 성공했을 때만 파싱
        }else if(resType ==6){//사용자 정보 업로드 성공
            Toast toast = Toast.makeText(MyPage.this,"성공적으로 정보가 업데이트 되었습니다",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void dataRecieve(String reBody){//Respond Body 파싱
        password_text =reBody.split("\n")[4];
        mail_text = reBody.split("\n")[5];
        phone_text = reBody.split("\n")[6];
        department_text = reBody.split("\n")[7];
        dataSplendAtfirst();
    }

    public void dataSplendAtfirst(){//데이터 뿌리기
        id_TV = (TextView) findViewById(R.id.id_mypage_TV2);
        password_ET = (EditText)findViewById(R.id.password_mypage_ET);
        mail_ET = (EditText)findViewById(R.id.mail_mypage_ET);
        department_ET = (EditText)findViewById(R.id.department_mypage_ET);
        phone_ET = (EditText)findViewById(R.id.phone_mypage_ET);

        id_TV.setText(id_text);
        password_ET.setText(password_text);
        mail_ET.setText(mail_text);
        department_ET.setText(department_text);
        phone_ET.setText(phone_text);
    }

    public void onDestroy(){//receiver 파괴
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    public class MyWebRequestReceiver extends BroadcastReceiver {//respond 리스너
        @Override
        public void onReceive(Context context, Intent intent) {
            respond = intent.getStringExtra("response");
            CommonReceive(respond);
            if(resType>0) {//만약에 성공하면
                //Toast toast = Toast.makeText(MyPage.this,"성공적으로 바뀌었습니다",Toast.LENGTH_SHORT);
                //toast.show();
            }else{//실패 -에러 메시지 출력
                Toast toast = Toast.makeText(MyPage.this,resBody,Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {//메뉴에서 뒤로가기 버튼을 눌렀을 때
        switch (item.getItemId()) {
            default:
                finish();
                return true;
        }
    }
}

