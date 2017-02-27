package org.lvu.adapters.SubAdapters.picture;

import android.content.Context;

import org.lvu.models.Data;

import java.util.List;

/**
 * Created by wuyr on 1/10/17 9:28 PM.
 */

public class Picture6Adapter extends Picture1Adapter {
    public Picture6Adapter(Context context, int layoutId, List<Data> data) {
        super(context, layoutId, data);
    }

    @Override
    protected String getUrl() {
        return "/artlist/jingpintaotu/%s.json";
    }
}