package org.lvu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.lvu.R;
import org.lvu.model.Data;
import org.lvu.utils.ImmerseUtil;

import java.util.List;

/**
 * Created by wuyr on 6/23/16 3:15 PM.
 */
public abstract class BasePictureListAdapter extends BaseListAdapter {

    ImageLoader mImageLoader;

    BasePictureListAdapter(Context context, int layoutId, List<Data> data) {
        super(context, layoutId, data);
        mImageLoader = ImageLoader.getInstance();
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
    public void onBindViewHolder(final BaseListAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mData.isEmpty())
            return;
        try {
            mImageLoader.displayImage(mData.get(position != 0 && position >= mData.size() ?
                            mData.size() - 1 : position).getSrc(), holder.image,
                    new DisplayImageOptions.Builder()
                            .showImageOnFail(R.drawable.ic_pic_bad)
                            .showImageOnLoading(R.drawable.ic_pic_loading)
                            .showImageForEmptyUri(R.drawable.ic_pic_bad)
                            .cacheInMemory(true).cacheOnDisk(true).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ViewHolder extends BaseListAdapter.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
