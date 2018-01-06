package com.jit.silly.hlnews.Baoliao;

import android.Manifest;
import android.app.Activity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jit.silly.hlnews.R;
import com.jit.silly.hlnews.Url.UrlAPI;
import com.jit.silly.hlnews.returnSuccess;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

import static com.jit.silly.hlnews.MyApplication.user1;

public class BaoActivity extends AppCompatActivity {
    private static final int IMAGE = 1;
    ImageView[] bpic = new ImageView[3];
    Bitmap[] bp = new Bitmap[3];
    ImageView addPic;
    EditText etT, etC;
    TextView bcancel, bpublish, btext;
    String[] imagePath = new String[3];
    String[] newimagePath = new String[3];
    String[] imageName = new String[3];
    String[] newimagePath2 = new String[3];
    int picNum = 0;
    returnSuccess returnS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao);
        verifyStoragePermissions(this);
        addPic = (ImageView) findViewById(R.id.badd_pic);

        bpic[0] = (ImageView) findViewById(R.id.boom_pic1);
        bpic[1] = (ImageView) findViewById(R.id.boom_pic2);
        bpic[2] = (ImageView) findViewById(R.id.boom_pic3);

        bcancel = (TextView) findViewById(R.id.boom_cancel);
        bpublish = (TextView) findViewById(R.id.boom_publish);

        etT = (EditText) findViewById(R.id.et_boom_t);
        etC = (EditText) findViewById(R.id.et_boom_c);
        btext = (TextView) findViewById(R.id.boom_text);
        etC.addTextChangedListener(mTextWatcher);

        bcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaoActivity.this.finish();
            }
        });
        bpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVLoadingIndicatorView loadingIndicatorView = (AVLoadingIndicatorView)findViewById(R.id.bao_load);
                loadingIndicatorView.setVisibility(View.VISIBLE);
                Log.i("pub", "onClick: ");
                if (!etT.getText().equals("") && !etC.getText().equals("")) {
                    Log.i("hot", "title:" + etT.getText().toString());
                    Log.i("hot", "context:" + etT.getText().toString());

                    for (int i = 0; i < 3; i++) {
                        imageName[i] = " ";
                    }

                    for (int i = 0; i < picNum; i++) {
                        imageName[i] = etT.getText().toString() + removeSuffix(user1.getUsername()) + i;
                        Log.i("hot", "image:" + imageName[i]);
                        newimagePath[i] = Environment.getExternalStorageDirectory() + "/" + imageName[i] + ".png";
                        copyFile(imagePath[i], newimagePath[i]);
                    }


                    for (int i = 0; i < 3; i++) {
                        if (imageName[i].equals(" ")) {
                            Log.i("image", i + " " + imageName[i]);
                            newimagePath2[i] = " ";
                        } else {
                            Log.i("image", i + " " + imageName[i]);
                            newimagePath2[i] = "http://106.15.194.192:8080/baoliao/" + imageName[i] + ".png";
                            Log.i("image", i + " " + newimagePath2[i]);
                        }
                    }

                    for (int j = 0; j < 3; j++) {
                        if (!imageName[j].equals(" ")) {
                            final int finalJ = j;
                            OkGo.post(UrlAPI.urlUpHead)
                                    .params("upload", new File(newimagePath[j]))
                                    .params("savePath", "c:\\baoliao")
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            Log.i("sds", "onSuccess: " + finalJ);
                                            deletePic(newimagePath[finalJ]);
                                        }
                                    });
                        }
                    }
                    OkGo.post(UrlAPI.urlUpBao)
                            .params("editorid", user1.getId())
                            .params("title", etT.getText().toString())
                            .params("content", etC.getText().toString())
                            .params("picurl1", newimagePath2[0])
                            .params("picurl2", newimagePath2[1])
                            .params("picurl3", newimagePath2[2])
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Gson gson = new Gson();
                                    Log.i("hot", ""+user1.getId()+" " +etT.getText().toString()+" "+etC.getText().toString()
                                    +" " +newimagePath2[0]+ " " +newimagePath2[1]+ " " + newimagePath2[2]);
                                    returnS = gson.fromJson(s, returnSuccess.class);
                                    Log.i("code", returnS.getCode() + "");
                                    Intent intent = getIntent();
                                    setResult(1, intent);
                                    finish();
                                }
                            });

                } else {
                    Toast.makeText(BaoActivity.this, R.string.text_null, Toast.LENGTH_SHORT).show();
                    loadingIndicatorView.setVisibility(View.INVISIBLE);
                }
            }
        });

        bpic[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picNum == 1) {
                    Log.i("picNum", "" + picNum);
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE);
                }
            }
        });
        bpic[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picNum == 2) {
                    Log.i("picNum", "" + picNum);
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE);
                }
            }
        });

        bpic[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("picNum", "" + picNum);
                if (picNum == 3) {
                    for (int i = 0; i < 2; i++) {
                        bp[i] = bp[i + 1];
                        imagePath[i] = imagePath[i + 1];
                        bpic[i].setImageBitmap(bp[i]);
                    }
                    bpic[2].setImageResource(R.drawable.add_pic);
                    bpic[2].setLongClickable(false);
                    picNum--;
                    Toast.makeText(BaoActivity.this, R.string.hot_pic_del, Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (picNum == 2) {
                    bp[0] = bp[1];
                    imagePath[0] = imagePath[1];
                    bpic[0].setImageBitmap(bp[0]);
                    bpic[1].setImageResource(R.drawable.add_pic);
                    bpic[1].setLongClickable(false);
                    bpic[2].setImageResource(R.drawable.false_pic);
                    bpic[2].setClickable(false);
                    bpic[2].setLongClickable(false);
                    picNum--;
                    Toast.makeText(BaoActivity.this, R.string.hot_pic_del, Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (picNum == 1) {
                    for (int i = 0; i < 2; i++) {
                        bpic[i].setImageResource(R.drawable.false_pic);
                        bpic[i].setClickable(false);
                        bpic[i].setLongClickable(false);
                    }
                    picNum--;
                    Toast.makeText(BaoActivity.this, R.string.hot_pic_del, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        bpic[1].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("picNum", "" + picNum);
                if (picNum == 2) {
                    bpic[1].setImageResource(R.drawable.add_pic);
                    bpic[1].setLongClickable(false);
                    bpic[2].setImageResource(R.drawable.false_pic);
                    bpic[2].setLongClickable(false);
                    picNum--;
                    Toast.makeText(BaoActivity.this, R.string.hot_pic_del, Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (picNum == 3) {
                    bp[1] = bp[2];
                    imagePath[1] = imagePath[2];
                    bpic[1].setImageBitmap(bp[1]);
                    bpic[2].setImageResource(R.drawable.add_pic);
                    bpic[2].setLongClickable(false);
                    picNum--;
                    Toast.makeText(BaoActivity.this, R.string.hot_pic_del, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        bpic[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (picNum == 3) {
                    bpic[2].setImageResource(R.drawable.add_pic);
                    bpic[2].setLongClickable(false);
                    picNum--;
                    Toast.makeText(BaoActivity.this, R.string.hot_pic_del, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });


        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("picNum", "" + picNum);
                if (picNum < 3) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE);
                } else {
                    Toast.makeText(BaoActivity.this, R.string.max_pics, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath[picNum] = c.getString(columnIndex);
            Bitmap bm = BitmapFactory.decodeFile(imagePath[picNum]);
            addPics(bm);
            c.close();
        }
    }

    public void addPics(Bitmap bm) {
        if (picNum < 3) {
            bp[picNum] = bm;
            bpic[picNum].setImageBitmap(bm);
            if (picNum < 2) {
                bpic[picNum + 1].setImageResource(R.drawable.add_pic);
                bpic[picNum + 1].setClickable(true);
            }
            bpic[picNum].setLongClickable(true);
            picNum++;
        }
    }

    public void copyFile(String oldPath, String newPath) {
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
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    public void deletePic(String PATH) {
        File file[] = new File[3];
        for (int i = 0; i < 3; i++) {
            file[i] = new File(PATH);
            if (file[i].exists()) {
                file[i].delete();
            }
        }
    }

    public String removeSuffix(String name) {
        StringBuffer sb = new StringBuffer(name);
        int n = sb.indexOf("@", 0);
        if (n != -1) {
            return sb.substring(0, n).toString();
        } else {
            return sb.toString();
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
//          mTextView.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            editStart = etC.getSelectionStart();
            editEnd = etC.getSelectionEnd();
            btext.setText(temp.length() + "/200");
            if (temp.length() > 200) {
                Toast.makeText(BaoActivity.this,
                        R.string.word_restriction, Toast.LENGTH_SHORT)
                        .show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                etC.setText(s);
                etC.setSelection(tempSelection);
            }
        }
    };

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
