package org.lvu.main.fragments.view_pager_content.new_content_fragments.video;

import org.lvu.R;
import org.lvu.adapters.BaseListAdapter;
import org.lvu.adapters.newAdapters.video.Video3Adapter;
import org.lvu.models.Data;

import java.util.ArrayList;

/**
 * Created by wuyr on 1/10/17 10:13 PM.
 */

public class Video3Fragment extends Video1Fragment {

    @Override
    protected BaseListAdapter getAdapter() {
        return new Video3Adapter(getActivity(), R.layout.adapter_video_item, new ArrayList<Data>());
    }

    @Override
    public void saveAdapterData() {
        if (mAdapter == null) return;
        try {
            mAdapter.saveDataToStorage(openFileOutput(Video3Fragment.class.getSimpleName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void restoreAdapterData() {
        if (mAdapter == null) return;
        try {
            mAdapter.restoreDataFromStorage(openFileInput(Video3Fragment.class.getSimpleName()));
        } catch (Exception e) {
            mAdapter.syncData("");
        }
    }
}
