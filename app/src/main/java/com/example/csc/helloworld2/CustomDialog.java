package com.example.csc.helloworld2;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CustomDialog extends Dialog {
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
    Activity mActivity;

    public CustomDialog(@NonNull Context context,String item_id_text,String leastPrice, String id_text) {
        super(context);
        this.id_text = id_text;
        min_price = Integer.valueOf(leastPrice);
        this.item_id_text = item_id_text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SEND);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        mActivity.registerReceiver(receiver, filter);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_custom_dialog);

        okayBtn = (Button) findViewById(R.id.dial_btn);
        price_ET = (EditText) findViewById(R.id.dial_ET);
        okayBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bidPrice = price_ET.getText().toString();
                int bidPricen = Integer.valueOf(bidPrice);
                if(bidPricen<=min_price||bidPrice==null) {//입력 받은 가격이 잘못 되었으면
                    Toast toast = Toast.makeText(getContext(), resBody, Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                //저장할 데이터
                code = "8";
                body = item_id_text + "\n" + bidPrice + "\n\0";
                body_size = String.valueOf(body.length()+1);
                image_size = "0";
                common_send = code + "\n" + body_size + "\n" + image_size + "\n" + id_text + "\n";
                common_send = CommonSendComplete(common_send);
                final_send = common_send + body;
                //final_send 수신****
                Intent sock = new Intent(mActivity, HelloIntentService.class);
                sock. putExtra("request", final_send);
                mActivity.startService(sock);

                //response 송신*****
            }
        });
    }

    public String CommonSendComplete(String common_send) {
        while (common_send.length() < 256) {
            common_send = common_send + "/0";
        }
        return common_send;
    }

    public void CommonReceive(String common_receive) {
        resType = Integer.valueOf(common_receive.split("\n")[0]);
        resBodySize = Integer.valueOf(common_receive.split("\n")[1]);
        resImageSize = Integer.valueOf(common_receive.split("\n")[2]);
        if (resBodySize != 0) {//signUp성공 - body 없음
            resBody = common_receive.split("\0")[1];
        }
    }

    public class MyWebRequestReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            respond = intent.getStringExtra("response");
            CommonReceive(respond);
            if (resType > 0) {//만약에 성공하면
                Toast toast = Toast.makeText(getContext(), "성공적으로 입찰", Toast.LENGTH_SHORT);
                toast.show();
                CustomDialog.this.cancel(); //toast띠우고 닫음
            } else {//실패 -에러 메시지 출력
                Toast toast = Toast.makeText(getContext(), resBody, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

