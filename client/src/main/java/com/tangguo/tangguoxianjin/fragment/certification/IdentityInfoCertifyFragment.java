package com.tangguo.tangguoxianjin.fragment.certification;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.activity.CertificationCenterActivity;
import com.tangguo.tangguoxianjin.common.BaseFragment;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.eventbus.CertificationMessageEvent;
import com.tangguo.tangguoxianjin.model.AuthStatusInfo;
import com.tangguo.tangguoxianjin.model.CertifyIdInfo;
import com.tangguo.tangguoxianjin.model.UploadImgInfo;
import com.tangguo.tangguoxianjin.net.HttpRequestService;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.DisplayImageOptionsUtil;
import com.tangguo.tangguoxianjin.util.FileUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.util.TextViewUtil;
import com.tangguo.tangguoxianjin.view.dialog.CommonDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;


public class IdentityInfoCertifyFragment extends BaseFragment {
    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;
    private Button btn_confirm;
    private ImageView iv_id_positive;
    private String pathImageId = "";//身份正面
    private String pathImageIdTurn = "";//身份背面
    private String pathImageLiveBody = "";//活体
    private ImageView iv_id_reverse;
    private ImageView tv_acitvity;
    private TextView tv_remark;
    private boolean isCertifyIdSuccess = false;
    private EditText ed_name, et_identity;
    private TextView ed_phone_num_effective;
    private boolean onlyPart = false;
    private SwipeRefreshLayout swipe_ly_id_certify;
    private int detailPart = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_identity_info_certify, container, false);
            btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
            iv_id_positive = (ImageView) view.findViewById(R.id.iv_id_positive);
            swipe_ly_id_certify = (SwipeRefreshLayout) view.findViewById(R.id.swipe_ly_id_certify);
            swipe_ly_id_certify.setColorSchemeResources(R.color.color_theme);
            iv_id_reverse = (ImageView) view.findViewById(R.id.iv_id_reverse);
            tv_acitvity = (ImageView) view.findViewById(R.id.tv_acitvity);
            tv_remark = (TextView) view.findViewById(R.id.tv_remark);
            ed_name = (EditText) view.findViewById(R.id.ed_name);
            et_identity = (EditText) view.findViewById(R.id.et_identity);
            ed_phone_num_effective = (TextView) view.findViewById(R.id.ed_phone_num_effective);
            isPrepared = true;
            lazyLoad();
        }
        return view;
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        Log.i("TAG", "threeFragment--lazyLoad");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CertificationCenterActivity activty = (CertificationCenterActivity) getActivity();
        if (activty.getShowTopInt() == 0) {
            onlyPart = true;
        } else onlyPart = false;
        this.detailPart = activty.getDetialPart();
        initListener();
        requestData(true);
        requestCertifyStatus();
        if (detailPart == MyConstants.CERTIFICATION_STEP_ONLY_PART_SESAME) showSesameDialog();
    }


    private void requestData(boolean flag) {
        requestConfig(flag);
        String remarkStr = "糖果现金提示：<br/><font color='#ef7030'>*</font>请填写您的真实有效个人信息，认证成功后不可修改<br/><font color='#ef7030'>*</font>您的信息仅供借款审核使用，不会再任何地方泄露您的信息";
        tv_remark.setText(Html.fromHtml(remarkStr));
    }


    private void initListener() {
        //刷新
        swipe_ly_id_certify.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(false);
            }
        });
        //提交
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(pathImageId)) {
                    showToast("请拍摄身份证正面照");
                } else if (StringUtils.isEmpty(pathImageIdTurn)) {
                    showToast("请拍摄身份证背面照");
                } else if (StringUtils.isEmpty(pathImageLiveBody)) {
                    showToast("请拍摄生活照");
                } else if (StringUtils.isEmpty(TextViewUtil.getText(ed_name))) {
                    showToast("请输入姓名");
                } else if (StringUtils.isEmpty(TextViewUtil.getText(et_identity))) {
                    showToast("请输入身份证号");
                } else if (StringUtils.isEmpty(TextViewUtil.getText(ed_phone_num_effective))) {
                    showToast("请输入身份证有效期限");
                } else showSubmitIdInfoDialog();

            }
        });
        //身份证正面
        iv_id_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_id_positive.getDrawable() == null) {
                    EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_ID_POSITIVE));
                } else {
                    if (isCertifyIdSuccess) showImage(pathImageId);
                    else EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_ID_POSITIVE));
                }

            }
        });
        //身份证反面
        iv_id_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_id_reverse.getDrawable() == null) {
                    EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_ID_TURN));
                } else {
                    if (isCertifyIdSuccess) showImage(pathImageIdTurn);
                    else EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_ID_TURN));
                }

            }
        });
        //生活照
        tv_acitvity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_acitvity.getDrawable() == null) {
                    EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_LIVE_BODY));
                } else {
                    showImage(pathImageLiveBody);
                }
            }
        });
        //有效日期
        ed_phone_num_effective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCertifyIdSuccess) dateSelect();
            }
        });

    }

    /**
     * 个人信息提交确认dialog
     */
    private void showSubmitIdInfoDialog() {
        CommonDialog mDialog = new CommonDialog(context);
        mDialog.setTitle("温馨提示");
        mDialog.setContentGravity(Gravity.LEFT);
        mDialog.setContent("个人实名信息提交后不能修改，您确认提交吗？");
        mDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestIdCertify();
            }
        });
        mDialog.setNegativeButton("取消");
        mDialog.show();

    }

    /**
     * 身份认证数据请求
     */
    private void requestIdCertify() {
        showSesameDialog();
    }

    public void setPositiveImage(String pathImage, int type) {
        if (type == MyConstants.CERTIFICATION_ID_POSITIVE) {
            uploadImg(pathImage, 1);
            ImageLoader.getInstance().displayImage("file://" + pathImage, iv_id_positive, DisplayImageOptionsUtil.getDisplayImageOptions(-1, true, true));
        } else if (type == MyConstants.CERTIFICATION_ID_TURN) {
            uploadImg(pathImage, 2);
            ImageLoader.getInstance().displayImage("file://" + pathImage, iv_id_reverse, DisplayImageOptionsUtil.getDisplayImageOptions(-1, true, true));
        } else if (type == MyConstants.CERTIFICATION_LIVE_BODY) {
            uploadImg(pathImage, 3);
            ImageLoader.getInstance().displayImage("file://" + pathImage, tv_acitvity, DisplayImageOptionsUtil.getDisplayImageOptions(-1, true, true));
        }
    }


    /**
     * 查看图片
     *
     * @param imageUrl
     */
    private void showImage(String imageUrl) {
        if (!StringUtils.isEmpty(imageUrl) && context != null) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_show_photo, null);
            ImageView tv_show_photo = (ImageView) dialogView.findViewById(R.id.tv_show_photo);
            ImageLoader.getInstance().displayImage(imageUrl, tv_show_photo, DisplayImageOptionsUtil.getDisplayImageOptions(-1, true, true));
            final Dialog dialog = new Dialog(context, R.style.commonDialog);
            dialog.setContentView(dialogView);
            tv_show_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    /**
     * 芝麻信用dialog
     */
    private void showSesameDialog() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_custom_layout, null);
        Button btn_confirm_ok = (Button) dialogView.findViewById(R.id.btn_confirm_ok);
        ImageButton ib_dialog_close = (ImageButton) dialogView.findViewById(R.id.ib_dialog_close);
        final Dialog dialog = new Dialog(context, R.style.commonDialog);
        dialog.setContentView(dialogView);
        btn_confirm_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* dialog.dismiss();
                startAc(SesameCreditActivity.class);
                getActivity().overridePendingTransition(R.anim.activity_translate_up_into, R.anim.activity_alpha_out);*/
                requestSaveIdInfo();
            }
        });
        ib_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 日历选择
     */
    private void dateSelect() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ed_phone_num_effective.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, year, month, day);
        datePicker.show();
    }


    public void setOnlyPart(int showTopInt, int detailPart) {
        if (showTopInt == 0) onlyPart = true;
        else onlyPart = false;
        this.detailPart = detailPart;
    }


    /**
     * 实名信息
     */
    private void requestConfig(boolean flag) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        requestNetData(UrlConstans.REALNAME_INFO, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.REALNAME_INFO_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                swipe_ly_id_certify.setRefreshing(false);
                if (successorfail && (!StringUtils.isEmpty(json))) {

                    try {
                        Gson gson = new Gson();
                        CertifyIdInfo mCertifyIdInfo = gson.fromJson(json, new TypeToken<CertifyIdInfo>() {
                        }.getType());
                        if (mCertifyIdInfo != null) {
                            if (!StringUtils.isEmpty(mCertifyIdInfo.getIdcardZmImgUrl())) {
                                ImageLoader.getInstance().displayImage(mCertifyIdInfo.getIdcardZmImgUrl(), iv_id_positive, DisplayImageOptionsUtil.getDisplayImageOptions(-1, true, true));
                                pathImageId = mCertifyIdInfo.getIdcardZmImgUrl();
                            }
                            if (!StringUtils.isEmpty(mCertifyIdInfo.getIdcardFmImgUrl())) {
                                pathImageIdTurn = mCertifyIdInfo.getIdcardFmImgUrl();
                                ImageLoader.getInstance().displayImage(mCertifyIdInfo.getIdcardFmImgUrl(), iv_id_reverse, DisplayImageOptionsUtil.getDisplayImageOptions(-1, true, true));
                            }
                            if (!StringUtils.isEmpty(mCertifyIdInfo.getIdcardLiveImgUrl())) {
                                ImageLoader.getInstance().displayImage(mCertifyIdInfo.getIdcardLiveImgUrl(), tv_acitvity, DisplayImageOptionsUtil.getDisplayImageOptions(-1, true, true));
                                pathImageLiveBody = mCertifyIdInfo.getIdcardLiveImgUrl();
                            }
                            ed_name.setText(StringUtils.setNameMask(mCertifyIdInfo.getName()));
                            et_identity.setText(StringUtils.setIdCardMask(mCertifyIdInfo.getIdCard()));
                            ed_phone_num_effective.setText(mCertifyIdInfo.getIdentityActiveDate());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 认证状态
     */
    private void requestCertifyStatus() {
        showProgressDialog();
        HttpRequestService.requestCertifyStatus(context, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        AuthStatusInfo mAuthStatusInfo = gson.fromJson(json, new TypeToken<AuthStatusInfo>() {
                        }.getType());
                        if (StringUtils.isEquals(mAuthStatusInfo.getIdentityVtStatus(), "1")) {
                            isCertifyIdSuccess = true;
                        } else isCertifyIdSuccess = false;
                        setCertifyIdEnable();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dismissProgressDialog();
            }
        });
    }

    /**
     * 输入框是否可输入
     */
    private void setCertifyIdEnable() {
        if (isCertifyIdSuccess) {
            ed_name.setEnabled(false);
            et_identity.setEnabled(false);
            ed_phone_num_effective.setEnabled(false);
            btn_confirm.setVisibility(View.GONE);
            tv_remark.setVisibility(View.GONE);
        } else {
            ed_name.setEnabled(true);
            et_identity.setEnabled(true);
            ed_phone_num_effective.setEnabled(true);
            btn_confirm.setVisibility(View.VISIBLE);
            tv_remark.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 上传头像
     *
     * @param filePath
     * @param type
     */
    private void uploadImg(String filePath, final int type) {
        File file = new File(filePath);
        showProgressDialog();
        Log.e(TAG, FileUtil.getFileSize(file));
        HashMap<String, String> params = new HashMap<>();
        params.put("uid", AccountInfoUtils.getUid(context));
        params.put("type", String.valueOf(type));
        HttpRequestService.requestUploadImg(context, UrlConstans.REALNAME_UPLOAD_IMG, params, UrlConstans.REALNAME_UPLOAD_IMG_CODE, file, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                dismissProgressDialog();
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        UploadImgInfo mUploadImgInfo = gson.fromJson(json, new TypeToken<UploadImgInfo>() {
                        }.getType());
                        if (mUploadImgInfo != null) {
                            if (type == 1) {
                                if (!StringUtils.isEmpty(mUploadImgInfo.getImgUrl())) pathImageId = mUploadImgInfo.getImgUrl();
                            } else if (type == 2) {
                                if (!StringUtils.isEmpty(mUploadImgInfo.getImgUrl())) pathImageIdTurn = mUploadImgInfo.getImgUrl();
                            } else if (type == 3) {
                                if (!StringUtils.isEmpty(mUploadImgInfo.getImgUrl())) pathImageLiveBody = mUploadImgInfo.getImgUrl();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 保存身份信息
     */
    private void requestSaveIdInfo() {
        String name = TextViewUtil.getText(ed_name);
        String idCard = TextViewUtil.getText(et_identity);
        String identityActiveDate = TextViewUtil.getText(ed_phone_num_effective);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("idCard", idCard);
        map.put("identityActiveDate", identityActiveDate);
        map.put("name", name);
        map.put("uid", AccountInfoUtils.getUid(context));
        requestNetData(UrlConstans.SAVE_ID_INFO_CERTIFY, map, true, MyConstants.HttpMethod.HTTP_POST, UrlConstans.SAVE_ID_INFO_CERTIFY_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        JSONObject jb = new JSONObject(json);
                        jb.has("id");
                        showToast("身份认证完成");
                        if (onlyPart) getActivity().finish();
                        else {
                            EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_STEP_PHONE));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}

