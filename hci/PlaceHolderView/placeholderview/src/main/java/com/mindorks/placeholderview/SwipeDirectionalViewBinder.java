package com.mindorks.placeholderview;

import android.graphics.PointF;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.mindorks.placeholderview.annotations.swipe.FingerLift;
import com.mindorks.placeholderview.annotations.swipe.SwipeTouch;
import com.mindorks.placeholderview.annotations.swipe.SwipingDirection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by janisharali on 09/08/17.
 */

public class SwipeDirectionalViewBinder<T, V extends FrameLayout,
        P extends SwipeDirectionalView.SwipeDirectionalOption,
        Q extends SwipeDecor> extends SwipeViewBinder<T, V, P, Q> {

    //    TODO: Make mHasInterceptedEvent a AtomicBoolean, to make it thread safe.
    private boolean mHasInterceptedEvent = false;
    private int mOriginalLeftMargin;
    private int mOriginalTopMargin;
    private float mTransXToRestore;
    private float mTransYToRestore;

    public SwipeDirectionalViewBinder(T resolver) {
        super(resolver);
    }

    @Override
    protected void setDefaultTouchListener(final V view) {
        setAnimatorListener();
        final DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();

        FrameLayout.LayoutParams layoutParamsOriginal = (FrameLayout.LayoutParams) view.getLayoutParams();
        mOriginalLeftMargin = layoutParamsOriginal.leftMargin;
        mOriginalTopMargin = layoutParamsOriginal.topMargin;

        mTransXToRestore = view.getTranslationX();
        mTransYToRestore = view.getTranslationY();

        view.setOnTouchListener(new View.OnTouchListener() {
            private float dx;
            private float dy;
            private int activePointerId = SwipeDecor.PRIMITIVE_NULL;
            private boolean resetDone = false;
            private PointF pointerCurrentPoint = new PointF();
            private PointF pointerStartingPoint = new PointF();

            @Override
            public boolean onTouch(final View v, MotionEvent event) {

                if (getSwipeOption().getIsTouchSwipeLocked()) {
                    if (getSwipeOption().getIsViewToRestoreOnTouchLock()) {
                        getSwipeOption().setIsViewToRestoreOnTouchLock(false);
                        animateSwipeRestore(v, mOriginalTopMargin, mOriginalLeftMargin, getSwipeType());
                    }
                    return true;
                }

                if (getSwipeOption().getIsViewLocked()) {
                    if (getSwipeOption().getIsViewToRestoredOnLock()) {
                        getSwipeOption().setIsViewToRestoredOnLock(false);
                        animateSwipeRestore(v, mOriginalTopMargin, mOriginalLeftMargin, getSwipeType());
                    }
                    return true;
                }

                if (!mHasInterceptedEvent) {
                    pointerStartingPoint.set(event.getRawX(), event.getRawY());
                    pointerCurrentPoint.set(event.getRawX(), event.getRawY());
                    activePointerId = event.getPointerId(0);
                    resetDone = false;
                    FrameLayout.LayoutParams layoutParamsOriginal = (FrameLayout.LayoutParams) v.getLayoutParams();
                    dx = pointerCurrentPoint.x - layoutParamsOriginal.leftMargin;
                    dy = pointerCurrentPoint.y - layoutParamsOriginal.topMargin;
                    mHasInterceptedEvent = true;
                }

                if(SwipeDirectionalView.SWIPE) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            break;

                        case MotionEvent.ACTION_POINTER_UP:
                            if (event.getPointerId(event.getActionIndex()) == activePointerId && !resetDone) {
                            } else {
                                break;
                            }

                        case MotionEvent.ACTION_UP:
                            if (!resetDone) {
                                if (!getSwipeOption().getIsPutBackActive()) {
                                    blockTouch();
                                }

                                bindFingerLift();

                                float transX = displayMetrics.widthPixels;
                                float transY = displayMetrics.heightPixels;

                                view.animate()
                                        .translationX(transX)
                                        .translationY(transY)
                                        .setInterpolator(new AccelerateInterpolator(getSwipeDecor().getSwipeAnimFactor()))
                                        .setDuration((long) (getSwipeDecor().getSwipeAnimTime() * 1.25))
                                        .setListener(getViewRemoveAnimatorListener())
                                        .start();
                            }
                            new CountDownTimer(getSwipeDecor().getSwipeAnimTime(), getSwipeDecor().getSwipeAnimTime()) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    mHasInterceptedEvent = false;
                                }
                            }.start();
                            resetDone = true;

                            break;

                        case MotionEvent.ACTION_MOVE:
                            if (!resetDone && event.findPointerIndex(activePointerId) != SwipeDecor.PRIMITIVE_NULL) {
                                pointerCurrentPoint.set(event.getRawX(), event.getRawY());
                                FrameLayout.LayoutParams layoutParamsTemp = (FrameLayout.LayoutParams) v.getLayoutParams();
                                layoutParamsTemp.topMargin = (int) (pointerCurrentPoint.y - dy);
                                layoutParamsTemp.leftMargin = (int) (pointerCurrentPoint.x - dx);
                                v.setLayoutParams(layoutParamsTemp);

                                int distanceMovedTop = layoutParamsTemp.topMargin - mOriginalTopMargin;
                                int distanceMovedLeft = layoutParamsTemp.leftMargin - mOriginalLeftMargin;
                                getCallback().onAnimateView(distanceMovedLeft, distanceMovedTop, displayMetrics.widthPixels / getSwipeOption().getWidthSwipeDistFactor(),
                                        displayMetrics.heightPixels / getSwipeOption().getHeightSwipeDistFactor(), SwipeDirectionalViewBinder.this);

                                broadcastMoveDirection(pointerCurrentPoint.x, pointerCurrentPoint.y,
                                        pointerStartingPoint.x, pointerStartingPoint.y, displayMetrics.widthPixels);
                            }

                            break;
                    }
                }  else {
                    blockTouch();
                }
                return true;
            }
        });
    }

    protected void bindSwipingDirection(SwipeDirection direction) {
        for (final Method method : getResolver().getClass().getDeclaredMethods()) {
            SwipingDirection annotation = method.getAnnotation(SwipingDirection.class);
            if (annotation != null) {
                Class<?>[] paramClass = method.getParameterTypes();
                if (paramClass != null && paramClass.length > 0 && paramClass[0] == SwipeDirection.class) {
                    try {
                        method.setAccessible(true);
                        method.invoke(getResolver(), direction);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void bindFingerLift() {
        for(final Method method : getResolver().getClass().getDeclaredMethods()) {
            FingerLift annotation = method.getAnnotation(FingerLift.class);
            if(annotation != null) {
                try {
                    method.setAccessible(true);
                    method.invoke(getResolver());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void bindSwipeTouch(float xStart, float yStart, float xCurrent, float yCurrent) {
        for (final Method method : getResolver().getClass().getDeclaredMethods()) {
            SwipeTouch annotation = method.getAnnotation(SwipeTouch.class);
            if (annotation != null) {
                try {
                    Class<?>[] paramClass = method.getParameterTypes();
                    if (paramClass != null && paramClass.length == 4
                            && paramClass[0].equals(Float.TYPE)
                            && paramClass[1].equals(Float.TYPE)
                            && paramClass[2].equals(Float.TYPE)
                            && paramClass[3].equals(Float.TYPE)) {
                        method.setAccessible(true);
                        method.invoke(getResolver(), xStart, yStart, xCurrent, yCurrent);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void broadcastMoveDirection(float xCurrent, float yCurrent, float xStart, float yStart, float widthLength) {

        // send the touch points
        bindSwipeTouch(xStart, yStart, xCurrent, yCurrent);

        if (xCurrent >= widthLength / 2) {

            // RIGHT
            bindSwipingDirection(SwipeDirection.RIGHT);

        } else if (xCurrent < widthLength / 2) {

            // LEFT
            bindSwipingDirection(SwipeDirection.LEFT);
        }
    }
}
