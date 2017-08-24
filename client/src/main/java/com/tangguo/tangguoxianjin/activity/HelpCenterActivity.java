package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.adapter.HelpCenterAdapter;
import com.tangguo.tangguoxianjin.adapter.WrapperExpandableListAdapter;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.model.HelpCenterInfo;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.view.ExpandableListView.FloatingGroupExpandableListView;

import java.util.HashMap;
import java.util.List;

public class HelpCenterActivity extends BaseActivity {

    private FloatingGroupExpandableListView idhelpcenterexpandlistviw;
    private HelpCenterAdapter adapter;
    WrapperExpandableListAdapter wrapperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        this.idhelpcenterexpandlistviw = (FloatingGroupExpandableListView) findViewById(R.id.id_help_center_expand_listviw);
        idhelpcenterexpandlistviw.setGroupIndicator(null);
        initView();

        requestData(true);

    }

    private void initView() {
        setTitle("帮助中心");
    }




    /**
     * 借款记录
     */
    private void requestData(boolean flag) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(HelpCenterActivity.this));
        requestNetData(UrlConstans.ACCOUNT_HELP, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.ACCOUNT_HELP_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        HelpCenterInfo mHelpCenterInfo = gson.fromJson(json, new TypeToken<HelpCenterInfo>() {
                        }.getType());
                        if (mHelpCenterInfo != null) {
                            setAdapt(mHelpCenterInfo.getQaList());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    private void setAdapt(List<HelpCenterInfo.QaListBeanX> qaList) {
        if (adapter == null) adapter = new HelpCenterAdapter(this, qaList);
        if (wrapperAdapter == null) wrapperAdapter = new WrapperExpandableListAdapter(adapter);
        idhelpcenterexpandlistviw.setAdapter(wrapperAdapter);
        idhelpcenterexpandlistviw.expandGroup(0);


        idhelpcenterexpandlistviw.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (adapter.getChild(groupPosition, childPosition) != null) {
                }
                return true;
            }
        });


    }

}
