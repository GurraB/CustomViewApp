package com.example.gustaf.customviewapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Gustaf on 2016-05-17.
 */
public class VisualSchedule extends View {

    private final String TAG = "VisualSchedule";
    private ArrayList<GraphEvent> events = new ArrayList<>();
    private Calendar day;

    private int mBackgroundColor;
    private int mStampColor;
    private int mScheduleColor = Color.CYAN;
    private int mHourLineColor;
    private int mHeaderTextColor;
    private int mPrimaryTextColor = Color.DKGRAY;
    private int mTimeStampColor = Color.BLUE;

    private Paint mBackgroundPaint;
    private Paint mLinePaint;
    private Paint mPrimaryTextPaint;
    private Paint mShadowPaint;
    private Paint mEventPaint;

    private float mTextHeight = 25;
    private float mSidebarWidth = 100;

    private float mWidth;
    private float mHeight;

    private float mHourHeight = 100;


    public VisualSchedule(Context context) {
        super(context);
    }

    public VisualSchedule(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VisualSchedule, 0, 0);
        try {
            mBackgroundColor = array.getInt(R.styleable.VisualSchedule_one, Color.GRAY);
            mStampColor = array.getInt(R.styleable.VisualSchedule_two, 0);
            mScheduleColor = array.getInt(R.styleable.VisualSchedule_three, 0);
        } finally {
            array.recycle();
        }
        init();
    }

    public VisualSchedule(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VisualSchedule(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mBackgroundPaint = createBackgroundPaint();
        mPrimaryTextPaint = createPrimaryTextPaint();
        mShadowPaint = createShadowPaint();
        mLinePaint = createLinePaint();
        mEventPaint = createEventPaint();
    }

    private Paint createBackgroundPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        return paint;
    }
    private Paint createPrimaryTextPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mPrimaryTextColor);
        paint.setTextSize(mTextHeight);
        return paint;
    }
    private Paint createShadowPaint() {
        Paint paint = new Paint(0);
        paint.setColor(0xff101010);
        paint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        return paint;
    }
    private Paint createLinePaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mHourLineColor);
        paint.setStrokeWidth(1);
        return paint;
    }
    private Paint createEventPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mTimeStampColor);
        return paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());

        mWidth = w - xpad;
        mHeight = mHourHeight * 24 + mTextHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, (int) mHeight + (int) mTextHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.v(TAG, "Width: " + String.valueOf(mWidth));
        Log.v(TAG, "Height: " + String.valueOf(mHeight));
        drawBackground(canvas);
        drawSidebar(canvas);
        drawHourLines(canvas);
        drawEvents(canvas);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, mWidth, mHeight + mTextHeight, mBackgroundPaint);
    }

    private void drawSidebar(Canvas canvas) {
        for (int i = 0; i < 25; i++) {
            canvas.drawText((i < 10 ? "0" + i : i) + ":00", 10, mHourHeight * i + mTextHeight, mPrimaryTextPaint);
        }
        canvas.drawLine(mSidebarWidth, 0, mSidebarWidth, mHeight + mTextHeight, mPrimaryTextPaint);
    }

    private void drawHourLines(Canvas canvas) {
        for (int i = 0; i < 25; i++) {
            float y = mHourHeight * i + mTextHeight + 1;
            canvas.drawLine(mSidebarWidth, y, mWidth, y, mPrimaryTextPaint);
        }
    }

    private void drawEvents(Canvas canvas) {
        Log.v(TAG, "events drawn");
        float widthPerDay = mWidth - mSidebarWidth;
        ArrayList<GraphEvent> dailyEvents = getGraphsForDay();
        Log.v(TAG, String.valueOf(dailyEvents.size()));
        for (GraphEvent event : dailyEvents) {
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            startTime.setTimeInMillis(event.getStart());
            endTime.setTimeInMillis(event.getStop());
            int startMinute = (startTime.get(Calendar.HOUR_OF_DAY) * 60) + startTime.get(Calendar.MINUTE);
            int endMinute = (startTime.get(Calendar.DAY_OF_YEAR) < endTime.get(Calendar.DAY_OF_YEAR) ? 1440 : (endTime.get(Calendar.HOUR_OF_DAY) * 60) + endTime.get(Calendar.MINUTE));
            Log.v(TAG, "start, end: " + startMinute + ", " + endMinute);
            float startY = startMinute * (mHourHeight / 60) + mTextHeight;
            float endY = endMinute * (mHourHeight / 60) + mTextHeight;
            if(event.isStamp())
                mEventPaint.setColor(mStampColor);
            else
                mEventPaint.setColor(mScheduleColor);
            mEventPaint.setColor(Color.BLUE);
            Log.v(TAG, "startX: " + mSidebarWidth + "\nstartY: " + startY + "\nwidth: " + widthPerDay / 2 + "\nendY: " + endY + "\nColor = BLUE: " + String.valueOf(mEventPaint.getColor() == Color.BLUE));
            canvas.drawRect(mSidebarWidth, startY, widthPerDay / 2 + mSidebarWidth, endY, mEventPaint);
            drawTitle();
        }
    }

    private void drawTitle() {

    }


    public void notifyDataChanged(ArrayList<GraphEvent> events) {
        this.events = events;
        invalidate();
    }

    private ArrayList<GraphEvent> getGraphsForDay() {
        ArrayList<GraphEvent> eventsforDay = new ArrayList<>();
        if(day == null)
            day = Calendar.getInstance();

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(day.getTimeInMillis());

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(day.getTimeInMillis());

        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 1);

        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        for (GraphEvent event : events)
            if((event.getStart() >= start.getTimeInMillis() && event.getStart() <= end.getTimeInMillis())
                ||
               (event.getStop() <= end.getTimeInMillis() && event.getStop() >= start.getTimeInMillis()))
                eventsforDay.add(event);
        return eventsforDay;
    }


//TODO----------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public int getmBackgroundColor() {
        return mBackgroundColor;
    }

    public void setmBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
        invalidate();
        requestLayout();
    }

    public void setDay(Calendar day) {
        this.day = day;
    }
}
