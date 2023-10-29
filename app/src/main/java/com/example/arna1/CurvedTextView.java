package com.example.arna1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import java.lang.reflect.Type;

public class CurvedTextView extends androidx.appcompat.widget.AppCompatTextView {
    private Path path = new Path();
    private TextPaint textPaint = new TextPaint();

    public CurvedTextView(Context context) {
        super(context);
        init();
    }

    public CurvedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurvedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint.setColor(Color.WHITE);
       // textPaint.setTypeface(Typeface.create("Serif",Typeface.ITALIC));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textPaint.setTextSize(getTextSize());
        textPaint.setTypeface(getTypeface());
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Get the text content
        String text = getText().toString();

        // Calculate the path for curving the text
        int textWidth = (int) textPaint.measureText(text);
        int hOffset = (getWidth() - textWidth) / 2; // Center the text horizontally
        int vOffset = getHeight() / 2; // Center the text vertically
        path.reset();
        path.addArc(0, 0, getWidth(), getHeight(), -150, 180);
        canvas.drawTextOnPath(text, path, hOffset, vOffset, textPaint);
    }
}
