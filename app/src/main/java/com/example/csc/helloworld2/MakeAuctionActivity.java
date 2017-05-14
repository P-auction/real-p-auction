package com.example.csc.helloworld2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */
public class MakeAuctionActivity extends AppCompatActivity {
    private MyWebRequestReceiver receiver;
    String code;
    String title;
    String description;
    String id_text;
    String body_size;
    String image_size;
    String common_send;
    String body;
    String final_send;
    String auctionTime;
    String endingdate;
    String leastPrice;
    String maxPrice;

    String respond;
    int resType;
    int resBodySize;
    int resImageSize;
    String resBody;

    EditText title_ET;
    EditText description_ET;
    EditText leastPrice_ET;
    EditText maxPrice_ET;
    EditText dueDate_ET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_auction);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SEND);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        registerReceiver(receiver, filter);

        Intent getIt = getIntent();
        id_text = getIt.getExtras().getString("id");
        //editText id 받기
        title_ET = (EditText) findViewById(R.id.auction_name_ET);
        description_ET = (EditText) findViewById(R.id.description_ET);
        leastPrice_ET = (EditText) findViewById(R.id.least_price_ET);
        maxPrice_ET = (EditText) findViewById(R.id.max_price_ET);
        dueDate_ET = (EditText) findViewById(R.id.time_limit_ET);

        Button registerBtn = (Button) findViewById(R.id.register_btn); //register button click
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //request data 만들기
                dataRequest();
            }
        });
    }

    public void dataRequest(){//보낼 메시지의 data를 Request한다
        code = "3";
        //title \n explanation \n endingdate \n auctiontime \n minprice \n maxprice \n \0
        endingdate = dueDate_ET.getText().toString();
        auctionTime = getTime();
        title = title_ET.getText().toString();
        description = description_ET.getText().toString();
        endingdate = endingdate.split("/")[0] + endingdate.split("/")[1]+endingdate.split("/")[2]+"235959";
        leastPrice=leastPrice_ET.getText().toString();
        maxPrice=maxPrice_ET.getText().toString();
        body = title+"\n"+description+"\n"+endingdate+"\n"+auctionTime+"\n"+leastPrice+"\n"+maxPrice+"\n\0";
        body_size = String.valueOf(body.length()+1);
        image_size = "0";
        common_send = code + "\n" + body_size + "\n" + image_size + "\n" + id_text + "\n";
        common_send = CommonSendComplete(common_send);
        final_send = common_send + body;
        //final send 송신****
        Intent it = new Intent(MakeAuctionActivity.this, HelloIntentService.class);
        it. putExtra("request", final_send);
        startService(it);
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
        if(resBodySize!=0) {//makeAuction upload 성공. body 없음
            resBody = common_receive.split("\n")[3];
        }
    }

    public class MyWebRequestReceiver extends BroadcastReceiver {//respond 리스너
        @Override
        public void onReceive(Context context, Intent intent) {
            respond = intent.getStringExtra("response");
            CommonReceive(respond);
            if(resType>0) {//만약에 성공하면
                Toast toast = Toast.makeText(MakeAuctionActivity.this,"성공적으로 등록되었습니다",Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }else{//실패 -에러 메시지 출력
                Toast toast = Toast.makeText(MakeAuctionActivity.this,resBody,Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public String getTime(){///현재 시간을 알려준다
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdfNow.format(date);
    }

    public boolean onOptionsItemSelected(MenuItem item) {//뒤로가기 버튼을 눌렀을 때
        switch (item.getItemId()) {
            default:
                finish();
                return true;
        }
    }
}

