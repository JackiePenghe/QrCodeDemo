package com.jackiepenghe.qrcodedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.jackiepenghe.baselibrary.BaseAppCompatActivity;
import com.jackiepenghe.baselibrary.Tool;
import com.jackiepenghe.qrcodecore.BarcodeType;
import com.jackiepenghe.qrcodecore.QRCodeView;
import com.jackiepenghe.qrcodelibrary.ZXingView;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ZXingScanActivity extends BaseAppCompatActivity {

    private static final String TAG = ZXingScanActivity.class.getSimpleName();
    private boolean notDestroyed = true;
    private ZXingView mZXingView;
    private QRCodeView.Delegate delegate = new QRCodeView.Delegate() {
        @Override
        public void onScanQRCodeSuccess(String result) {
            Tool.toastL(ZXingScanActivity.this, "result = " + result);
            vibrate();
            mZXingView.startSpot(); // 延迟0.5秒后开始识别
        }

        @Override
        public void onScanQRCodeOpenCameraError() {
            Tool.toastL(ZXingScanActivity.this, "onScanQRCodeOpenCameraError");
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
        return R.layout.activity_zxing_scan;
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
        mZXingView = findViewById(R.id.zxing_view);
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
        mZXingView.setDelegate(delegate);
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
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy();
        notDestroyed = false;
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_preview:
                mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
                break;
            case R.id.stop_preview:
                mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
                break;
            case R.id.start_spot:
                mZXingView.startSpot(); // 延迟0.5秒后开始识别
                break;
            case R.id.stop_spot:
                mZXingView.stopSpot(); // 停止识别
                break;
            case R.id.start_spot_showrect:
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.stop_spot_hiddenrect:
                mZXingView.stopSpotAndHiddenRect(); // 停止识别，并且隐藏扫描框
                break;
            case R.id.show_scan_rect:
                mZXingView.showScanRect(); // 显示扫描框
                break;
            case R.id.hidden_scan_rect:
                mZXingView.hiddenScanRect(); // 隐藏扫描框
                break;
            case R.id.decode_scan_box_area:
                mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
                break;
            case R.id.decode_full_screen_area:
                mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(false); // 识别整个屏幕中的码
                break;
            case R.id.open_flashlight:
                mZXingView.openFlashlight(); // 打开闪光灯
                break;
            case R.id.close_flashlight:
                mZXingView.closeFlashlight(); // 关闭闪光灯
                break;
            case R.id.scan_one_dimension:
                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                mZXingView.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_two_dimension:
                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                mZXingView.setType(BarcodeType.TWO_DIMENSION, null); // 只识别二维条码
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_qr_code:
                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                mZXingView.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_code128:
                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                mZXingView.setType(BarcodeType.ONLY_CODE_128, null); // 只识别 CODE_128
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_ean13:
                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                mZXingView.setType(BarcodeType.ONLY_EAN_13, null); // 只识别 EAN_13
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_high_frequency:
                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                mZXingView.setType(BarcodeType.HIGH_FREQUENCY, null); // 只识别高频率格式，包括 QR_CODE、EAN_13、CODE_128
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_all:
                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                mZXingView.setType(BarcodeType.ALL, null); // 识别所有类型的码
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.scan_custom:
                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式

                Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
                List<BarcodeFormat> formatList = new ArrayList<>();
                formatList.add(BarcodeFormat.QR_CODE);
                formatList.add(BarcodeFormat.EAN_13);
                formatList.add(BarcodeFormat.CODE_128);
                hintMap.put(DecodeHintType.POSSIBLE_FORMATS, formatList); // 可能的编码格式
                hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); // 花更多的时间用于寻找图上的编码，优化准确性，但不优化速度
                hintMap.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 编码字符集
                mZXingView.setType(BarcodeType.CUSTOM, hintMap); // 自定义识别的类型

                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
                break;
            case R.id.choose_qrcde_from_gallery:
                /*
                从相册选取二维码图片，这里为了方便演示，使用一个的是固定的图片
                https://github.com/bingoogolapple/BGAPhotoPicker-Android
                这个库来从图库中选择二维码图片，这个库不是必须的，你也可以通过自己的方式从图库中选择图片
                 */
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qrcode);
                mZXingView.decodeQRCode(bitmap);
                break;
            default:
                break;
        }
    }
}
