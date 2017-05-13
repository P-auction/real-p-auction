package com.example.csc.helloworld2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;
    MyListAdapter myListAdapter;
    ArrayList<List_item> list_itemArrayList;

    String code;
    String sort;
    String id_text;
    String item_id_text;
    String body_size;
    String image_size;
    String common_send;
    String body;
    String final_send;
    String title;
    String leastPrice;
    String maxPrice;

    String respond;
    int resType;
    int resBodySize;
    int resImageSize;
    String resBody;
    private MyWebRequestReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SEND);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        registerReceiver(receiver, filter);

        listView = (ListView)findViewById(R.id.listview1);
        list_itemArrayList = new ArrayList<List_item>();

        Intent getIt = getIntent();
        id_text = getIt.getExtras().getString("id");
        //제일 처음에는 sort를 0이라고 함
        dataRequest("0");//dataRequest에 송신하는 부분 있음

        //response 받기

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();*/
                Intent intent = new Intent(MainActivity.this, MakeAuctionActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        if(resBodySize!=0) {//MainActivity body 있음
            resBody = common_receive.split("\n")[3];
            if(resType>0) dataReceive(resBody, resBodySize/54);
        }
    }

    public void dataRequest(String sortType){ //dataRequest
        code = "5"; //list 받아오기
        sort = sortType;
        body = sort+"\n\0";
        body_size =String.valueOf(body.length()+1);
        image_size ="0";
        common_send = code+"\n"+body_size+"\n"+image_size+"\n" +id_text+"\n";
        common_send = CommonSendComplete(common_send);
        final_send = common_send+body;
        //final_send 송신
        Intent it = new Intent(MainActivity.this, HelloIntentService.class);
        it. putExtra("request", final_send);
        startService(it);
    }

    public void dataReceive(String reBody, int num){ //dataRequest
        //5 : item_id \n title \n min_price \n max_price \n (body size = 54 * row의 개수)
        int i=0;
        num=num*4;
        //리스트 초기화
        listView.setAdapter(null);
        list_itemArrayList.clear();
        for(i=0;i<num;){
            item_id_text = reBody.split("\n")[i];
            title = reBody.split("\n")[i+1];
            leastPrice = reBody.split("\n")[i+2];
            maxPrice = reBody.split("\n")[i+3];
            i=i+4;
            list_itemArrayList.add(new List_item(title,Integer.valueOf(leastPrice),Integer.valueOf(maxPrice)));
        }
        listView.setAdapter(myListAdapter);
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
            if(resType!=0) {//만약에 성공하면
                Toast toast = Toast.makeText(MainActivity.this,"성공적으로 데이터가 업로드 되었습니다",Toast.LENGTH_SHORT);
                toast.show();
            }else{//실패 -에러 메시지 출력
                Toast toast = Toast.makeText(MainActivity.this,resBody,Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.nav_recent) {
            sort = "0";
            item.setChecked(true);
            dataRequest(sort);
            return true;
        } else if (id == R.id.nav_older) {
            sort = "1";
            item.setChecked(true);
            dataRequest(sort);
            return true;
        } else if (id == R.id.nav_cheaper) {
            sort = "2";
            item.setChecked(true);
            dataRequest(sort);
            return true;
        } else if (id == R.id.nav_expensive) {
            sort = "3";
            item.setChecked(true);
            dataRequest(sort);
            return true;
        } else if (id == R.id.nav_most_liked) {
            sort = "4";
            item.setChecked(true);
            dataRequest(sort);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_my_items) {
            return true;
        } else if (id == R.id.nav_like_list) {
            return true;
        } else if (id == R.id.nav_my_page) {
            Intent intent = new Intent(MainActivity.this, MyPage.class);
            intent.putExtra("id",id_text);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
