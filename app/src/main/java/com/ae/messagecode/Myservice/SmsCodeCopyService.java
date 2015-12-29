package com.ae.messagecode.Myservice;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.ae.messagecode.model.SmsCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AE on 2015/12/25.
 */
public class SmsCodeCopyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("codeService","start");

        if (intent!=null) {
            //发送人
            String sender = intent.getStringExtra("sender");
            //时间
            Long date = intent.getLongExtra("date", 0);
            //短信内容
            String smsContent = intent.getStringExtra("smsContent");
            if (!sender.equals("") && date != 0 && !smsContent.equals("")) {

                Date timeDate = new Date(date);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //时间
                String time = simpleDateFormat.format(timeDate);
                Pattern p = Pattern.compile("[\\d]{4,8}");
                Log.i("codeService", smsContent);
                Matcher mCode = p.matcher(smsContent);
                if (mCode.find()) {
                    Log.i("codeService", mCode.group());
                    String code = mCode.group();
                    //复制到剪切板
                    ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData cd = ClipData.newPlainText("label", code);
                    clip.setPrimaryClip(cd); // 复制
                    SmsCode smsCode = new SmsCode();
                    smsCode.setCode(code);
                    smsCode.setSender(sender);
                    smsCode.setTime(time);
                    smsCode.save();
                } else
                    Toast.makeText(SmsCodeCopyService.this, "未能读取到验证码", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
