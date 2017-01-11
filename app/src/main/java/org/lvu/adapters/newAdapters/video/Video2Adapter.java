package org.lvu.adapters.newAdapters.video;

import android.content.Context;

import org.lvu.models.Data;

import java.util.List;

/**
 * Created by wuyr on 1/10/17 9:26 PM.
 */

public class Video2Adapter extends Video1Adapter {
    public Video2Adapter(Context context, int layoutId, List<Data> data) {
        super(context, layoutId, data);
    }

    @Override
    protected String getUrl() {
        return "vodlist/6.html";
    }

    @Override
    protected String getPageUrl() {
        return "vodlist/6-%s.html";
    }
}
