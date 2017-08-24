package com.tangguo.tangguoxianjin.view.progress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.tangguo.tangguoxianjin.R;


/**
 * colorful arc progress bar
 * Created by shinelw on 12/4/15.
 */
public class CustomProgressBar extends View {


    //外弧直径
    private int[] colors;
    //背部进度颜色
    private int back_color;
    private float bgArcWidth = dipToPx(2);
    private float progressWidth = dipToPx(2);
    private RectF bgRect;
    private Paint allArcPaint;
    private Paint progressPaint;
    private RectF progressRect;
    private Paint progressBgPaint;
    private ValueAnimator progressAnimator;
    private float currentAngle;
    private float centerX;
    private float centerY;

    public CustomProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     *
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
        int color1 = a.getColor(R.styleable.CustomProgressBar_custom_progress_front_color1, Color.GREEN);
        int color2 = a.getColor(R.styleable.CustomProgressBar_custom_progress_front_color2, color1);
        int color3 = a.getColor(R.styleable.CustomProgressBar_custom_progress_front_color3, color1);
        colors = new int[]{color1, color2, color3, color3};
        back_color = a.getColor(R.styleable.CustomProgressBar_custom_progress_back_color, ContextCompat.getColor(context, R.color.bg_common));
        bgArcWidth = a.getDimension(R.styleable.CustomProgressBar_custom_progress_back_width, dipToPx(2));
        progressWidth = a.getDimension(R.styleable.CustomProgressBar_custom_progress_front_width, dipToPx(2));
        a.recycle();
    }


    private void initView() {


    }


    @Override
    protected void onDraw(Canvas canvas) {

        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = 0;
        bgRect.left = 0;
        bgRect.right = getMeasuredWidth();
        bgRect.bottom = getMeasuredHeight();

        //弧形的矩阵区域
        progressRect = new RectF();
        progressRect.top = bgArcWidth;
        progressRect.left = bgArcWidth;
        progressRect.right = getMeasuredWidth() - bgArcWidth;
        progressRect.bottom = getMeasuredHeight() - bgArcWidth;

        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;


        //矩阵区域
        allArcPaint = new Paint();
        allArcPaint.setColor(Color.parseColor("#00000000"));


        //外弧形
        progressBgPaint = new Paint();
        progressBgPaint.setAntiAlias(true);
        progressBgPaint.setStyle(Paint.Style.STROKE);
        progressBgPaint.setStrokeWidth(bgArcWidth);
        progressBgPaint.setColor(back_color);
        progressBgPaint.setStrokeCap(Paint.Cap.ROUND);


        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);

        //抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawRect(bgRect, allArcPaint);


        //内弧
        canvas.drawArc(progressRect, 0, 360, false, progressBgPaint);


        //设置渐变色
        SweepGradient sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(90, centerX, centerY);
        sweepGradient.setLocalMatrix(matrix);
        progressPaint.setShader(sweepGradient);

        //当前进度
        canvas.drawArc(progressRect, 270, currentAngle, false, progressPaint);


    }


    /**
     * 设置当前值
     *
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setAnimation(0f, currentValues);
        }

    }


    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void setAnimation(float last, float current) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(300);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        progressAnimator.start();
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
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


}
