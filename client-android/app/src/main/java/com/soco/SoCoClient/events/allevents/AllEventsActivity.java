package com.soco.SoCoClient.events.allevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient._ref.Actor;
import com.soco.SoCoClient._ref.MyAdapter;
import com.soco.SoCoClient.events.CreateEventActivity;

import java.util.ArrayList;
import java.util.List;

public class AllEventsActivity extends ActionBarActivity {

    static final String tag = "AllEventsActivity";

    private RecyclerView mRecyclerView;

    private MyAdapter myAdapter;

    private List<Actor> actors = new ArrayList<Actor>();

    private String[] names = { "朱茵", "张柏芝", "张敏", "巩俐", "黄圣依", "赵薇", "莫文蔚", "如花" };

    private String[] pics = { "p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        //test below
        actors.add(new Actor("朱茵", "p1"));
        actors.add(new Actor("朱茵", "p1"));
        actors.add(new Actor("朱茵", "p1"));

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        // 设置LinearLayoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 设置ItemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        mRecyclerView.setHasFixedSize(true);
        // 初始化自定义的适配器
        myAdapter = new MyAdapter(this, actors);
        // 为mRecyclerView设置适配器
        mRecyclerView.setAdapter(myAdapter);
    }

    public void createevent(View view){
        Log.v(tag, "create event");
        Intent i = new Intent(this, CreateEventActivity.class);
        startActivity(i);
    }

}
