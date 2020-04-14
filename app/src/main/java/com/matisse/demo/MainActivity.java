package com.matisse.demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.matisse.demo.R;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private PicItemAdapter picItemAdapter = new PicItemAdapter();
    private static final int REQUEST_CODE_CHOOSE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.takeBtn).setOnClickListener(this);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setAdapter(picItemAdapter);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
        GridLayoutManager manager = new GridLayoutManager(this,3);

        recyclerView.setLayoutManager(manager);

    }


    @SuppressLint("CheckResult")
    @Override
    public void onClick(final View v) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            startAction(v);
                        } else {
                            Toast.makeText(MainActivity.this, R.string.permission_request_denied, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public void startAction(View v) {
        if (v.getId() == R.id.takeBtn) {
            Matisse.from(MainActivity.this)
                    .choose(MimeType.ofImage(), false)
                    .countable(true)
                    .capture(true)
                    //必须的
                    .captureStrategy(new CaptureStrategy(true, "com.matisse.demo.fileprovider"))
                    .maxSelectable(9)
                    .showSingleMediaType(true)
//                    .gridExpectedSize()
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    //缩略图
                    .thumbnailScale(0.85f)
                    //使用 glide 引擎
                    .imageEngine(new GlideEngine())
                    //预览照片时隐藏工具栏
                    .autoHideToolbarOnSingleTap(true)
                    //原图开关
                    .originalEnable(true)
                    .forResult(REQUEST_CODE_CHOOSE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            picItemAdapter.setData(Matisse.obtainResult(data));
        }
    }

}
