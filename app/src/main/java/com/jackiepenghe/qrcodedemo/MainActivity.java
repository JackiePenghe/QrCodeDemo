package com.jackiepenghe.qrcodedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jackiepenghe.baselibrary.BaseAppCompatActivity;
import com.jackiepenghe.baselibrary.Tool;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.Setting;

import java.util.List;

public class MainActivity extends BaseAppCompatActivity {

    private Button scanButton;
    private Button createButton;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.scan_btn:
                    AndPermission.with(MainActivity.this)
                            .runtime()
                            .permission(Permission.CAMERA)
                            .onGranted(onGrantedListener)
                            .onDenied(onDeniedListener)
                            .rationale(rationaleListener)
                            .start();
                    break;
                case R.id.create_btn:
                    Intent intent = new Intent(MainActivity.this,CreateQrCodeActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
    private Action<List<String>> onGrantedListener = new Action<List<String>>() {
        @Override
        public void onAction(List<String> permissions) {
            toScanActivity();
        }
    };
    private Action<List<String>> onDeniedListener = new Action<List<String>>() {
        @Override
        public void onAction(List<String> permissions) {
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                AndPermission.with(MainActivity.this)
                        .runtime()
                        .setting()
                        .onComeback(onComebackListener)
                        .start();
                return;
            }
            Tool.toastL(MainActivity.this, R.string.no_camara_permission);
        }
    };
    private Rationale<List<String>> rationaleListener = new Rationale<List<String>>() {
        @Override
        public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.no_camara_permission)
                    .setMessage(R.string.request_camara_permission)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executor.execute();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executor.cancel();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    };
    private Setting.Action onComebackListener = new Setting.Action() {
        @Override
        public void onAction() {
            scanButton.performClick();
        }
    };

    /**
     * 标题栏的返回按钮被按下的时候回调此函数
     */
    @Override
    protected void titleBackClicked() {
        onBackPressed();
    }

    /**
     * 在设置布局之前需要进行的操作
     */
    @Override
    protected void doBeforeSetLayout() {

    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    /**
     * 在设置布局之后，进行其他操作之前，所需要初始化的数据
     */
    @Override
    protected void doBeforeInitOthers() {

    }

    /**
     * 初始化布局控件
     */
    @Override
    protected void initViews() {
        scanButton = findViewById(R.id.scan_btn);
        createButton = findViewById(R.id.create_btn);
    }

    /**
     * 初始化控件数据
     */
    @Override
    protected void initViewData() {

    }

    /**
     * 初始化其他数据
     */
    @Override
    protected void initOtherData() {

    }

    /**
     * 初始化事件
     */
    @Override
    protected void initEvents() {
        scanButton.setOnClickListener(onClickListener);
        createButton.setOnClickListener(onClickListener);
    }

    /**
     * 在最后进行的操作
     */
    @Override
    protected void doAfterAll() {

    }

    /**
     * 设置菜单
     *
     * @param menu 菜单
     * @return 只是重写 public boolean onCreateOptionsMenu(Menu menu)
     */
    @Override
    protected boolean createOptionsMenu(Menu menu) {
        return false;
    }

    /**
     * 设置菜单监听
     *
     * @param item 菜单的item
     * @return true表示处理了监听事件
     */
    @Override
    protected boolean optionsItemSelected(MenuItem item) {
        return false;
    }

    private void toScanActivity() {
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        startActivity(intent);
    }

}
