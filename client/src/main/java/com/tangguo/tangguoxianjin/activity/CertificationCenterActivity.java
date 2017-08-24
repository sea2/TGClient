package com.tangguo.tangguoxianjin.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.eventbus.BaseEvent;
import com.tangguo.tangguoxianjin.eventbus.CertificationMessageEvent;
import com.tangguo.tangguoxianjin.fragment.certification.BankInfoCertifyFragment;
import com.tangguo.tangguoxianjin.fragment.certification.CertifySuccessFragment;
import com.tangguo.tangguoxianjin.fragment.certification.IdentityInfoCertifyFragment;
import com.tangguo.tangguoxianjin.fragment.certification.PhoneInfoCertifyFragment;
import com.tangguo.tangguoxianjin.model.AuthStatusInfo;
import com.tangguo.tangguoxianjin.net.HttpRequestService;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.FileDirectoryUtil;
import com.tangguo.tangguoxianjin.util.FileUtil;
import com.tangguo.tangguoxianjin.util.IntentUtil;
import com.tangguo.tangguoxianjin.util.LogManager;
import com.tangguo.tangguoxianjin.util.PermissionsUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.util.SystemUtil;
import com.tangguo.tangguoxianjin.view.TitleLayout;
import com.tangguo.tangguoxianjin.view.customcamera.BitmapUtils;
import com.tangguo.tangguoxianjin.view.customcamera.CameraActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CertificationCenterActivity extends BaseActivity {


    private android.widget.ImageView ividinfo;
    private android.widget.TextView tvidinfo;
    private android.widget.ImageView ivphoneinfo;
    private android.widget.TextView tvphoneinfo;
    private android.widget.ImageView ivbankinfo;
    private android.widget.TextView tvbankinfo;
    private android.view.View lineleft;
    private android.view.View lineright;
    private IdentityInfoCertifyFragment idFragment;
    private PhoneInfoCertifyFragment phoneFragment;
    private BankInfoCertifyFragment bankFragment;
    private CertifySuccessFragment successFragment;
    private File mFile;
    //拍照权限申请
    private final int CERTIFY_CENTER_PERMISSION_CAMERA = 15;
    //通讯录权限申请
    private final int CERTIFY_CENTER_PERMISSION_READ_CONTST = 16;
    //拍照权限申请后的动作code
    private int CERTIFY_CENTER_PERMISSION_CAMERA_REQUESTCODE;
    private final String CURR_INDEX = "currIndex";
    private int currIndex = 0;
    private int showTopInt = 1;
    //认证具体步骤参数
    private int detialPart = 0;
    private ArrayList<String> fragmentTags = new ArrayList<>(Arrays.asList("IdentityInfoCertifyFragment", "PhoneInfoCertifyFragment", "BankInfoCertifyFragment", "CertifySuccessFragment"));
    private android.widget.RelativeLayout rlcertifycentertopstatue;
    private android.widget.FrameLayout flcontent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_center);

        if (savedInstanceState != null) {
            initFromSavedInstantsState(savedInstanceState);
            try {
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                if (fragmentManager.findFragmentByTag(fragmentTags.get(0)) != null)
                    idFragment = (IdentityInfoCertifyFragment) fragmentManager.findFragmentByTag(fragmentTags.get(0));
                if (fragmentManager.findFragmentByTag(fragmentTags.get(1)) != null)
                    phoneFragment = (PhoneInfoCertifyFragment) fragmentManager.findFragmentByTag(fragmentTags.get(1));
                if (fragmentManager.findFragmentByTag(fragmentTags.get(2)) != null)
                    bankFragment = (BankInfoCertifyFragment) fragmentManager.findFragmentByTag(fragmentTags.get(2));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventBus.getDefault().register(this);
        initIntent();
        initView();
        initTitle();
        initListener();


        initData(currIndex);
    }

    private void initIntent() {
        Intent it = getIntent();
        if (it != null) {
            if (it.hasExtra(MyConstants.CERTIFICATION_STEP)) currIndex = it.getIntExtra(MyConstants.CERTIFICATION_STEP, 0);
            if (it.hasExtra(MyConstants.CERTIFICATION_SHOW_TOP)) showTopInt = it.getIntExtra(MyConstants.CERTIFICATION_SHOW_TOP, 1);
            if (it.hasExtra(MyConstants.CERTIFICATION_STEP_ONLY_PART))
                detialPart = it.getIntExtra(MyConstants.CERTIFICATION_STEP_ONLY_PART, 0);
        }
    }

    private void initView() {
        this.flcontent = (FrameLayout) findViewById(R.id.fl_content);
        this.rlcertifycentertopstatue = (RelativeLayout) findViewById(R.id.rl_certify_center_top_statue);
        this.tvbankinfo = (TextView) findViewById(R.id.tv_bank_info);
        this.ivbankinfo = (ImageView) findViewById(R.id.iv_bank_info);
        this.tvphoneinfo = (TextView) findViewById(R.id.tv_phone_info);
        this.ivphoneinfo = (ImageView) findViewById(R.id.iv_phone_info);
        this.tvidinfo = (TextView) findViewById(R.id.tv_id_info);
        this.ividinfo = (ImageView) findViewById(R.id.iv_id_info);
        this.lineright = (View) findViewById(R.id.line_right);
        this.lineleft = (View) findViewById(R.id.line_left);
        //头部状态进度显或隐
        if (showTopInt == 1) rlcertifycentertopstatue.setVisibility(View.VISIBLE);
        else rlcertifycentertopstatue.setVisibility(View.GONE);
    }

    private void initTitle() {
        if (basePageView != null) {
            if (showTopInt == 0) {
                switch (currIndex) {
                    case 0:
                        basePageView.setTitle("身份认证");
                        break;
                    case 1:
                        if (detialPart == MyConstants.CERTIFICATION_STEP_ONLY_PART_CONTACT) basePageView.setTitle("紧急联系人");
                        if (detialPart == MyConstants.CERTIFICATION_STEP_ONLY_PART_PHONE) basePageView.setTitle("手机认证");
                        break;
                    case 2:
                        basePageView.setTitle("银行卡认证");
                        break;
                }

            } else basePageView.setTitle("极速认证");
            basePageView.setBack(new TitleLayout.OnBackListener() {
                @Override
                public void onBack() {
                    backConfirm();
                }
            });
        }
    }

    private void initListener() {


    }

    private void initData(int step) {
        if (showTopInt == 0) initProgressTop(step);
        else requestCertifyStatus();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURR_INDEX, currIndex);
        super.onSaveInstanceState(outState);
    }

    private void initFromSavedInstantsState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currIndex = savedInstanceState.getInt(CURR_INDEX);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_BACK == keyCode) {
            backConfirm();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (intent.hasExtra(MyConstants.CERTIFICATION_STEP)) {
                int stepIntent = intent.getIntExtra(MyConstants.CERTIFICATION_STEP, currIndex);
                if (stepIntent != currIndex) {
                    requestCertifyStatus();
                }
            }
        }
    }


    private void backConfirm() {
        if (phoneFragment != null && phoneFragment.isVisible()) {
            if (phoneFragment.getPhoneCodeShow()) {
                phoneFragment.getPhoneCodeBackInputShow();
            } else finish();
        } else finish();
    }


    /**
     * 认证状态
     */
    private void requestCertifyStatus() {
        showProgressDialog();
        HttpRequestService.requestCertifyStatus(CertificationCenterActivity.this, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        AuthStatusInfo mAuthStatusInfo = gson.fromJson(json, new TypeToken<AuthStatusInfo>() {
                        }.getType());
                        if (StringUtils.isEquals(mAuthStatusInfo.getIdentityVtStatus(), "0")) {
                            initProgressTop(0);
                        } else if (StringUtils.isEquals(mAuthStatusInfo.getSesameVtStatus(), "0")) {
                            initProgressTop(0);
                            detialPart = MyConstants.CERTIFICATION_STEP_ONLY_PART_SESAME;
                        } else if (StringUtils.isEquals(mAuthStatusInfo.getPhoneVtStatus(), "0")) {
                            initProgressTop(1);
                            detialPart = MyConstants.CERTIFICATION_STEP_ONLY_PART_PHONE;
                        } else if (StringUtils.isEquals(mAuthStatusInfo.getContactVtStatus(), "0")) {
                            initProgressTop(1);
                            detialPart = MyConstants.CERTIFICATION_STEP_ONLY_PART_CONTACT;
                        } else if (StringUtils.isEquals(mAuthStatusInfo.getBankCardVtStatus(), "0")) {
                            initProgressTop(2);
                        } else {
                            initProgressTop(6);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dismissProgressDialog();
            }
        });
    }

    /**
     * 认证进度
     *
     * @param step
     */
    private void initProgressTop(int step) {
        currIndex = step;
        switch (step) {
            case 0:
                if (idFragment == null) idFragment = new IdentityInfoCertifyFragment();
                hideFragment(phoneFragment);
                hideFragment(bankFragment);
                hideFragment(idFragment);
                idFragment.setOnlyPart(showTopInt, detialPart);
                addOrShowFragment(idFragment, R.id.fl_content, fragmentTags.get(0));
                this.lineright.setBackgroundResource(R.color.bg_line);
                this.lineleft.setBackgroundResource(R.color.bg_line);
                this.tvidinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.color_theme));
                this.ividinfo.setBackgroundResource(R.drawable.icon_certify_identity_information_selected);
                this.tvphoneinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.tv_color_4d4d4d));
                this.ivphoneinfo.setBackgroundResource(R.drawable.icon_certify_phone_information);
                this.tvbankinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.tv_color_4d4d4d));
                this.ivbankinfo.setBackgroundResource(R.drawable.icon_certify_bank_information);
                break;
            case 1:
                if (phoneFragment == null) phoneFragment = new PhoneInfoCertifyFragment();
                hideFragment(idFragment);
                hideFragment(bankFragment);
                phoneFragment.setOnlyPart(showTopInt, detialPart);
                addOrShowFragment(phoneFragment, R.id.fl_content, fragmentTags.get(1));
                this.lineright.setBackgroundResource(R.color.bg_line);
                this.lineleft.setBackgroundResource(R.color.color_theme);
                this.tvidinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.color_theme));
                this.ividinfo.setBackgroundResource(R.drawable.icon_certify_identity_information_selected);
                this.tvphoneinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.color_theme));
                this.ivphoneinfo.setBackgroundResource(R.drawable.icon_certify_phone_information_selected);
                this.tvbankinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.tv_color_4d4d4d));
                this.ivbankinfo.setBackgroundResource(R.drawable.icon_certify_bank_information);

                break;
            case 2:
                if (bankFragment == null) bankFragment = new BankInfoCertifyFragment();
                hideFragment(idFragment);
                hideFragment(phoneFragment);
                bankFragment.setOnlyPart(showTopInt);
                addOrShowFragment(bankFragment, R.id.fl_content, fragmentTags.get(2));
                this.lineright.setBackgroundResource(R.color.color_theme);
                this.lineleft.setBackgroundResource(R.color.color_theme);
                this.tvidinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.color_theme));
                this.ividinfo.setBackgroundResource(R.drawable.icon_certify_identity_information_selected);
                this.tvphoneinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.color_theme));
                this.ivphoneinfo.setBackgroundResource(R.drawable.icon_certify_phone_information_selected);
                this.tvbankinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.color_theme));
                this.ivbankinfo.setBackgroundResource(R.drawable.icon_certify_bank_information_selected);
                break;
            case 6:
                if (successFragment == null) successFragment = new CertifySuccessFragment();
                hideFragment(phoneFragment);
                hideFragment(bankFragment);
                hideFragment(idFragment);
                addOrShowFragment(successFragment, R.id.fl_content, fragmentTags.get(3));
                this.lineright.setBackgroundResource(R.color.color_theme);
                this.lineleft.setBackgroundResource(R.color.color_theme);
                this.tvidinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.color_theme));
                this.ividinfo.setBackgroundResource(R.drawable.icon_certify_identity_information_selected);
                this.tvphoneinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.color_theme));
                this.ivphoneinfo.setBackgroundResource(R.drawable.icon_certify_phone_information_selected);
                this.tvbankinfo.setTextColor(ContextCompat.getColor(CertificationCenterActivity.this, R.color.color_theme));
                this.ivbankinfo.setBackgroundResource(R.drawable.icon_certify_bank_information_selected);
                break;
            default:
                break;
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(BaseEvent messageEvent) {
        if (messageEvent instanceof CertificationMessageEvent) {
            int typeEvent = ((CertificationMessageEvent) messageEvent).type;
            if (typeEvent == MyConstants.CERTIFICATION_ID_POSITIVE || typeEvent == MyConstants.CERTIFICATION_ID_TURN || typeEvent == MyConstants.CERTIFICATION_LIVE_BODY) {
                //拍照
                applyPermission(typeEvent);
            } else if (typeEvent == MyConstants.CERTIFICATION_RALETION || typeEvent == MyConstants.CERTIFICATION_RALETION2) {
                //选择通讯录
                applyContactsPermission(typeEvent);
            } else {
                //top进度切换
                requestCertifyStatus();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * @param requestCode 拍照权限检测
     */
    private synchronized void applyPermission(int requestCode) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] strPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            String[] strNeed = PermissionsUtil.getNeedApplyPermissions(strPermission);
            if (strNeed != null && strNeed.length > 0) {
                CERTIFY_CENTER_PERMISSION_CAMERA_REQUESTCODE = requestCode;
                ActivityCompat.requestPermissions(this, strNeed, CERTIFY_CENTER_PERMISSION_CAMERA);
                return;
            }
        } else {
            String[] strPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            String[] strNeed = PermissionsUtil.getNeedApplyPermissions(strPermission);
            if (strNeed != null && strNeed.length > 0) {
                showToast("请开启拍照权限");
                SystemUtil.openSetting(CertificationCenterActivity.this);
                return;
            }
        }
        if (requestCode == MyConstants.CERTIFICATION_LIVE_BODY) {
            IntentUtil.liveBodyTake(CertificationCenterActivity.this);
        } else takePhone(requestCode);
    }

    /**
     * @param typeEvent 通讯录权限检测
     */
    private synchronized void applyContactsPermission(int typeEvent) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] strPermission = new String[]{Manifest.permission.READ_CONTACTS};
            String[] strNeed = PermissionsUtil.getNeedApplyPermissions(strPermission);
            if (strNeed != null && strNeed.length > 0) {
                ActivityCompat.requestPermissions(this, strNeed, CERTIFY_CENTER_PERMISSION_READ_CONTST);
                return;
            }
        }

        Intent intent = new Intent(CertificationCenterActivity.this, ContactActivity.class);
        startActivityForResult(intent, typeEvent);

    }


    /**
     * 自定义相机
     *
     * @param requestCode
     */
    protected void takePhone(int requestCode) {

        Intent intent;
        String hintTake = "";
        intent = new Intent(CertificationCenterActivity.this, CameraActivity.class);
        mFile = FileDirectoryUtil.getOwnCacheDirectory(CertificationCenterActivity.this);
        //文件保存的路径和名称
        intent.putExtra("file", mFile.toString());
        //拍照时的提示文本
        if (requestCode == MyConstants.CERTIFICATION_ID_POSITIVE) {
            hintTake = "请将证件摆正，正面置于框内。";
        } else if (requestCode == MyConstants.CERTIFICATION_ID_TURN) {
            hintTake = "请将证件摆正，反面置于框内。";
        }
        intent.putExtra("hint", hintTake);
        //是否使用整个画面作为取景区域(全部为亮色区域)
        intent.putExtra("hideBounds", false);
        //最大允许的拍照尺寸（像素数）
        intent.putExtra("maxPicturePixels", 3840 * 2160);
        startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && resultCode != 200) return;
        final int type = requestCode;
        if (requestCode == MyConstants.CERTIFICATION_ID_POSITIVE || requestCode == MyConstants.CERTIFICATION_ID_TURN) {
            if (data == null) {
                return;
            }
            mFile = new File(data.getStringExtra("file"));
            Observable.create(new Observable.OnSubscribe<File>() {
                @Override
                public void call(Subscriber<? super File> subscriber) {
                    //可以做各种复杂的操作,然后进行回调
                    Bitmap bitmapCompress = BitmapUtils.compressToResolution(mFile, 1920 * 1080);
                    Bitmap bitmapCrop = BitmapUtils.crop(bitmapCompress);
                    if (type == MyConstants.CERTIFICATION_ID_POSITIVE)
                        subscriber.onNext(BitmapUtils.writeBitmapToFile(bitmapCrop, "mFilePosivite.jpg"));
                    else if (type == MyConstants.CERTIFICATION_ID_TURN)
                        subscriber.onNext(BitmapUtils.writeBitmapToFile(bitmapCrop, "mFileTurn.jpg"));
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( //此行为订阅,只有真正的被订阅,整个流程才算生效
                    new Observer<File>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, e.toString());
                        }

                        @Override
                        public void onNext(File mFile) {
                            String pathEnd = mFile.toString();
                            LogManager.i(TAG, "拍照图片大小" + FileUtil.getFileSize(mFile));
                            DiskCacheUtils.removeFromCache(pathEnd, ImageLoader.getInstance().getDiskCache());
                            MemoryCacheUtils.removeFromCache(pathEnd, ImageLoader.getInstance().getMemoryCache());
                            if (idFragment != null) {idFragment.setPositiveImage(pathEnd, type);
                            }
                        }
                    });

        } else if (requestCode == MyConstants.CERTIFICATION_LIVE_BODY) {
            int code = data.getIntExtra("detectResultCode", 0);
            if (code == 1) {
                String imagePath = data.getStringExtra("imagePath");
                idFragment.setPositiveImage(imagePath, requestCode);
            } else {
                showToast("识别错误，请重新拍照");
            }
        } else if (requestCode == MyConstants.CERTIFICATION_RALETION || requestCode == MyConstants.CERTIFICATION_RALETION2) {
            if (data == null) {
                return;
            }
            String number = data.getStringExtra("number");
            phoneFragment.setSelectPhone(number, requestCode);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CERTIFY_CENTER_PERMISSION_CAMERA:
                applyPermission(CERTIFY_CENTER_PERMISSION_CAMERA_REQUESTCODE);
                break;
            case CERTIFY_CENTER_PERMISSION_READ_CONTST:
                applyPermission(CERTIFY_CENTER_PERMISSION_CAMERA_REQUESTCODE);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public int getShowTopInt() {
        return showTopInt;
    }

    public int getDetialPart() {
        return detialPart;
    }
}
