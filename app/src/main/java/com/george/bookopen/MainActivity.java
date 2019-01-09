package com.george.bookopen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.george.bookopen.book.BookOpenView;
import com.george.bookopen.book.BookOpenViewValue;

public class MainActivity extends AppCompatActivity {
    private BookOpenView mBookOpenView;
    private View itemView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemView = findViewById(R.id.itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewGroup window = (ViewGroup) getWindow().getDecorView();
                int[] location = new int[2];
                itemView.getLocationInWindow(location);

                BookOpenViewValue startValue = new BookOpenViewValue(location[0], location[1],
                        location[0] + (itemView.getRight() - itemView.getLeft()),
                        location[1] + (itemView.getBottom() - itemView.getTop()));

                BookOpenViewValue endValue = new BookOpenViewValue(window.getLeft(),
                        window.getTop(), window.getRight(), window.getBottom());

                mBookOpenView = new BookOpenView(MainActivity.this);
                window.addView(mBookOpenView);
                mBookOpenView.setEndListener(new BookOpenView.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, Main2Activity.class));
                        overridePendingTransition(R.anim.read_fade_in, R.anim.read_fade_out);
                    }

                    @Override
                    public void onRemove() {
                        window.removeView(mBookOpenView);
                    }
                });
                mBookOpenView.startAnim(startValue, endValue);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBookOpenView != null) {
            mBookOpenView.closeAnim();
        }
    }
}
