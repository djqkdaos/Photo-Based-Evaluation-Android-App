package com.example.admin.clothes.ranking;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.admin.clothes.firstGetrating.MainActivity;
import com.example.admin.clothes.firstGetrating.firstGetrating;
import com.example.admin.clothes.profile.profile;
import com.example.admin.clothes.util.BottomNavigationBarHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.List;

public class ranking extends AppCompatActivity {
    private static final String TAG = "ranking";
    public static Activity ranking_Avtivity;
    private Context mcontext = ranking.this;
    private final int activity_Num = 1;
    public boolean boolRank_Activity;
    String title[] = {"."};
    String rank[] = {"."};
    int pics = R.drawable.evaluation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Log.d(TAG, "onCreate: started");
        ranking_Avtivity = ranking.this;

        setupBottomNavigationView();

        ListView list = (ListView)findViewById(R.id.list);
        CustomList adapter = new CustomList(ranking.this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ranking.this,title[position] + "는 " + rank[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

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
        BottomNavigationBarHelper.enableNavigation(mcontext, bottomNavigationViewEx,4);
        Menu menu1 = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu1.getItem(activity_Num);
        menuItem.setChecked(true);

    }
    public class CustomList extends ArrayAdapter<String> {
        Activity context;
        public CustomList(Activity context){
            super(context, R.layout.custom_list_layout, title);
            this.context = context;
        }
        public View getView(final int position, View con, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.custom_list_layout, null, true);
            TextView txtT = (TextView)rowView.findViewById(R.id.txtV_reult_onenline);
            TextView txtR = (TextView)rowView.findViewById(R.id.txtV_result_point);
            ImageView img = (ImageView)rowView.findViewById(R.id.imageView);
            txtT.setText(title[position]);
            txtR.setText(rank[position]);
            img.setImageResource(pics);

            return rowView;
        }
    }
    //앱종료 하기 위한 소스----------------------------------------------

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //빽(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        if((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder d = new AlertDialog.Builder(ranking_Avtivity);
            d.setTitle("종료여부");
            d.setMessage("정말 종료 하시겠습니꺄?");
            //d.setIcon(R.drawable.ic_launcher);

            d.setPositiveButton("예",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    ranking_Avtivity.finish();
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