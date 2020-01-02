package com.example.admin.clothes.evaluation;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import org.json.simple.JSONObject;
import com.example.admin.clothes.R;
import com.example.admin.clothes.util.BottomNavigationBarHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

//사진업로드
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class evaluation extends Activity  {
    private static final String TAG = "evaluation";
    private final int activity_Num = 0;
    private Context mcontext = evaluation.this;
    public static Activity evaluation_Activity;
    ImageView imgV_eval;
    Bitmap bitmap;
    String user_id;
    String rt;
    String rt2;
    String json1;
    String json2;
    String img_Url;
    JSONObject jObject = new JSONObject();
    Button btn_eval;
    TextView oneline_eval;
    SeekBar scoreBar;
    TextView txtV_score;
    public int score_int;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        evaluation_Activity = evaluation.this;
        imgV_eval = (ImageView)findViewById(R.id.imgView_eval);
        oneline_eval = (TextView)findViewById(R.id.txtV_oneline_eval);
        scoreBar = (SeekBar)findViewById(R.id.bar_scroe);
        txtV_score = (TextView)findViewById(R.id.txtV_scroe);

        Log.d(TAG, "onCreate:Starting.");
        //user_info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            System.out.println("firstgatRating 유저아이디 가져오기 성공!!"+user.getUid());
            user_id = user.getUid();
        } else {
            System.out.println("유저아이디 가져오기 실패!!"+user.getUid());
        }

        setupBottomNavigationView();

        thread1.start();
//        btn_eval.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imgview imgview = new imgview();
//            }
//        });
        //thread2.start();
//        try {
//
//            //  메인 스레드는 작업 스레드가 이미지 작업을 가져올 때까지
//            //  대기해야 하므로 작업스레드의 join() 메소드를 호출해서
//            //  메인 스레드가 작업 스레드가 종료될 까지 기다리도록 합니다.
//
//            thread2.join();
//
//            //  이제 작업 스레드에서 이미지를 불러오는 작업을 완료했기에
//            //  UI 작업을 할 수 있는 메인스레드에서 이미지뷰에 이미지를 지정합니다.
//
//
//
//        } catch (InterruptedException e) {
//
//        }
        scoreBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                //thumb가 멈출 때 호출
                //여기에다가 점수 주는거 넣으면 됨
                Thread thread_score_upload = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            System.out.println("점수주기 실행");
                            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
                            qparams.add(new BasicNameValuePair("id", user_id));
                            qparams.add(new BasicNameValuePair("hash",rt ));
                            qparams.add(new BasicNameValuePair("point", score_int+""));
                            URI uri = URIUtils.createURI("http", "djqkdaos.iptime.org", 8084, "/nullserver/pointup",
                                    URLEncodedUtils.format(qparams, "UTF-8"), null);
                            HttpGet get = new HttpGet(uri);
                            HttpClient client = new DefaultHttpClient();
                            HttpResponse response = client.execute(get);
                            HttpEntity resEntity = response.getEntity();
                            if(resEntity != null){
                                String res = EntityUtils.toString(resEntity);
                                System.out.println(res);
                                rt = res; // 네트워크 통신 결과값 받는거
                                if(rt.equals("done")){
                                    System.out.println("점수등록 성공!");
                                }
                                System.out.println("쓰레드1 네트워크통신 결과 : "+rt);

                            }


                        }catch(Exception e){
                            //Toast.makeText(getApplicationContext(), e.getMessage() ,Toast.LENGTH_LONG).show();
                            System.out.println("점수주기기에러 : "+e);
                       }
                        //Toast.makeText(getApplicationContext(), rt ,Toast.LENGTH_LONG).show();
                    }
                });
                thread_score_upload.start();
                try{
                    thread1.start();
                }catch(Exception e){
                    System.out.println("쓰레드 실행되는지 확인:"+e);
                    thread1 = null;
                    thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println("쓰레드1 재실행");
                                List<NameValuePair> qparams = new ArrayList<NameValuePair>();
                                qparams.add(new BasicNameValuePair("id", user_id));
                                URI uri = URIUtils.createURI("http", "djqkdaos.iptime.org", 8084, "/nullserver/randphoto",
                                        URLEncodedUtils.format(qparams, "UTF-8"), null);
                                HttpGet get = new HttpGet(uri);
                                HttpClient client = new DefaultHttpClient();
                                HttpResponse response = client.execute(get);
                                HttpEntity resEntity = response.getEntity();
                                if(resEntity != null){
                                    String res = EntityUtils.toString(resEntity);
                                    System.out.println(res);
                                    rt = res; // 네트워크 통신 결과값 받는거
                                    System.out.println("쓰레드1재실행 네트워크통신 결과 : "+rt);

                                }
                                try {
                                    thread2.start();
                                }catch (Exception e){
                                    thread2 = null;

                                    Thread thread2 = new Thread(new Runnable(){
                                        @Override
                                        public void run() {
                                            try {
                                                System.out.println("쓰레드 2 실행");
                                                List<NameValuePair> qparams = new ArrayList<NameValuePair>();
                                                String tmppp = rt;//이미지 해쉬값
                                                qparams.add(new BasicNameValuePair("filehash", tmppp));
                                                URI uri = URIUtils.createURI("http", "djqkdaos.iptime.org", 8084, "/nullserver/photoview",
                                                        URLEncodedUtils.format(qparams, "UTF-8"), null);
                                                HttpGet get = new HttpGet(uri);
                                                HttpClient client = new DefaultHttpClient();
                                                HttpResponse response = client.execute(get);
                                                HttpEntity resEntity = response.getEntity();
                                                if(resEntity != null){
                                                    String res = EntityUtils.toString(resEntity);
                                                    System.out.println(res);
                                                    rt2 = res; // 네트워크 통신 결과값 받는거
                                                    System.out.println(rt2);

                                                }



                                            }catch(Exception e){
                                                //Toast.makeText(getApplicationContext(), e.getMessage() ,Toast.LENGTH_LONG).show();
                                                System.out.println("에러2 : "+e);
                                            }

                                            JSONParser parser = new JSONParser();
                                            try {
                                                JSONObject jsonObj = (JSONObject)parser.parse(rt2);
                                                json1 = jsonObj.get("msg").toString();
                                                json2 = jsonObj.get("filepath").toString();
                                                System.out.println("json msg : "+json1);
                                                System.out.println("json filepath : "+json2);

                                                imgview imgvi = new imgview();
                                                imgvi.execute();


                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }



                                            //Toast.makeText(getApplicationContext(), rt ,Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    thread2.start();
                                }



                            }catch(Exception e){
                                //Toast.makeText(getApplicationContext(), e.getMessage() ,Toast.LENGTH_LONG).show();
                                System.out.println("에러1 : "+e);
                            }
                            //Toast.makeText(getApplicationContext(), rt ,Toast.LENGTH_LONG).show();
                        }
                    });
                    thread1.start();


                }
                DialogInterface.OnClickListener canceListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtV_score.setText("0");
                        scoreBar.setProgress(0);
                        dialog.dismiss();

                    }
                };
                new AlertDialog.Builder(evaluation.this)
                        .setTitle("점수평가 완료!")
                        .setNegativeButton("확인", canceListener)
                        .show();



            }
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                //thumb가 움직이기 시작할 때 호출
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                //thumb 가 움직일때 마다 호출
                txtV_score.setText(progress+"");
                score_int = progress;
            }
        });





    }
    //앱종료 하기 위한 소스----------------------------------------------

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //빽(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        if((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder d = new AlertDialog.Builder(evaluation_Activity);
            d.setTitle("종료여부");
            d.setMessage("정말 종료 하시겠습니꺄?");
            //d.setIcon(R.drawable.ic_launcher);

            d.setPositiveButton("예",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                   evaluation_Activity.finish();
                }
            });

            d.setNegativeButton("아니요",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });
            d.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//---------------------------------------------------------------------

    /*BottomNavigationView setup*/
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: seting up bottomNavigationView");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationBarHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        bottomNavigationViewEx.enableAnimation(false);
//        bottomNavigationViewEx.enableItemShiftingMode(false);
//        bottomNavigationViewEx.enableShiftingMode(false);
//        bottomNavigationViewEx.setTextVisibility(false); 이렇게 바로 적용 가능
//        이런식으로 개별 인텐트를 통해 개별 동작가능
//        Intent intent1 = new Intent(evaluation.this, evaluation.class);
//        evaluation.this.startActivity(intent1);
        //-----------------------------------
        BottomNavigationBarHelper.enableNavigation(mcontext, bottomNavigationViewEx, 1);
        Menu menu1 = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu1.getItem(activity_Num);
        menuItem.setChecked(true);

    }
    //네트워크 통신-------------------------------------------
    //--랜덤사진 해쉬 값 호출-----------------
    class imgview extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //Gongi1 = "���������� �����������Դϴ�.";
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            //System.out.println("��������" + Gongi1);
            //arrayAnn.add(Gongi1);
            imgV_eval.setImageBitmap(bitmap);
            oneline_eval.setText(json1);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub


        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL("http://djqkdaos.iptime.org:11113/"+json2); // URL 주소를 이용해서 URL 객체 생성

                //URL url = new URL("http://kiraon.gonetis.com:11114/2017-06-13/76db3b360dce73e0401b8eebf4602e8851080ffe1edc3364a9d338f2f8ae16bd.jpg"); // URL 주소를 이용해서 URL 객체 생성"
                System.out.println("이미지뷰 이미지 주소"+url);
                //  아래 코드는 웹에서 이미지를 가져온 뒤
                //  이미지 뷰에 지정할 Bitmap을 생성하는 과정

                try {
                    System.out.println("이미지뷰 클래스 실행");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    System.out.println("실행1.conn:"+conn);
                    InputStream is = conn.getInputStream();
                    System.out.println("실행1.is:"+is);
                    bitmap = BitmapFactory.decodeStream(is);

                    is.close();

                }catch (Exception e){
                    System.out.println("imgTherd오류:"+e);
                }


            } catch(IOException ex) {

            }
            return null;
        }


    }
    Thread thread1 = new Thread(new Runnable(){
        @Override
        public void run() {
            try {
                System.out.println("쓰레드1 실행");
                List<NameValuePair> qparams = new ArrayList<NameValuePair>();
                qparams.add(new BasicNameValuePair("id", user_id));
                URI uri = URIUtils.createURI("http", "djqkdaos.iptime.org", 8084, "/nullserver/randphoto",
                        URLEncodedUtils.format(qparams, "UTF-8"), null);
                HttpGet get = new HttpGet(uri);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(get);
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    String res = EntityUtils.toString(resEntity);
                    System.out.println(res);
                    rt = res; // 네트워크 통신 결과값 받는거
                    System.out.println("쓰레드1 네트워크통신 결과 : "+rt);

                }
                thread2.start();


            }catch(Exception e){
                //Toast.makeText(getApplicationContext(), e.getMessage() ,Toast.LENGTH_LONG).show();
                System.out.println("에러1 : "+e);
            }
            //Toast.makeText(getApplicationContext(), rt ,Toast.LENGTH_LONG).show();
        }
    });
    //---랜덤된 사진이 서버에 어디에있는지 경로찾기
    Thread thread2 = new Thread(new Runnable(){
        @Override
        public void run() {
            try {
                System.out.println("쓰레드 2 실행");
                List<NameValuePair> qparams = new ArrayList<NameValuePair>();
                String tmppp = rt;//이미지 해쉬값
                qparams.add(new BasicNameValuePair("filehash", tmppp));
                URI uri = URIUtils.createURI("http", "djqkdaos.iptime.org", 8084, "/nullserver/photoview",
                        URLEncodedUtils.format(qparams, "UTF-8"), null);
                HttpGet get = new HttpGet(uri);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(get);
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    String res = EntityUtils.toString(resEntity);
                    System.out.println(res);
                    rt2 = res; // 네트워크 통신 결과값 받는거
                    System.out.println("rt2값:"+rt2);

                }



            }catch(Exception e){
                //Toast.makeText(getApplicationContext(), e.getMessage() ,Toast.LENGTH_LONG).show();
                System.out.println("에러2 : "+e);
            }

            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObj = (JSONObject)parser.parse(rt2);
                json1 = jsonObj.get("msg").toString();
                json2 = jsonObj.get("filepath").toString();
                System.out.println("json msg : "+json1);
                System.out.println("json filepath : "+json2);

                imgview imgvi = new imgview();
                imgvi.execute();


            } catch (ParseException e) {
                e.printStackTrace();
            }



            //Toast.makeText(getApplicationContext(), rt ,Toast.LENGTH_LONG).show();
        }
    });

    }








