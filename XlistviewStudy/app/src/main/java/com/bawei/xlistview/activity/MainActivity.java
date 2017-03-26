package com.bawei.xlistview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.xlistview.R;
import com.bawei.xlistview.bean.NewsInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import xlistview.bawei.com.xlistviewlibrary.XListView;

public class MainActivity extends AppCompatActivity {

    private XListView xlv;
    private List<String> list = new ArrayList<>();
    private Handler handler = null;
    //channelId=1&startNum=1
    private int channelId = 1;
    private int startNum = 0;
    private JSONArray data;
    private List<NewsInfo> newsInfos = new ArrayList<>();
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
       /* //数据
        getData();*/
        //找控件
        initView();
        //显示刷新时间
        onLoadTime();


    }

    private void initView() {
        xlv = (XListView) findViewById(R.id.xlv);
        xlv.setPullRefreshEnable(true);
        xlv.setPullLoadEnable(true);
        xlv.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                //显示刷新时间
                startNum = 0;
                getServerData();
                onLoadTime();
                Toast.makeText(MainActivity.this, "下拉刷新", Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xlv.stopRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                Toast.makeText(MainActivity.this, "上拉加载", Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startNum++;

                        xlv.stopLoadMore();
                        getServerData();
                    }
                }, 2000);
            }
        });
        //列表展示
        //   xlv.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list));

        //获取网络数据
        getServerData();
    }

    private void getServerData() {
        AsyncHttpClient client = new AsyncHttpClient();
        //http://www.93.gov.cn/93app/data.do?channelId=1&startNum=1
        String url = "http://www.93.gov.cn/93app/data.do?" + "channelId=" + 0 + "&startNum=" + startNum;
        client.get(this, url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MainActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(MainActivity.this, "请求数据成功", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        NewsInfo newsInfo = new NewsInfo();
                        JSONObject jo = data.getJSONObject(i);
                        String fromname = jo.getString("FROMNAME");
                        newsInfo.setFROMNAME(fromname);
                        newsInfos.add(newsInfo);
                    }
                    //  initList();
                    initListData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initListData() {
        if (adapter == null) {
            adapter = new NewsAdapter();
            xlv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    private void initList() {
        MyAdapter adapter = new MyAdapter();
        xlv.setAdapter(adapter);

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.length();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(MainActivity.this);
            try {
                JSONObject jsonObject = data.getJSONObject(position);
                textView.setText(jsonObject.getString("FROMNAME"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return textView;
        }


    }

    private class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(MainActivity.this);
            textView.setText(newsInfos.get(position).getFROMNAME());
            return textView;
        }
    }

    private void onLoadTime() {
        xlv.stopRefresh();
        xlv.stopLoadMore();
        //获取系统当前时间
        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM--dd HH:mm:ss");
        Date date = new Date(timeMillis);
        String time = format.format(date);
        xlv.setRefreshTime(time);

    }

    private void getData() {
        for (int i = 0; i < 20; i++) {
            list.add("条目数据展示：" + i);
        }
    }


}
