package com.george.bookopen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.george.bookopen.book.BookOpenViewValue;
import com.george.bookopen.book2.BookOpenView2;

public class Main3Activity extends AppCompatActivity {
    private BookOpenView2 mBookOpenView;
    private View itemView2;
    private int widthPx;
    private int heightPx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        itemView2 = findViewById(R.id.itemView);
        widthPx = getResources().getDisplayMetrics().widthPixels;
        heightPx = getResources().getDisplayMetrics().heightPixels;

        itemView2 = findViewById(R.id.itemView2);
        itemView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewGroup window = (ViewGroup) getWindow().getDecorView();
                int[] location = new int[2];
                int centerX = widthPx/2;
                int centerY = heightPx/2;

                itemView2.getLocationInWindow(location);
                BookOpenViewValue startValue = new BookOpenViewValue(location[0], location[1],
                        location[0] + (itemView2.getRight() - itemView2.getLeft()),
                        location[1] + (itemView2.getBottom() - itemView2.getTop()));
                int viewW = startValue.getRight()-startValue.getLeft();
                int viewH = startValue.getBottom()-startValue.getTop();

                BookOpenViewValue endValue = new BookOpenViewValue(centerX-viewW/2, centerY-viewH/2,
                        centerX+viewW/2,
                        centerY+viewH/2);
                mBookOpenView = new BookOpenView2(Main3Activity.this);

                window.addView(mBookOpenView);
                mBookOpenView.setEndListener(new BookOpenView2.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        Main3Activity.this.startActivity(new Intent(Main3Activity.this, Main2Activity.class));
                        overridePendingTransition(R.anim.read_fade_in, R.anim.read_fade_out);
                    }
                    @Override
                    public void onRemove() {
                        window.removeView(mBookOpenView);
                    }
                });
                mBookOpenView.startAnimTran(Main3Activity.this,startValue, endValue);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mBookOpenView != null) {
            mBookOpenView.closeAnim2();
        }
    }
}
