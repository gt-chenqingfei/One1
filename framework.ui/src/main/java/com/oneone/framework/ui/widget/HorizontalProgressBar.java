package com.oneone.framework.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.oneone.framework.ui.R;


/**
 * @author qingfei.chen
 * @since 2017-04-12 09:58:53
 * </br>
 */
public class HorizontalProgressBar extends View {
    private PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

    private static final int DEFAULT_HEIGHT_DP = 25;
    private static final float MAX_PROGRESS = 100f;

    private int barHeight = DEFAULT_HEIGHT_DP;
    private Paint textPaint;
    private Paint pgPaint;
    private String progressText;
    private Rect textRect;
    private RectF bgRectF;
    private Bitmap pgBitmap;
    private Canvas pgCanvas;
    private float progress;
    private float progressMax = MAX_PROGRESS;
    private int loadingColor;
    private int textColor;
    private int textSize;
    private int radius;
    private BitmapShader bitmapShader;

    public HorizontalProgressBar(Context context) {
        this(context, null, 0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBar);
        try {
            textSize = (int) ta.getDimension(R.styleable.HorizontalProgressBar_textSize, 12);
            loadingColor = ta.getColor(R.styleable.HorizontalProgressBar_loadingColor, Color.parseColor("#40c4ff"));
            textColor = ta.getColor(R.styleable.HorizontalProgressBar_textColor, Color.parseColor("#ffffff"));
            radius = (int) ta.getDimension(R.styleable.HorizontalProgressBar_radius, 0);
            barHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBar_height, DEFAULT_HEIGHT_DP);
        } finally {
            ta.recycle();
        }
    }

    private void init() {

        pgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pgPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);

        textRect = new Rect();
        bgRectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());

        initPgBitmap();
    }

    private void initPgBitmap() {
        pgBitmap = Bitmap.createBitmap(getMeasuredWidth() - 1, getMeasuredHeight() - 1, Bitmap.Config.ARGB_8888);
        pgCanvas = new Canvas(pgBitmap);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        switch (heightSpecMode) {
            case MeasureSpec.AT_MOST:
                height = barHeight;
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                height = barHeight;
                break;
        }


        setMeasuredDimension(widthSpecSize, height);

        if (pgBitmap == null) {
            init();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawProgress(canvas);
        drawProgressText(canvas);
    }


    /**
     * Draw progress
     *
     * @param canvas current canvas
     */
    private void drawProgress(Canvas canvas) {
        if (pgPaint == null) {
            init();
        }
        pgPaint.setColor(loadingColor);

        float right = (progress / progressMax) * getMeasuredWidth();
        pgCanvas.save();
        pgCanvas.clipRect(0, 0, right, getMeasuredHeight());
        pgCanvas.drawColor(loadingColor);
        pgCanvas.restore();


        pgPaint.setXfermode(mode);
        pgPaint.setXfermode(null);

        bitmapShader = new BitmapShader(pgBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        pgPaint.setShader(bitmapShader);
        canvas.drawRoundRect(bgRectF, radius, radius, pgPaint);
    }

    /**
     * Draw progress text
     *
     * @param canvas current canvas
     */
    private void drawProgressText(Canvas canvas) {
        textPaint.setColor(textColor);
        String text = getProgressText();
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        int tWidth = textRect.width();
        int tHeight = textRect.height();
        float xCoordinate = (getMeasuredWidth() - tWidth) / 2;
        float yCoordinate = (getMeasuredHeight() + tHeight) / 2;
        canvas.drawText(text, xCoordinate, yCoordinate, textPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setProgress(float progress, float progressMax) {
        this.progress = progress;
        this.progressMax = progressMax;
        invalidate();
    }

    public void reset() {
        progress = 0;
        progressText = "";
        initPgBitmap();
        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public void setText(String text) {
        progressText = text;
    }

    private String getProgressText() {

        return progressText + progress + "%";
    }

    private int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
