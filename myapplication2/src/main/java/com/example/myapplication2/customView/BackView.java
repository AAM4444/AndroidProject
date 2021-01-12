package com.example.myapplication2.customView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.myapplication2.R;
import com.example.myapplication2.Utils;

public class BackView extends View {

    public Paint paint;
    Resources resources = getResources();

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
        float width = Utils.getWidth();
        float height = Utils.getHeight();
        canvas.drawRoundRect(0, 0, width/2, height, width/16, width/16, paint);
    }

    private void SetupPaint() {
        paint = new Paint();
        int customViewColor = resources.getColor(R.color.colorOrangeDark, null);
        paint.setColor(customViewColor);
    }
}