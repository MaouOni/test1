package com.example.examen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatternLockView extends View {
    private Paint dotPaint;
    private Paint linePaint;
    private Paint visibleLinePaint;
    private Path userPath;
    private Path visiblePath;

    private List<Integer> userPattern = new ArrayList<>();
    private final List<Integer> correctPattern = Arrays.asList(1, 2, 3);
    private int lastDot = -1;

    private float[][] dotPositions = new float[3][2];

    public PatternLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initButton(context);
    }

    private void initButton(Context context) {
        Button exitButton = new Button(context);
        exitButton.setText("Salir");
        exitButton.setOnClickListener(v -> ((Activity) context).finish());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(0, 0, 0, 50);

        if (getParent() instanceof RelativeLayout) {
            ((RelativeLayout) getParent()).addView(exitButton, params);
        } else {
            Log.e("PatternLockView", "Parent class is not RelativeLayout, and button cannot be added.");
        }
    }

    private void init() {
        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setColor(0xFF666666);
        dotPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(0x00000000); // Transparent color for the internal path
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(10);

        visibleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        visibleLinePaint.setColor(0xFFFFFFFF); // White color for the visible line
        visibleLinePaint.setStyle(Paint.Style.STROKE);
        visibleLinePaint.setStrokeWidth(10);

        userPath = new Path();
        visiblePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int centerX = w / 2;
        int centerY = h / 2;
        int step = Math.min(w, h) / 4;
        dotPositions[0] = new float[]{centerX - step, centerY - step};
        dotPositions[1] = new float[]{centerX + step, centerY - step};
        dotPositions[2] = new float[]{centerX, centerY + step};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (float[] dotPosition : dotPositions) {
            canvas.drawCircle(dotPosition[0], dotPosition[1], 50, dotPaint);
        }
        // Draw the user input path with the transparent line
        canvas.drawPath(userPath, linePaint);
        // Draw the visible straight path
        canvas.drawPath(visiblePath, visibleLinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                userPath.moveTo(touchX, touchY);
                handleTouch(touchX, touchY);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                userPath.lineTo(touchX, touchY);
                handleTouch(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (userPattern.equals(correctPattern)) {
                    Toast.makeText(getContext(), "Patrón correcto", Toast.LENGTH_SHORT).show();
                    getContext().startActivity(new Intent(getContext(), ColorList.class));
                } else {
                    Toast.makeText(getContext(), "Patrón inválido", Toast.LENGTH_SHORT).show();
                }
                userPath.reset();
                userPattern.clear();
                visiblePath.reset();
                lastDot = -1;
                invalidate();
                break;
        }
        return true;
    }

    private void handleTouch(float x, float y) {
        float touchRadius = 150;

        for (int i = 0; i < dotPositions.length; i++) {
            float dx = dotPositions[i][0] - x;
            float dy = dotPositions[i][1] - y;
            if (Math.sqrt(dx * dx + dy * dy) < touchRadius) {
                if (lastDot != i) {
                    userPattern.add(i + 1);
                    if (lastDot != -1) {
                        visiblePath.moveTo(dotPositions[lastDot][0], dotPositions[lastDot][1]);
                        visiblePath.lineTo(dotPositions[i][0], dotPositions[i][1]);
                    }
                    lastDot = i;
                    Log.d("D", "Punto añadido: " + (i + 1));
                }
                break;
            }
        }
    }
}
