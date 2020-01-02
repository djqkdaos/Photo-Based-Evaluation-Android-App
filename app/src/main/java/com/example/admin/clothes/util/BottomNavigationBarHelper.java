package com.example.admin.clothes.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.admin.clothes.R;
import com.example.admin.clothes.evaluation.evaluation;
import com.example.admin.clothes.firstGetrating.firstGetrating;
import com.example.admin.clothes.profile.profile;
import com.example.admin.clothes.ranking.ranking;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by admin on 2017-06-03.
 */

public class BottomNavigationBarHelper {
    private static final String TAG = "BottomNavigationBarHelp";




    evaluation evaluation_Activity = (evaluation)evaluation.evaluation_Activity;

    public static  void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationVIew");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }
    //인텐트를 이용한 하단 네비게이션바 액션동작
    public  static void enableNavigation(final Context context, BottomNavigationViewEx view, final int act){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_evaluation:
                        Intent intent1 = new Intent(context, evaluation.class);//AVCIVITY_NUM = 0;
                        context.startActivity(intent1);
                        if(act == 1){
                            evaluation evaluation_Avtivity1 = (evaluation)evaluation.evaluation_Activity;
                            evaluation_Avtivity1.finish();
                        }else  if(act == 2){
                            firstGetrating firstGetrating_Activity1 = (firstGetrating)firstGetrating.firstGetrating_Avtivity;
                            firstGetrating_Activity1.finish();
                        }else if(act == 3){
                            profile profile_Activity1 = (profile)profile.profile_Avtivity;
                            profile_Activity1.finish();
                        }else if(act==4){
                            ranking ranking_Avtivity1 = (ranking)ranking.ranking_Avtivity;
                            ranking_Avtivity1.finish();
                        }



                        break;
                    case R.id.ic_rank:
                        Intent intent2 = new Intent(context, ranking.class);//AVCIVITY_NUM = 1;

                        context.startActivity(intent2);
//                        //**이전 액티비티 종료
                        if(act == 1){
                            evaluation evaluation_Avtivity1 = (evaluation)evaluation.evaluation_Activity;
                            evaluation_Avtivity1.finish();
                        }else  if(act == 2){
                            firstGetrating firstGetrating_Activity1 = (firstGetrating)firstGetrating.firstGetrating_Avtivity;
                            firstGetrating_Activity1.finish();
                        }else if(act == 3){
                            profile profile_Activity1 = (profile)profile.profile_Avtivity;
                            profile_Activity1.finish();
                        }else if(act==4){
                            ranking ranking_Avtivity1 = (ranking)ranking.ranking_Avtivity;
                            ranking_Avtivity1.finish();
                        }

//                        //*****************************

                        break;
                    case R.id.ic_score:
                        Intent intent3 = new Intent(context, firstGetrating.class);//AVCIVITY_NUM = 2;
                        context.startActivity(intent3);
                        //**이전 액티비티 종료
                        if(act == 1){
                            evaluation evaluation_Avtivity1 = (evaluation)evaluation.evaluation_Activity;
                            evaluation_Avtivity1.finish();
                        }else  if(act == 2){
                            firstGetrating firstGetrating_Activity1 = (firstGetrating)firstGetrating.firstGetrating_Avtivity;
                            firstGetrating_Activity1.finish();
                        }else if(act == 3){
                            profile profile_Activity1 = (profile)profile.profile_Avtivity;
                            profile_Activity1.finish();
                        }else if(act==4){
                            ranking ranking_Avtivity1 = (ranking)ranking.ranking_Avtivity;
                            ranking_Avtivity1.finish();
                        }

//                        //**************************

                        break;
                    case R.id.ic_profile:
                        Intent intent4 = new Intent(context, profile.class);//AVCIVITY_NUM = 3;
                        context.startActivity(intent4);
//                        //**이전액티비티제거
                        if(act == 1){
                            evaluation evaluation_Avtivity1 = (evaluation)evaluation.evaluation_Activity;
                            evaluation_Avtivity1.finish();
                        }else  if(act == 2){
                            firstGetrating firstGetrating_Activity1 = (firstGetrating)firstGetrating.firstGetrating_Avtivity;
                            firstGetrating_Activity1.finish();
                        }else if(act == 3){
                            profile profile_Activity1 = (profile)profile.profile_Avtivity;
                            profile_Activity1.finish();
                        }else if(act==4){
                            ranking ranking_Avtivity1 = (ranking)ranking.ranking_Avtivity;
                            ranking_Avtivity1.finish();
                        }

//                        //************

                        break;
                }


                return false;
            }
        });
    }
    //-----------------------------------------------------------------------


}
