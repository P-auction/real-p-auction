package com.example.csc.helloworld2;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomDialog extends Activity {
    private MyWebRequestReceiver receiver;
    private EditText price_ET;
    private Button okayBtn;
    private String bidPrice;

    String item_id_text;
    int min_price;
    String code;
    String id_text;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dialog);

        Intent getit = getIntent();
        id_text = getit.getExtras().getString("id");
        min_price = Integer.valueOf(getit.getExtras().getString("min_price"));
        item_id_text = getit.getExtras().getString("item_id");

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SEND);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        registerReceiver(receiver, filter);

        okayBtn = (Button) findViewById(R.id.dial_btn);
        price_ET = (EditText) findViewById(R.id.dial_ET);
        okayBtn.setOnClickListener(new View.OnClickListener() {//dial_btn 클릭
            public void onClick(View v) {
                bidPrice = price_ET.getText().toString();
                int bidPricen = Integer.valueOf(bidPrice);
                if(bidPricen<=min_price||bidPrice==null) {//입력 받은 가격이 잘못 되었으면
                    Toast toast = Toast.makeText(CustomDialog.this, "가격을 다시 입력해주세요", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                //request 요청하기
                dataRequest();
            }
        });
    }

    public void dataRequest(){//서버에 데이터 request
        code = "8";
        body = item_id_text + "\n" + bidPrice + "\n\0";
        body_size = String.valueOf(body.length()+1);
        image_size = "0";
        common_send = code + "\n" + body_size + "\n" + image_size + "\n" + id_text + "\n";
        common_send = CommonSendComplete(common_send);
        final_send = common_send + body;
        //final_send 송신
        Intent sock = new Intent(CustomDialog.this, HelloIntentService.class);
        sock. putExtra("request", final_send);
        startService(sock);
    }

    public String CommonSendComplete(String common_send) {//헤더를 완성하는 메소드(256byte로 맞추기)
        while (common_send.length() < 256) {
            common_send = common_send + "\0";
        }
        return common_send;
    }


    public void CommonReceive(String common_receive) {//헤더랑 body 구분, 헤더 파싱
        resType = Integer.valueOf(common_receive.split("\n")[0]);
        resBodySize = Integer.valueOf(common_receive.split("\n")[1]);
        resImageSize = Integer.valueOf(common_receive.split("\n")[2]);
        if (resBodySize != 0) {//signUp성공 - body 없음
            resBody = common_receive.split("\n")[3];
        }
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
            if (resType > 0) {//만약에 성공하면
                Toast toast = Toast.makeText(CustomDialog.this, "성공적으로 입찰", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            } else {//실패 -에러 메시지 출력
                Toast toast = Toast.makeText(CustomDialog.this, resBody, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

