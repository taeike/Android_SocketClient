package com.example.taeik.android_socketclient;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends Activity{

    private ArrayList<DayDataType> dataList = new ArrayList<DayDataType>();
    private ArrayList<DayTimeDataType> timedataList = new ArrayList<DayTimeDataType>();
    private String url = "http://220.69.203.16:8087/javascript_simple.html";
    private static final String serverIP = "220.69.203.16";
    private static final int sockPort = 8088;
    SocketThread sThread;                                      // 안드로이드에서는 소케을 반드시 스레드 문맥에서 작동하게 하여야 함
    PrintWriter writer;                                       // 클라이언트로 부터 메시지를 받는 스트림
    Socket sock;                                               // 통신이 일어날 소켓 (이 프로그램에서는 틀라이언트가 1개만 접속될 것을 전게로 함
    String aLine = "";
    private BufferedReader buffRecv;
    private android.widget.Button streaming_Button;
    private android.widget.Button motorControl_Button;
    //private android.widget.Button preference_Button;
    private android.widget.Button left_Button;
    private android.widget.Button right_Button;
    //private android.widget.Button modeSet_Button;
    private android.widget.Button checkstate_Button;
    private android.widget.Button checkstate_Button2;
    private android.widget.Button exit_Button;
    private WebView webView;
    private int motorControl = 0;
    int checkDataGraph = 0;
    boolean _isBtnDown = false;
    TouchThread rth;

    enum Choices {AAA, VIDEO_OFF, LEFT, RIGHT, AUTO, MANUAL, EXIT, INTRUSION_DETECTION_MODE, SITUATION_DETECTION_MODE, PREFERENCESETTING,GET_COUNT_DATA
        ,LEFT_STOP,RIGHT_STOP,GET_TIMEDATA,VIDEO_ON}
    DataInputStream is;
    ;    // 버튼 선택 종류 명시, ordinal에 따라 번호로 변경가능 (0~ )

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        sThread = new SocketThread(serverIP, sockPort);      // 스레드 생성 및 초기화
        sThread.start();                                        //  작동시작
        setUp();//레이아웃에 필요한 위젯들을 셋팅
        setUpListener();
        rth = new TouchThread(1);
        rth.run();
        Log.d("ㅉㅉ", "ㅁㄴㅇㅁㅇㄴ");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setUp() {
        streaming_Button = (Button) findViewById(R.id.Streaming_Button);
        //modeSet_Button = (Button) findViewById(R.id.ModeSet_Button);
        //preference_Button = (Button) findViewById(R.id.Preference_Button);
        left_Button = (Button) findViewById(R.id.Left_Button);
        right_Button = (Button) findViewById(R.id.Right_Button);
        motorControl_Button = (Button) findViewById(R.id.MotorControl_Button);
        checkstate_Button = (Button) findViewById(R.id.CheckState_Button);
        webView = (WebView) findViewById(R.id.z);
        exit_Button = (Button) findViewById(R.id.Exit_button);
        checkstate_Button2 = (Button) findViewById(R.id.CheckState_Button2);
    }

    public void setUpListener() {
        //*********************StreamingListener***************************
        streaming_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (streaming_Button.getText().equals("Streaming_ON")) {
                    streaming_Button.setText("Streaming_OFF");
                    streaming_Button.setBackgroundResource(R.mipmap.on);
                    writer.println(Choices.VIDEO_ON.ordinal());

                    SystemClock.sleep(3000);

                    webView.setPadding(0, 0, 0, 0);
                    webView.setInitialScale(100);
                    webView.getSettings().setBuiltInZoomControls(false);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setLoadWithOverviewMode(true);
                    webView.getSettings().setUseWideViewPort(true);
                    webView.getSettings().setBuiltInZoomControls(true);
                    webView.getSettings().setSupportZoom(true);
                    webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    // String url ="http://220.69.203.16:8084/javascript_simple.html";
                    webView.loadUrl(url);
                } else {
                    streaming_Button.setText("Streaming_ON");
                    streaming_Button.setBackgroundResource(R.mipmap.off);
                    writer.println(Choices.VIDEO_OFF.ordinal());
                }
                SystemClock.sleep(1000);
                Log.d("Button", "스트리밍 버튼이 눌려짐");
            }
        });
        //*********************LeftButtonListener***************************
        left_Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        if (motorControl == 1) {
                            _isBtnDown = true;
                            TouchThread lth = new TouchThread(2);
                            lth.start();
                            Log.d("Button", "왼쪽 버튼이 눌려짐 끝");
                        }
                        Log.d("Button", "왼쪽 버튼이 눌려짐");
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        if (motorControl == 1) {
                            _isBtnDown = false;
                            writer.println(Choices.LEFT_STOP.ordinal());
                            SystemClock.sleep(100);
                        }
                        Log.d("Button", "왼쪽 버튼이 눌려짐 땜");
                        ;

                    default:
                        break;
                }
                return false;
            }
        });
        //*********************RightButtonListener***************************
        right_Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        if (motorControl == 1) {
                            Log.d("Button", "오른쪽 버튼이 눌려짐");
                            _isBtnDown = true;
                            TouchThread rth = new TouchThread(1);
                            rth.start();
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:

                        if (motorControl == 1) {
                            _isBtnDown = false;
                            writer.println(Choices.RIGHT_STOP.ordinal());
                            SystemClock.sleep(100);
                        }
                        Log.d("Button", "오른쪽 버튼이 눌려짐땜");
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        //********************MotorControlButtonListener*********************
        motorControl_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (motorControl_Button.getText().equals("MANUAL")) {
                    writer.println(Choices.MANUAL.ordinal());
                    motorControl_Button.setText("AUTO");
                    motorControl_Button.setBackgroundResource(R.mipmap.auto);
                    motorControl = 1;
                    Log.d("Button", "메뉴얼 버튼이 눌려짐 끝");
                } else {
                    writer.println(Choices.AUTO.ordinal());
                    motorControl_Button.setText("MANUAL");
                    motorControl_Button.setBackgroundResource(R.mipmap.manual);
                    motorControl = 0;
                    Log.d("Button", "오토 버튼이 눌려짐 끝");
                }
                SystemClock.sleep(10);
                Log.d("Button", "모터컨트롤버튼이 눌려짐");
            }
        });
        //************************ModeSetButton***************
        /*
        modeSet_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writer.println(Choices.SITUATION_DETECTION_MODE.ordinal());
                SystemClock.sleep(1000);
                Log.d("Button", "모드셋버튼이 눌려짐");
            }
        });*/
        //***********************PreferenceButton****************************
        /*
        preference_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writer.println(Choices.PREFERENCESETTING.ordinal());
                SystemClock.sleep(1000);
                Log.d("Button", "프리퍼런스컨트롤버튼이 눌려짐");
            }
        });*/
        //**********************checkState***********************************
        checkstate_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataGraph = 0;
                SystemClock.sleep(100);
                writer.println(Choices.GET_COUNT_DATA.ordinal());
                Log.d("Button", "GET_COUNT_DATA 눌려짐"+Choices.GET_COUNT_DATA.ordinal());
            }
        });
        checkstate_Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataGraph = 1;
                SystemClock.sleep(100);
                writer.println(Choices.GET_TIMEDATA.ordinal());

                Log.d("Button", "GET_TIMEDATA 눌려짐"+Choices.GET_TIMEDATA.ordinal());
            }
        });

        //***********************Exit******************************************
        exit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writer.println(Choices.EXIT.ordinal());
                SystemClock.sleep(1000);
                Log.d("Button", "프리퍼런스컨트롤버튼이 눌려짐");
            }
        });
    }

    public class SocketThread extends Thread {
        String addr;
        int port;

        public SocketThread(String addr, int port) {
            this.addr = addr;
            this.port = port;
        }

        public void run() {
            try {
                sock = new Socket(addr, port);
                writer = new PrintWriter(sock.getOutputStream(), true);
                buffRecv = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                SocketReceiverThread receiverThread = new SocketReceiverThread();
                receiverThread.start();
                while ( true ) {
                    Thread.yield();                                     // 이 스레드는 소겟을 확보하기 위해 필요 함 (안드로이드에서는 그래야 함
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    writer.close();
                    sock.close();
                    buffRecv.close();
                } catch (Exception e) {
                    Log.e("Error = ", "Error in closing Socket or Stream");
                }
            }
        }
    }

    public class SocketReceiverThread extends Thread {

        @Override
        public void run() {
            try {
                Log.d("sockreceiver", "대기중");
                while (true) {
                    aLine = buffRecv.readLine();
                    if (checkDataGraph == 0) pharseDayDataString(aLine);
                    else pharseDayTimeDataString(aLine);
                    buffRecv.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void pharseDayDataString(String data){

        String[] str = new String(data).split("/");
        for(String n :str){ Log.d("데이터",n);}
        for(int i=1;i<14;i++){
            if(i%2==1)
            {
                dataList.add(new DayDataType(str[i],Integer.parseInt(str[i-1])));
                Log.d("zz",str[i-1]+"  "+str[i]+"   "+i);
            }
        }
        Intent it = new Intent(this, Dataanalysis.class);
        it.putExtra("data", dataList);
        startActivity(it);
    }
    public void pharseDayTimeDataString(String data){
        Log.d("아아아아", "넘어왔다");
        String[] str = new String(data).split("/");
        String date="";
        int end = 0;
        int start=-0;
        int m=0;
        int l=0;
        int d=0;
        int count = -1;

        //for(String n :str){ Log.d("데이터",n);}
        if(str[0].equals("1")){
            m = Integer.parseInt(str[1]);
            l = 0;
            d = 0;
            date = str[2];
            start = 3;
            end = 42 - 4;
            timedataList.add(new DayTimeDataType(date,m,l,d));
        }
        else if(str[0].equals("2")){
            m = Integer.parseInt(str[1]);
            l = Integer.parseInt(str[3]);
            d = 0;
            date = str[2];
            start = 5;
            end = 42- 2;
            timedataList.add(new DayTimeDataType(date,m,l,d));
            Log.d("@","1");
        }
        else{
            start=1;
            end = 42;
        }
        int index = 0 ;
        Log.d("@",""+start+"   "+""+end);
        for(int j=start; j <= end ; j++){
            count ++;
            if(count== 0){
                m = Integer.parseInt(str[j]);
                Log.d("@","count = 1 m = "+m);
            }
            else if(count == 1){
                date = str[j];
                Log.d("@","count = 2 date = "+date);
            }
            else if(count == 2){
                l = Integer.parseInt(str[j]);
                Log.d("@","count = 3 l = "+l);
            }
            else if(count == 4){
                d = Integer.parseInt(str[j]);
                Log.d("@","count = 4 d = "+d);
            }
            else if(count == 5){
                timedataList.add(new DayTimeDataType(date,m,l,d));
                count = -1;
                Log.d("날짜 확인","count = 5"+timedataList.get(index++).getDate()+"\n");
            }

        }

        Log.d("아아아아", "넘어왔다2");

        if(str[0].equals("1")){
            m = 0;
            end++;
            l = Integer.parseInt(str[end]);
            end++;
            date = str[end];
            end++;
            d = Integer.parseInt(str[end]);
            timedataList.add(new DayTimeDataType(date,m,l,d));
            Log.d("날짜 확인","count = 5 ->"+timedataList.get(index++).getDate()+"아침:"+m+" 점심:"+l+"저녁"+d+"\n");
        }
        else if(str[0].equals("2")){
            m = 0;
            l = 0;
            end++;
            d = Integer.parseInt(str[end]);
            timedataList.add(new DayTimeDataType(str[end-1],m,l,d));
            Log.d("@","3");
        }
        else{

        }
        Log.d("@","4");
        for(DayTimeDataType n :timedataList){
            Log.d("데이터",n.getDate() +"아침 : "+n.getMornigTimeCount()+" 점심 : "+n.getLunchTimeCount()+" 저녁 : "+n.getDinnerTimeCount());
        }
        Log.d("@","end1");

        Intent in = new Intent(MainActivity.this, dayTimeDataAnalysis.class);
        in.putExtra("data", timedataList);
        startActivity(in);
    }
    @Override
    protected void onDestroy() {// 종료할 시에 소켓과 스트림 모두 닫음
        super.onDestroy();
        Log.d("ㅎㅇㅎㅇㅎㅇ", "ㅇㅇㅇ");
        try {
            writer.println(Choices.EXIT.ordinal());
            Thread.sleep(1);
            writer.println(Choices.VIDEO_OFF.ordinal());           // 서버에 영상전송 종료 요청
            Thread.sleep(1);
            writer.close();
            Thread.sleep(1);
            buffRecv.close();
            Thread.sleep(1);
            sock.close();
        } catch (Exception e) {
            Log.e("stream error = ", "onDestroy에서 소켓/스트림 닫는데 문제 발생");
        }
    }
    private class TouchThread extends Thread
    {   int n;
        public TouchThread(int n){
            this.n = n;
        }
        @Override
        public void run()
        {

                while (_isBtnDown) {

                    try {
                        if (n == 1) {
                            writer.println(Choices.RIGHT.ordinal());
                        } else {
                            writer.println(Choices.LEFT.ordinal());
                        }
                        Thread.sleep(10);
                        Log.d("Button", "스래드 돌고있음");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

    }
}