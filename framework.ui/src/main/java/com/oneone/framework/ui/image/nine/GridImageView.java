package com.oneone.framework.ui.image.nine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.oneone.framework.ui.R;
import com.oneone.framework.ui.utils.ScreenUtil;

/**
 * Created by Jaeger on 16/2/24.
 * <p>
 * Email: chjie.jaeger@gamil.com
 * GitHub: https://github.com/laobie
 */
public class GridImageView extends ImageView {
    private String text = "";

    public GridImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridImageView(Context context) {
        super(context);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(text)) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(getResources().getColor(R.color.color_black_trans_50));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

            float textSize = ScreenUtil.dip2px(20);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(getResources().getColor(R.color.color_white));
            Rect r = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
            paint.getTextBounds(text, 0, text.length(), r);
            float y = canvas.getHeight() / 2f + r.height() / 2f - r.bottom;
            canvas.drawText(text, canvas.getWidth() / 2, y, paint);
        }
    }
}