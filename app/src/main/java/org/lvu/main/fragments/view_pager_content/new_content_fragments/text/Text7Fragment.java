package org.lvu.main.fragments.view_pager_content.new_content_fragments.text;

import org.lvu.R;
import org.lvu.adapters.BaseListAdapter;
import org.lvu.adapters.newAdapters.text.Text7Adapter;
import org.lvu.models.Data;

import java.util.ArrayList;

/**
 * Created by wuyr on 1/10/17 10:15 PM.
 */

public class Text7Fragment extends Text1Fragment {
    @Override
    protected BaseListAdapter getAdapter() {
        return new Text7Adapter(getActivity(), R.layout.adapter_list_item, new ArrayList<Data>());
    }

    @Override
    public void saveAdapterData() {
        if (mAdapter == null) return;
        try {
            mAdapter.saveDataToStorage(openFileOutput(Text7Fragment.class.getSimpleName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void restoreAdapterData() {
        if (mAdapter == null) return;
        try {
            mAdapter.restoreDataFromStorage(openFileInput(Text7Fragment.class.getSimpleName()));
        } catch (Exception e) {
            mAdapter.syncData("");
        }
    }
}
