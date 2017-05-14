package com.example.csc.helloworld2;

        import android.animation.Animator;
        import android.animation.AnimatorListenerAdapter;
        import android.annotation.TargetApi;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.pm.PackageManager;
        import android.support.annotation.NonNull;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.app.LoaderManager.LoaderCallbacks;

        import android.content.CursorLoader;
        import android.content.Loader;
        import android.database.Cursor;
        import android.net.Uri;
        import android.os.AsyncTask;

        import android.os.Build;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.ArrayAdapter;
        import android.widget.AutoCompleteTextView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;
        import java.util.ArrayList;
        import java.util.List;

        import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity //implements LoaderCallbacks<Cursor>
 {
    String code;
    String id_text;
    String password_text;
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

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private MyWebRequestReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SEND);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        registerReceiver(receiver, filter);

        //edit text id 가져오기
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button); //sign in button click
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //id와 password가 맞는지 확인하기 위해 데이터 요청하기기
                dataRequest();
            }
        });

        Button mEmailSignUpButton = (Button)findViewById(R.id.email_sign_up_button); //sign up button click
        mEmailSignUpButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                //가입 화면으로 넘어가기
                Intent it = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(it);
                finish();
            }
        });
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
        if(resBodySize!=0) {//login성공 - body 없음
            resBody = common_receive.split("\n")[3];
        }
    }
    @Override
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
                Intent it = new Intent(LoginActivity.this, MainActivity.class);
                it.putExtra("id", id_text);
                startActivity(it);
                finish();
            }else{//실패 -에러 메시지 출력
                Toast toast = Toast.makeText(LoginActivity.this,resBody,Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void dataRequest(){//login 데이터 확인하기 위한 dataRequest
        code = "2";
        id_text = mEmailView.getText().toString();
        password_text = mPasswordView.getText().toString();
        body = id_text+"\n"+password_text+"\n\0";
        body_size =String.valueOf(body.length()+1);
        image_size ="0";
        common_send = code+"\n"+body_size+"\n"+image_size+"\n" +id_text+"\n";
        common_send = CommonSendComplete(common_send);
        final_send = common_send+body;
        //final_send 송신 *****
        Intent it = new Intent(LoginActivity.this, HelloIntentService.class);
        it. putExtra("request", final_send);
        startService(it);
    }
}