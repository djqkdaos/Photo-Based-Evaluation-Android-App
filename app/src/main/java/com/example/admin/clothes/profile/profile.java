package com.example.admin.clothes.profile;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.clothes.R;
import com.example.admin.clothes.evaluation.evaluation;
import com.example.admin.clothes.firstGetrating.firstGetrating;
import com.example.admin.clothes.ranking.ranking;
import com.example.admin.clothes.util.BottomNavigationBarHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class profile extends AppCompatActivity {
    private static final String TAG = "profile";
    public static Activity profile_Avtivity;
    private Context mcontext = profile.this;
    private final int activity_Num = 3;
    public String providerId;
    String uid;
    String name;
    String email;
    Uri photoUrl;
    String Jsontmp, Jsontmp1, Jsontmp2, Jsontmp3, Jsontmp4;
    String listhash[] = new String[20];
    String listmsg[] = new String[20];
    String listpoint[] = new String[20];
    int Jsonint;
    List<String> JsonDate = new ArrayList<String>();
    public TextView txtV_userEmail;
    public TextView txtV_userName;
    public TextView custom_txtV_oneline;
    public TextView custom_txtV_score;
    ImageView custom_imgV;
    ImageView imgV_profile;
    Bitmap bitmap;

    Bitmap bitmap_result[] = new Bitmap[20];
    String title[] = {"그림1", "그림2", "그림3", "그림4", "그림5", "그림6", "그림7"};
      int pics = R.drawable.evaluation;
    String user_id;
    ListView c_list;
    String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_Avtivity = profile.this;
        txtV_userEmail = (TextView) findViewById(R.id.txtV_userEmail);
        txtV_userName = (TextView) findViewById(R.id.txtV_userName);
        imgV_profile = (ImageView) findViewById(R.id.imgView_profile);


        Log.d(TAG, "onCreate: started");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            System.out.println("firstgatRating 유저아이디 가져오기 성공!!"+user.getUid());
            user_id = user.getUid().toString();
        } else {
            System.out.println("유저아이디 가져오기 실패!!"+user.getUid());
        }
        //-----------------------유저 아이디가져오기-----------------
        setupToolBar();
        setupBottomNavigationView();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                providerId = profile.getProviderId();

                // UID specific to the provider
                uid = profile.getUid();

                // Name, email address, and profile photo Url
                name = profile.getDisplayName();
                email = user.getEmail();
                photoUrl = profile.getPhotoUrl();
            }

        }
        //------------------유저 프로필가져오기-------------------------
        System.out.println("이메일 정보" + email.toString() + "이름정보" + name.toString());
        txtV_userEmail.setText(email.toString());
        txtV_userName.setText(name.toString());
        //---------이미지 집어넣기-----------------------------------------------------
        Thread mThread = new Thread() {

            @Override
            public void run() {

                try {
                    URL url = new URL(photoUrl.toString()); // URL 주소를 이용해서 URL 객체 생성
                    //URL url = new URL("http://www.droidviet.com/images/up_img/dalats_com_up12_09_2010_11_091610742910.archos-android.jpg"); // URL 주소를 이용해서 URL 객체 생성"
                    //  아래 코드는 웹에서 이미지를 가져온 뒤
                    //  이미지 뷰에 지정할 Bitmap을 생성하는 과정

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (IOException ex) {

                }
            }
        };

        mThread.start(); // 웹에서 이미지를 가져오는 작업 스레드 실행.

        try {

            //  메인 스레드는 작업 스레드가 이미지 작업을 가져올 때까지
            //  대기해야 하므로 작업스레드의 join() 메소드를 호출해서
            //  메인 스레드가 작업 스레드가 종료될 까지 기다리도록 합니다.

            mThread.join();

            //  이제 작업 스레드에서 이미지를 불러오는 작업을 완료했기에
            //  UI 작업을 할 수 있는 메인스레드에서 이미지뷰에 이미지를 지정합니다.

            imgV_profile.setImageBitmap(bitmap);
        } catch (InterruptedException e) {

        }
        //---------------프로필이미지가져오기-------------------------
        c_list = (ListView) findViewById(R.id.result_list);

        imgview view = new imgview();
        view.execute();



    }
    //----------커스텀리스트------------
    class CustomList extends ArrayAdapter<String> {
        Activity context;

        public CustomList(Activity context) {
            super(context, R.layout.custom_list_layout, title);
            this.context = context;
        }

        public View getView(final int position, View con, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.custom_list_layout, null, true);
            custom_txtV_oneline = (TextView) rowView.findViewById(R.id.txtV_reult_onenline);
            custom_txtV_score = (TextView) rowView.findViewById(R.id.txtV_result_point);
            custom_imgV = (ImageView) rowView.findViewById(R.id.imageView);

            if (listmsg[position] != null) {
                custom_txtV_oneline.setText("메세지:"+listmsg[position]);
            }else{
                custom_txtV_oneline.setText("메세지:");
            }
            if (listmsg[position] != null) {
                custom_txtV_score.setText("평가 점수:"+listpoint[position]);
            }else{
                custom_txtV_score.setText("평가 점수:");
            }


            custom_imgV.setImageBitmap(bitmap_result[position]);//스트링해쉬값을 비트맵으로 전환

            return rowView;
        }
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_ToolBar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: clicked menu item:" + item);

                switch (item.getItemId()) {
                    case R.id.profileMenu:
                        Log.d(TAG, "onMenuItemClick: Navitgating to Profile preferences.");
                }
                return false;
            }
        });
    }
    //메뉴툴바---------------------------------------------------------------------------

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
        BottomNavigationBarHelper.enableNavigation(mcontext, bottomNavigationViewEx, 3);
        Menu menu1 = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu1.getItem(activity_Num);
        menuItem.setChecked(true);

    }

    //----------메뉴--------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);

        return true;
    }

    //---------------------


    class imgview extends AsyncTask<Void, Void, Void> {

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
            //여기에 리스트 이미지뷰에 표시할꺼
            //------------------리스트뷰 실행-----------------------
            CustomList adapter = new CustomList(profile.this);
            c_list.setAdapter(adapter);
            //---------------------------------------
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                System.out.println("백그라운드 실행");
                List<NameValuePair> qparams = new ArrayList<NameValuePair>();
                qparams.add(new BasicNameValuePair("userid", user_id));
                System.out.println("백그라운드 실행 유저아이디:" + qparams);
                URI uri = URIUtils.createURI("http", "djqkdaos.iptime.org", 8084, "/nullserver/myphoto",
                        URLEncodedUtils.format(qparams, "UTF-8"), null);
                System.out.println("백그라운드 실행 Url:" + uri);
                HttpGet get = new HttpGet(uri);
                System.out.println("백그라운드 실행 HttpGet get:" + get);
                HttpClient client = new DefaultHttpClient();
                System.out.println("백그라운드 실행 HttpClient client:" + client);
                HttpResponse response = client.execute(get);
                System.out.println("백그라운드 실행 HttpResponse response:" + response);
                HttpEntity resEntity = response.getEntity();
                System.out.println("백그라운드 실행 HttpEntity resEntity:" + resEntity);
                if (resEntity != null) {
                    res = EntityUtils.toString(resEntity);

                    System.out.println("res값:" + res);
                    Jsontmp = res; // 네트워크 통신 결과값 받는거
                    System.out.println("네트워크 통신값 받기:" + Jsontmp);


                }

            } catch (Exception e) {
                System.out.println("myphoto 쓰레드 오류:" + e);
            }

                JSONParser parser = new JSONParser();        // 파서 2개 만들어야함, 이건 몇개의 리스트가 있는지
            try {

                JSONObject jsonObj = (JSONObject) parser.parse(Jsontmp);
                if(jsonObj != null) {


                    Jsontmp1 = jsonObj.get("allcount").toString();
                    System.out.println("jsontmp1 : " + Jsontmp1);

                    //System.out.println("json filepath : "+json2);
                    Jsonint = Integer.parseInt(Jsontmp1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            JSONParser parser2 = new JSONParser();        // 파서 2개 만들어야함, 이건 배열에 가따넣기
            try {
                JSONObject jsonObj2 = (JSONObject) parser2.parse(Jsontmp);

                for (int i = 1; i <= Jsonint; i++) {  // 몇개있는지 확인후 배열에 순서대로 넣는다.
                    listpoint[i - 1] = jsonObj2.get("point" + i).toString();
                    System.out.println(listpoint[i - 1]);
                    listhash[i - 1] = jsonObj2.get("hash" + i).toString();
                    System.out.println("포토 해쉬값:" + listhash[i - 1]);
                    listmsg[i - 1] = jsonObj2.get("msg" + i).toString();
                    System.out.println("메세지 값:" + listmsg[i - 1]);
                    JsonDate.add(jsonObj2.get("Date" + i).toString());
                    System.out.println("Date값:" + JsonDate.get(i - 1));
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
            //받아온 해쉬값을 서버쪽에서 이미지를 받아옴
            for (int i = 1; i <= Jsonint; i++) {
                try {
                    URL url = new URL("http://djqkdaos.iptime.org:11113/" + JsonDate.get(i - 1) + "/" + listhash[i - 1] + ".jpg"); // URL 주소를 이용해서 URL 객체 생성

                    //URL url = new URL("http://kiraon.gonetis.com:11114/2017-06-13/76db3b360dce73e0401b8eebf4602e8851080ffe1edc3364a9d338f2f8ae16bd.jpg"); // URL 주소를 이용해서 URL 객체 생성"
                    System.out.println("이미지뷰 이미지 주소" + url);
                    //  아래 코드는 웹에서 이미지를 가져온 뒤
                    //  이미지 뷰에 지정할 Bitmap을 생성하는 과정

                    try {
                        System.out.println("이미지뷰 클래스 실행");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        System.out.println("실행1.conn:" + conn);
                        InputStream is = conn.getInputStream();
                        System.out.println("실행1.is:" + is);
                        bitmap_result[i - 1] = BitmapFactory.decodeStream(is);

                        is.close();

                    } catch (Exception e) {
                        System.out.println("imgTherd오류:" + e);
                    }


                } catch (IOException ex) {

                }
            }



            return null;
        }



    }
    //앱종료 하기 위한 소스----------------------------------------------

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //빽(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        if((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder d = new AlertDialog.Builder(profile_Avtivity);
            d.setTitle("종료여부");
            d.setMessage("정말 종료 하시겠습니꺄?");
            //d.setIcon(R.drawable.ic_launcher);

            d.setPositiveButton("예",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                   profile_Avtivity.finish();
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

}
    /*
    Thread thread_result = new Thread(new Runnable(){
        @Override
        public void run() {
            try {
                System.out.println("쓰레드 2 실행");
                List<NameValuePair> qparams = new ArrayList<NameValuePair>();
                String tmppp = rt;//이미지 해쉬값
                qparams.add(new BasicNameValuePair("filehash", tmppp));
                URI uri = URIUtils.createURI("http", "kiraon.gonetis.com", 8084, "/nullserver/photoview",
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

                evaluation.imgview imgvi = new evaluation.imgview();
                imgvi.execute();


            } catch (ParseException e) {
                e.printStackTrace();
            }



            //Toast.makeText(getApplicationContext(), rt ,Toast.LENGTH_LONG).show();
        }
    });
*/
