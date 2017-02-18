package com.app.infideap.postingapp;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Shiburagi on 19/02/2017.
 */
public class ReactAnimationDialog extends Dialog {
    private OnDismissListener listener;

    public ReactAnimationDialog(Context context) {
        super(context);
    }

    public ReactAnimationDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ReactAnimationDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void dismiss() {
        listener.onDismiss(this);
//        super.dismiss();
    }

    public void dismissSuper(){
        super.dismiss();
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);

        this.listener = listener;
    }
}
