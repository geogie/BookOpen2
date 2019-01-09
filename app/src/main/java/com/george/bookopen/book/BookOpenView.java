package com.george.bookopen.book;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.george.bookopen.R;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created By George
 * Description:
 */
public class BookOpenView extends FrameLayout implements Animator.AnimatorListener {
    private static final String TAG = "BookOpenView";
    /**
     * 开始位置参数
     */
    private BookOpenViewValue mStartValue;

    private AnimatorSet mAnimatorSet1;// 封面页动画
    private AnimatorSet mAnimatorSet2;// 内容页动画

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

    public BookOpenView(@NonNull Context context) {
        super(context, null, 0);
        initView(context);
    }

    public BookOpenView(@NonNull Context context, @NonNull AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookOpenView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
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
     * 书本打开效果
     * startValue-->endValue
     */
    public synchronized void startAnim(final BookOpenViewValue startValue, BookOpenViewValue endValue) {
        if (!isOpened.get()) {
            ViewGroup.LayoutParams layoutParams;
            /*-------------------cover_content----------------------------- */
            // 如果有图片,可以在此给设置,图解尽量不要太大,10K以内为佳
            mStartValue = startValue;
            Log.d(TAG, " \nstartValue:" + startValue.toString() + "\nendValue:" + endValue.toString());
            layoutParams = page_cover.getLayoutParams();
            // 把startValue的width和height给page_cover
            layoutParams.width = startValue.getRight() - startValue.getLeft();
            layoutParams.height = startValue.getBottom() - startValue.getTop();

            // 解除约束==》出现在左上角(0,0)
            page_cover.setLayoutParams(layoutParams);// 容器
            page_cover_content.setLayoutParams(layoutParams);// 图片
            page_content.setLayoutParams(layoutParams);

            // 把startValue的坐标(x,y)给page_cover
            page_cover.setTranslationX(startValue.getLeft());
            page_cover.setTranslationY(startValue.getTop());

            page_content.setTranslationX(startValue.getLeft());
            page_content.setTranslationY(startValue.getTop());

            // 计算缩放的起始位置和缩放中心点
            final int x1 = ((layoutParams.width * (endValue.getRight() - layoutParams.width)) - layoutParams.width * (endValue.getRight() - startValue.getRight())) / (endValue.getRight() - layoutParams.width);

            final float sX1 = (layoutParams.width - x1 + endValue.getRight() - startValue.getRight()) * 1.0f / (layoutParams.width - x1);
            final float sX2 = (x1 + startValue.getLeft()) * 1.0f / x1;

            final float sX = Math.max(sX1, sX2);
            final int y1 = ((layoutParams.height * (endValue.getBottom() - layoutParams.height))
                    - layoutParams.height * (endValue.getBottom() - startValue.getBottom()))
                    / (endValue.getBottom() - layoutParams.height);
            final float sY1 = (layoutParams.height - y1 + endValue.getBottom() - startValue.getBottom()) * 1.0f / (layoutParams.height - y1);
            final float sY2 = (y1 + startValue.getTop()) * 1.0f / y1;
            final float sY = Math.max(sY1, sY2);
            mStartValue.setX(x1);
            mStartValue.setsX(sX);
            mStartValue.setY(y1);
            mStartValue.setsY(sY);
            Log.d(TAG, " \nmStartValue:" + mStartValue.toString());

            // 首先确定翻页的坐标（以书边为轴，进行 x 轴方向反转）
            page_cover.setPivotX(0);
            page_cover.setPivotY(y1);

            // 对 封面进行缩放
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(page_cover, "scaleX", 1.0f, sX * 0.8f);
            scaleX.setDuration(1000);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(page_cover, "scaleY", 1.0f, sY);
            scaleY.setDuration(1000);

            // 封面进行平移到屏幕外
            ObjectAnimator translationLine = ObjectAnimator.ofFloat(page_cover, "translationX", startValue.getLeft(), 0);
            translationLine.setDuration(1000);
            // 对封面进行旋转
            ObjectAnimator rotationY = ObjectAnimator.ofFloat(page_cover, "rotationY", 0, -150);
            rotationY.setDuration(600);
            mAnimatorSet1 = new AnimatorSet();
            mAnimatorSet1.playTogether(scaleX, scaleY, translationLine, rotationY);
            mAnimatorSet1.start();

            /*-------------------page_content----------------------------- */
            page_content.setPivotX(0);
            page_content.setPivotY(y1);//106
            // 缩放
            ObjectAnimator scaleX3 = ObjectAnimator.ofFloat(page_content, "scaleX", 1.0f, sX);
            scaleX3.setDuration(1000);
            ObjectAnimator scaleY3 = ObjectAnimator.ofFloat(page_content, "scaleY", 1.0f, sY);
            scaleY3.setDuration(1000);
            // 平移
            ObjectAnimator translationLine2 = ObjectAnimator.ofFloat(page_content, "translationX", startValue.getLeft(), 0);
            translationLine2.setDuration(1000);
            mAnimatorSet2 = new AnimatorSet();
            mAnimatorSet2.playTogether(scaleX3, scaleY3, translationLine2);
            mAnimatorSet2.addListener(this);
            mAnimatorSet2.start();
        }
    }

    /**
     * 关闭动画  , 逻辑和开始动画相反
     */
    public synchronized void closeAnim() {
        if (isOpened.get()) {
            setVisibility(VISIBLE);
            setAlpha(1.0f);

            page_cover.setScaleX(mStartValue.getsX() * 0.8f);
            page_cover.setScaleY(mStartValue.getsY());
            page_cover.setRotationY(-150);
            // 缩放
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(page_cover, "scaleX", mStartValue.getsX() * 0.8f, 1.0f);
            scaleX.setDuration(1000);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(page_cover, "scaleY", mStartValue.getsY(), 1.0f);
            scaleY.setDuration(1000);

            ObjectAnimator translationLine = ObjectAnimator.ofFloat(page_cover, "translationX", 0, mStartValue.getLeft());
            translationLine.setDuration(1000);

            ObjectAnimator rotationY = ObjectAnimator.ofFloat(page_cover, "rotationY", -150, 0);
            rotationY.setDuration(600);
            rotationY.setStartDelay(400);
            mAnimatorSet1 = new AnimatorSet();
            mAnimatorSet1.playTogether(scaleX, scaleY, translationLine, rotationY);
            mAnimatorSet1.start();

            /* ---------------------page_content--------------------------- */
            ObjectAnimator scaleX3 = ObjectAnimator.ofFloat(page_content, "scaleX", mStartValue.getsX(), 1.0f);
            scaleX3.setDuration(1000);
            ObjectAnimator scaleY3 = ObjectAnimator.ofFloat(page_content, "scaleY", mStartValue.getsY(), 1.0f);
            scaleY3.setDuration(1000);

            ObjectAnimator translationLine3 = ObjectAnimator.ofFloat(page_content, "translationX", 0, mStartValue.getLeft());
            translationLine3.setDuration(1000);
            mAnimatorSet2 = new AnimatorSet();
            mAnimatorSet2.addListener(this);
            mAnimatorSet2.playTogether(scaleX3, scaleY3, translationLine3);
            mAnimatorSet2.start();

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

    private OnAnimationEndListener mEndListener;

    public void setEndListener(OnAnimationEndListener endListener) {
        mEndListener = endListener;
    }

    public void cancel() {
        mAnimatorSet1.cancel();
    }
}
