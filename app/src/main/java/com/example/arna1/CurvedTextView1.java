package com.example.arna1;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;

public class CurvedTextView1 extends androidx.appcompat.widget.AppCompatTextView {
    private Path path = new Path();
    private TextPaint textPaint = new TextPaint();

    public CurvedTextView1(Context context) {
        super(context);
        init();
    }

    public CurvedTextView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurvedTextView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint.setColor(Color.RED);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(getTextSize());
        textPaint.setTypeface(getTypeface());
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String text = getText().toString();
        int textWidth = (int) textPaint.measureText(text);
        int textHeight = (int) (textPaint.descent() - textPaint.ascent());

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int radius = Math.min(centerX, centerY);

        int startAngle = 90;
        int sweepAngle = -180;

        int left = centerX - radius;
        int top = centerY - radius;
        int right = centerX + radius;
        int bottom = centerY + radius;

        path.reset();
        path.moveTo(left, bottom);
      //  path.quadTo(centerX, bottom - radius * 2, right, bottom);
        path.arcTo(left, top, right, bottom, startAngle, sweepAngle, false);
        canvas.drawTextOnPath(text, path, 0, -textHeight / 2f, textPaint);
    }
}
