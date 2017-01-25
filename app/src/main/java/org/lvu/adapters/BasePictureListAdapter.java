package org.lvu.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import org.lvu.R;
import org.lvu.customize.WrapContentDraweeView;
import org.lvu.models.Data;
import org.lvu.utils.ImmerseUtil;

import java.util.List;

/**
 * Created by wuyr on 6/23/16 3:15 PM.
 */
public abstract class BasePictureListAdapter extends BaseListAdapter {

    protected BasePictureListAdapter(Context context, int layoutId, List<Data> data) {
        super(context, layoutId, data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_BOTTOM && ImmerseUtil.isAboveKITKAT()
                && ImmerseUtil.isHasNavigationBar(mContext))
            return new FooterHolder(mLayoutInflater.inflate(
                    R.layout.recycler_view_item_footer, parent, false));
        return new ViewHolder(mLayoutInflater.inflate(mLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseListAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        initItemImage(holder, position);
    }

    protected void initItemImage(final BaseListAdapter.ViewHolder holder, int position) {
        if (mData.isEmpty())
            return;
        try {
            holder.image.setImageURI(Uri.parse(mData.get(position != 0 && position >= mData.size() ? mData.size() - 1 : position).getSrc()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ViewHolder extends BaseListAdapter.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            image = (WrapContentDraweeView) itemView.findViewById(R.id.image);
        }
    }
}
