package com.george.bookopen.book4;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.george.bookopen.R;
import com.george.bookopen.book.BookOpenViewValue;
import com.george.bookopen.book2.BookOpenView2;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created By George
 * Description:
 */
public class BookOpenView4 extends FrameLayout implements Animator.AnimatorListener {
    private static final String TAG = "BookOpenView2";
    /**
     * 开始位置参数
     */
    private BookOpenViewValue mStartValue;
    private BookOpenViewValue mStartValueTran;
    private BookOpenViewValue mBigValue;

    private AnimatorSet mAnimatorSet;// 动画
    /**
     * 封面页view
     */
    private ImageView page_cover_content;
    private ConstraintLayout page_cover;

    /**
     * 内容页view
     */
    private RelativeLayout page_content;

    /**
     * 动画状态
     */
    private AtomicBoolean isOpened = new AtomicBoolean(false);

    public BookOpenView4(@NonNull Context context) {
        super(context, null, 0);
        initView(context);
    }

    public BookOpenView4(@NonNull Context context, @NonNull AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookOpenView4(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.book_open_close, this);
        setBackgroundColor(Color.TRANSPARENT);
        page_cover_content = findViewById(R.id.page_cover_content);
        page_cover = findViewById(R.id.page_cover);
        page_content = findViewById(R.id.page_content);
    }

    /**
     * 平移过度到目标位置---》书本打开效果
     */
    public synchronized void startAnimTran(final BookOpenViewValue startValue, final BookOpenViewValue translateValue, final BookOpenViewValue bigValue) {
        if (!isOpened.get()) {
            ViewGroup.LayoutParams layoutParams;
            mStartValue = startValue;
            mBigValue = bigValue;
            mStartValueTran = translateValue;
            Log.d(TAG, " \nmStartValue:" + mStartValue.toString() + "\nendValue:" + mStartValueTran.toString());
            layoutParams = page_cover.getLayoutParams();
            // 把startValue的width和height给page_cover
            layoutParams.width = mStartValue.getRight() - mStartValue.getLeft();
            layoutParams.height = mStartValue.getBottom() - mStartValue.getTop();

            // 解除约束==》出现在左上角(0,0)
            page_cover.setLayoutParams(layoutParams);// 容器
            page_cover_content.setLayoutParams(layoutParams);// 图片
            page_content.setLayoutParams(layoutParams);

            // 把startValue的坐标(x,y)给page_cover
            page_cover.setTranslationX(mStartValue.getLeft());
            page_cover.setTranslationY(mStartValue.getTop());

            page_content.setTranslationX(mStartValue.getLeft());
            page_content.setTranslationY(mStartValue.getTop());

            // 平移page_cover和page_content
            ObjectAnimator translationX = ObjectAnimator.ofFloat(page_cover, "translationX", mStartValue.getLeft(), mStartValueTran.getLeft());
            translationX.setInterpolator(new AccelerateDecelerateInterpolator());// 快-》慢
            translationX.setDuration(2000);
            ObjectAnimator translationY = ObjectAnimator.ofFloat(page_cover, "translationY", mStartValue.getTop(), mStartValueTran.getTop());
            translationY.setInterpolator(new AccelerateDecelerateInterpolator());
            translationY.setDuration(2000);
            ObjectAnimator translationX2 = ObjectAnimator.ofFloat(page_content, "translationX", mStartValue.getLeft(), mStartValueTran.getLeft());
            translationX2.setInterpolator(new AccelerateDecelerateInterpolator());
            translationX2.setDuration(2000);
            ObjectAnimator translationY2 = ObjectAnimator.ofFloat(page_content, "translationY", mStartValue.getTop(), mStartValueTran.getTop());
            translationY2.setInterpolator(new AccelerateDecelerateInterpolator());
            translationY2.setDuration(2000);

            mBigValue.setPivotX(0);
            mBigValue.setPivotY((startValue.getRight() - startValue.getLeft()) / 2);
            // 旋转的坐标重新定位
            page_cover.setPivotX(mBigValue.getPivotX());
            page_cover.setPivotY(mBigValue.getPivotY());

            //打开书，旋转page_cover
            ObjectAnimator rotationY = ObjectAnimator.ofFloat(page_cover, "rotationY", 0, -180);
//            rotationY.setInterpolator(new AccelerateDecelerateInterpolator());// 快-》慢
            rotationY.setInterpolator(new AccelerateInterpolator());// 快-》慢
            rotationY.setDuration(2000);
            rotationY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    Log.d(TAG, "startAnimTran:" + animation.getAnimatedValue());
                    if (value < -90) {
                        page_cover.setBackgroundColor(getResources().getColor(R.color.grey));
                    }
                }
            });

            page_content.setPivotX(mBigValue.getPivotX());
            page_content.setPivotY(mBigValue.getPivotY());
            float viewW = startValue.getRight() - startValue.getLeft();
            float viewH = startValue.getBottom() - startValue.getTop();
            float bigw = mBigValue.getRight() - mBigValue.getLeft();
            float bigH = mBigValue.getBottom() - mBigValue.getTop();
            float sY = bigH / viewH;
            float sX = bigw / 2 / viewW;
            mBigValue.setsX(sX);
            mBigValue.setsY(sY);
            // 旋转的坐标重新定位
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(page_cover, "scaleX", 1.0f, sX);
            scaleX.setInterpolator(new AccelerateInterpolator());// 慢-》快
//            scaleX.setStartDelay(1000);
            scaleX.setDuration(2000);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(page_cover, "scaleY", 1.0f, sY);
            scaleY.setInterpolator(new AccelerateInterpolator());// 慢-》快
//            scaleY.setStartDelay(1000);
            scaleY.setDuration(2000);
            ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(page_content, "scaleX", 1.0f, sX);
            scaleX2.setInterpolator(new AccelerateInterpolator());// 慢-》快
//            scaleX2.setStartDelay(1000);
            scaleX2.setDuration(2000);
            ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(page_content, "scaleY", 1.0f, sY);
            scaleY2.setInterpolator(new AccelerateInterpolator());// 慢-》快
//            scaleY2.setStartDelay(1000);
            scaleY2.setDuration(2000);

            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(translationX, translationY, translationX2, translationY2, rotationY, scaleX, scaleY, scaleX2, scaleY2);
            mAnimatorSet.addListener(this);
            mAnimatorSet.start();
        }
    }

    /**
     * 关闭动画  , 逻辑和开始动画相反
     */
    public synchronized void closeAnim() {
        if (isOpened.get()) {
            setVisibility(VISIBLE);
            setAlpha(1.0f);

            page_cover.setScaleX(mBigValue.getsX());
            page_cover.setScaleY(mBigValue.getsY());
            page_cover.setRotationY(-180);
            // 缩放
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(page_cover, "scaleX", mBigValue.getsX(), 1.0f);
            scaleX.setInterpolator(new AccelerateDecelerateInterpolator());// 快-》慢
            scaleX.setDuration(1000);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(page_cover, "scaleY", mBigValue.getsY(), 1.0f);
            scaleY.setInterpolator(new AccelerateDecelerateInterpolator());// 快-》慢
            scaleY.setDuration(1000);

            ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(page_content, "scaleX", mBigValue.getsX(), 1.0f);
            scaleX2.setInterpolator(new AccelerateDecelerateInterpolator());// 快-》慢
            scaleX2.setDuration(1000);
            ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(page_content, "scaleY", mBigValue.getsY(), 1.0f);
            scaleY2.setInterpolator(new AccelerateDecelerateInterpolator());// 快-》慢
            scaleY2.setDuration(1000);

            ObjectAnimator rotationY = ObjectAnimator.ofFloat(page_cover, "rotationY", -180, 0);
            rotationY.setInterpolator(new AccelerateInterpolator());// 慢-》快
            rotationY.setStartDelay(1000);
            rotationY.setDuration(1000);
            rotationY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    Log.d(TAG, "startAnimTran:" + animation.getAnimatedValue());
                    if (value < -90) {
                        page_cover.setBackgroundColor(getResources().getColor(R.color.grey));
                    } else {
                        page_cover.setBackgroundColor(getResources().getColor(R.color.blue));
                    }
                }
            });

            ObjectAnimator translationX = ObjectAnimator.ofFloat(page_cover, "translationX", mStartValueTran.getLeft(), mStartValue.getLeft());
            translationX.setInterpolator(new AccelerateInterpolator());// 慢->快
            translationX.setDuration(2000);
            ObjectAnimator translationY = ObjectAnimator.ofFloat(page_cover, "translationY", mStartValueTran.getTop(), mStartValue.getTop());
            translationY.setInterpolator(new AccelerateInterpolator());
            translationY.setDuration(2000);
            ObjectAnimator translationX2 = ObjectAnimator.ofFloat(page_content, "translationX", mStartValueTran.getLeft(), mStartValue.getLeft());
            translationX2.setInterpolator(new AccelerateInterpolator());
            translationX2.setDuration(2000);
            ObjectAnimator translationY2 = ObjectAnimator.ofFloat(page_content, "translationY", mStartValueTran.getTop(), mStartValue.getTop());
            translationY2.setInterpolator(new AccelerateInterpolator());
            translationY2.setDuration(2000);


            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.addListener(this);
            mAnimatorSet.playTogether(scaleX, scaleY, scaleX2, scaleY2, rotationY,translationX,translationY,translationX2,translationY2);
            mAnimatorSet.start();

        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (isOpened.get()) { // 关闭书本动画执行了
            isOpened.set(false);
            if (null != mEndListener) {
                mEndListener.onRemove();
            }
        } else { // 打开书本动画
            isOpened.set(true);
            if (null != mEndListener) {
                mEndListener.onAnimationEnd();
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public interface OnAnimationEndListener {
        void onAnimationEnd();

        void onRemove();
    }

    private BookOpenView2.OnAnimationEndListener mEndListener;

    public void setEndListener(BookOpenView2.OnAnimationEndListener endListener) {
        mEndListener = endListener;
    }

    public void cancel() {
        mAnimatorSet.cancel();
    }
}
