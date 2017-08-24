package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

/**
 *
 *
 */
public class XCMLoadingLayout extends FlipLoadingLayout {

	private ImageView mHeaderProgressImage;
	private Animation mHeaderProgAnimation;

	public XCMLoadingLayout(Context context, Mode mode,
							Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

		mHeaderProgressImage = (ImageView) mInnerLayout
				.findViewById(R.id.img_pull_to_refresh_progress);
		mHeaderProgAnimation = AnimationUtils.loadAnimation(context,
				R.anim.refershing);
	}

	@Override
	protected void refreshingImpl() {
		mHeaderImage.clearAnimation();
		// mHeaderImage.setVisibility(View.INVISIBLE);
		// mHeaderProgress.setVisibility(View.VISIBLE);
		mHeaderProgress.setVisibility(View.GONE);
		
		setLoadingDrawable(getContext().getResources().getDrawable(getDefaultDrawableResId()));
		mHeaderImage.setVisibility(View.VISIBLE);

		if (null != mHeaderProgressImage) {
			mHeaderProgressImage.setVisibility(View.VISIBLE);
			mHeaderProgressImage.startAnimation(mHeaderProgAnimation);
		}

	}

	@Override
	protected void resetImpl() {
		mHeaderProgress.setVisibility(View.GONE);
		
		mHeaderImage.clearAnimation();
		// mHeaderProgress.setVisibility(View.GONE);
		mHeaderImage.setVisibility(View.VISIBLE);
		mHeaderImage.setImageDrawable(imageDrawable);

		if (null != mHeaderProgressImage) {
			mHeaderProgressImage.clearAnimation();
			mHeaderProgressImage.setVisibility(View.GONE);
		}
	}

	@Override
	protected void completeImpl() {
		super.completeImpl();
		mHeaderProgress.setVisibility(View.GONE);
		
		if (null != mHeaderProgressImage) {
			mHeaderProgressImage.clearAnimation();
			mHeaderProgressImage.setVisibility(View.GONE);
		}
		mHeaderImage.setVisibility(View.VISIBLE);
		setLoadingDrawable(getContext().getResources().getDrawable(R.drawable.complete_success));
	}
	
	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {
		if (null != imageDrawable) {
			final int dHeight = imageDrawable.getIntrinsicHeight();
			final int dWidth = imageDrawable.getIntrinsicWidth();

			/**
			 * We need to set the width/height of the ImageView so that it is
			 * square with each side the size of the largest drawable dimension.
			 * This is so that it doesn't clip when rotated.
			 */
			ViewGroup.LayoutParams lp = mHeaderImage.getLayoutParams();
			lp.width = lp.height = Math.max(dHeight, dWidth);
			mHeaderImage.requestLayout();

			/**
			 * We now rotate the Drawable so that is at the correct rotation,
			 * and is centered.
			 */
//			mHeaderImage.setScaleType(ScaleType.MATRIX);
			mHeaderImage.setScaleType(ScaleType.CENTER_INSIDE);
			Matrix matrix = new Matrix();
			matrix.postTranslate((lp.width - dWidth) / 2f, (lp.height - dHeight) / 2f);
			matrix.postRotate(getDrawableRotationAngle(), lp.width / 2f, lp.height / 2f);
			mHeaderImage.setImageMatrix(matrix);
		}
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.ptr_flip;
	}
}
