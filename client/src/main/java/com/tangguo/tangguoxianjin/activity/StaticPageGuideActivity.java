package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 静态引导页面
 *
 * @author zhangyangyang
 */
public class StaticPageGuideActivity extends BaseActivity implements View.OnClickListener {
    private View lastpage;
    private int[] images;
    private List<View> viewList;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUserDefinedTitle = false;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_view_pager);
        hideTitleBar();
        initWeight();
        initImages();
        initWeightData();
    }

    private void initWeight() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void initImages() {
        Button go_main;
        images = new int[]{R.drawable.view_start1, R.drawable.view_start2, R.drawable.view_start3, R.drawable.view_start4};
        lastpage = LayoutInflater.from(this).inflate(R.layout.activity_fuide_start_view, null);
        go_main = (Button) lastpage.findViewById(R.id.btn_go_main);
        go_main.setOnClickListener(this);

    }

    private void initWeightData() {
        viewList = new ArrayList<>();
        for (int i = 0; i < images.length + 1; i++) {
            if (i == images.length) {
                viewList.add(lastpage);
            } else {
                View view = new View(this);
                view.setBackgroundResource(images[i]);
                viewList.add(view);
            }
        }
        viewPager.setAdapter(pagerAdapter);
    }

    public PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {

            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));

        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "title";
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));

            return viewList.get(position);
        }

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startAc(MainActivity.class);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_main:
                startAc(MainActivity.class);
                finish();
                break;
        }
    }
}
