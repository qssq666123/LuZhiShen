package org.lvu.main.activities;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.lvu.R;
import org.lvu.adapters.NavigationViewAdapter;
import org.lvu.customize.MyCollapsingToolbarLayout;
import org.lvu.customize.MySnackBar;
import org.lvu.customize.NavigationView;
import org.lvu.customize.OnAppBarStateChangedListener;
import org.lvu.main.fragments.BaseFragment;
import org.lvu.main.fragments.FavoritesFragment;
import org.lvu.main.fragments.IndexFragment;
import org.lvu.main.fragments.PictureAreaFragment;
import org.lvu.main.fragments.TextAreaFragment;
import org.lvu.main.fragments.VideoAreaFragment;
import org.lvu.utils.ImmerseUtil;

/**
 * Created by wuyr on 12/27/16 8:07 PM.
 */

public class MainActivity extends BaseActivity {

    private static final int ANIMATION_DURATION = 800;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private AppBarLayout mAppBarLayout;
    private MyCollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mBackground;
    private Toolbar mToolbar;
    private View mPageInfo;
    private TextView mTotalPages;
    private EditText mCurrentPage;
    private FloatingActionButton mFloatingButton;
    private static int mBackgroundColor = -1;
    private int totalPages;
    private boolean isStateChanged, isBackgroundChanged;
    private static boolean isAppBarExpanded = true, isAppBarCollapsed, isScrimsShown;
    private BaseFragment mShowingFragment;
    private OnAppBarExpandedListener mOnAppBarExpandedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main_view);
        initView();
        initImmerse();
    }

    private void initView() {
        findViews();
        setSupportActionBar(mToolbar);
        setListeners();
    }

    private void initPalette(@DrawableRes int resId) {
        switch (resId) {
            case R.drawable.bg_video:
                mBackgroundColor = getResources().getColor(R.color.bg_video);
                break;
            case R.drawable.bg_pic:
                mBackgroundColor = getResources().getColor(R.color.bg_pic);
                break;
            case R.drawable.bg_novel:
                mBackgroundColor = getResources().getColor(R.color.bg_novel);
                break;
            default:
        }
        mBackground.setImageResource(resId);
        //getVibrantSwatch == null
        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        ((ImageView) findViewById(R.id.collapsing_background)).setImageBitmap(bitmap);
        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch != null) {
                    mBackgroundColor = swatch.getRgb();
                    /*if (!isBackgroundChanged && isAppBarExpanded)
                        refreshViewsColor();*
                }
            }
        });*/

    }

    private void findViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mCollapsingToolbarLayout = (MyCollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mBackground = (ImageView) findViewById(R.id.collapsing_background);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        /*mPageInfo = findViewById(R.id.page_info);
        mTotalPages = (TextView) mToolbar.findViewById(R.id.total_page);
        mCurrentPage = (EditText) mToolbar.findViewById(R.id.current_page);*/
        mFloatingButton = (FloatingActionButton) findViewById(R.id.floating_button);
    }

    private void setListeners() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        /*mCurrentPage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    int cp;
                    try {
                        cp = Integer.parseInt(mCurrentPage.getText().toString());
                    } catch (NumberFormatException e) {
                        //setCurrentPage(1);
                        cp = 1;
                    }
                    if (cp > totalPages)
                        cp = totalPages;
                    if (cp < 1)
                        cp = 1;
                    if (mShowingFragment != null)
                        mShowingFragment.jumpToPage(cp);
                    return true;
                }
                return false;
            }
        });*/
        mNavigationView.setOnItemClickListener(new NavigationViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int stringId) {
                Fragment fragment;
                switch (stringId) {
                    case R.string.area_index:
                        fragment = new IndexFragment();
                        break;
                    case R.string.area_video:
                        fragment = new VideoAreaFragment();
                        initPalette(R.drawable.bg_video);
                        break;
                    case R.string.area_picture:
                        fragment = new PictureAreaFragment();
                        initPalette(R.drawable.bg_pic);
                        break;
                    case R.string.area_text:
                        fragment = new TextAreaFragment();
                        initPalette(R.drawable.bg_novel);
                        break;
                    case R.string.favorites:
                        fragment = new FavoritesFragment();
                        break;
                    case R.string.download_manager:
                        fragment = null;
                        startActivity(new Intent(MainActivity.this, DownloadManagerActivity.class));
                        break;
                    case R.string.change_skin:
                        fragment = null;
                        changeSkin();
                        break;
                    case R.string.settings:
                        fragment = null;
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        break;
                    case R.string.exit:
                        fragment = mShowingFragment;
                        exit();
                        break;
                    default:
                        fragment = null;
                        break;
                }
                mAppBarLayout.setExpanded(false);
                closeDrawer();
                if (fragment != null && showFragment(fragment))
                    mCollapsingToolbarLayout.setTitle(getString(stringId));
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new OnAppBarStateChangedListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    isAppBarExpanded = true;
                    isAppBarCollapsed = false;
                    if (isStateChanged)
                        refreshViewsColor();
                } else if (state == State.COLLAPSED) {
                    isAppBarExpanded = false;
                    isAppBarCollapsed = true;
                    if (isStateChanged)
                        resetViewsColor();
                } else {
                    isAppBarExpanded = false;
                    isAppBarCollapsed = false;
                }
            }
        });
        mCollapsingToolbarLayout.setOnScrimsCHangedListener(new MyCollapsingToolbarLayout.OnScrimsChangedListener() {
            @Override
            public void onChanged(boolean isShown) {
                isStateChanged = flag != isShown;
                isScrimsShown = isShown;
                if (isStateChanged) {
                    if (isShown)
                        resetViewsColor();
                    else refreshViewsColor();
                }
                flag = isShown;
            }
        });
    }

    private void switchInputMethod() {
        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean showFragment(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed() || fragment == mShowingFragment)
            return false;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.wait_replace, fragment).commitAllowingStateLoss();
        if (fragment instanceof BaseFragment)
            mShowingFragment = (BaseFragment) fragment;
        /*mCurrentPage.setText("");
        mCurrentPage.clearFocus();
        mTotalPages.setText("");
        hidePagesView();*/
        return true;
    }

    private boolean flag = true;

    private synchronized void refreshViewsColor() {
        if (mBackgroundColor != -1) {
            int themeColor = getThemeColor(1);
            startAnimation(mFloatingButton, themeColor, mBackgroundColor);
            if (mOnAppBarExpandedListener != null) {
                mOnAppBarExpandedListener.onExpanded(themeColor, mBackgroundColor);
            }
            notifyChanged();
        }
    }

    private void resetViewsColor() {
        if (mBackgroundColor != -1) {
            int themeColor = getThemeColor(1);
            startAnimation(mFloatingButton, mBackgroundColor, themeColor);
            if (mOnAppBarExpandedListener != null) {
                mOnAppBarExpandedListener.onCollapsed(mBackgroundColor, themeColor);
            }
            notifyChanged();
        }
    }

    private void notifyChanged() {
        isBackgroundChanged = true;
        isStateChanged = false;
    }

    public static TransitionDrawable getAnimation(int startColor, int endColor) {
        TransitionDrawable result = new TransitionDrawable(new Drawable[]{
                new ColorDrawable(startColor), new ColorDrawable(endColor)});
        result.startTransition(ANIMATION_DURATION);
        return result;
    }

    private void startAnimation(final FloatingActionButton fab, int startColor, final int endColor) {
        final ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        final Drawable background = fab.getContentBackground();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animator) {
                background.setColorFilter((int) animator.getAnimatedValue(), PorterDuff.Mode.SRC_ATOP);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fab.setBackgroundTintList(ColorStateList.valueOf(endColor));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.start();
    }

    public void startAnimation(final CardView cardView, int startColor, final int endColor) {
        final ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animator) {
                cardView.setCardBackgroundColor(ColorStateList.valueOf((int) animator.getAnimatedValue()));
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cardView.setCardBackgroundColor(ColorStateList.valueOf(endColor));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.start();
    }

    private Bitmap drawable2bitmap(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

    private void initImmerse() {
        if (ImmerseUtil.isAboveKITKAT()) {
            /*getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //init topView
            /*mTopView = findViewById(R.id.status_bar_view);
            AppBarLayout.LayoutParams topLP = new AppBarLayout.LayoutParams(
                    AppBarLayout.LayoutParams.MATCH_PARENT, ImmerseUtil.getStatusBarHeight(this));
            mTopView.setLayoutParams(topLP);*/
            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE)
                changeToLandscape();
            else changeToPortrait();
        }
    }

    private void changeToLandscape() {
        CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) mToolbar.getLayoutParams();
        lp.topMargin = 0;
        mToolbar.setLayoutParams(lp);
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar
        uiFlags |= 0x00001000;
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        mNavigationView.changeToLandscape();
    }

    private void changeToPortrait() {
        CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) mToolbar.getLayoutParams();
        lp.topMargin = ImmerseUtil.getStatusBarHeight(this);
        mToolbar.setLayoutParams(lp);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mNavigationView.changeToPortrait();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (ImmerseUtil.isAboveKITKAT()) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
                changeToLandscape();
            else
                changeToPortrait();
        }
    }

    public static boolean isAppBarExpanded() {
        return isAppBarExpanded;
    }

    public static boolean isAppBarCollapsed() {
        return isAppBarCollapsed;
    }

    //上次按下返回键的时间
    private long mLastTime;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else {
            if ((System.currentTimeMillis() - mLastTime) < 2000)
                finish();
            mLastTime = System.currentTimeMillis();
            MySnackBar.show(findViewById(R.id.root_view),
                    getString(R.string.press_back_exit), Snackbar.LENGTH_SHORT);
        }
    }

    private AlertDialog exitDialog;

    private void exit() {
        if (exitDialog == null) {
            exitDialog = new AlertDialog.Builder(this).setMessage(R.string.confirm_exit)
                    .setPositiveButton(R.string.yes,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).setNegativeButton(R.string.no, null).setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (getResources().getConfiguration().orientation
                                    == Configuration.ORIENTATION_LANDSCAPE)
                                fullScreen();
                        }
                    }).create();
        }
        if (exitDialog.isShowing())
            return;
        exitDialog.show();
        closeDrawer();
    }

    private void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /*
    private void clearCache() {
        ImageLoader.getInstance().clearMemoryCache();
        //ImageLoader.getInstance().clearDiskCache();
        //获取缓存路径
        File cacheDir = ImageLoader.getInstance().getDiskCache().getDirectory();
        //获取该目录下大于30k的文件
        File[] temps = cacheDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.length() >= 30720;
            }
        });
        //删除大于30k的文件
        for (File tmp : temps)
            tmp.delete();
        //获取该目录下所有文件
        File[] files = cacheDir.listFiles();
        //记录该目录的总大小
        long length = 0;
        //计算该目录总大小
        for (File f : files)
            length += f.length();
        //如果该目录总大小大于或等于5m，则删除一部分文件
        if (length >= 5242880) {
            //文件夹目标大小(2.5m)
            long targetLength = 2621440;
            //冒泡算法 最小的排到最前面（按修改日期排序）
            List<File> list = Arrays.asList(files);
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = 1; j < list.size() - i; j++) {
                    File tmp;
                    if (list.get(j - 1).lastModified() > list.get(j).lastModified()) {
                        tmp = list.get(j - 1);
                        list.set((j - 1), list.get(j));
                        list.set(j, tmp);
                    }
                }
            }
            long tmp;
            for (File file : list) {
                tmp = file.length();
                if (file.delete())
                    length -= tmp;
                //每删除一个文件判断是否以达到目标大小
                if (length <= targetLength)
                    break;
            }
        }
    }
    */
    @Override
    public void finish() {
        super.finish();
        /*if (Environment.isExternalStorageEmulated() && getExternalCacheDir() != null) {
            try {
                java.lang.Process process = Runtime.getRuntime().exec("rm -r " + getExternalCacheDir().toString());
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }**/
        //clearCache();
        if (mShowingFragment != null)
            mShowingFragment.saveAdapterData();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public CoordinatorLayout getRootView() {
        return (CoordinatorLayout) findViewById(R.id.root_view);
    }

    public static int getBackgroundColor() {
        return mBackgroundColor;
    }

    public static boolean isScrimsShown() {
        return isScrimsShown;
    }

    /**
     * Slide Up Animation
     * mContent.setY(mContent.getY() + 120);
     * mContent.setAlpha(0);
     * mContent.animate().translationY(0).setDuration(500).alpha(1);
     */
/*
    public void setTotalPages(int totalPages) {
        if (totalPages > 0) {
            this.totalPages = totalPages;
            mTotalPages.setText(String.format("页共%s页", totalPages + ""));
            showPagesView();
            LogUtil.print(mTotalPages.getText().toString());
        }
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage > 0) {
            if (currentPage > totalPages)
                currentPage = totalPages;
            if (currentPage < 1)
                currentPage = 1;
            mCurrentPage.setText(String.valueOf(currentPage));
            showPagesView();
        }
    }

    private void showPagesView(){
        mPageInfo.setVisibility(View.VISIBLE);
    }

    public void hidePagesView() {
        mPageInfo.setVisibility(View.GONE);
    }*/

    public BaseFragment getShowingFragment() {
        return mShowingFragment;
    }

    public void setOnAppBarExpandedListener(OnAppBarExpandedListener listener) {
        mOnAppBarExpandedListener = listener;
    }

    public interface OnAppBarExpandedListener {
        void onExpanded(int startColor, int endColor);

        void onCollapsed(int startColor, int endColor);
    }
}
