package org.lvu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import org.lvu.R;
import org.lvu.model.Data;
import org.lvu.utils.HttpUtil;
import org.lvu.utils.ImmerseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuyr on 6/23/16 6:16 PM.
 */
public class GifPictureAdapter extends EvilComicsAdapter {

    public GifPictureAdapter(Context context, int layoutId, List<Data> data) {
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
    protected String getUrl() {
        return "http://www.lovefou.com/dongtaitu/";
    }

    @Override
    public void syncData(@NonNull String url) {
        if (url.isEmpty())
            url = URL;
        HttpUtil.getGifList(url, mSyncDataCallbackListener);
    }

    @Override
    public void loadMore() {
        if (mNextPageUrl == null || mNextPageUrl.isEmpty())
            syncData("");
        else
            HttpUtil.getGifList(mNextPageUrl, mLoadMoreCallbackListener);
    }

    @Override
    public void refreshData() {
        HttpUtil.getGifList(URL, mRefreshDataCallbackListener);
        mData = new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends BasePictureListAdapter.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.progress_bar);
        }
    }
}
