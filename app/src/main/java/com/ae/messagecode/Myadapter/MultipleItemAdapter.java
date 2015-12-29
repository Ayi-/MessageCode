package com.ae.messagecode.Myadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ae.messagecode.R;
import com.ae.messagecode.model.SmsCode;

import java.util.List;


/**
 * Created by AE on 2015/12/25.
 */
public class MultipleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    List<SmsCode> messageList;


    public MultipleItemAdapter(Context context, List<SmsCode> messageList) {
        this.mContext = context;
        this.messageList = messageList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public MultipleItemAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    //创建新View，被LayoutManager所调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_text, parent, false));

    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ItemViewHolder) holder).sendTextView.setText(messageList.get(position).getSender());
        ((ItemViewHolder) holder).dataTextView.setText(messageList.get(position).getTime());
        ((ItemViewHolder) holder).codeTextView.setText(messageList.get(position).getCode());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView sendTextView;
        TextView dataTextView;
        TextView codeTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            sendTextView = (TextView) itemView.findViewById(R.id.tv_sender);
            dataTextView = (TextView) itemView.findViewById(R.id.tv_date);
            codeTextView = (TextView) itemView.findViewById(R.id.tv_code);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }

}
