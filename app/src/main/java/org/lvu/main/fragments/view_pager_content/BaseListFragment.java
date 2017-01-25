package org.lvu.main.fragments.view_pager_content;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import org.lvu.R;
import org.lvu.adapters.BaseListAdapter;
import org.lvu.customize.CircleProgressBar;
import org.lvu.customize.MySnackBar;
import org.lvu.customize.RefreshLayout;
import org.lvu.main.activities.MainActivity;
import org.lvu.models.Data;
import org.lvu.utils.ImmerseUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by wuyr on 4/6/16 2:22 PM.
 */
public abstract class BaseListFragment extends Fragment{

    protected View mRootView;
    protected RecyclerView mRecyclerView;
    protected RefreshLayout mRefreshLayout;
    protected CircleProgressBar mJumpBar;
    protected boolean isJumping;
    protected BaseListAdapter mAdapter;
    protected MainActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        init();
        return mRootView;
    }

    protected void init() {
        initRefreshLayout();
        initAdapter();
        initRecyclerView();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            changeToLandscape();
        else changeToPortrait();
    }

    private void initAdapter() {
        mAdapter = getAdapter();
        mAdapter.setOnSyncDataFinishListener(new BaseListAdapter.OnFinishListener() {
            @Override
            public void onFinish() {
                handleOnFinish();
            }

            @Override
            public void onFailure(String reason) {
                handleOnFailure(reason);
            }
        });
        mAdapter.setOnLoadMoreFinishListener(mAdapter.getOnSyncDataFinishListener());
        mAdapter.setOnRefreshDataFinishListener(mAdapter.getOnSyncDataFinishListener());
        mAdapter.setOnJumpPageFinishListener(mAdapter.getOnSyncDataFinishListener());
        mAdapter.setOnItemClickListener(getOnItemClickListener());
        mAdapter.setOnItemLongClickListener(getOnItemLongClickListener());
        restoreAdapterData();
    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        saveAdapterData();
        if (mAdapter != null)
            mAdapter.clearData();
        if (isVisibleToUser)
            restoreAdapterData();
    }*/

    public void loadData(){
        restoreAdapterData();
    }

    private void handleOnFinish() {
        if (getActivity() == null)
            return;
        refreshPagesInfo();
        if (mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
        if (isJumping)
            hideJumpBar();
        mRecyclerView.smoothScrollToPosition(0);
    }

    /**
     * while(textView.getHeight() > textView.getParent().getHeight())
     * textView.setTextSize(textView.getTextSize() -2);
     */

    public void refreshPagesInfo() {
        /*if (mActivity == null || mAdapter == null ||
                mActivity.getShowingFragment() == null ||
                mActivity.getShowingFragment().getShowingFragment() == null)
            return;
        int tp = mAdapter.getTotalPages(), cp = mAdapter.getCurrentPage();
        if (tp != -1 && cp != -1) {
            mActivity.setTotalPages(tp);
            mActivity.setCurrentPage(cp);
        } else mActivity.hidePagesView();*/
    }

    public void refreshViewsColor(int startColor, int endColor) {
        try {
            List<BaseListAdapter.ViewHolder> holders = getVisibilityHolders();
            for (BaseListAdapter.ViewHolder tmp : holders) {
                if (tmp != null && mActivity != null) {
                    mActivity.startAnimation(tmp.root, startColor, endColor);
                    tmp.isBgColorChanged = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetViewsColor(int startColor, int endColor) {
        List<BaseListAdapter.ViewHolder> holders = getColorChangedHolders();
        for (BaseListAdapter.ViewHolder tmp : holders) {
            if (tmp != null && tmp.isBgColorChanged && mActivity != null) {
                mActivity.startAnimation(tmp.root, startColor, endColor);
                tmp.isBgColorChanged = false;
            }
        }
    }

    public List<BaseListAdapter.ViewHolder> getVisibilityHolders() {
        List<BaseListAdapter.ViewHolder> result = new ArrayList<>();
        if (mRecyclerView != null) {
            if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int firstPos = layoutManager.findFirstVisibleItemPosition(),
                        lastPos = layoutManager.findLastVisibleItemPosition();
                for (int i = firstPos; i < lastPos; i++)
                    result.add(mAdapter.getHolderByPosition(mRecyclerView, i));
            } else if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
                int[] pos = layoutManager.findFirstVisibleItemPositions(null);
                int firstPos = Math.min(pos[0], pos[1]);
                int[] pos2 = layoutManager.findLastVisibleItemPositions(null);
                int lastPos = Math.max(pos2[0], pos2[1]);
                for (int i = firstPos; i < lastPos; i++)
                    result.add(mAdapter.getHolderByPosition(mRecyclerView, i));
            }
        }
        return result;
    }

    private List<BaseListAdapter.ViewHolder> getColorChangedHolders() {
        List<BaseListAdapter.ViewHolder> result = new ArrayList<>(),
                tmp = getVisibilityHolders();
        for (BaseListAdapter.ViewHolder holder : tmp) {
            if (holder != null && holder.isBgColorChanged)
                result.add(holder);
        }
        return result;
    }

    private boolean flag = true;

    private void handleOnFailure(String reason) {
        if (flag && reason.equals("无可用网络。\t(向右滑动清除)")) {
            if (isJumping) {
                try {
                    mJumpBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isJumping = false;
                mRefreshLayout.setEnabled(true);
            }
        } else if (isJumping)
            hideJumpBar();
        if (mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
        flag = false;
        if (getActivity() != null && ((MainActivity) getActivity()).getRootView() != null)
            MySnackBar.show(((MainActivity) getActivity()).getRootView(), reason, Snackbar.LENGTH_INDEFINITE);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(mAdapter);
        animationAdapter.setFirstOnly(false);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setLayoutManager(getLayoutManager());
       /* mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            //用来标记是否正在向最后一个滑动，既是否向下滑动
            boolean isSlidingToLast;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 当不滚动时
                    //获取最后一个完全显示的ItemPosition
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                    int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
                    int lastVisiblePos = getMaxElem(lastVisiblePositions);
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部
                    if (!mRefreshLayout.isRefreshing() && isSlidingToLast && !isLoadBarHiding && !isJumping &&
                            lastVisiblePos == totalItemCount) {
                        showJumpBar();
                        mAdapter.loadNext();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                isSlidingToLast = isScrollDown(dy);
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager)
                    if (!mRefreshLayout.isRefreshing() && isSlidingToLast && !isLoadBarHiding && !isJumping &&
                            (((LinearLayoutManager) recyclerView.getLayoutManager())
                                    .findLastVisibleItemPosition() == mAdapter.getDataSize() - 1 ||
                                    ((LinearLayoutManager) recyclerView.getLayoutManager())
                                            .findLastVisibleItemPosition() == mAdapter.getDataSize() - 2)) {
                        showJumpBar();
                        mAdapter.loadNext();
                    }
            }
        });
    }

    private int getMaxElem(int[] arr) {
        int maxVal = Integer.MIN_VALUE;
        for (int anArr : arr)
            if (anArr > maxVal)
                maxVal = anArr;
        return maxVal;
    }

    protected boolean isScrollDown(int y) {
        return y > 0;
    }*/
    }

    private void initRefreshLayout() {
        mJumpBar = (CircleProgressBar) mRootView.findViewById(R.id.progressbar);
        if (ImmerseUtil.isHasNavigationBar(getActivity())) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mJumpBar.getLayoutParams();
            lp.bottomMargin += ImmerseUtil.getNavigationBarHeight(getActivity());
            mJumpBar.setLayoutParams(lp);
        }
        mRefreshLayout = (RefreshLayout) mRootView.findViewById(R.id.refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.menu_text_color);
        mJumpBar.setColorSchemeResources(R.color.menu_text_color);
        List<Integer> data = new ArrayList<>();
        int[] array = R.styleable.AppCompatTheme;
        for (int tmp : array)
            data.add(tmp);
        TypedArray a = getActivity().obtainStyledAttributes(R.styleable.AppCompatTheme);
        int color = a.getColor(data.indexOf(R.attr.colorPrimary), getActivity()
                .getResources().getColor(R.color.bluePrimary));
        mRefreshLayout.setProgressBackgroundColorSchemeColor(color);
        a.recycle();
        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.loadPrevious();
            }

            @Override
            public void onLoad() {
                mAdapter.loadNext();
            }
        });
        isJumping = true;
        mRefreshLayout.setEnabled(false);
    }

    private Animation showAnimation;

    private void showJumpBar() {
        isJumping = true;
        if (showAnimation == null)
            initShowAnimation();
        try {
            mJumpBar.startAnimation(showAnimation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initShowAnimation() {
        showAnimation = new TranslateAnimation(0, 0, 150, 0);
        showAnimation.setDuration(250);
        showAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mJumpBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private boolean isAnimationNormalEnd;

    private class HideLoadMoreBarThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!isAnimationNormalEnd) {
                try {
                    mJumpBar.post(new Runnable() {
                        @Override
                        public void run() {
                            mJumpBar.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    ExecutorService threadPool = Executors.newSingleThreadExecutor();
    private Animation hideAnimation;

    private void hideJumpBar() {
        if (isJumping || mJumpBar.getVisibility() == View.VISIBLE) {
            isJumping = false;
            mRefreshLayout.setEnabled(true);
            if (hideAnimation == null)
                initHideAnimation();
            try {
                isAnimationNormalEnd = false;
                mJumpBar.startAnimation(hideAnimation);
                threadPool.execute(new HideLoadMoreBarThread());
            } catch (Exception e) {
                try {
                    mJumpBar.setVisibility(View.GONE);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    private void initHideAnimation() {
        hideAnimation = new ScaleAnimation(
                1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        hideAnimation.setDuration(250);
        hideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mJumpBar.setVisibility(View.GONE);
                isAnimationNormalEnd = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            changeToLandscape();
        else
            changeToPortrait();
    }

    public void jumpToPage(int page) {
        mAdapter.jumpToPage(page);
        mAdapter.setCurrentPage(page);
        showJumpBar();
        mRefreshLayout.setEnabled(false);
    }

    public void changeToLandscape() {
        /*if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager manager = ((StaggeredGridLayoutManager) mRecyclerView.getLayoutManager());
            //int spanCount = manager.getSpanCount()
            int[] firstVisiblePositions = manager.findFirstVisibleItemPositions(new int[manager.getSpanCount()]);
            int currentPos = getMaxElem(firstVisiblePositions);

            StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(manager2);
            manager2.scrollToPosition(currentPos);
        }*/
        if (mAdapter != null)
            mAdapter.changeToLandscape();
    }

    public void changeToPortrait() {
        /*if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager manager = ((StaggeredGridLayoutManager) mRecyclerView.getLayoutManager());
            int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
            int currentPos = getMaxElem(lastVisiblePositions);

            StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(manager2);
            manager2.scrollToPosition(currentPos);
        }*/
        if (mAdapter != null)
            mAdapter.changeToPortrait();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity)
            mActivity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        if (mAdapter != null) {
            mAdapter.setOwnerIsDestroyed(true);
            saveAdapterData();
        }
        super.onDetach();
        if (mActivity != null)
            mActivity = null;
    }

    protected void showDialog(final Data data, String itemName) {
        new AlertDialog.Builder(getActivity()).setItems(new String[]{itemName}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                longClickLogic(data);
            }
        }).show();
    }

    protected BufferedReader openFileInput(String name) throws Exception {
        return new BufferedReader(new FileReader(makeFilename(getActivity().getFilesDir(), name)));
    }

    protected BufferedWriter openFileOutput(String name) throws Exception {
        return new BufferedWriter(new FileWriter(makeFilename(getActivity().getFilesDir(), name)));
    }

    private File makeFilename(File base, String name) {
        if (name.indexOf(File.separatorChar) < 0) {
            return new File(base, name);
        }
        throw new IllegalArgumentException(
                "File " + name + " contains a path separator");
    }

    protected abstract void longClickLogic(Data data);

    public abstract void saveAdapterData();

    protected abstract void restoreAdapterData();

    protected abstract BaseListAdapter getAdapter();

    protected abstract BaseListAdapter.OnItemClickListener getOnItemClickListener();

    protected abstract BaseListAdapter.OnItemLongClickListener getOnItemLongClickListener();

    protected abstract RecyclerView.LayoutManager getLayoutManager();
}
