package com.app.infideap.postingapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.app.infideap.notificationapp.R;
import com.app.infideap.postingapp.entity.Post;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;


/**
 * Created by Shiburagi on 14/02/2017.
 */

public class ReactPopupDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = ReactPopupDialogFragment.class.getSimpleName();
    private static final String ARG_X = "ARG_X";
    private static final String ARG_Y = "ARG_Y";
    private static final String ARG_WIDTH = "ARG_WIDTH";
    private static final String ARG_HEIGHT = "ARG_HEIGHT";
    private static final String ARG_HOLDER = "ARG_HOLDER";
    private int x, y;
    private int width, height;
    private ImageView[] imageViews;
    private int dp60;
    private int duration;
    private View backgroundLayout;

    public static ReactPopupDialogFragment newInstance(int x, int y, int visibleWidth, int visibleHeight, Post holder) {
        ReactPopupDialogFragment fragment = new ReactPopupDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_X, x);
        bundle.putInt(ARG_Y, y);
        bundle.putInt(ARG_WIDTH, visibleWidth);
        bundle.putInt(ARG_HEIGHT, visibleHeight);
        bundle.putSerializable(ARG_HOLDER, holder);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        Bundle bundle = getArguments();
        if (bundle != null) {
            x = bundle.getInt(ARG_X);
            y = bundle.getInt(ARG_Y);
            width = bundle.getInt(ARG_WIDTH);
            height = bundle.getInt(ARG_HEIGHT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_react_popup_dialog, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView likeImageView = (ImageView) view.findViewById(R.id.imageView_like);
        ImageView loveImageView = (ImageView) view.findViewById(R.id.imageView_love);
        ImageView laughingImageView = (ImageView) view.findViewById(R.id.imageView_laughing);
        ImageView shockedImageView = (ImageView) view.findViewById(R.id.imageView_shocked);
        ImageView sadImageView = (ImageView) view.findViewById(R.id.imageView_sad);
        ImageView angryImageView = (ImageView) view.findViewById(R.id.imageView_angry);
        backgroundLayout = view.findViewById(R.id.layout_background);
        dp60 = getResources().getDimensionPixelSize(R.dimen.react_dialog_height);
        duration = 150;
        imageViews = new ImageView[]{
                likeImageView,
                loveImageView,
                laughingImageView,
                shockedImageView,
                sadImageView,
                angryImageView
        };
        for (int i = 0; i < imageViews.length; i++) {
            ImageView imageView = imageViews[i];
            animate(imageView, dp60, dp60, 0f, 0f, 0, 0);
            animate(imageView, dp60, 0, 0f, 1f, 0, getDuration(duration, i));
        }

        animateBackground(backgroundLayout, dp60, 0, 0f, 1f, 150);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    private int getDuration(int duration, int i) {
        return (int) Math.pow(i * 4, 2) + duration;
    }


    private void animateBackground(View view, int startY, int endY, float startAlpha, float endAlpha, int duration) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationY", startY, endY),
                ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha)
        );
        set.setDuration(duration);
        set.start();
    }

    private void animate(ImageView imageView, int startY, int endY,
                         float scaleStart, float scaleEnd, int delay, int duration) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(imageView, "translationY", startY, endY),
                ObjectAnimator.ofFloat(imageView, "scaleX", scaleStart, scaleEnd),
                ObjectAnimator.ofFloat(imageView, "scaleY", scaleStart, scaleEnd),
                ObjectAnimator.ofFloat(imageView, "alpha", scaleStart, scaleEnd)

        );
        set.setDuration(duration).setStartDelay(delay);
        set.start();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new ReactAnimationDialog(getContext());
    }

    public void setOnDismissListener(final DialogInterface.OnDismissListener listener) {
        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface dialog) {
                int maxDuration = 0;
                for (int i = 0; i < imageViews.length; i++) {
                    ImageView imageView = imageViews[i];
                    maxDuration = getDuration(duration, i);
                    animate(imageView, 0, dp60, 1f, 0f, 0, maxDuration);
                }

                animateBackground(backgroundLayout, 0, dp60, 1f, 0f, duration);
                if (getView() == null)
                    listener.onDismiss(dialog);
                else
                    getView().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    ((ReactAnimationDialog) dialog).dismissSuper();
                                    listener.onDismiss(dialog);
                                }
                            }, maxDuration
                    );
            }
        });

        getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;


        int height =
                getResources().getDimensionPixelSize(R.dimen.react_dialog_height);
        int marginX =
                getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);

        int marginY =
                getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);

        int actionBarHeight = getResources().getDimensionPixelSize(R.dimen.actionBarSize);

        windowParams.x = x - marginX * 3;

        Log.e(TAG, height + " > " + y);
        if (height > y - actionBarHeight - height) {
            windowParams.y = y + marginY / 3;
        } else
            windowParams.y = y - marginY - height - marginY / 2;

        windowParams.gravity = Gravity.START | Gravity.TOP;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            window.setElevation(0);
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    @Override
    public void onStart() {
        super.onStart();


    }
}
