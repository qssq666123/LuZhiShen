package org.lvu.adapters.newAdapters.text;

import android.content.Context;

import org.lvu.models.Data;

import java.util.List;

/**
 * Created by wuyr on 1/10/17 9:29 PM.
 */

public class Text8Adapter extends Text1Adapter {
    public Text8Adapter(Context context, int layoutId, List<Data> data) {
        super(context, layoutId, data);
    }

    @Override
    protected String getUrl() {
        return "artlist/20.html";
    }

    @Override
    protected String getPageUrl() {
        return "artlist/20-%s.html";
    }
}
