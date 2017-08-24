package com.tangguo.tangguoxianjin.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.adapter.FragmentAdapter;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.common.MyApplication;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.eventbus.BaseEvent;
import com.tangguo.tangguoxianjin.eventbus.MessageEvent;
import com.tangguo.tangguoxianjin.fragment.AccountPageFragment;
import com.tangguo.tangguoxianjin.fragment.CertifyPageFragment;
import com.tangguo.tangguoxianjin.fragment.HomePageFragment;
import com.tangguo.tangguoxianjin.util.IntentUtil;
import com.tangguo.tangguoxianjin.util.PermissionsUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FRAGMENT_TAGS = "fragmentTags";
    private static final String CURR_INDEX = "currIndex";
    private static int currIndex = 0;
    private static final int REQUEST_READ_LOG = 101;
    private RadioGroup group;
    //fragment
    private Fragment mFragmentTwo, mFragmentThree;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> fragmentTags;
    private FragmentAdapter homePageFragmentAdapter;
    private ViewPager viewpager_container;
    HomePageFragment mHomePageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUserDefinedTitle = false;
        isUserDefinedColorForStatusBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            initFromSavedInstantsState(savedInstanceState);
        }
        EventBus.getDefault().register(this);
        Intent it = getIntent();
        if (it != null && it.hasExtra(MyConstants.MAIN_PAGE)) {
            currIndex = it.getIntExtra(MyConstants.MAIN_PAGE, 0);
        }
        initView();
        initData();
        //申请权限
        applyPermission();
    }

    private void applyPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] strPermission = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.ACCESS_COARSE_LOCATION};
            String[] strNeed = PermissionsUtil.getNeedApplyPermissions(strPermission);
            if (strNeed != null && strNeed.length > 0) {
                ActivityCompat.requestPermissions(this, strNeed, REQUEST_READ_LOG);
                return;
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURR_INDEX, currIndex);
        outState.putStringArrayList(FRAGMENT_TAGS, fragmentTags);
    }


    private void initFromSavedInstantsState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currIndex = savedInstanceState.getInt(CURR_INDEX);
            fragmentTags = savedInstanceState.getStringArrayList(FRAGMENT_TAGS);
        }

    }

    private void initData() {

        fragmentTags = new ArrayList<>(Arrays.asList("OneFragment", "TwoFragment", "ThreeFragment", "FourFragment"));
        mHomePageFragment = new HomePageFragment();
        mFragmentTwo = new CertifyPageFragment();
        mFragmentThree = new AccountPageFragment();
        mFragmentList.add(mHomePageFragment);
        mFragmentList.add(mFragmentTwo);
        mFragmentList.add(mFragmentThree);
        homePageFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList);
        viewpager_container.setOffscreenPageLimit(2);
        viewpager_container.setAdapter(homePageFragmentAdapter);

        if (currIndex != 0 && viewpager_container != null) viewpager_container.setCurrentItem(currIndex);
    }

    private void initView() {
        group = (RadioGroup) findViewById(R.id.group);
        viewpager_container = (ViewPager) findViewById(R.id.viewpager_container);
        viewpager_container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currIndex = position;
                switch (position) {
                    case 0:
                        group.check(R.id.foot_bar_home);
                        break;
                    case 1:
                        group.check(R.id.foot_bar_im);
                        break;
                    case 2:
                        group.check(R.id.main_footbar_user);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.foot_bar_home:
                        viewpager_container.setCurrentItem(0, false);
                        break;
                    case R.id.foot_bar_im:
                        viewpager_container.setCurrentItem(1, false);
                        break;
                    case R.id.main_footbar_user:
                        viewpager_container.setCurrentItem(3, false);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mHomePageFragment != null) {
            mHomePageFragment.setBannerStatue(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mHomePageFragment != null) {
            mHomePageFragment.setBannerStatue(false);
        }
    }


    private long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_BACK == keyCode) {

            long currentTime = System.currentTimeMillis();
            long waitTime = 2000;
            if ((currentTime - touchTime) >= waitTime) {
                showToast("再按一次退出");
                touchTime = currentTime;
            } else {
                MyApplication.getInstance().exit();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_LOG:
                applyPermission();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(BaseEvent messageEvent) {
        if (messageEvent instanceof MessageEvent) {
            int type = ((MessageEvent) messageEvent).type;
            if (type == 1) { //打电话
                String messageStr = ((MessageEvent) messageEvent).message;
                applyCallPermission(messageStr);
            }
        }

    }


    private void applyCallPermission(String num) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] strPermission = new String[]{Manifest.permission.CALL_PHONE};
            String[] strNeed = PermissionsUtil.getNeedApplyPermissions(strPermission);
            if (strNeed != null && strNeed.length > 0) {
                ActivityCompat.requestPermissions(this, strNeed, REQUEST_READ_LOG);
                return;
            }
        }
        IntentUtil.callPhone(MainActivity.this, num);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
