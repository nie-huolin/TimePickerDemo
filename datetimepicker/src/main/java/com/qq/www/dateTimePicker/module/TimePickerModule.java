package com.qq.www.dateTimePicker.module;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.weex.plugin.annotation.WeexModule;
import com.benmu.framework.constant.WXEventCenter;
import com.benmu.framework.manager.ManagerFactory;
import com.benmu.framework.manager.impl.dispatcher.DispatchEventManager;
import com.benmu.framework.model.WeexEventBean;
import com.qq.www.dateTimePicker.widget.CustomDatePicker;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * <pre>
 * 项目名：
 * 作　者：	niehl
 * 时　间：	2018/5/31
 * 描  述:
 * </pre>
 */
@WeexModule(name = "dateTimePicker", lazyLoad = true)
public class TimePickerModule extends WXModule {

    private static final String SUCCESS = "success";
    private static final String CANCEL = "cancel";
    private static final String ERROR = "error";
    private static final String RESULT = "result";
    private static final String DATA = "data";

    String title, max, min, titleColor, confirmTitle, confirmTitleColor, cancelTitle, cancelTitleColor, value;

    @JSMethod
    public void open(String params, JSCallback resultCallback) {
        WeexEventBean eventBean = new WeexEventBean();
        eventBean.setKey(WXEventCenter.EVENT_OPEN);
        eventBean.setJsParams(params);
        eventBean.setJscallback(resultCallback);
        eventBean.setContext(mWXSDKInstance.getContext());
        ManagerFactory.getManagerService(DispatchEventManager.class).getBus().post(eventBean);
        datePicker(eventBean.getJsParams(), resultCallback);
    }


    private void datePicker(String params, final JSCallback callback) {
        try {
            JSONObject jsonObject = new JSONObject(params);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            String now = sdf.format(new Date());
            if (jsonObject.has("value")) {
                value = !TextUtils.isEmpty(jsonObject.getString("value")) ? jsonObject.getString("value") : now;
            } else {
                value = now;
            }
            if (jsonObject.has("title")) {
                title = !TextUtils.isEmpty(jsonObject.getString("title")) ? jsonObject.getString("title") : "选择时间";
            } else {
                title = "选择时间";
            }
            if (jsonObject.has("max")) {
                max = !TextUtils.isEmpty(jsonObject.getString("max")) ? jsonObject.getString("max") : "2099-12-31 23:59";
            } else {
                max = "2099-12-31 23:59";
            }
            if (jsonObject.has("min")) {
                min = !TextUtils.isEmpty(jsonObject.getString("min")) ? jsonObject.getString("min") : "1900-12-31 00:00";
            } else {
                min = "1900-12-31 00:00";
            }
            if (jsonObject.has("titleColor")) {
                titleColor = !TextUtils.isEmpty(jsonObject.getString("titleColor")) ? jsonObject.getString("titleColor") : "#0092ff";
            } else {
                titleColor = "#0092ff";
            }
            if (jsonObject.has("confirmTitle")) {
                confirmTitle = !TextUtils.isEmpty(jsonObject.getString("confirmTitle")) ? jsonObject.getString("confirmTitle") : "完成";
            } else {
                confirmTitle = "完成";
            }
            if (jsonObject.has("confirmTitleColor")) {
                confirmTitleColor = !TextUtils.isEmpty(jsonObject.getString("confirmTitleColor")) ? jsonObject.getString("confirmTitleColor") : "#0092ff";
            } else {
                confirmTitleColor = "#0092ff";
            }
            if (jsonObject.has("cancelTitle")) {
                cancelTitle = !TextUtils.isEmpty(jsonObject.getString("cancelTitle")) ? jsonObject.getString("cancelTitle") : "取消";
            } else {
                cancelTitle = "取消";
            }
            if (jsonObject.has("cancelTitleColor")) {
                cancelTitleColor = !TextUtils.isEmpty(jsonObject.getString("cancelTitleColor")) ? jsonObject.getString("cancelTitleColor") : "#0092ff";
            } else {
                cancelTitleColor = "#0092ff";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new CustomDatePicker(mWXSDKInstance.getContext(), min, max, title, titleColor,
                confirmTitle, confirmTitleColor, cancelTitle, cancelTitleColor, value, new CustomDatePicker.OnPickListener() {

            @Override
            public void onPick(boolean set, @Nullable String time) {
                if (set) {
                    Map<String, Object> ret = new HashMap<>(2);
                    ret.put(RESULT, SUCCESS);
                    ret.put(DATA, time);
                    callback.invoke(ret);
                } else {
                    Map<String, Object> ret = new HashMap<>(2);
                    ret.put(RESULT, CANCEL);
                    ret.put(DATA, null);
                    callback.invoke(ret);
                }
            }
        });
    }

}
