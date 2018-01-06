package com.jit.silly.hlnews.personal;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jit.silly.hlnews.MainActivity;
import com.jit.silly.hlnews.MyApplication;
import com.jit.silly.hlnews.R;
import com.jit.silly.hlnews.Url.UrlAPI;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static com.jit.silly.hlnews.MyApplication.Rgemail;

/**
 * Created by moqiandemac on 2017/6/19.
 */

public class Register_Profile extends Fragment {
    private static final int IMAGE = 1;
    EditText etBirth, etPsw1, etPsw2, etInfo, etNick;
    RadioButton rbSex;
    RadioGroup rgSex;
    CircleImageView headImage;
    Bitmap image;
    TextView tvSubmit;
    boolean h = true;
    returnList returnInfo;
    String imagePath;
    String newimagePath = " ";
    String imageName;
    int sex;
    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View register_profile = inflater.inflate(R.layout.register_profile, container, false);
        verifyStoragePermissions(getActivity());
        ImageView image_cancel_p = (ImageView) register_profile.findViewById(R.id.cancel_p_rg);
        etPsw1 = (EditText) register_profile.findViewById(R.id.et_password1);
        etPsw2 = (EditText) register_profile.findViewById(R.id.et_password2);
        etNick = (EditText) register_profile.findViewById(R.id.et_nickname);
        etBirth = (EditText) register_profile.findViewById(R.id.et_birth);
        etInfo = (EditText) register_profile.findViewById(R.id.et_intro);
        tvSubmit = (TextView) register_profile.findViewById(R.id.tv_pfsubmit);
        rgSex = (RadioGroup) register_profile.findViewById(R.id.rg_sex);

        image_cancel_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                getActivity().setResult(1, intent);
                getActivity().finish();
            }
        });
        headImage = (CircleImageView) register_profile.findViewById(R.id.head_image);
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
                h = false;
            }
        });

        etBirth.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        etBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                rbSex = (RadioButton) register_profile.findViewById(group.getCheckedRadioButtonId());
                if (rbSex.getText().toString().equals("male")) {
                    headImage.setImageResource(R.drawable.immale);
                }
                if (rbSex.getText().toString().equals("female")) {
                    headImage.setImageResource(R.drawable.imfemale);
                }
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AVLoadingIndicatorView loadingIndicatorView = (AVLoadingIndicatorView)getActivity().findViewById(R.id.reg_load);
                loadingIndicatorView.setVisibility(View.VISIBLE);
                if (etBirth.getText().toString().equals("") || etNick.getText().toString().equals("") ||
                        etPsw1.getText().toString().equals("") || etPsw2.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), R.string.text_null, Toast.LENGTH_SHORT).show();
                } else {
                    if (rbSex.getText().toString().equals("female")) {
                        sex = 1;
                    } else {
                        sex = 0;
                    }
                    if (!newimagePath.equals(" ")) {
                        Log.i("image", newimagePath);
                        OkGo.post(UrlAPI.urlUpHead)
                                .params("upload", new File(newimagePath))
                                .params("savePath", "c:\\headimage")
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        Log.i("sds", "onSuccess: ");
                                        deleteFile(newimagePath);
                                    }
                                });
                    }

                    OkGo.post(UrlAPI.urlReg)
                            .params("username", Rgemail)
                            .params("password", etPsw1.getText().toString())
                            .params("sex", sex)
                            .params("nickname", etNick.getText().toString())
                            .params("birthday", etBirth.getText().toString())
                            .params("introduction", etInfo.getText().toString())
                            .params("imageUrl", "http://106.15.194.192:8080/headimage/" + imageName + ".png")
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Gson gson = new Gson();
                                    returnInfo = gson.fromJson(s, returnList.class);
                                    Log.i("code", returnInfo.getCode() + "");
                                    if (returnInfo.getCode() == 200 && returnInfo.getMsg().equals("success")) {
                                        Intent intent = getActivity().getIntent();
                                        getActivity().setResult(0, intent);
                                        getActivity().finish();

                                    } else {
                                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                        loadingIndicatorView.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                }
            }
        });

        return register_profile;
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etBirth.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);

            imageName = removeSuffix(Rgemail);
            newimagePath = Environment.getExternalStorageDirectory() + "/" + imageName + ".png";
            Log.i("reg", "imageName:" + imagePath);
            copyImage(imagePath, newimagePath);

            Bitmap bm = BitmapFactory.decodeFile(newimagePath);
            image = bm;
            ((CircleImageView) getView().findViewById(R.id.head_image)).setImageBitmap(image);
            c.close();
        }
    }

    public void copyImage(String oldPath, String newPath) {
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

    public void deleteFile(String PATH) {
        File file = new File(PATH);
        if (file.exists()) {
            file.delete();
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
