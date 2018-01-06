package com.jit.silly.hlnews.personal;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jit.silly.hlnews.R;
import com.jit.silly.hlnews.Url.UrlAPI;
import com.jit.silly.hlnews.returnSuccess;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static com.jit.silly.hlnews.MyApplication.Rgemail;
import static com.jit.silly.hlnews.MyApplication.user1;

public class ProfileActivity extends AppCompatActivity {
    private static final int IMAGE = 1;
    ImageView cancelPro;
    TextView submitPro;
    CircleImageView headimagePro;
    EditText nicknamePro, birthPro, introPro;
    returnSuccess returnInfo;
    String imagePath;
    String newimagePath;
    String imageName;
    int x = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        verifyStoragePermissions(this);
        cancelPro = (ImageView) findViewById(R.id.cancel_p_pro);
        submitPro = (TextView) findViewById(R.id.tv_pfsubmit_pro);
        headimagePro = (CircleImageView) findViewById(R.id.head_image_pro);
        nicknamePro = (EditText) findViewById(R.id.et_nickname_pro);
        birthPro = (EditText) findViewById(R.id.et_birth_pro);
        introPro = (EditText) findViewById(R.id.et_intro_pro);

        if (user1.isSex() == 1 && user1.getImageUrl().length() < 10) {
            Glide.with(this).load(R.drawable.imfemale).into(headimagePro);
        }

        cancelPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Glide.with(this).load(user1.getImageUrl()).into(headimagePro);
        nicknamePro.setText(user1.getNickname());
        birthPro.setText(user1.getBirthday());
        introPro.setText(user1.getIntroduction());

        headimagePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = 0;
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        });

        birthPro.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        birthPro.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });

        submitPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("reg", "x:" + x);
                if (x == 0) {
                    OkGo.post(UrlAPI.urlUpHead)
                            .params("upload", new File(newimagePath))
                            .params("savePath", "c:\\headimage")
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Log.i("sds", "onSuccess: ");
                                    deleteImage(newimagePath);
                                }
                            });
                }
                OkGo.get(UrlAPI.urlReUser)
                        .params("id", user1.getId())
                        .params("username", user1.getUsername())
                        .params("password", user1.getPassword())
                        .params("nickname", nicknamePro.getText().toString())
                        .params("sex", user1.isSex())
                        .params("birthday", birthPro.getText().toString())
                        .params("introduction", introPro.getText().toString())
                        .params("imageUrl", user1.getImageUrl())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Gson gsonFg = new Gson();
                                returnInfo = gsonFg.fromJson(s, returnSuccess.class);
                                Log.i("code", "onSuccess: " + returnInfo.getCode());
                                if (returnInfo.getCode() == 200 && returnInfo.getMsg().equals("success")) {
                                    user1.setNickname(nicknamePro.getText().toString());
                                    user1.setBirthday(birthPro.getText().toString());
                                    user1.setIntroduction(introPro.getText().toString());
                                    Intent intent = getIntent();
                                    setResult(10, intent);
                                    Log.i("code", "" + returnInfo.getCode());
                                    finish();
                                }
                            }
                        });

            }
        });

    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthPro.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("reg", "requestCode" + requestCode);
        Log.i("reg", "resultCode" + resultCode);
        Log.i("reg", "data" + data);
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);

            imageName = removeSuffix1(user1.getUsername());
            newimagePath = Environment.getExternalStorageDirectory() + "/" + imageName + ".png";
            Log.i("reg", "imageName:" + imagePath);
            copyImage(imagePath, newimagePath);
            Log.i("reg", "newimageName:" + newimagePath);
            Bitmap bm = BitmapFactory.decodeFile(newimagePath);
            ((CircleImageView) findViewById(R.id.head_image_pro)).setImageBitmap(bm);
            c.close();
        }
    }

    private void copyImage(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteImage(String PATH) {
        File file = new File(PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    private String removeSuffix1(String name) {
        StringBuffer sb = new StringBuffer(name);
        int n = sb.indexOf("@", 0);
        if (n != -1) {
            return sb.substring(0, n).toString();
        } else {
            return sb.toString();
        }
    }

    private static final String[] PERMISSION_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 100;

    private void verifyStoragePermissions(Activity activity) {
        int permissionWrite = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_EXTERNAL_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
