package org.lvu.main.fragments.view_pager_content.picture;

import org.lvu.R;
import org.lvu.adapters.BaseListAdapter;
import org.lvu.adapters.SubAdapters.picture.Picture8Adapter;
import org.lvu.models.Data;

import java.util.ArrayList;

/**
 * Created by wuyr on 1/10/17 10:16 PM.
 */

public class Picture8Fragment extends Picture1Fragment {

    @Override
    protected BaseListAdapter getAdapter() {
        return new Picture8Adapter(getActivity(), R.layout.adapter_picture_list_item, new ArrayList<Data>());
    }

    @Override
    public void saveAdapterData() {
        if (mAdapter == null) return;
        try {
            mAdapter.saveDataToStorage(openFileOutput(Picture8Fragment.class.getSimpleName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void restoreAdapterData() {
        if (mAdapter == null) return;
        try {
            mAdapter.restoreDataFromStorage(openFileInput(Picture8Fragment.class.getSimpleName()));
        } catch (Exception e) {
            mAdapter.syncData("");
        }
    }
}
