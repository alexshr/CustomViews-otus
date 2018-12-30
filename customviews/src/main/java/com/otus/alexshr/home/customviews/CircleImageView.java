package com.otus.alexshr.home.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.otus.alexshr.home.customviews.customviews.R;

import timber.log.Timber;

import static com.otus.alexshr.home.customviews.utils.Utils.bitmapFromDrawable;

public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int DEFAULT_BORDER_WIDTH = 4;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_CIRCLE_BACKGROUND_COLOR = Color.TRANSPARENT;
    private final Matrix shaderMatrix = new Matrix();
    private final Paint bitmapPaint = new Paint();
    private final Paint borderPaint = new Paint();
    private final Paint backgroundPaint = new Paint();
    private RectF drawableRect;
    private RectF borderRect;
    private int borderColor = DEFAULT_BORDER_COLOR;
    private int borderWidth = DEFAULT_BORDER_WIDTH;
    private int backgroundColor = DEFAULT_CIRCLE_BACKGROUND_COLOR;

    private Bitmap bitmap;
    private BitmapShader bitmapShader;

    private boolean isImageApplied, isSizesAndColorsApplied;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);

        borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
        borderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);
        backgroundColor = a.getColor(R.styleable.CircleImageView_background_color,
                DEFAULT_CIRCLE_BACKGROUND_COLOR);

        a.recycle();

        init();
    }

    private void init() {
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);
        backgroundPaint.setAntiAlias(true);
        bitmapPaint.setAntiAlias(true);
    }

    private void applyImage() {
        Timber.d("applyImage");
        bitmap = bitmapFromDrawable(getDrawable());

        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        bitmapPaint.setShader(bitmapShader);

        isImageApplied = true;

        applySizesAndColors();
    }

    private void applySizesAndColors() {

        //region ================= calculate view rects =================
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();

        int d = Math.min(width, height);

        float left = getPaddingLeft() + (width - d) / 2f;
        float right = getWidth() - getPaddingRight() - (width - d) / 2f;
        float top = getPaddingTop() + (height - d) / 2f;
        float bottom = getHeight() - getPaddingBottom() - (height - d) / 2f;

        borderRect = new RectF(left, top, right, bottom);

        drawableRect = new RectF();
        drawableRect.set(borderRect);
        if (borderWidth > 0) drawableRect.inset(borderWidth, borderWidth);
        //endregion

        //region ================= setup bitmapShader matrix =================
        float scale, dx = 0, dy = 0;

        shaderMatrix.set(null);

        if (bitmap.getWidth() * drawableRect.height() > drawableRect.width() * bitmap.getHeight()) {
            scale = drawableRect.height() / (float) bitmap.getHeight();
            dx = (drawableRect.width() - bitmap.getWidth() * scale) * 0.5f;
            Timber.d("bitmap.getWidth()*scale/2=%s", bitmap.getHeight() * scale / 2);
        } else {
            scale = drawableRect.width() / (float) bitmap.getWidth();
            dy = (drawableRect.height() - bitmap.getHeight() * scale) * 0.5f;
            Timber.d("bitmap.getHeight()*scale/2=%s", bitmap.getWidth() * scale / 2);
        }

        shaderMatrix.setScale(scale, scale);
        shaderMatrix.postTranslate(dx + drawableRect.left, dy + drawableRect.top);

        bitmapShader.setLocalMatrix(shaderMatrix);
        //endregion

        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);

        backgroundPaint.setColor(backgroundColor);

        isSizesAndColorsApplied = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Timber.d("isImageApplies=%s, isSizesAndColorsApplied=%s", isImageApplied, isSizesAndColorsApplied);
        if (!isImageApplied) {
            applyImage();
        }
        if (!isSizesAndColorsApplied) applySizesAndColors();

        if (backgroundColor != Color.TRANSPARENT) {
            canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRect.width() / 2, backgroundPaint);
        }

        if (borderWidth > 0) {
            canvas.drawCircle(borderRect.centerX(), borderRect.centerY(), borderRect.width() / 2 - borderWidth / 2, borderPaint);
        }
        canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRect.width() / 2, bitmapPaint);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Timber.d("w=%d, h=%d, oldw=%d; oldh=%d", w, h, oldw, oldh);
        isSizesAndColorsApplied = false;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(@ColorInt int borderColor) {
        Timber.d("borderColor=%d", borderColor);
        this.borderColor = borderColor;
        isSizesAndColorsApplied = false;
        invalidate();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(@ColorInt int backgroundColor) {
        Timber.d("backgroundColor=%d", backgroundColor);
        this.backgroundColor = backgroundColor;
        isSizesAndColorsApplied = false;
        invalidate();
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        Timber.d("borderWidth=%d", borderWidth);
        this.borderWidth = borderWidth;
        isSizesAndColorsApplied = false;
        invalidate();
    }

    public void setImageBitmap(Bitmap bm) {
        isImageApplied = false;
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        isImageApplied = false;
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        isImageApplied = false;
        super.setImageResource(resId);
    }

    @Override
    public void setImageURI(Uri uri) {
        isImageApplied = false;
        super.setImageURI(uri);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        isSizesAndColorsApplied = false;
        super.setScaleType(scaleType);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        Timber.d("padding: %s", new Rect(left, top, right, bottom));
        isSizesAndColorsApplied = false;
        super.setPadding(left, top, right, bottom);
    }
}
