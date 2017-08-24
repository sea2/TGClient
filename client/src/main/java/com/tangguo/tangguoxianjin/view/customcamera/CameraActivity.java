package com.tangguo.tangguoxianjin.view.customcamera;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.MyApplication;
import com.tangguo.tangguoxianjin.util.SystemUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Camera API. Android KitKat 及以前版本的 Android 使用 Camera API.
 */
@SuppressWarnings("deprecation")
public class CameraActivity extends Activity {


    File mFile;
    Camera mCamera;
    CameraPreview mPreview;
    long mMaxPicturePixels;
    Subscription subscription = null;
    /**
     * 预览的最佳尺寸是否已找到
     */
    volatile boolean mPreviewBestFound;

    /**
     * 拍照的最佳尺寸是否已找到
     */
    volatile boolean mPictureBestFound;

    /**
     * finish()是否已调用过
     */
    volatile boolean mFinishCalled;
    private FrameLayout flcamerapreview;
    private View viewcameradark0;
    private TextView tvcamerahint;
    private LinearLayout viewcameradark1;
    private ImageView ivcamerabutton;
    private ImageButton ibcameraclose;
    Camera.Parameters params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_custom);

        this.ibcameraclose = (ImageButton) findViewById(R.id.ib_camera_close);
        this.ivcamerabutton = (ImageView) findViewById(R.id.iv_camera_button);
        this.viewcameradark1 = (LinearLayout) findViewById(R.id.view_camera_dark1);
        this.tvcamerahint = (TextView) findViewById(R.id.tv_camera_hint);
        this.viewcameradark0 = (View) findViewById(R.id.view_camera_dark0);
        this.flcamerapreview = (FrameLayout) findViewById(R.id.fl_camera_preview);
        ibcameraclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinishCalled = true;
                finish();
            }
        });
        preInitData();
    }


    protected void preInitData() {


        mFile = new File(getIntent().getStringExtra("file"));
        tvcamerahint.setText(getIntent().getStringExtra("hint"));
        if (getIntent().getBooleanExtra("hideBounds", false)) {
            viewcameradark0.setVisibility(View.INVISIBLE);
            viewcameradark1.setVisibility(View.INVISIBLE);
        }
        mMaxPicturePixels = getIntent().getIntExtra("maxPicturePixels", 3840 * 2160);
        initCamera();
        ivcamerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera == null) return;
                ivcamerabutton.setClickable(false);
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(final byte[] data, Camera camera) {
                        Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                try {
                                    if (mFile.exists()) mFile.delete();
                                    FileOutputStream fos = new FileOutputStream(mFile);
                                    fos.write(data);
                                    try {
                                        fos.close();
                                    } catch (Exception ignored) {
                                    }
                                    subscriber.onNext(200);
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                setResult(integer, getIntent().putExtra("file", mFile.toString()));
                                mFinishCalled = true;
                                finish();
                            }
                        });
                    }
                });
            }
        });


    }

    void initCamera() {
        Observable.create(new Observable.OnSubscribe<Camera>() {
                              @Override
                              public void call(Subscriber<? super Camera> subscriber) {
                                  subscriber.onNext(CameraUtils.getCamera());
                              }
                          }

        ).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Camera>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Camera camera) {
                if (camera == null) {
                    //   showToast("相机开启失败，再试一次吧");
                    mFinishCalled = true;
                    finish();
                    return;
                }
                mCamera = camera;
                mPreview = new CameraPreview(CameraActivity.this, mCamera, new CameraPreview.ThrowableListener() {
                    @Override
                    public void onThrowable(Throwable throwable, boolean showToast) {
                        Toast.makeText(MyApplication.getInstance(), "相机开启权限获取失败,请开启相机权限", Toast.LENGTH_LONG).show();
                        SystemUtil.openSetting(CameraActivity.this);
                        mFinishCalled = true;
                        finish();
                    }
                });
                flcamerapreview.addView(mPreview);
                initParams();
            }
        });

    }

    void initParams() {
        params = mCamera.getParameters();
        //若相机支持自动开启/关闭闪光灯，则使用. 否则闪光灯总是关闭的.
        List<String> flashModes = params.getSupportedFlashModes();
        if (flashModes != null && flashModes.contains(Camera.Parameters.FLASH_MODE_AUTO))
            params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        mPreviewBestFound = false;
        mPictureBestFound = false;
        //寻找最佳预览尺寸，即满足16:9比例，且不超过1920x1080的最大尺寸;若找不到，则使用满足16:9的最大尺寸.
        //若仍找不到，使用最大尺寸;详见CameraUtils.findBestSize方法.

        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        Camera.Size findBestSize = CameraUtils.findBestSize(false, previewSizes, 1920 * 1080);
        if (findBestSize != null) {
            mPreviewBestFound = true;
            Log.e("---", "--" + findBestSize.width + "---" + findBestSize.height);
            params.setPreviewSize(findBestSize.width, findBestSize.height);
            if (mPictureBestFound) initFocusParams(params);
        }
        //寻找最佳拍照尺寸，即满足16:9比例，且不超过maxPicturePixels指定的像素数的最大Size;若找不到，则使用满足16:9的最大尺寸.
        //若仍找不到，使用最大尺寸;详见CameraUtils.findBestSize方法.
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        Camera.Size pictureSize = CameraUtils.findBestSize(true, pictureSizes, mMaxPicturePixels);
        if (pictureSize != null) {
            mPictureBestFound = true;
            Log.e("---", "--" + pictureSize.width + "---" + pictureSize.height);
            params.setPictureSize(pictureSize.width, pictureSize.height);
            if (mPreviewBestFound) initFocusParams(params);
        }
    }

    void initFocusParams(Camera.Parameters params) {
        //若支持连续对焦模式，则使用.
        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            setParameters(params);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            //进到这里，说明不支持连续对焦模式，退回到点击屏幕进行一次自动对焦.
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            setParameters(params);
            //点击屏幕进行一次自动对焦.
            flcamerapreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraUtils.autoFocus(mCamera);
                }
            });
            //4秒后进行第一次自动对焦
            subscription = Observable.interval(2, 4, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Long aLong) {
                    if (mCamera != null) CameraUtils.autoFocus(mCamera);
                }
            });
        }
    }


    void setParameters(Camera.Parameters params) {
        try {
            mCamera.setParameters(params);
        } catch (Exception e) {
            //非常罕见的情况
            //个别机型在SupportPreviewSizes里汇报了支持某种预览尺寸，但实际是不支持的，设置进去就会抛出RuntimeException.
            e.printStackTrace();
            try {
                //遇到上面所说的情况，只能设置一个最小的预览尺寸
                params.setPreviewSize(1920, 1080);
                mCamera.setParameters(params);
            } catch (Exception e1) {
                //到这里还有问题，就是拍照尺寸的锅了，同样只能设置一个最小的拍照尺寸
                e1.printStackTrace();
                try {
                    params.setPictureSize(1920, 1080);
                    mCamera.setParameters(params);
                } catch (Exception ignored) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        mFinishCalled = true;
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mCamera.stopPreview();
            mCamera.setPreviewDisplay(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mCamera.release();
            mCamera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!mFinishCalled) finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) subscription.unsubscribe();
    }


}
