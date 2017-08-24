package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.adapter.base.CommonAdapter;
import com.tangguo.tangguoxianjin.adapter.base.ViewHolder;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.model.BorrowRecordInfo;
import com.tangguo.tangguoxianjin.model.BorrowRecordListInfo;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.IntentUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.view.CircleImageView;
import com.tangguo.tangguoxianjin.view.CustomImageButton;
import com.tangguo.tangguoxianjin.view.XListView.XListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BorrowRecordActivity extends BaseActivity implements XListView.IXListViewListener {

    private com.tangguo.tangguoxianjin.view.XListView.XListView lvborrowrecord;

    private List<BorrowRecordInfo> mBorrowRecordInfoDatas = null;
    CommonAdapter<BorrowRecordInfo> mCommonAdapter;
    private BorrowRecordListInfo mBorrowRecordListInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_record);
        this.lvborrowrecord = (XListView) findViewById(R.id.lv_borrow_record);
        lvborrowrecord.setPullRefreshEnable(true);
        lvborrowrecord.setPullLoadEnable(false);
        lvborrowrecord.setAutoLoadEnable(false);
        lvborrowrecord.setXListViewListener(this);
        lvborrowrecord.setRefreshTime(getTime());
        initView();
        initListener();
        initData();
    }


    private void initView() {
        setTitle("借款记录");

        // 设置适配器
        lvborrowrecord.setAdapter(mCommonAdapter = new CommonAdapter<BorrowRecordInfo>(getApplicationContext(), mBorrowRecordInfoDatas, R.layout.item_list_borrow_record) {
            @Override
            public void convert(ViewHolder helper, final BorrowRecordInfo item) {
                if (item != null) {
                    helper.setText(R.id.tv_borrow_record_id, "借款编号：" + item.getRecordId());
                    helper.setText(R.id.tv_borrow_record_money, item.getRecordBorrowMoney() + "元");
                    helper.setText(R.id.tv_borrow_record_time_line, item.getRecordBorrowLines() + "天");
                    String statusStr = "";
                    String statusTime = "";
                    switch (item.getRecordBorrowStatus()) {
                        case "1":
                            statusStr = "审批中";
                            statusTime = "放款时间：等待放款";
                            break;
                        case "2":
                            statusStr = "申请失败";
                            statusTime = "放款时间：申请失败";
                            break;
                        case "3":
                            statusStr = "待还款";
                            statusTime = "放款时间：" + item.getOpenMoneyTime() + "\n应还时间：" + item.getRepayTime();
                            break;
                        case "4":
                            statusStr = "已还款";
                            statusTime = "放款时间：" + item.getOpenMoneyTime() + "\n应还时间：" + item.getRepayTime();
                            break;
                        case "5":
                            statusStr = "逾期";
                            statusTime = "放款时间：" + item.getOpenMoneyTime() + "\n应还时间：" + item.getRepayTime();
                            break;
                        default:
                            statusStr = "未知状态";
                            break;
                    }
                    helper.setText(R.id.tv_borrow_record_status, statusStr);
                    helper.setText(R.id.tv_borrow_time, statusTime);
                    TextView tv_borrow_record_id_right = helper.getView(R.id.tv_borrow_record_id_right);
                    CustomImageButton btn_repay = helper.getView(R.id.btn_repay);
                    CircleImageView iv_dot = helper.getView(R.id.iv_dot);
                    tv_borrow_record_id_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IntentUtil.startWebActivity(BorrowRecordActivity.this, BorrowRecordActivity.class, item.getProtocolUrl());
                        }
                    });
                    if (StringUtils.isEquals(item.getRecordBorrowStatus(), "3") || StringUtils.isEquals(item.getRecordBorrowStatus(), "5")) {
                        btn_repay.setVisibility(View.VISIBLE);
                        iv_dot.setImageResource(R.color.red);
                    } else {
                        btn_repay.setVisibility(View.GONE);
                        iv_dot.setImageResource(R.color.tv_color_808080);
                    }
                    btn_repay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle mb = new Bundle();
                            mb.putString("recordId", item.getRecordId());
                            startAc(RepaymentActivity.class, mb);
                        }
                    });
                }
            }
        });
    }


    private void initListener() {
    }


    private void initData() {
        requestData(true);
    }

    @Override
    public void onRefresh() {
        requestData(false);
    }


    @Override
    public void onLoadMore() {
        onLoad();
    }


    private void onLoad() {
        lvborrowrecord.stopRefresh();
        lvborrowrecord.stopLoadMore();
        lvborrowrecord.setRefreshTime(getTime());
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }


    /**
     * 借款记录
     */
    private void requestData(boolean flag) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(BorrowRecordActivity.this));
        requestNetData(UrlConstans.BORROW_RECORD, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.BORROW_RECORD_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        mBorrowRecordListInfo = gson.fromJson(json, new TypeToken<BorrowRecordListInfo>() {
                        }.getType());
                        if (mBorrowRecordListInfo != null) {
                            mBorrowRecordInfoDatas = mBorrowRecordListInfo.getRecordList();
                            mCommonAdapter.referData(mBorrowRecordInfoDatas);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                onLoad();
            }
        });
    }

}
