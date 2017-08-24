package com.tangguo.tangguoxianjin.fragment.certification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.activity.BorrowMoneyActivity;
import com.tangguo.tangguoxianjin.common.BaseFragment;
import com.tangguo.tangguoxianjin.util.IntentUtil;

/**
 * Created by lhy on 2017/5/15.
 */

public class CertifySuccessFragment extends BaseFragment {



    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;
    private Button btn_confirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_success_certify, container, false);
            btn_confirm = (Button) view.findViewById(R.id.btn_confirm_code_end);
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
        Log.e("CertifySuccessFragment", "threeFragment--lazyLoad");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
    }

    private void initListener() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.backPageActivity(getActivity(),BorrowMoneyActivity.class);
            }
        });
    }

}