package com.tangguo.tangguoxianjin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.model.HelpCenterInfo;
import com.tangguo.tangguoxianjin.util.ArraysUtils;

import java.util.List;

public class HelpCenterAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    private String defaultGrorpName = "default";
    List<HelpCenterInfo.QaListBeanX> groupList;

    public HelpCenterAdapter(Context context, List<HelpCenterInfo.QaListBeanX> groupList) {
        mContext = context;
        defaultGrorpName = "帮助";
        this.groupList = groupList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        if (ArraysUtils.isNotEmpty(groupList)) {
            return groupList.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.expand_listview_group_item, parent, false);
        }
        convertView.setTag(R.id.team_singlechat_id_send, groupPosition);
        convertView.setTag(R.id.team_singlechat_id_close, -1);
        final TextView text = (TextView) convertView.findViewById(R.id.help_center__list_group_item_text);
        HelpCenterInfo.QaListBeanX groupItem = groupList.get(groupPosition);
        if (groupItem != null) {
            text.setText(groupItem.getTitle());
        }
        final ImageView expandedImage = (ImageView) convertView.findViewById(R.id.help_center__list_group_expanded_image);
        final int resId = isExpanded ? R.drawable.help_center_group_up : R.drawable.help_center_group_down;
        expandedImage.setImageResource(resId);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupList != null) {
            if (groupList.size() >= (groupPosition + 1)) {
                if (groupList.get(groupPosition) != null) {
                    if (groupList.get(groupPosition).getQaList() != null) return groupList.get(groupPosition).getQaList().size();
                }
            }
        }
        return 0;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition).getQaList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.expand_listview_child_item, parent, false);
        }
        convertView.setTag(R.id.team_singlechat_id_send, groupPosition);
        convertView.setTag(R.id.team_singlechat_id_close, childPosition);
        final TextView textName = (TextView) convertView.findViewById(R.id.tv_help_center_item);

        textName.setText(groupList.get(groupPosition).getQaList().get(childPosition).getText());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
