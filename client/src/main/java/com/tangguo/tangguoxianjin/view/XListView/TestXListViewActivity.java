package com.tangguo.tangguoxianjin.view.XListView;

import android.os.Bundle;
import android.os.Handler;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.adapter.base.CommonAdapter;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.model.BorrowRecordInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TestXListViewActivity extends BaseActivity implements XListView.IXListViewListener {

    private com.tangguo.tangguoxianjin.view.XListView.XListView lvborrowrecord;

    private List<BorrowRecordInfo> mBorrowRecordInfoDatas = null;
    CommonAdapter<BorrowRecordInfo> mCommonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_record);
        this.lvborrowrecord = (XListView) findViewById(R.id.lv_borrow_record);
        lvborrowrecord.setPullRefreshEnable(true);
        lvborrowrecord.setPullLoadEnable(true);
        lvborrowrecord.setAutoLoadEnable(true);
        lvborrowrecord.setXListViewListener(this);
        lvborrowrecord.setRefreshTime(getTime());
        initView();
        initListener();
        initData();
    }


    private void initView() {


    }


    private void initListener() {
    }


    private void initData() {
        mBorrowRecordInfoDatas = new ArrayList<>();
        BorrowRecordInfo bri = new BorrowRecordInfo();
        bri.setOpenMoneyTime("2017.6.8");
        bri.setRecordBorrowLines("14天");
        bri.setRecordBorrowMoney("200元");
        bri.setRecordBorrowStatus("借款中");
        bri.setRecordId("4599867");
        mBorrowRecordInfoDatas.add(bri);
        mBorrowRecordInfoDatas.add(bri);

        mCommonAdapter.referData(mBorrowRecordInfoDatas);

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 2500);
    }


    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 2500);
    }


    private void onLoad() {
        lvborrowrecord.stopRefresh();
        lvborrowrecord.stopLoadMore();
        lvborrowrecord.setRefreshTime(getTime());
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}
