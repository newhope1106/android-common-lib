package cn.appleye.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

/**
 * 显示demo效果界面
 * */
public class DemoShowActivity extends AppCompatActivity {
    private SlidingRootNav mSlidingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLeftMenu(toolbar, savedInstanceState);
    }

    /**
     * 初始化左侧栏菜单
     * */
    private void initLeftMenu(Toolbar toolbar, Bundle savedInstanceState){
        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left)
                .inject();
    }

}
