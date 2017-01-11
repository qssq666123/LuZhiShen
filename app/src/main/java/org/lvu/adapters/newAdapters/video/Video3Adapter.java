package org.lvu.adapters.newAdapters.video;

import android.content.Context;

import org.lvu.models.Data;

import java.util.List;

/**
 * Created by wuyr on 1/10/17 9:26 PM.
 */

public class Video3Adapter extends Video1Adapter {
    public Video3Adapter(Context context, int layoutId, List<Data> data) {
        super(context, layoutId, data);
    }

    @Override
    protected String getUrl() {
        return "vodlist/4.html";
    }

    @Override
    protected String getPageUrl() {
        return "vodlist/4-%s.html";
    }
}
