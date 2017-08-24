package com.tangguo.tangguoxianjin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.util.NumberUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 数字自动增长动画
 *
 * @author xiaocaimi@xcm.com
 */
@SuppressLint("AppCompatCustomView")
public class AnimationNumberView extends TextView {
    // 动画时长 ms
    private int duration = 400;
    private int duration2 = 600;
    private float rateNumber;

    private double startNumber;
    private double endNumber;

    private int intSizeResourcesId = 0;
    private int floatSizeResourcesId = 0;
    private boolean isNeedDecimal = false;

    public AnimationNumberView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        intSizeResourcesId = getResources().getDimensionPixelSize(R.dimen.text_size_18);
        floatSizeResourcesId = getResources().getDimensionPixelSize(R.dimen.text_size_15);
    }

    public AnimationNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intSizeResourcesId = getResources().getDimensionPixelSize(R.dimen.text_size_18);
        floatSizeResourcesId = getResources().getDimensionPixelSize(R.dimen.text_size_15);
    }

    public AnimationNumberView(Context context) {
        super(context);
        intSizeResourcesId = getResources().getDimensionPixelSize(R.dimen.text_size_18);
        floatSizeResourcesId = getResources().getDimensionPixelSize(R.dimen.text_size_15);
    }

    /**
     * 显示动画  只显示整数
     *
     * @param startNumber   开始增长数字
     * @param endNumber     结束增长数字
     * @param isNeedDecimal 是否需要小数
     */
    public void showNumberWithAnimation(String startNumber, String endNumber, boolean isNeedDecimal) {
        this.isNeedDecimal = isNeedDecimal;
        try {
            this.startNumber = StringUtils.toDouble(startNumber, 0);
            this.endNumber = Double.parseDouble(endNumber);
        } catch (Exception e) {
            if (endNumber != null) this.setText(endNumber);
            return;
        }
        // 修改number属性，会调用setNumber方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rateNumber", 0, 1);
        objectAnimator.setDuration(duration);
        // 加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    public void setRateNumber(float number) {
        this.rateNumber = number;
        double currentShowNumber = endNumber * rateNumber;
        if (currentShowNumber < startNumber) {
            currentShowNumber = startNumber;
        }
        if (isNeedDecimal) setText(NumberUtil.commonFormatNumZeroStr(currentShowNumber));
        else setText(NumberUtil.commonFormatNum(currentShowNumber));
    }

    /**
     * 整数位和小数位字体大小不同的显示动画
     *
     * @param startNumber
     * @param endNumber
     * @param intSizeId   整数大小
     * @param floatSizeId 小数大小
     */
    public void showNumberWithAnimationBySize(double startNumber, double endNumber, int intSizeId, int floatSizeId) {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.intSizeResourcesId = intSizeId;
        this.floatSizeResourcesId = floatSizeId;

        if (endNumber <= 0) {//负数情况不用动画
            setRateNumberBySize(1.0f);
            return;
        }

        // 修改number属性，会调用setNumber方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rateNumberBySize", 0, 1);
        objectAnimator.setDuration(duration);
        // 加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    /**
     * 整数位和小数位字体大小不同的显示动画
     *
     * @param startNumber
     * @param endNumber
     * @param intSizeId   整数大小
     * @param floatSizeId 小数大小
     */
    public void showNumberWithAnimationBySize(double startNumber, String endNumber, int intSizeId, int floatSizeId) {
        this.startNumber = startNumber;
        this.intSizeResourcesId = intSizeId;
        this.floatSizeResourcesId = floatSizeId;
        try {
            this.endNumber = Double.parseDouble(endNumber);
        } catch (Exception e) {
            if (endNumber != null) this.setText(endNumber);
            return;
        }
        if (this.endNumber <= 0) {//负数情况不用动画
            setRateNumberBySize(1.0f);
            return;
        }

        // 修改number属性，会调用setNumber方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rateNumberBySize", 0, 1);
        objectAnimator.setDuration(duration);
        // 加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    public void showNumberWithAnimationBySize(double startNumber, double endNumber, int intSizeId, int floatSizeId, int durationLong) {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.intSizeResourcesId = intSizeId;
        this.floatSizeResourcesId = floatSizeId;

        if (endNumber <= 0) {//负数情况不用动画
            setRateNumberBySize(1.0f);
            return;
        }

        // 修改number属性，会调用setNumber方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rateNumberBySize", 0, 1);
        objectAnimator.setDuration(durationLong);
        // 加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    /**
     * 整数位和小数位字体大小不同的显示动画
     *
     * @param startNumber
     * @param endNumber
     * @param intSizeId   整数大小 变小
     * @param floatSizeId 小数大小
     */
    public void showNumberWithAnimationBySizeChangeToSmall(double startNumber, double endNumber, int intSizeId, int floatSizeId, int durationLong) {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.intSizeResourcesId = intSizeId;
        this.floatSizeResourcesId = floatSizeId;

        if (endNumber < 0) {//负数情况不用动画
            setRateNumberBySize(1.0f);
            return;
        }

        // 修改number属性，会调用setNumber方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rateNumberBySize", 0, 1);
        objectAnimator.setDuration(durationLong);
        // 加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    public void setRateNumberBySize(float number) {
        this.rateNumber = number;
        double currentShowNumber = startNumber + ((endNumber - startNumber) * rateNumber);
//        LogManager.e("currentShowNumber=="+currentShowNumber);
//        if (endNumber > 0) {
//            if (currentShowNumber < startNumber) {
//                currentShowNumber = startNumber;
//            }
//        }
        String num = NumberUtil.commonFormatNumZeroStr(currentShowNumber);
        if (!StringUtils.isEmpty(num)) {
            final SpannableStringBuilder sb = new SpannableStringBuilder(num);
            sb.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(intSizeResourcesId)), 0, num.indexOf(".") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(floatSizeResourcesId)), num.indexOf(".") + 1, num.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            this.setText(sb);
        } else {
            this.setText("");
        }
    }

    public void showNumberWithAnimation2(double startNumber, double endNumber, boolean isNeedShrink) {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.isNeedShrink = isNeedShrink;

        if (endNumber <= 0) {//负数情况不用动画
            setRateNumber2(1.0f);
            return;
        }

        // 修改number属性，会调用setNumber方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rateNumber2", 0, 1);
        objectAnimator.setDuration(duration);
        // 加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

    private boolean isNeedShrink = false;

    public void setRateNumber2(float number) {
        this.rateNumber = number;
        this.rateNumber = number;
        double currentShowNumber = endNumber * rateNumber;
        if (endNumber > 0) {
            if (currentShowNumber < startNumber) {
                currentShowNumber = startNumber;
            }
        }
        String num = NumberUtil.commonFormatNumZeroStr(currentShowNumber);
        if (!StringUtils.isEmpty(num)) {
            final SpannableStringBuilder sb = new SpannableStringBuilder(num);
            boolean isDefaultSize = true;
            if (isNeedShrink) {
                isDefaultSize = num.length() < 16;
            }
            sb.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(isDefaultSize ? R.dimen.text_size_18 : R.dimen.text_size_15)), 0, num.indexOf(".") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(isDefaultSize ? R.dimen.text_size_14 : R.dimen.text_size_11)), num.indexOf(".") + 1, num.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            this.setText(sb);
        } else {
            this.setText("");
        }
    }

    public double getDoubleValue() {
        if (null != getText()) {
            try {
                String strValue = getText().toString();
                return Double.parseDouble(strValue.replaceAll(",", ""));
            } catch (Exception ex) {
                return 0;
            }
        }
        return 0;
    }

}
