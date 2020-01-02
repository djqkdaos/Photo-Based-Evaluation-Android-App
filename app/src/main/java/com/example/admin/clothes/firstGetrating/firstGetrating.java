package com.example.admin.clothes.firstGetrating;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.clothes.R;
import com.example.admin.clothes.util.BottomNavigationBarHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;

import static android.R.attr.bitmap;

import static com.example.admin.clothes.R.id.imgV_user_upload;
import static com.example.admin.clothes.R.id.txtView_img_Path;
import static com.example.admin.clothes.R.id.txtView_oneline;
import static com.example.admin.clothes.R.id.wrap_content;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_EXTRA_RESULT;

public class firstGetrating extends AppCompatActivity{
    private static final String TAG = "firstGetrating";
    public static Activity firstGetrating_Avtivity;
    private Context mcontext = firstGetrating.this;
    private final int activity_Num = 2;

    //사진 불러 오는데 필요한 변수
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private FirebaseAuth mAuth;
    private Uri mlmageCaptureUri;
    private ImageView iv_UserPhoto;
    private int id_view;
    private String absoultePath;
    public TextView txtV_imgPath;
    String upload_filePath = null;
    String oneline_msg;
    //private DB_Manager dbmanager;

    String googleuser_return = null;
    String url = null;
    String rt = null;
    String paths = null;

    String chagehash=null;
    Uri imageName =null;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String temp_path;
    final Context context =this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_getrating);
        firstGetrating_Avtivity = firstGetrating.this;


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            System.out.println("firstgatRating 유저아이디 가져오기 성공!!"+user.getUid());
        } else {
            System.out.println("유저아이디 가져오기 실패!!"+user.getUid());
        }
        googleuser_return = user.getUid();




        Log.d(TAG, "onCreate: started");


        setupBottomNavigationView();
        Button btn_choose_photo = (Button) this.findViewById(R.id.btn_choose_photo);
        Button btn_upLoad = (Button) findViewById(R.id.btn_upload);
        iv_UserPhoto = (ImageView) this.findViewById(R.id.imgV_user_upload);
        final TextView txtV_oneline = (TextView)findViewById(txtView_oneline);
        txtV_imgPath = (TextView)findViewById(txtView_img_Path);
        txtV_imgPath.setVisibility(View.GONE);
        final EditText edit_oneline = (EditText)findViewById(R.id.edit_oneline);

        btn_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구형사진띄우기
//                id_view = v.getId();
//                if (v.getId() == R.id.btn_choose_photo) {
//                    DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            doTakePhotoAction();
//
//
//                        }
//                    };
//                    DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            doTakeAlbumAction();
//                        }
//                    };
//                    DialogInterface.OnClickListener canceListener = new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    };
//                    new AlertDialog.Builder(firstGetrating.this)
//                            .setTitle("업로드할 이미지선택")
//                            .setPositiveButton("사진활영", cameraListener)
//                            .setNeutralButton("앨범선택", albumListener)
//                            .setNegativeButton("취소", canceListener)
//                            .show();
//                }
                //크롭 액티비티 실행
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(firstGetrating.this);
                // start cropping activity for pre-acquired image saved on the device


            }
        });
        btn_upLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String tmppp = getRealPathFromURI(mlmageCaptureUri);//파일 절대 경로 저장
//                upload_filePath = tmppp;
                 upload_filePath = txtV_imgPath.getText().toString();
                System.out.println("선택한 사진 경로"+ upload_filePath);
                sha256(upload_filePath);//해쉬값 만드는 메소드

                oneline_msg = edit_oneline.getText().toString();
                try{
                    thread1.start();
                }catch(Exception e){
                    thread1 = null;
                    thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://djqkdaos.iptime.org:8084/nullserver/upload?filehash="+chagehash+"&userid="+googleuser_return+"&msg="+oneline_msg);
                            try {
                                MultipartEntity entity = new MultipartEntity();
                                System.out.println("http 업로드 동작 파일경로 : "+upload_filePath);//이거 수정안한거 아니가
                                File file = new File(upload_filePath);
                                entity.addPart("files", new FileBody(file));

                                httppost.setEntity(entity);
                                HttpResponse response = httpclient.execute(httppost);


                                HttpEntity resEntity = response.getEntity();

                                BufferedReader reader = new BufferedReader(new InputStreamReader(
                                        response.getEntity().getContent(), "UTF-8"));
                                String sResponse;
                                StringBuilder s = new StringBuilder();

                                while ((sResponse = reader.readLine()) != null) {
                                    s = s.append(sResponse);

                                }
                            } catch (ClientProtocolException e) {
                                System.out.println("오류발생3 : " + e); //에러 표시시켜봥
                            } catch (IOException e) {
                                System.out.println("오류발생4 : " + e);
                            }
                        }
                    });


                }
                DialogInterface.OnClickListener canceListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            edit_oneline.setText("");
                            iv_UserPhoto.setImageBitmap(null);


                        }
                    };
                new AlertDialog.Builder(context)
                            .setTitle("평가받기가 시작됩니다!")
                            .setNegativeButton("확인", canceListener)
                            .show();

            }

        });
        //------------------------------------
    }
    //*******************크롭이미지****************************
//    public void onSelectImageClick(View view) {
//        CropImage.activity(null)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .start(this);
//    }
    //*********************************************************
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
        BottomNavigationBarHelper.enableNavigation(mcontext, bottomNavigationViewEx,2);
        Menu menu1 = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu1.getItem(activity_Num);
        menuItem.setChecked(true);

    }
//    //카메라에서 사진 촬영
//    public void doTakePhotoAction()//촬영후 이미지 가져오기
//    {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        //임시로 사용할 파일의 경로를 생성
//        url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";//파일 이름
//        mlmageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));//구글신님 찾아야함 니 지금 사진 계속 하는게 이걸로 하는거임? 카메라에서 사진 촬영 ㄴㄴ 앨범에서 가져왔음 변수 Uri
//
//        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mlmageCaptureUri);
//        startActivityForResult(intent, PICK_FROM_CAMERA);
//    }
//
//    //앨범에서 이미지 가져오기
//    public void doTakeAlbumAction()//앨범에서 이미지 가져오기기
//    {
//        //앨범호출
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        startActivityForResult(intent, PICK_FROM_ALBUM);
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode != RESULT_OK)
//            return;
//        switch (requestCode) {
//            case PICK_FROM_ALBUM: {
//                //이후의 처리가 카메라와 같으므로 break없이 진행
//                mlmageCaptureUri = data.getData();//사진 사진이름 가져오기
//                imageName = data.getData();
//                Log.d("앨범가져오기", mlmageCaptureUri.getPath().toString());//이름 기준으로 경로찾아서 String으로 지정
//                upload_filePath = getRealPathFromURI(mlmageCaptureUri);
//
//
//
//            }
//            case PICK_FROM_CAMERA: {
//                //이미지를 가져온후 리사이즈할 이미지 크기를 결정합니다.
//                //이후에 이미지 크롭 어플리케이셩을 호출
//                Intent intent = new Intent("com.android.camera.action.CROP");//크롭하는거 실행
//                ///////////////////////////////////////////////////////////////////////////////////////////
//
//                intent.setDataAndType(mlmageCaptureUri, "image/*");
//
//                //CROP할 이미지를 200*200 크기로 저장
//                intent.putExtra("outputX",200);//CROP한 이미지의 x축 크기
//                intent.putExtra("outputY", 200);//CROP한 이미지의 y축크기
//                intent.putExtra("aspectX", 1);//CROP박스의 x축 비율
//                intent.putExtra("aspectY", 1);//CROP박스의 y축 비율
//                intent.putExtra("scale", true);
//                intent.putExtra("return-data", true);
//
//
//
//                startActivityForResult(intent, CROP_FROM_IMAGE);//CROP_FROM_CAMERA case문이동
//                Log.d("카메라사진가져오기", mlmageCaptureUri.getPath().toString());
//                break;
//
//            }
//            case CROP_FROM_IMAGE: {
//                System.out.println("크롭_프롬_이미지");
//
//                System.out.println(requestCode);
//                //크롭이 된 이후의 이미지를 넘겨 받음
//                //이미지뷰에 이미지를 보여 준다거나 부가적인 작업 이후에 임시파일 삭제.
//                if (resultCode != RESULT_OK) {
//                    return;
//                }
//                final Bundle extras = data.getExtras();
//                System.out.println("extras: "+extras);
//                //CROP된 이미지를 저장하기 위한 FILE경로
//                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/clothes/" + System.currentTimeMillis() + ".jpg"; // 이거 쓰자 이거쓰면 여기서 찍은 사진만 올가는데? 흐음 잠시
//
//
//                if(extras != null) {
//                    Bitmap photo = extras.getParcelable("data");//CROP된 BITMAP
//                    iv_UserPhoto.setImageBitmap(photo);//레이아웃 이미지칸에 CROP된BITMAP을 보여줌
//
//                    System.out.println("요청보내기");
//                    storeCropImage(photo, filePath);//CROP된 이미지를 외부 저장소 앨범에 저장한다
//
//
//
//                    absoultePath = filePath;
//                }
//
//
//                //임시파일 삭제
//                File f = new File(mlmageCaptureUri.getPath());
//                if (f.exists()) {
//                    f.delete();
//                }
//                break;
//
//            }
//        }
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);//크롭된 이미지 데이터
            try {
                Bundle extras = data.getExtras();
                System.out.println("extras변수정보:"+extras);
                System.out.println("data변수정보:"+data);
                System.out.println("result변수정보:"+result);
            }catch (Exception e){
                System.out.println("크롭중간에 취소");
            }

            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/clothes/" + System.currentTimeMillis() + ".jpg";

            if (resultCode == RESULT_OK) {
                ((ImageView) findViewById(R.id.imgV_user_upload)).setImageURI(result.getUri());
                System.out.println("이미지뷰에 올라랑 이미지경로:"+result.getUri());
                //이미지 리사이징--------------------------------------------
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 5;
                //-----------------------------------------------------------
                Bitmap photo = BitmapFactory.decodeFile( result.getUri().getPath(),options);


                //CROP된 이미지를 저장하기 위한 FILE경로
                storeCropImage(photo, filePath);

                absoultePath = filePath;
                //임시파일 제거
                File f = new File(result.getUri().getPath());
                if (f.exists()) {
                    f.delete();
                }

                //Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
                Toast.makeText(this, "사진 저장완료!", Toast.LENGTH_SHORT).show();

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
            }
    }

//
//    public String getRealPathFromURI(Uri contentUri) { // 선택한 이미지 절대경로 불러오기
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//
//    }



    //Bitmap을 저장하는 부분
    private void storeCropImage(Bitmap bitmap, String filePath) {
        System.out.println("실행됨");
        System.out.println("비트맵 데이터 출력:"+bitmap);
        //imagefolder 폴더를 생성하여 이미지를 저장하는 방식이다.
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/clothes";
        File directory_imagefolder = new File(dirPath);

        if (!directory_imagefolder.exists()) {//imagefolder 디렉토리에 폴더가 없다면..(새로 이미지를 저장할경우)
            directory_imagefolder.mkdir();
        }

        File copyFile = new File(filePath);
        System.out.println("저장요청한 파일 경로 : "+filePath);
        txtV_imgPath.setText(filePath);

        BufferedOutputStream out = null;
//------------------------------여기서 오류뿜는거 부터 해결하면댐-----------------------------
        try {

            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            //sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류뿜뿜:"+e);
        }

    }

    //------사진해쉬값추출
    FileInputStream fis = null;
    public void sha256(String val){
        try {
            MessageDigest md = MessageDigest.getInstance("sha256");
            fis = new FileInputStream(val);// path,sdpath+"/download/MobileWeb_intent.apk"

            //BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            byte[] dataBytes = new byte[64000];

            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            ;

            fis.close();
            byte[] mdbytes = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100,
                        16).substring(1));
            }

            //hashBox.setText(sb.toString());
            chagehash = sb.toString(); //결과값 가져오기
        }catch (Exception e){

        }
        return;
    }
    Thread thread1 = new Thread(new Runnable() {
        @Override
        public void run() {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://djqkdaos.iptime.org:8084/nullserver/upload?filehash="+chagehash+"&userid="+googleuser_return+"&msg="+oneline_msg);

            try {
                MultipartEntity entity = new MultipartEntity();
                System.out.println("http 업로드 동작 파일경로 : "+upload_filePath);//이거 수정안한거 아니가
                File file = new File(upload_filePath);
                entity.addPart("files", new FileBody(file));

                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);


                HttpEntity resEntity = response.getEntity();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();

                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }
            } catch (ClientProtocolException e) {
                System.out.println("오류발생1 : " + e); //에러 표시시켜봥
            } catch (IOException e) {
                System.out.println("오류발생2 : " + e);
            }

        }
    });
    //앱종료 하기 위한 소스----------------------------------------------

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //빽(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        if((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder d = new AlertDialog.Builder(firstGetrating_Avtivity);
            d.setTitle("종료여부");
            d.setMessage("정말 종료 하시겠습니꺄?");
            //d.setIcon(R.drawable.ic_launcher);

            d.setPositiveButton("예",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    firstGetrating_Avtivity.finish();
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
