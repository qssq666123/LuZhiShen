package org.lvu.adapters.newAdapters.video;

import android.content.Context;

import org.lvu.models.Data;

import java.util.List;

/**
 * Created by wuyr on 1/10/17 9:27 PM.
 */

public class Video5Adapter extends Video1Adapter {
    public Video5Adapter(Context context, int layoutId, List<Data> data) {
        super(context, layoutId, data);
    }

    @Override
    protected String getUrl() {
        return "vodlist/7.html";
    }

    @Override
    protected String getPageUrl() {
        return "vodlist/7-%s.html";
    }
}
