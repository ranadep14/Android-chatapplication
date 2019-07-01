package com.cloudsinc.soulmobile.nativechatapplication.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

public class CustomLinearLayoutManager extends LinearLayoutManager {
    public CustomLinearLayoutManager(Context context) {
        super(context);
        setAutoMeasureEnabled(false);
    }

    public CustomLinearLayoutManager(Context context, int orientation,
                                     boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLinearLayoutManager(Context context, AttributeSet attrs,
                                     int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        //return super.supportsPredictiveItemAnimations();
        return false;
    }
}