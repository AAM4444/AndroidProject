package com.example.myapplication2.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

import com.example.myapplication2.R;

import java.util.jar.Attributes;

public class BackView extends View {

    public Paint paint;
    public RectF rectF;

    public BackView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        SetupPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(0, 0, 275, 275, 50, 50, paint);
    }

    private void SetupPaint() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAlpha(100);
    }
}