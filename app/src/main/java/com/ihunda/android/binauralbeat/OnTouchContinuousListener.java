package com.ihunda.android.binauralbeat;

import android.view.MotionEvent;
import android.view.View;

public abstract class OnTouchContinuousListener implements View.OnTouchListener {

    private static final int MIN_REPEAT_MS = 50;
    private static final int NORMAL_REPEAT_MS = 200;
    private static final int LONG_PRESS_NUM = 20;

    private final int mInitialRepeatDelay;
    private int mNormalRepeatDelay;
    private final int mMinRepeatDelay;
    private View mView;
    private int fires;

    /**
     * Construct listener with default delays
     */
    public OnTouchContinuousListener() {
        this.mInitialRepeatDelay = 500;
        this.mNormalRepeatDelay = NORMAL_REPEAT_MS;
        this.mMinRepeatDelay = MIN_REPEAT_MS;
        fires = 0;
    }


    private final Runnable repeatRunnable = new Runnable() {
        @Override
        public void run() {

            // as long the button is press we continue to repeat
            if (mView.isPressed()) {

                // Fire the onTouchRepeat event
                if (++fires > LONG_PRESS_NUM)
                    onTouchRepeat(mView, true);
                else
                    onTouchRepeat(mView, false);

                // Schedule the repeat
                mNormalRepeatDelay = Math.max(mMinRepeatDelay, (int) (mNormalRepeatDelay * .9));

                mView.postDelayed(repeatRunnable, mNormalRepeatDelay);
            }
        }
    };

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about the
     *              event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mView = v;

            // Fire the first touch straight away
            onTouchRepeat(mView, false);

            mNormalRepeatDelay = NORMAL_REPEAT_MS;
            fires = 0;

            // Start the incrementing with the initial delay
            mView.postDelayed(repeatRunnable, mInitialRepeatDelay);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mView.removeCallbacks(repeatRunnable);
        }

//    switch (event.getAction()) {
//	  case MotionEvent.ACTION_DOWN:
//		  v .getBackground().setColorFilter(Color.parseColor("#FF9933"), PorterDuff.Mode.MULTIPLY);
//		  v .invalidate();
//		  break;
//	  case MotionEvent.ACTION_CANCEL:
//		  v .getBackground().clearColorFilter();
//		  v .invalidate();
//		  break;
//	  case MotionEvent.ACTION_UP:
//		  v .getBackground().clearColorFilter();
//		  v .invalidate();
//		  break;
//	  }

        // don't return true, we don't want to disable buttons default behavior
        return false;
    }

    /**
     * Called when the target item should be changed due to continuous touch. This
     * happens at first press, and also after each repeat timeout. Releasing the
     * touch will stop the repeating.
     */
    public abstract void onTouchRepeat(View view, boolean longpress);

}
