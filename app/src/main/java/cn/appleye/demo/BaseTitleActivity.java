package cn.appleye.demo;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * @author Liu Liaopu
 * @date 2018/8/9
 * @description 带title的基Activity
 */
public class BaseTitleActivity extends AppCompatActivity {

    @Override
    public void setTitle(CharSequence title){
        super.setTitle(title);

        TextView titleView = findViewById(R.id.title_view);
        if(titleView != null){
            titleView.setText(title);
        }
    }

    @Override
    public void setContentView(View view){
        super.setContentView(view);

        initViews();
    }

    @Override
    public void setContentView(int resId){
        super.setContentView(resId);

        initViews();
    }

    /**
     * 初始化基础Activity的控件
     * */
    private void initViews(){
        View backView = findViewById(R.id.back_view);
        if(backView != null){
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }
}
