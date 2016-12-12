package com.visa.socialshare.lib.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.visa.socialshare.lib.R;
import com.visa.socialshare.lib.R2;
import com.visa.socialshare.lib.models.CustomShareItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxie on 10/11/16.
 */

public class CustomChooserAdapter extends BaseAdapter {
    private Context mContext;
    private List<CustomShareItem> targets;
    private int itemLayoutId;

    public CustomChooserAdapter(Context context, List<CustomShareItem> targets) {
        this.mContext = context;
        this.targets = targets;
    }

    @Override
    public int getCount() {
        return targets.size();
    }

    @Override
    public Object getItem(int position) {
        return targets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_chooser_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CustomShareItem target = targets.get(position);
        viewHolder.imageView.setImageDrawable(target.getResolveInfo().loadIcon(mContext.getPackageManager()));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R2.id.custom_chooser_item_img)
        ImageView imageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
