package com.jit.silly.hlnews;

import android.app.Application;

import com.jit.silly.hlnews.personal.VerifyInfo;
import com.lzy.okgo.OkGo;

import java.util.logging.Level;

import com.jit.silly.hlnews.personal.UserInfo;

/**
 * Created by moqiandemac on 2017/6/12.
 */

public class MyApplication extends Application {
    public static UserInfo user1;
    public static String username1;
    public static String password1;
    public static VerifyInfo vercode1;
    public static String Rgemail;
    public static int lg = 1;
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.init(this);
        try {
            OkGo.getInstance()
                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    .debug("OkGo", Level.INFO, true)
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间
                    .setRetryCount(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
