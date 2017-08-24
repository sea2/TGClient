package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.adapter.base.CommonAdapter;
import com.tangguo.tangguoxianjin.adapter.base.ViewHolder;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.model.MessageInfo;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.view.CircleImageView;
import com.tangguo.tangguoxianjin.view.XListView.XListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyMessageActivity extends BaseActivity implements XListView.IXListViewListener {

    private List<MessageInfo.MessageListBean> messageList;
    private CommonAdapter<MessageInfo.MessageListBean> mCommonAdapter;
    private XListView lvmessagemine;
    private MessageInfo mMessageInfo;
    private int pageNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        this.lvmessagemine = (XListView) findViewById(R.id.lv_message_mine);
        lvmessagemine.setPullRefreshEnable(true);
        lvmessagemine.setXListViewListener(this);
        lvmessagemine.setRefreshTime(getTime());
        initView();
        initListener();
        initData();
    }


    private void initView() {
        setTitle("我的消息");

        // 设置适配器
        lvmessagemine.setAdapter(mCommonAdapter = new CommonAdapter<MessageInfo.MessageListBean>(getApplicationContext(), messageList, R.layout.item_list_message_mine) {
            @Override
            public void convert(ViewHolder helper, final MessageInfo.MessageListBean item) {
                if (item != null) {
                    helper.setText(R.id.tv_message_title, item.getTitle());
                    helper.setText(R.id.tv_message_time, item.getDate());
                    helper.setText(R.id.tv_message_content, item.getText());

                    CircleImageView iv_dot = helper.getView(R.id.iv_message_dot);
                    if (StringUtils.isEquals(item.getStatus(), "0")) iv_dot.setImageResource(R.color.red);
                    else iv_dot.setImageResource(R.color.tv_color_b3b3b3);
                }
            }
        });
    }


    private void initListener() {
        requestMessage(true);
    }


    private void initData() {
        requestMessage(true);


    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        requestMessage(false);
    }


    @Override
    public void onLoadMore() {
        addMessage(false);
    }


    private void onLoad() {
        lvmessagemine.stopRefresh();
        lvmessagemine.stopLoadMore();
        lvmessagemine.setRefreshTime(getTime());
    }


    /**
     * 消息
     */
    private void requestMessage(boolean flag) {
        if (StringUtils.isEmpty(AccountInfoUtils.getUid(MyMessageActivity.this))) return;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("pageNo", "1");
        map.put("uid", AccountInfoUtils.getUid(MyMessageActivity.this));
        requestNetData(UrlConstans.ACCOUNT_MESSAGE, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.ACCOUNT_MESSAGE_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        mMessageInfo = gson.fromJson(json, new TypeToken<MessageInfo>() {
                        }.getType());
                        if (mMessageInfo != null) {
                            messageList = mMessageInfo.getMessageList();
                            mCommonAdapter.referData(messageList);
                            addMessage(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                onLoad();
            }
        });
    }

    private void addMessage(boolean flag) {
        pageNo++;
        if (StringUtils.isEmpty(AccountInfoUtils.getUid(MyMessageActivity.this))) return;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("pageNo", String.valueOf(pageNo));
        map.put("uid", AccountInfoUtils.getUid(MyMessageActivity.this));
        requestNetData(UrlConstans.ACCOUNT_MESSAGE, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.ACCOUNT_MESSAGE_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        MessageInfo mMessageInfoNew = gson.fromJson(json, new TypeToken<MessageInfo>() {
                        }.getType());
                        if (mMessageInfoNew != null) {
                            List<MessageInfo.MessageListBean> mMessageListNew = mMessageInfoNew.getMessageList();
                            if (mMessageListNew != null && mMessageListNew.size() > 0 && messageList != null) {
                                messageList.addAll(mMessageListNew);
                                mCommonAdapter.referData(messageList);
                            }

                            if (mMessageListNew != null && mMessageListNew.size() > 0) {
                                lvmessagemine.setPullLoadEnable(true);
                                lvmessagemine.setAutoLoadEnable(true);
                            } else {
                                lvmessagemine.setPullLoadEnable(false);
                                lvmessagemine.setAutoLoadEnable(false);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                onLoad();
            }
        });
    }


    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}

