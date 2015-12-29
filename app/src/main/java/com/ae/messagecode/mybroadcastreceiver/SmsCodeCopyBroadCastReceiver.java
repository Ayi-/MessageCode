package com.ae.messagecode.mybroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.ae.messagecode.Myservice.SmsCodeCopyService;

/**
 * Created by AE on 2015/12/25.
 */
public class SmsCodeCopyBroadCastReceiver extends BroadcastReceiver {

    public static final String SMS_RECEIVED_ACTION ="android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.i("AE","receive");
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
//            Log.i("AE", "sms get!");
            //获取Bundle
            Bundle myBundle = intent.getExtras();
            if (myBundle != null) {
//                Log.i("AE", "myBundle get!");
                //短信存放
                SmsMessage[] smsMessages = null;
                //获取短信那个啥
                Object[] pdus = (Object[]) myBundle.get("pdus");
                smsMessages = new SmsMessage[pdus.length];
                //短信内容
                String smsContent="";
                //循环获取短信

                for (int i = 0; i < smsMessages.length; i++) {
//                    Log.i("AE", "get sms");
                    //版本不同 API不同
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = myBundle.getString("format");
                        smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
//                    Log.i("AE", "get sms content");

                    //短信内容
                    smsContent = smsMessages[i].getDisplayMessageBody();
                    if(smsContent.contains("验证码"))
                    {
                        //收件人
                        String sender = smsMessages[i].getDisplayOriginatingAddress();
                        //时间
                        long date = smsMessages[i].getTimestampMillis();

//                        Log.i("AE", sender + '\t' + smsContent + '\t' + date);
//                        Toast.makeText(context, smsContent, Toast.LENGTH_SHORT).show();

                        //发送数据给服务
                        Intent startIntent = new Intent(context, SmsCodeCopyService.class);
                        startIntent.putExtra("sender",sender);
                        startIntent.putExtra("date",date);
                        startIntent.putExtra("smsContent",smsContent);
                        context.startService(startIntent);
                        break;
                    }

                }

            }
        }
    }

}
