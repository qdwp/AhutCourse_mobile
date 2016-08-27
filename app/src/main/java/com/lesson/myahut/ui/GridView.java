package com.lesson.myahut.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;

import com.lesson.myahut.R;
import com.lesson.myahut.entity.Lesson;
import com.lesson.myahut.handler.Lesson_GridPosition;
import com.lesson.myahut.handler.LessonManager;
import com.lesson.myahut.handler.LessonName;
import com.lesson.myahut.handler.TimeTable;

/**
 * Created by qidunwei on 2016/1/10.
 */
public class GridView extends View {

    private Context context;
    private Lesson[][] lessons;
    private TimeTable timetable;

    private static float weekNameSize;
    private static float weekNameMargin;
    private static float lessonNameSize;
    private static float lessonNameMargin;
    private static float lessonPlaceSize;
    private static float lessonNamePlaceGap;
    private static float timePartitionGap;

    private int lessonNameMaxLines = 3;
    private int lessonNameMaxLength = 2;// 每行
    private int lessonPlaceMaxLength = 4;
    private int lessonPlaceMaxLines = 2;

    private int markX = -1, markY = -1;

    public static final int BLUE = Color.parseColor("#3F92D2");
    public static final int RED = Color.parseColor("#B22222");
    public static final int BLACK = Color.parseColor("#333333");
    public static final int LIGHTBLUE = Color.parseColor("#ECEFF2");

    private static float lessonInfoSize;
    private static float lessonInfoPadding;
    private static float lessonInfoPaddingLeft;
    private static NinePatchDrawable lessonInfoBackground;

    private Lesson_GridPosition lessonPosition;
    private Lesson lesson;
    private int lessonInfoX, lessonInfoY;
    private Rect lessonInfoBorder = new Rect();
    private boolean showLessonInfo = false;
    private boolean lessonInfoIsShowing = false;
    private boolean showLessonBackgroundIfNoLesson;

    /**
     * 如果 lesson 为空，加载本地数据
     *
     * @param _context
     * @param _lessons
     */
    public GridView(Context _context, Lesson[][] _lessons) {
        super(_context);
        context = _context;
        lessons = _lessons;
        if (lessons == null) {
            lessons = LessonManager.getInstance(context).getLessons();
        }
        timetable = TimeTable.getInstance(context);

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLessonInfo();
            }
        });
    }

    /**
     * 刷新时间表
     * @param seasonWinter
     */
    public void refreshTimeTable(boolean seasonWinter){
        timetable = TimeTable.getInstance(context).refreshBeginEndTime(seasonWinter);
    }

    /**
     * 刷新课程表
     */
    public void refreshLessons(){
        LessonManager.getInstance(context).getAllLessons();
        lessons = LessonManager.getInstance(context).getLessons();
    }

    /**
     * 获取当前所处表格位置
     *
     * @return
     */
    public Lesson_GridPosition getCurrentGridPosition() {
        return calcGridPosition();
    }

    /**
     * 更新课程位置
     *
     * @param showLessonBackgroundIfNoLesson
     */
    public void updateLessonPosition(boolean showLessonBackgroundIfNoLesson) {
        this.showLessonBackgroundIfNoLesson = showLessonBackgroundIfNoLesson;
        lessonPosition = calcGridPosition();
        showLessonInfo = false;
        invalidate();
    }

    /**
     * 刷新课程表视图
     */
    public void refreshView() {
        lessons = LessonManager.getInstance(context).getLessons();
        showLessonInfo = false;
        lessonPosition = null;
        lesson = null;
        invalidate();
        lessonInfoIsShowing = false;
    }

    private TextPaint textPaint = new TextPaint();
    private Canvas canvas;
    private float top, weekNameHeight;
    private float viewWidth, viewHeight, calendarWidth, calendarHeight, cellWidth, cellHeight;
    private float leftBorder[] = new float[8];
    private float topBorder[] = new float[6];
    private float bottomBorder[] = new float[6];
    private Paint linePaint;

    /**
     * 开始执行画布功能
     *
     * @param _canvas
     */
    @Override
    protected void onDraw(Canvas _canvas) {
        canvas = _canvas;
        init();
        drawWeekNames();
        drawLines();
        drawLessonBackground();
        drawLesson();
        drawLessonInfo();
    }

    /**
     * 初始化表格组建信息
     */
    private void init() {
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Style.FILL);

        weekNameSize = context.getResources().getDimension(R.dimen.weekname_size);
        weekNameMargin = context.getResources().getDimension(R.dimen.weekname_margin);
        lessonNameSize = context.getResources().getDimension(R.dimen.lessonname_size);
        lessonPlaceSize = context.getResources().getDimension(R.dimen.lessonplace_size);
        lessonInfoSize = context.getResources().getDimension(R.dimen.lessoninfo_size);
        lessonInfoPadding = context.getResources().getDimension(R.dimen.lessoninfo_padding);
        lessonInfoPaddingLeft = context.getResources().getDimension(R.dimen.lessoninfo_padding_left);

        viewWidth = this.getMeasuredWidth();
        viewHeight = this.getMeasuredHeight();
//        viewWidth = this.getWidth();
//        viewHeight = this.getHeight();

        calendarWidth = viewWidth;
        cellWidth = calendarWidth / 7;
        lessonNameMargin = cellWidth / 15;
        weekNameHeight = weekNameSize + weekNameMargin * 2 + 5;

        top = weekNameHeight;

        calendarHeight = viewHeight - weekNameHeight;
        timePartitionGap = calendarHeight / 40;
        cellHeight = (calendarHeight - timePartitionGap * 2) / 5;
        lessonNamePlaceGap = cellHeight / 40;

        lessonNameMaxLength = (int) ((cellWidth - lessonNameMargin) / lessonNameSize);
        lessonPlaceMaxLength = (int) (cellWidth / lessonPlaceSize);
        lessonNameMaxLines = (int) ((cellHeight - lessonPlaceMaxLength * 2 - lessonNamePlaceGap) / lessonNameSize);

        for (int i = 0; i < 8; i++) {
            leftBorder[i] = cellWidth * i;
        }
        for (int i = 0; i < 6; i++) {
            topBorder[i] = top + cellHeight * i;
            if (i > 1) topBorder[i] += timePartitionGap;
            if (i > 3) topBorder[i] += timePartitionGap;
        }
        for (int i = 0; i < 6; i++) {
            bottomBorder[i] = top + cellHeight * (i + 1);
            if (i > 1) bottomBorder[i] += timePartitionGap;
            if (i > 3) bottomBorder[i] += timePartitionGap;
        }
    }

    /**
     * 展示周一至周日的名称信息
     */
    private void drawWeekNames() {
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(weekNameSize);
        Paint paintbg = new Paint();
        paintbg.setColor(BLUE);
        String[] weekNames = context.getResources().getStringArray(R.array.week_name);
        canvas.drawRect(0, 0, canvas.getWidth(), weekNameHeight, paintbg);

        float weekNameXOffset = (cellWidth - textPaint.measureText(weekNames[0])) / 2; //都是两个字
        float weekNameYOffset = weekNameSize + weekNameMargin;
        for (int i = 0; i < 7; i++) {
            canvas.drawText(weekNames[i], leftBorder[i] + weekNameXOffset, weekNameYOffset, textPaint);
        }
    }

    /**
     * 为表格画线
     */
    private void drawLines() {
        linePaint = new Paint();
        linePaint.setARGB(80, 0, 0, 0);
        linePaint.setStyle(Style.STROKE);
        linePaint.setPathEffect(new DashPathEffect(new float[]{5, 10}, 0));

        Path path = new Path();
        /* 画横线 */
        for (int i = 1; i < 5; i++) {
//            canvas.drawLine(0, topBorder[i], calendarWidth, topBorder[i], linePaint);
            path.moveTo(0, topBorder[i]);
            path.lineTo(calendarWidth, topBorder[i]);
            canvas.drawPath(path, linePaint);
        }

//        Path path1 = new Path();
        path.moveTo(0, bottomBorder[1]);
        path.lineTo(calendarWidth, bottomBorder[1]);
        canvas.drawPath(path, linePaint);
//
//        Path path2 = new Path();
        path.moveTo(0, bottomBorder[3]);
        path.lineTo(calendarWidth, bottomBorder[3]);
        canvas.drawPath(path, linePaint);

//        canvas.drawLine(0, bottomBorder[1], calendarWidth, bottomBorder[1], linePaint);
//        canvas.drawLine(0, bottomBorder[3], calendarWidth, bottomBorder[3], linePaint);

        /* 画竖线 */
        for (int i = 1; i < 7; i++) {
//            canvas.drawLine(leftBorder[i], top, leftBorder[i], bottomBorder[1], linePaint);
            path.moveTo(leftBorder[i], top);
            path.lineTo(leftBorder[i], bottomBorder[1]);
            canvas.drawPath(path, linePaint);
        }
        for (int i = 1; i < 7; i++) {
//            canvas.drawLine(leftBorder[i], topBorder[2], leftBorder[i], bottomBorder[3], linePaint);
            path.moveTo(leftBorder[i], topBorder[2]);
            path.lineTo(leftBorder[i], bottomBorder[3]);
            canvas.drawPath(path, linePaint);
        }
        for (int i = 1; i < 7; i++) {
//            canvas.drawLine(leftBorder[i], topBorder[4], leftBorder[i], bottomBorder[4], linePaint);
            path.moveTo(leftBorder[i], topBorder[4]);
            path.lineTo(leftBorder[i], bottomBorder[4]);
            canvas.drawPath(path, linePaint);
        }
    }

    /**
     * 画课程背景
     */
    private void drawLessonBackground() {
        if (lessonPosition == null) return;
        if (!showLessonBackgroundIfNoLesson && !lessonPosition.hasLesson) return;

        Paint paintbg = new Paint();
        paintbg.setColor(LIGHTBLUE);
        canvas.drawRect(leftBorder[lessonPosition.week] + 1, topBorder[lessonPosition.time] + 1, leftBorder[lessonPosition.week + 1] - 1, bottomBorder[lessonPosition.time] - 1, paintbg);
        if (lesson != null) {
            if (lessonCanAppend(lesson)) {
                canvas.drawRect(leftBorder[lessonPosition.week] + 1, topBorder[lessonPosition.time + 1] - 2, leftBorder[lessonPosition.week + 1] - 1, bottomBorder[lessonPosition.time + 1] - 1, paintbg);
            } else if (lessonIsAppended(lesson)) {
                canvas.drawRect(leftBorder[lessonPosition.week] + 1, topBorder[lessonPosition.time - 1] + 1, leftBorder[lessonPosition.week + 1] - 1, bottomBorder[lessonPosition.time - 1] + 2, paintbg);
            }
        }
    }

    /**
     * 展示课程信息,
     * 课程数组按天分组
     */
    private void drawLesson() {
        for (Lesson[] lessonsOfDay : lessons) {
            if (lessonsOfDay != null) {
                for (Lesson lesson : lessonsOfDay) {
                    if (lesson != null) {
                        drawSingleLesson(lesson);
                    }
                }
            }
        }
    }

    private float textLeft, textTop;

    /**
     * 展示一般课程
     *
     * @param lesson
     */
    private void drawSingleLesson(Lesson lesson) {
        // 一般课程
        if (lesson == null) return;
        textPaint.setTextSize(lessonNameSize);
        int week, time;
        int appendMode = lessonAppendMode(lesson);
        if (appendMode == -1) {
            return;// 连续四节课，后两节课不画
        }

        week = lesson.week;
        time = lesson.time;
        String place;
        LessonName name = new LessonName(lesson.name);
        place = lesson.place;
        float cellHeight = this.cellHeight;
        int lessonNameMaxLines = this.lessonNameMaxLines;
        /**
         * 课程为连续四节课，且当前为前两节课
         */
        if (appendMode == 1) {
            Lesson nextLesson = (!TimeTable.isValidWeekTime(week, time)) ? null : lessons[week][time + 1];
            if (nextLesson == null) return;
            if (timetable.isNowHavingLesson(nextLesson) == 0) return;
            cellHeight *= 2;
            lessonNameMaxLines = (int) ((cellHeight - lessonPlaceMaxLength * 2 - lessonNamePlaceGap) / lessonNameSize);
            if (lessonPosition == null || (!lesson.atPosition(lessonPosition) && !nextLesson.atPosition(lessonPosition))) {
                Paint bgPaint = new Paint();
                bgPaint.setColor(Color.WHITE);
                canvas.drawLine(leftBorder[week], bottomBorder[time], leftBorder[week + 1], bottomBorder[time], bgPaint);
            }
        }

        float length = name.length();
        int lines = (int) ((length - 1) / (float) lessonNameMaxLength) + 1;
        if (lines > lessonNameMaxLines) lines = lessonNameMaxLines;
        int lessonNameMaxLength = this.lessonNameMaxLength;
        if (lessonNameMaxLength == 3 && length >= 4 && length <= 5) {
            lines = 2;
            lessonNameMaxLength = 2;
        }

        int placeLines = 2;
        boolean isRoomNumber = false;
        if (place.length() <= lessonPlaceMaxLength) {
            placeLines = 1;
        } else if (place.length() <= lessonPlaceMaxLength * lessonPlaceMaxLines) {
            if (isInt(place.substring(place.length() - 3, place.length()))) {
                isRoomNumber = true;
                placeLines = 2;

            } else {
                placeLines = lessonPlaceMaxLines;
            }
        }

        /* 画课程名 */
        textTop = topBorder[time] + (cellHeight - lessonPlaceSize * placeLines - lessonNamePlaceGap + lessonNameSize * lines) / 2; //课程名最后一行的底部Y

        if (!lesson.isBeforeEnd(timetable.numOfWeek)) {
            textPaint.setARGB(30, 0, 0, 0);
        } else {
            textPaint.setColor(BLACK);
        }

        String text;
        for (int i = 0; i < lines; i++) {
            if (name.length() <= lessonNameMaxLength) {
                text = name.toString();
            } else {
                text = name.substring(0, lessonNameMaxLength);
                name = new LessonName(name.substring(lessonNameMaxLength));
            }
            textLeft = leftBorder[week] + (cellWidth - textPaint.measureText(text)) / 2;
            canvas.drawText(text, textLeft, textTop - lessonNameSize * (lines - i - 1), textPaint);
        }


        /* 画地点 */
        textPaint.setTextSize(lessonPlaceSize);
        if (!lesson.isBeforeEnd(timetable.numOfWeek)) {
            textPaint.setARGB(30, 0, 0, 0);
        } else {
            textPaint.setColor(Color.parseColor("#B22222"));
        }

        textTop += lessonNamePlaceGap + lessonPlaceSize;

        if (place.length() <= lessonPlaceMaxLength) {
            textLeft = leftBorder[week] + (cellWidth - textPaint.measureText(place)) / 2;
            canvas.drawText(place, textLeft, textTop, textPaint);
        } else if (place.length() <= lessonPlaceMaxLength * lessonPlaceMaxLines) {
            if (isRoomNumber) {
                String building = place.substring(0, place.length() - 3);
                String roomNumber = place.substring(place.length() - 3, place.length());
                if (building.length() > lessonPlaceMaxLength)
                    building = building.substring(0, lessonPlaceMaxLength);
                textLeft = leftBorder[week] + (cellWidth - textPaint.measureText(building)) / 2;
                canvas.drawText(building, textLeft, textTop, textPaint);
                textLeft = leftBorder[week] + (cellWidth - textPaint.measureText(roomNumber)) / 2;
                canvas.drawText(roomNumber, textLeft, textTop + lessonPlaceSize, textPaint);
            } else {
                text = place.substring(0, lessonPlaceMaxLength);
                textLeft = leftBorder[week] + (cellWidth - textPaint.measureText(text)) / 2;
                canvas.drawText(text, textLeft, textTop, textPaint);
                text = place.substring(lessonPlaceMaxLength);
                if (text.length() > lessonPlaceMaxLength)
                    text = text.substring(0, lessonPlaceMaxLength);
                textLeft = leftBorder[week] + (cellWidth - textPaint.measureText(text)) / 2;
                canvas.drawText(text, textLeft, textTop + lessonPlaceSize, textPaint);
            }
        }
    }

    private boolean isInt(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证课程是否可添加 后两节有课
     * 连续四节课时，后两节课不画，只显示前两节课
     *
     * @param lesson
     * @return
     */
    public boolean lessonCanAppend(Lesson lesson) {
        if (lesson.time == 0 || lesson.time == 2) {
            Lesson appendLesson = lessons[lesson.week][lesson.time + 1];
            if (appendLesson != null) {
                if (appendLesson.name.contentEquals(lesson.name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 验证课程是否可添加 前两节有课
     * 连续四节课时，当前课程为后两节，则返回不画
     *
     * @param lesson
     * @return
     */
    public boolean lessonIsAppended(Lesson lesson) {
        if (lesson.time == 1 || lesson.time == 3) {
            Lesson appendLesson = lessons[lesson.week][lesson.time - 1];
            if (appendLesson != null) {
                if (appendLesson.name.contentEquals(lesson.name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int lessonAppendMode(Lesson lesson) {
        if (lessonCanAppend(lesson)) return 1;
        if (lessonIsAppended(lesson)) return -1;
        return 0;
    }

    /**
     * 展示课程信息
     */
    private void drawLessonInfo() {
        if (!showLessonInfo) return;

        textPaint.setColor(BLACK);
        textPaint.setTextSize(lessonInfoSize);

        if (lessonInfoBackground == null) {
            /* context.getResources().getDrawable(int id) 在 API Level 21 中被废弃
             * 使用 context.getResources().getDrawable(int id, Resources.Theme theme) 代替
             * 另外：.9.png 图片报错需要用 Android studio 重新修改图片 */
            lessonInfoBackground = (NinePatchDrawable) context.getResources().getDrawable(R.drawable.info_bg);
//            lessonInfoBackground = (NinePatchDrawable)
//                    context.getResources().getDrawable(R.drawable.info_bg, null);
        }

        boolean flipLeft = false;
        boolean flipTop = false;

        String teacherText = lesson.teacher;
        if (teacherText.length() > 5) {
            teacherText = teacherText.substring(0, 5);
        }
        String durationText = lesson.getDuration();
        String timeText = timetable.begintime[lesson.time] + "-"
                + timetable.endtime[lesson.time];

        int textWidth, textHeight;

        textWidth = (int) textPaint.measureText(timeText);
        textWidth += lessonInfoPaddingLeft * 2;
        textHeight = (int) (lessonInfoPadding * 3 + lessonInfoSize * 3);
        textHeight += lessonInfoBackground.getIntrinsicHeight() / 4;

        if (lessonInfoX + textWidth > viewWidth) flipLeft = true;
        if (lessonInfoY + textHeight > viewHeight) flipTop = true;

        lessonInfoBorder.left = flipLeft ? lessonInfoX - textWidth : lessonInfoX;
        lessonInfoBorder.top = flipTop ? lessonInfoY - textHeight : lessonInfoY;
        lessonInfoBorder.right = flipLeft ? lessonInfoX : lessonInfoX + textWidth;
        lessonInfoBorder.bottom = flipTop ? lessonInfoY : lessonInfoY + textHeight;
        lessonInfoBackground.setBounds(lessonInfoBorder);
        lessonInfoBackground.draw(canvas);

        float textLeft = lessonInfoBorder.left + lessonInfoPaddingLeft;

        canvas.drawText(teacherText, textLeft, lessonInfoBorder.top + lessonInfoPadding + lessonInfoSize, textPaint);
        canvas.drawText(durationText, textLeft, lessonInfoBorder.top + lessonInfoPadding * 2 + lessonInfoSize * 2, textPaint);
        canvas.drawText(timeText, textLeft, lessonInfoBorder.top + lessonInfoPadding * 3 + lessonInfoSize * 3, textPaint);
        lessonInfoIsShowing = true;
    }

    private Lesson_GridPosition calcGridPosition() {
        //return null if not in grid
        int gridX = (int) (markX / cellWidth);
        int gridY = -1;
        for (int i = 0; i < 5; i++) {
            if (markY > topBorder[i] && markY < bottomBorder[i])
                gridY = i;
        }
        if (!TimeTable.isValidWeekTime(gridX, gridY)) return null;
        lesson = lessons[gridX][gridY];
        Lesson_GridPosition position = new Lesson_GridPosition();
        position.week = gridX;
        position.time = gridY;
        if (lesson != null) {
            position.hasLesson = true;
            position.name = lesson.name;
        }
        return position;
    }

    /**
     * 显示课程信息
     */
    private void showLessonInfo() {
        if (lessonInfoIsShowing) {
            if (lessonInfoBorder.contains(markX, markY)) {
//                Util.makeToast(context, "坐标:" + markX + "-" + markY);
//                openLessonmateActivity();
            } else if ((lessonPosition = calcGridPosition()) != null && lessonPosition.hasLesson) {
                lessonInfoX = markX;
                lessonInfoY = markY;
                showLessonInfo = true;
                invalidate();
            } else {
                showLessonBackgroundIfNoLesson = false;
                showLessonInfo = false;
                invalidate();
                lessonInfoIsShowing = false;
            }
        } else {
            lessonPosition = calcGridPosition();
            if (lessonPosition == null || !lessonPosition.hasLesson) return;
            lessonInfoX = markX;
            lessonInfoY = markY;
            showLessonInfo = true;
            invalidate();
        }
    }

    /**
     * 重写屏幕点击事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        markX = (int) event.getX();
        markY = (int) event.getY();
        return super.onTouchEvent(event);
    }

}
