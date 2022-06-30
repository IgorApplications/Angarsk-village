package com.iapp.angara.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NavigateImageView extends androidx.appcompat.widget.AppCompatImageView {

    private int colorDown = Color.argb(100, 255, 255, 255);

    public NavigateImageView(Context context) {
        super(context);
    }

    public NavigateImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigateImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColorDown(int colorDown) {
        this.colorDown = colorDown;
    }

    public int getColorDown() {
        return colorDown;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getDrawable().setColorFilter(colorDown, PorterDuff.Mode.SRC_ATOP);
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                getDrawable().clearColorFilter();
                invalidate();
                break;
            }
        }

        return super.onTouchEvent(event);
    }
}
