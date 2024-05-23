package com.example.examen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class FractalView extends View {
    private Paint paint;
    private Paint initialPaint;

    public FractalView(Context context) {
        super(context);
        init();
    }

    public FractalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(android.graphics.Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4f);

        initialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        initialPaint.setColor(android.graphics.Color.TRANSPARENT);
        initialPaint.setStyle(Paint.Style.STROKE);
        initialPaint.setStrokeWidth(4f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();

        drawFractal(canvas, width / 2, height / 2, Math.min(width, height) / 2, true);
    }

    private void drawFractal(Canvas canvas, float cx, float cy, float radius, boolean isFirst) {
        if (radius < 10f) return;

        float rhombusSize = (float) (radius * Math.sqrt(2));
        Paint currentPaint = isFirst ? initialPaint : paint;

        canvas.drawLine(cx - rhombusSize, cy, cx, cy - rhombusSize, currentPaint);
        canvas.drawLine(cx, cy - rhombusSize, cx + rhombusSize, cy, currentPaint);
        canvas.drawLine(cx + rhombusSize, cy, cx, cy + rhombusSize, currentPaint);
        canvas.drawLine(cx, cy + rhombusSize, cx - rhombusSize, cy, currentPaint);

        canvas.drawCircle(cx, cy, radius, paint);
        drawFractal(canvas, cx, cy, radius / (float) Math.sqrt(2), false);
    }
}
