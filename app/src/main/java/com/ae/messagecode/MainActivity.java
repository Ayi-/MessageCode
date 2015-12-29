package com.ae.messagecode;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ae.messagecode.Myadapter.MultipleItemAdapter;
import com.ae.messagecode.model.SmsCode;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<SmsCode> messageList;

    private final int pageItemNum = 7;
    //记录当前刷新页数
    private int page = -1;
    //记录当前加载项位置
    private int itemPosition = -1;
    private LinearLayoutManager linearLayoutManager;
    private boolean onLoading;
    private MultipleItemAdapter multipleItemAdapter;
    //设置Toast,避免重复出现
    private Toast mToast;

    //计时
    private long firsttime=0;
    private long secondtime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase db = Connector.getDatabase();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

//        //这里用线性显示 类似于listview
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        // 这里用线性宫格显示 类似于grid view
//        // mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        // 这里用线性宫格显示 类似于瀑布流
//        // mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
//        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        messageList = DataSupport.order("id desc")
                .limit(pageItemNum)
                        //.offset(10)
                .find(SmsCode.class);
        page = 1;
        itemPosition = messageList.size() % pageItemNum;
        Log.i("pos", itemPosition + "");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.i("scroll", dx + "  " + dy);
                secondtime = SystemClock.uptimeMillis();
                if(secondtime-firsttime>3000)
                {
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = multipleItemAdapter.getItemCount();
                int pastItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (!onLoading) {

                    if ((pastItems + visibleItemCount) >= totalItemCount && dy>0) {

                        showToast("正在加载...");
                        onLoading = true;
                        new LoadCodeTask().execute();
                        // load something new and set adapter notifyDatasetChanged
                        // 记得在 load something 完了以后把 onLoading 赋值为 false

                    }
                }
            }
            }
        });
        multipleItemAdapter = new MultipleItemAdapter(this, messageList);
        mRecyclerView.setAdapter(multipleItemAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mRecyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {
//                @SuppressLint("LongLogTag")
//                @Override
//                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    Log.i("setOnScrollChangeListener", v + "");
//
//                    Log.i("setOnScrollChangeListener", scrollX + "," + scrollY);
//                    Log.i("setOnScrollChangeListener old", oldScrollX + "," + oldScrollY);
//                }
//            });
//        } else {


//        }

    }


    class LoadCodeTask extends AsyncTask<Void, Void, List<SmsCode>> {

        //后台
        @Override
        protected List<SmsCode> doInBackground(Void... params) {
            firsttime = SystemClock.uptimeMillis();
            return DataSupport.order("id desc")
                    .offset(page * pageItemNum + itemPosition)
                    .limit(pageItemNum)
                    .find(SmsCode.class);
        }

        @Override
        protected void onPostExecute(List<SmsCode> list) {
            int listSize = list.size();
            Log.i("load", listSize + "");
            if (listSize > 0) {
                messageList.addAll(list);
                multipleItemAdapter.notifyDataSetChanged();

                showToast("加载完成");
                if (listSize + itemPosition >= pageItemNum) {
                    page += 1;
                    itemPosition = listSize + itemPosition - pageItemNum;
                } else {
                    itemPosition = listSize + itemPosition;
                }
            } else
                showToast("没有更多数据");
            Log.i("page", page + "  " + itemPosition);
            onLoading = false;
        }

    }

    public void showToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public void onBackPressed() {
        cancelToast();
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        Log.i("state","onRestart");

        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i("state","onResume");
        Log.i("resume",messageList.get(0).getId()+"");
         List<SmsCode> listupda=DataSupport.where("id > ?",String.valueOf(messageList.get(0).getId())).order("id desc")
                    .find(SmsCode.class);
        int listSize = listupda.size();
        Log.i("resume",listSize+"");

        if (listSize > 0) {
            messageList.addAll(0, listupda);
            multipleItemAdapter.notifyDataSetChanged();

            showToast("加载完成");
            if (listSize + itemPosition >= pageItemNum) {
                page += 1;
                itemPosition = listSize + itemPosition - pageItemNum;
            } else {
                itemPosition = listSize + itemPosition;
            }
        }
        super.onResume();

    }
}