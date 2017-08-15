package com.starpy.sdk.plat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.starpy.sdk.R;
import com.starpy.sdk.plat.data.PlatContract;
import com.starpy.sdk.plat.data.bean.response.PlatMenuAllModel;
import com.starpy.sdk.plat.data.presenter.PlatPresenterImpl;
import com.starpy.sdk.plat.fragment.InformationFragment;

public class PlatMainActivity extends AppCompatActivity implements PlatContract.IPlatView {

    private static final String TAG = PlatMainActivity.class.getCanonicalName();
    private DrawerLayout mDrawerLayout;

    private GridView gridView;

    private View leftSwitchvViewiew;
    private View rightSwitchvViewiew;

    private FrameLayout menuDrawerFrameLayout;
    private FrameLayout contentFrameLayout;

    private FragmentManager fragmentManager;

    PlatPresenterImpl platPresenter;

    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            Log.i(TAG,"onDrawerSlide");
            leftSwitchvViewiew.setVisibility(View.GONE);
            isDrawerOpen = false;
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            Log.i(TAG,"onDrawerOpened");
            leftSwitchvViewiew.setVisibility(View.GONE);
            rightSwitchvViewiew.setBackgroundResource(R.drawable.plat_menu_open);
            isDrawerOpen = true;
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            Log.i(TAG,"onDrawerClosed");
            leftSwitchvViewiew.setVisibility(View.VISIBLE);
            rightSwitchvViewiew.setBackgroundResource(R.drawable.plat_menu_closeleft);
            isDrawerOpen = false;
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            Log.i(TAG,"onDrawerStateChanged");
            if (!isDrawerOpen){
                leftSwitchvViewiew.setVisibility(View.VISIBLE);
                rightSwitchvViewiew.setBackgroundResource(R.drawable.plat_menu_closeleft);
            }
        }
    };
    private PlatMenuGridViewAdapter platMenuGridViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plat_activity_content_page);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        menuDrawerFrameLayout = (FrameLayout) findViewById(R.id.plat_menu_drawer);//侧滑菜单

        leftSwitchvViewiew = findViewById(R.id.plat_menu_switch_view);
        rightSwitchvViewiew = findViewById(R.id.plat_menu__right_switch_view);

        contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);//fragment页面

        initData();

        mDrawerLayout.addDrawerListener(drawerListener);

        gridView = (GridView) findViewById(R.id.plat_menu_drawer_gridview);

        platMenuGridViewAdapter = new PlatMenuGridViewAdapter(this);

        gridView.setAdapter(platMenuGridViewAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG,"onItemClick");
                if (currentClickView != null) {
                    currentClickView.setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundResource(R.drawable.plat_menu_item_bg);
                currentClickView = view;

                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(contentFrameLayout.getId(),new InformationFragment());
                fragmentTransaction.commit();

            }
        });

        leftSwitchvViewiew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG," mDrawerLayout onClick");
                mDrawerLayout.openDrawer(menuDrawerFrameLayout);
            }
        });

        rightSwitchvViewiew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(menuDrawerFrameLayout);
            }
        });


    }

    private void initData() {

        platPresenter = new PlatPresenterImpl();
        platPresenter.setBaseView(this);
        platPresenter.reqeustPlatMenuData(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDrawerLayout != null && drawerListener != null){
            mDrawerLayout.removeDrawerListener(drawerListener);
        }
    }

    /**
     * 当前被点击的item
     */
    private View currentClickView;

    /**
     * 侧滑菜单是否处于打开状态
     */
    private boolean isDrawerOpen;

    @Override
    public void reqeustPlatMenuDataSuccess(PlatContract.RequestType requestType, PlatMenuAllModel platMenuAllModel) {
        platMenuGridViewAdapter.setPlatMenuBeans(platMenuAllModel.getData());
        platMenuGridViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void reqeustDataFail(PlatContract.RequestType requestType) {

    }
}
