package com.heytz.trackWrapper;

import android.content.Context;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class starts transmit to activation
 */
public class trackWrapper extends CordovaPlugin {

    private static String TAG = "=====trackWrapper.class====";
    private CallbackContext socketCallbackContext;
    private Context context;
    // 轨迹服务ID
    private long serviceId = 0;
    // 设备标识
    private String entityName = "myTrace";
    private Trace mTrace;
    private LBSTraceClient mTraceClient;
    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    private boolean isNeedObjectStorage = false;

    // 初始化轨迹服务监听器
    private OnTraceListener mTraceListener = null;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity().getApplicationContext();
        int gatherInterval = 5;
        // 打包回传周期(单位:秒)
        int packInterval = 10;
        mTraceClient = new LBSTraceClient(context);

        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
        initListener();
    }


    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        socketCallbackContext = callbackContext;
        if (action.equals("stopTrack")) {
            // 停止采集
            mTraceClient.stopGather(mTraceListener);
            // 停止服务
            mTraceClient.stopTrace(mTrace, mTraceListener);

            return true;
        }
        if (action.equals("startTrack")) {
            String ak = args.getString(0);
            serviceId = Integer.parseInt(args.getString(1));
            entityName = args.getString(2);
            // 初始化轨迹服务
            mTrace = new Trace(serviceId, entityName, isNeedObjectStorage);
            // 初始化轨迹服务客户端
            mTraceClient.startTrace(mTrace, mTraceListener);

            return true;
        }
        return false;
    }

    private void initListener() {
        mTraceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int errorNo, String message) {

            }
            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {
            }

            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {
            }

            // 开启采集回调
            @Override
            public void onStartGatherCallback(int status, String message) {
            }

            // 停止采集回调
            @Override
            public void onStopGatherCallback(int status, String message) {
            }

            // 推送回调
            @Override
            public void onPushCallback(byte messageNo, PushMessage message) {
            }
        };
    }
}
