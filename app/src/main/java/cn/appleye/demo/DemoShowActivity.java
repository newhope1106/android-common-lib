package cn.appleye.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.appleye.commonlib.utils.ReflectUtil;
import cn.appleye.commonlib.widget.FontImageView;

/**
 * 显示demo效果界面
 * */
public class DemoShowActivity extends AppCompatActivity {
    private SlidingRootNav mSlidingRootNav;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private ArrayList<MenuItem> mFragmentDataList = new ArrayList<>();
    private Map<String , Fragment> mFragmentMap = new HashMap<>();

    private static class MenuItem{
        String menuName;
        String fragmentName;

        public MenuItem(String menuName, String fragmentName){
            this.menuName = menuName;
            this.fragmentName= fragmentName;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLeftMenu(toolbar, savedInstanceState);

        showDocumentFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * 显示文档
     * */
    private void showDocumentFragment(){
        showFragment("cn.appleye.demo.fragment.DocumentFragment");
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

        //初始化事件
        findViewById(R.id.document_view).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                showDocumentFragment();
            }
        });

        initMenuData();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initMenuData(){
        mFragmentDataList.add(new MenuItem("network", "cn.appleye.demo.fragment.NetworkFragment"));
        mFragmentDataList.add(new MenuItem("thread", "cn.appleye.demo.fragment.ThreadFragment"));
        mFragmentDataList.add(new MenuItem("util", "cn.appleye.demo.fragment.UtilFragment"));
    }

    private void showFragment(String fragmentCls){
        Fragment fragment = mFragmentMap.get(fragmentCls);
        if(fragment == null){
            try{
                fragment = (Fragment)ReflectUtil.newInstance(fragmentCls, null, null);
                mFragmentMap.put(fragment.getClass().getName(), fragment);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        showFragment(fragment);
    }

    private void showFragment(Fragment fragment){
        if(fragment != null && !fragment.isVisible()){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }
    }

    private void hideFragment(Fragment fragment){
        if(fragment != null && fragment.isVisible()){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(DemoShowActivity.this).inflate(R.layout.menu_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String menuName = mFragmentDataList.get(position).menuName;
            if(menuName != null && menuName.length() > 0){
                holder.fontImageView.setText(menuName.substring(0, 1));
            }
            if(position % 3 == 0){
                holder.fontImageView.setBackgroundColor(getResources().getColor(R.color.gray));
            } else if(position % 3 == 1){
                holder.fontImageView.setBackgroundColor(getResources().getColor(R.color.yellow));
            } else {
                holder.fontImageView.setBackgroundColor(getResources().getColor(R.color.red));
            }

            holder.menuTitleView.setText(menuName);

            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(onMenuItemClick);
        }

        @Override
        public int getItemCount() {
            return mFragmentDataList.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        FontImageView fontImageView;
        TextView menuTitleView;

        ViewHolder(View itemView) {
            super(itemView);

            fontImageView = (FontImageView)itemView.findViewById(R.id.font_image_view);
            menuTitleView = (TextView)itemView.findViewById(R.id.menu_title_view);
        }
    }

    private View.OnClickListener onMenuItemClick = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            int position = (int)view.getTag();
            MenuItem menuItem = mFragmentDataList.get(position);
            showFragment(menuItem.fragmentName);
        }
    };
}
