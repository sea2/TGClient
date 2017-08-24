package com.tangguo.tangguoxianjin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.tangguo.tangguoxianjin.R;


/**
 * Created by lhy on 2017/4/14.
 */

public class CustomImageButton extends View {


    /**
     * 按钮图片
     */
    private final Bitmap mImage;
    private int mImageWidth = 0;
    private int mImageHight = 0;
    /**
     * 按钮内容
     */
    private String mTitle;
    /**
     * 字体的颜色
     */
    private int mTextColor;
    /**
     * 字体的大小
     */
    private int mTextSize;

    private Paint mPaint;
    /**
     * 对文本的约束
     */
    private Rect mTextBound;
    /**
     * 控制整体布局
     */
    private Rect rect;
    /**
     * 图片和最左边距离
     */
    private int imageLeftPadding = dipToPx(6);
    /**
     * 边框宽度
     */
    private float strokeWidth = dipToPx(1);
    /**
     * 图片和文字的距离
     */
    private final int imageTextSpacing;
    /**
     * 圆角度数
     */
    private int radiusBg;

    public CustomImageButton(Context context) {
        this(context, null);
    }

    public CustomImageButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageButton, defStyleAttr, 0);
        int customImage = a.getResourceId(R.styleable.CustomImageButton_image, 0);
        if (customImage == 0) {
            mImage = null;
        } else {
            mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.CustomImageButton_image, R.drawable.ic_launcher));
            mImageWidth = mImage.getWidth();
            mImageHight = mImage.getHeight();
        }
        mTitle = a.getString(R.styleable.CustomImageButton_titleText);
        radiusBg = a.getDimensionPixelSize(R.styleable.CustomImageButton_radiusBg, -1);
        imageTextSpacing = a.getDimensionPixelSize(R.styleable.CustomImageButton_imageTextSpacing, dipToPx(2));
        mTextColor = a.getColor(R.styleable.CustomImageButton_titleTextColor, Color.parseColor("#ffaf30"));
        mTextSize = a.getDimensionPixelSize(R.styleable.CustomImageButton_titleTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        a.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mTextBound = new Rect();
        // 计算了描绘字体需要的范围
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBound);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            float textWidth = mTextBound.width();
            width = (int) (getPaddingLeft() + textWidth + getPaddingRight() + mImageWidth + heightSize / 2 + imageLeftPadding / 2);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            float textHeight = mTextBound.height();
            height = (int) (getPaddingTop() + textHeight + getPaddingBottom());
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint _paint = new Paint();
        _paint.setColor(Color.parseColor("#00000000"));
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), _paint);

        _paint.setColor(mTextColor);
        _paint.setAntiAlias(true);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(strokeWidth);


        RectF r1 = new RectF();
        r1.left = strokeWidth;
        r1.right = getMeasuredWidth() - strokeWidth;
        r1.top = strokeWidth;
        r1.bottom = getMeasuredHeight() - strokeWidth;
        if (radiusBg == -1) radiusBg = getMeasuredHeight() / 2;
        canvas.drawRoundRect(r1, radiusBg, radiusBg, _paint);

        Rect rect = new Rect();

        rect.left = getPaddingLeft() + imageLeftPadding;
        rect.right = mImageWidth + getPaddingLeft() + imageLeftPadding;
        rect.top = getMeasuredHeight() / 2 - mImageHight / 2;
        rect.bottom = getMeasuredHeight() / 2 + mImageHight / 2;

        if (mImage != null) canvas.drawBitmap(mImage, null, rect, _paint);


        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float baseY = getHeight() - (getHeight() - fontHeight) / 2 - fontMetrics.bottom;

        int imageTextSpacingNew;
        if (mImage != null) imageTextSpacingNew = mImageWidth + imageTextSpacing;
        else imageTextSpacingNew = 0;
        canvas.drawText(mTitle, (getMeasuredWidth() + imageTextSpacingNew) / 2, baseY - 2, mPaint);
    }


    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }


    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
