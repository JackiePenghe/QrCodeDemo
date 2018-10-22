package com.jackiepenghe.qrcodedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jackiepenghe.baselibrary.BaseAppCompatActivity;
import com.jackiepenghe.baselibrary.Tool;
import com.jackiepenghe.qrcodecore.BarcodeType;
import com.jackiepenghe.qrcodecore.QRCodeView;
import com.jackiepenghe.zbarlibrary.BarcodeFormat;
import com.jackiepenghe.zbarlibrary.ZBarView;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author jackie
 */
public class ZBarScanActivity extends BaseAppCompatActivity {

    private ZBarView zBarView;
    private QRCodeView.Delegate delegateListener = new QRCodeView.Delegate() {
        @Override
        public void onScanQRCodeSuccess(String result) {
            Tool.toastL(ZBarScanActivity.this,"resut = " + result);
            vibrate();
            zBarView.startSpot(); // 延迟0.5秒后开始识别
        }

        @Override
        public void onScanQRCodeOpenCameraError() {

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
        return R.layout.activity_zbar_scan;
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
        zBarView = findViewById(R.id.zbar_view);
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
        zBarView.setDelegate(delegateListener);
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

    @Override
    protected void onStart() {
        super.onStart();
        zBarView.startCamera();
        zBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
    }

    @Override
    protected void onStop() {
        super.onStop();
        zBarView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zBarView.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_preview:
                zBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
                break;
            case R.id.stop_preview:
                zBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
                break;
            case R.id.start_spot:
                zBarView.startSpot(); // 延迟0.5秒后开始识别
                break;
            case R.id.stop_spot:
                zBarView.stopSpot(); // 停止识别
                break;
            case R.id.start_spot_showrect:
                zBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.stop_spot_hiddenrect:
                zBarView.stopSpotAndHiddenRect(); // 停止识别，并且隐藏扫描框
                break;
            case R.id.show_scan_rect:
                zBarView.showScanRect(); // 显示扫描框
                break;
            case R.id.hidden_scan_rect:
                zBarView.hiddenScanRect(); // 隐藏扫描框
                break;
            case R.id.decode_scan_box_area:
                zBarView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
                break;
            case R.id.decode_full_screen_area:
                zBarView.getScanBoxView().setOnlyDecodeScanBoxArea(false); // 识别整个屏幕中的码
                break;
            case R.id.open_flashlight:
                zBarView.openFlashlight(); // 打开闪光灯
                break;
            case R.id.close_flashlight:
                zBarView.closeFlashlight(); // 关闭闪光灯
                break;
            case R.id.scan_one_dimension:
                zBarView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                zBarView.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
                zBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_two_dimension:
                zBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                zBarView.setType(BarcodeType.TWO_DIMENSION, null); // 只识别二维条码
                zBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_qr_code:
                zBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                zBarView.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
                zBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_code128:
                zBarView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                zBarView.setType(BarcodeType.ONLY_CODE_128, null); // 只识别 CODE_128
                zBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_ean13:
                zBarView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                zBarView.setType(BarcodeType.ONLY_EAN_13, null); // 只识别 EAN_13
                zBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_high_frequency:
                zBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                zBarView.setType(BarcodeType.HIGH_FREQUENCY, null); // 只识别高频率格式，包括 QR_CODE、EAN_13、CODE_128
                zBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_all:
                zBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                zBarView.setType(BarcodeType.ALL, null); // 识别所有类型的码
                zBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_custom:
                zBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式

                List<BarcodeFormat> formatList = new ArrayList<>();
                formatList.add(BarcodeFormat.QRCODE);
                formatList.add(BarcodeFormat.EAN13);
                formatList.add(BarcodeFormat.CODE128);
                zBarView.setType(BarcodeType.CUSTOM, formatList); // 自定义识别的类型

                zBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.choose_qrcde_from_gallery:
                /*
                从相册选取二维码图片，这里为了方便演示，使用一个的是固定的图片
                https://github.com/bingoogolapple/BGAPhotoPicker-Android
                这个库来从图库中选择二维码图片，这个库不是必须的，你也可以通过自己的方式从图库中选择图片
                 */
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qrcode);
                zBarView.decodeQRCode(bitmap);
                break;
            default:
                break;
        }
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
