package com.tangguo.tangguoxianjin.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.util.ScreenUtil;

/**
 * 等比例显示图片，显示大图
 */
public class BorrowHelpActivity extends BaseActivity {


    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_help);
        ImageView ivphotoborrow = (ImageView) findViewById(R.id.iv_photo_borrow);
        setTitle("借款攻略");


        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_borrow_help);
        float height = bitmap.getHeight();
        float width = bitmap.getWidth();
        float screenWidth = new ScreenUtil(this).getWidth();
        float rale = screenWidth / width;
        float drawableHeigthEnd = rale * height;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) drawableHeigthEnd);
        ivphotoborrow.setLayoutParams(params);
        ivphotoborrow.setScaleType(ImageView.ScaleType.FIT_XY);
        ivphotoborrow.setImageBitmap(bitmap);


    }


    @Override
    protected void onDestroy() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        super.onDestroy();
    }
}
