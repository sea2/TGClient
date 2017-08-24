package com.tangguo.tangguoxianjin.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.MyApplication;
import com.tangguo.tangguoxianjin.model.ListDialogInfo;
import com.tangguo.tangguoxianjin.util.ArraysUtils;
import com.tangguo.tangguoxianjin.util.DisplayImageOptionsUtil;
import com.tangguo.tangguoxianjin.util.ScreenUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhy on 2017/4/25.
 */

public class ListSelectDialog extends Dialog {

    private View mView;
    private TextView tvTitle = null;// 标题
    private TextView tvContent = null;// 内容
    private List<ListDialogInfo> mListDialogInfoList = new ArrayList<>();
    private String sureText;
    private View.OnClickListener sureClickListener;
    private ListView lv_dialog;
    private MyAdapter myAdapter;
    private SelectListener selectListener = null;

    public ListSelectDialog(Context context) {
        super(context, R.style.commonDialog);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        mView = getLayoutInflater().inflate(R.layout.dialog_list_select_layout, null);
        setContentView(mView);
        initViews(context);
    }

    /**
     * 设置标题头
     *
     * @param title
     */
    public void setTitle(String title) {
        if (title != null) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (null != title) {
            setTitle(title.toString());
        }
    }


    /**
     * 设置内容
     *
     * @param content
     */
    public void setContent(String content) {
        if (content != null) {
            tvContent.setVisibility(View.VISIBLE);
            String dialogContent = StringUtils.formatText(content);
            /*if (dialogContent.indexOf("\n") >= 0) {
                tvContent.setGravity(Gravity.LEFT);
			}*/
            tvContent.setText(dialogContent);
        }
    }

    @SuppressWarnings("deprecation")
    public void setContentHtml(String content) {
        if (content != null) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(Html.fromHtml(content));
        }
    }

    //内容的布局
    public void setContentGravity(int gravityInt) {
        if (tvContent != null) tvContent.setGravity(gravityInt);
    }

    public void setContent(Spanned spanned) {
        if (null != spanned) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(spanned);
        }
    }

    public void setList(List<ListDialogInfo> stringList) {
        if (null != stringList) {
            this.mListDialogInfoList = stringList;
            myAdapter.setList(stringList);
        }
    }

    public void setList(List<ListDialogInfo> stringList,int gravity) {
        if (null != stringList) {
            this.mListDialogInfoList = stringList;
            myAdapter.setList(stringList,gravity);
        }
    }



    public void setStringList(List<String> stringList) {
        if (null != stringList) {
            for (String str : stringList) {
                ListDialogInfo ldi = new ListDialogInfo("", str, "", "");
                if (mListDialogInfoList != null) mListDialogInfoList.add(ldi);
            }
            myAdapter.setList(mListDialogInfoList);
        }
    }

    public void setSelectListener(final SelectListener selectListener) {
        if (null != selectListener) {
            if (lv_dialog != null) lv_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListDialogInfo selectStr = (ListDialogInfo) parent.getAdapter().getItem(position);
                    selectListener.OnSelectOne(selectStr);
                    dismiss();
                }
            });
        }
    }


    /**
     * window层显示dialog,不依附与Activity(注：必须加入 <uses-permission
     * android:name="android.permission.SYSTEM_ALERT_WINDOW" />)
     */
    public void setShowOnWindow() {
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);// windows层显示
    }

    private void initViews(Context context) {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_content);
        lv_dialog = (ListView) findViewById(R.id.lv_dialog);
        myAdapter = new MyAdapter(context, mListDialogInfoList);
        lv_dialog.setAdapter(myAdapter);

    }

    @Override
    public void show() {
        if (getWindow() != null) {
            WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
            p.width = (int) (new ScreenUtil(MyApplication.getInstance()).getWidth() * 0.9); // 宽度设置为屏幕的0.9
            this.getWindow().setAttributes(p);
        }
        super.show();
    }


    class MyAdapter extends BaseAdapter {

        private Context context;
        public boolean isShowCheckbox = false;
        private List<ListDialogInfo> listDialogInfoList;
        private boolean isAllSelected = false;
        private LayoutInflater mInflater = null;
        int gravity = 0;  //0：居中    1：左对齐


        public MyAdapter(Context context, List<ListDialogInfo> list) {
            super();
            this.context = context;
            mInflater = LayoutInflater.from(context);
            this.listDialogInfoList = list;
            if (null == listDialogInfoList) {
                listDialogInfoList = new ArrayList<ListDialogInfo>();
            }
        }


        public void setList(List<ListDialogInfo> list) {
            if (list != null) {
                this.listDialogInfoList = list;
                notifyDataSetChanged();
            }
        }

        public void setList(List<ListDialogInfo> list,int gravity) {
            if (list != null) {
                this.listDialogInfoList = list;
                notifyDataSetChanged();
                this.gravity = gravity;
            }
        }

        @Override
        public int getCount() {
            if (ArraysUtils.isEmpty(listDialogInfoList)) {
                return 0;
            }
            return listDialogInfoList.size();
        }

        @Override
        public ListDialogInfo getItem(int position) {
            if (listDialogInfoList != null && listDialogInfoList.size() > 0) return listDialogInfoList.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_list_dialog_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_show_info = (TextView) convertView.findViewById(R.id.tv_show_info);
                viewHolder.ll_content = (LinearLayout) convertView.findViewById(R.id.ll_content);
                viewHolder.view_space = (View) convertView.findViewById(R.id.view_space);
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ListDialogInfo dataItem = listDialogInfoList.get(position);
            viewHolder.tv_show_info.setText(dataItem.getTitle());

            if (gravity == 1) {
                viewHolder.view_space.setVisibility(View.VISIBLE);
                viewHolder.ll_content.setGravity(Gravity.LEFT);
            } else {
                viewHolder.view_space.setVisibility(View.GONE);
                viewHolder.ll_content.setGravity(Gravity.CENTER);
            }

            if (!StringUtils.isEmpty(dataItem.getImageUrl())) {
                viewHolder.iv_icon.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(dataItem.getImageUrl(), viewHolder.iv_icon, DisplayImageOptionsUtil.getDisplayImageOptions(-1, true, true));
            } else viewHolder.iv_icon.setVisibility(View.GONE);
            return convertView;
        }


         class ViewHolder {
            TextView tv_show_info;
            ImageView iv_icon;
            View view_space;
            LinearLayout ll_content;
        }


    }


    public interface SelectListener {
        void OnSelectOne(ListDialogInfo mListDialogInfo);
    }


}
