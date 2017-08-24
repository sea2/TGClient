package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.util.DisplayImageOptionsUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ShowPhotoActivity extends BaseActivity {

    private android.widget.ImageView tvshowphoto;
    String pathImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_scale_into,R.anim.activity_scale_out);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isUserDefinedColorForStatusBar = false;
        isUserDefinedTitle = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        this.tvshowphoto = (ImageView) findViewById(R.id.tv_show_photo);
        if (getIntent() != null) {
            pathImage = getIntent().getStringExtra("pathImage");
        }
        if (StringUtils.isEmpty(pathImage)) finish();
        else ImageLoader.getInstance().displayImage(pathImage, tvshowphoto, DisplayImageOptionsUtil.getDisplayImageOptions(-1, true, true));
    }


}
