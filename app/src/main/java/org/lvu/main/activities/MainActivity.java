package org.lvu.main.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.lvu.R;
import org.lvu.adapter.MenuListAdapter;
import org.lvu.customize.MenuList;
import org.lvu.customize.MySnackBar;
import org.lvu.main.fragments.AsiaPictureFragment;
import org.lvu.main.fragments.BaseListFragment;
import org.lvu.main.fragments.ChinaVideoFragment;
import org.lvu.main.fragments.EuropePictureFragment;
import org.lvu.main.fragments.EuropeVideoFragment;
import org.lvu.main.fragments.EvilComicsFragment;
import org.lvu.main.fragments.ExcitedNovelFragment;
import org.lvu.main.fragments.FamilyMessNovelFragment;
import org.lvu.main.fragments.FamilyPhotoFragment;
import org.lvu.main.fragments.FunnyJokeFragment;
import org.lvu.main.fragments.GifPictureFragment;
import org.lvu.main.fragments.JapanVideoFragment;
import org.lvu.main.fragments.LewdWifeNovelFragment;
import org.lvu.main.fragments.SchoolNovelFragment;
import org.lvu.utils.ImmerseUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

import io.vov.vitamio.Vitamio;

/**
 * Created by wuyr on 6/1/16 3:33 PM.
 */

@SuppressWarnings("ConstantConditions")
public class MainActivity extends BaseActivity implements MenuListAdapter.OnItemClickListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private View mTopView;
    private MenuList mMenuList;
    private BaseListFragment mShowingFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(getApplicationContext());
        setContentView(R.layout.activity_main_view);
        initViews();
        initImmerse();
        initFragment();
    }

    private void initFragment() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NavigationActivity.POSITION, -1),
                stringId = intent.getIntExtra(NavigationActivity.STRING_ID, -1);
        if (position != -1 && stringId != -1) {
            mMenuList.setSelectedPos(position);
            onClick(stringId);
        }
    }

    private void showFragment(BaseListFragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && this.isDestroyed())
            return;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.wait_replace, fragment).commitAllowingStateLoss();
        mShowingFragment = fragment;
    }

    public void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
    }

    public void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    public void setDrawerLockMode(boolean lock) {
        mDrawerLayout.setDrawerLockMode(lock ?
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED);
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
            mTopView = findViewById(R.id.status_bar_view);
            AppBarLayout.LayoutParams topLP = new AppBarLayout.LayoutParams(
                    AppBarLayout.LayoutParams.MATCH_PARENT, ImmerseUtil.getStatusBarHeight(this));
            mTopView.setLayoutParams(topLP);
            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE)
                changeToLandscape();
            else changeToPortrait();
        }
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.menu_navigation);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mMenuList = (MenuList) findViewById(R.id.menu_list);
        mMenuList.setOnItemClickListener(this);
        mMenuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.change_skin:
                        changeSkin();
                        break;
                    case R.id.exit:
                        exit();
                        break;
                    default:
                }
            }
        });
    }

    @Override
    protected void changeSkin() {
        super.changeSkin();
        closeDrawer();
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

    private void changeToLandscape() {
        mTopView.setVisibility(View.GONE);
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar
        uiFlags |= 0x00001000;
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        mMenuList.changeToLandscape();
    }

    private void changeToPortrait() {
        mTopView.setVisibility(View.VISIBLE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mMenuList.changeToPortrait();
    }

    private OnBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    public void onClick(int stringId) {
        BaseListFragment fragment;
        switch (stringId) {
            case R.string.menu_china_video:
                fragment = new ChinaVideoFragment();
                break;
            case R.string.menu_europe_video:
                fragment = new EuropeVideoFragment();
                break;
            case R.string.menu_japan_video:
                fragment = new JapanVideoFragment();
                break;
            case R.string.menu_family_pic:
                fragment = new FamilyPhotoFragment();
                break;
            case R.string.menu_asia_pic:
                fragment = new AsiaPictureFragment();
                break;
            case R.string.menu_europe_pic:
                fragment = new EuropePictureFragment();
                break;
            case R.string.menu_evil_pics:
                fragment = new EvilComicsFragment();
                break;
            case R.string.menu_gif:
                fragment = new GifPictureFragment();
                break;
            case R.string.menu_excited_novel:
                fragment = new ExcitedNovelFragment();
                break;
            case R.string.menu_family_mess_novel:
                fragment = new FamilyMessNovelFragment();
                break;
            case R.string.menu_school_novel:
                fragment = new SchoolNovelFragment();
                break;
            case R.string.menu_lewd_wife_novel:
                fragment = new LewdWifeNovelFragment();
                break;
            case R.string.menu_funny_joke:
                fragment = new FunnyJokeFragment();
                break;
            default:
                fragment = null;
                break;
        }
        if (fragment != null)
            showFragment(fragment);
        mToolbar.setTitle(stringId);
        closeDrawer();
    }

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

    //上次按下返回键的时间
    private long mLastTime;

    @Override
    public void onBackPressed() {
        if (!mOnBackPressedListener.onBackPressed()) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
                mDrawerLayout.closeDrawer(GravityCompat.START);
            else {
                if ((System.currentTimeMillis() - mLastTime) < 2000)
                    finish();
                mLastTime = System.currentTimeMillis();
                MySnackBar.show(findViewById(R.id.coordinator),
                        getString(R.string.press_back_exit), Snackbar.LENGTH_SHORT);
            }
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
                            }).setNegativeButton(R.string.no, null).create();
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
        }*/
        mShowingFragment.saveAdapterData();
        mShowingFragment = null;
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
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }
}
