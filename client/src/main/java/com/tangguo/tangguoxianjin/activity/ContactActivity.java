package com.tangguo.tangguoxianjin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.view.contact.CharacterParser;
import com.tangguo.tangguoxianjin.view.contact.ClearEditText;
import com.tangguo.tangguoxianjin.view.contact.ConstactUtil;
import com.tangguo.tangguoxianjin.view.contact.PinyinComparator;
import com.tangguo.tangguoxianjin.view.contact.SideBar;
import com.tangguo.tangguoxianjin.view.contact.SortAdapter;
import com.tangguo.tangguoxianjin.view.contact.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author http://blog.csdn.net/finddreams
 * @Description:联系人显示界面
 */
public class ContactActivity extends BaseActivity {

    private View mBaseView;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private Map<String, String> callRecords;

    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private Subscription subscription = null;
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contact);
        initView();
        initData();
    }

    private void initView() {
        sideBar = (SideBar) this.findViewById(R.id.sidr_bar);
        dialog = (TextView) this.findViewById(R.id.tv_dialog);
        sortListView = (ListView) this.findViewById(R.id.sortlist);
        setTitle("联系人");
    }

    private void initData() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                String number = callRecords.get(((SortModel) adapter.getItem(position)).getName());
                Intent intent = new Intent();
                intent.putExtra("number", number);
                ContactActivity.this.setResult(200, intent);
                ContactActivity.this.finish();
            }
        });

        showProgressDialog();
        subscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //可以做各种复杂的操作,然后进行回调
                callRecords = ConstactUtil.getAllCallRecords(ContactActivity.this);
                subscriber.onNext("1");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( //此行为订阅,只有真正的被订阅,整个流程才算生效
                new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                        dismissProgressDialog();
                        List<String> constact = new ArrayList<String>();
                        for (Iterator<String> keys = callRecords.keySet().iterator(); keys.hasNext(); ) {
                            String key = keys.next();
                            constact.add(key);
                        }
                        String[] names = new String[]{};
                        names = constact.toArray(names);
                        SourceDateList = filledData(names);

                        // 根据a-z进行排序源数据
                        Collections.sort(SourceDateList, pinyinComparator);
                        adapter = new SortAdapter(ContactActivity.this, SourceDateList);
                        sortListView.setAdapter(adapter);

                        mClearEditText = (ClearEditText) ContactActivity.this.findViewById(R.id.filter_edit);
                        mClearEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

                            @Override
                            public void onFocusChange(View arg0, boolean arg1) {
                                mClearEditText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

                            }
                        });
                        // 根据输入框输入值的改变来过滤搜索
                        mClearEditText.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                                filterData(s.toString());
                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, s);
                    }
                });


    }


    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) subscription.unsubscribe();
    }
}
