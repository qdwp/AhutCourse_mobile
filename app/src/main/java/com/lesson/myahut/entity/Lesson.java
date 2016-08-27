package com.lesson.myahut.entity;

import com.lesson.myahut.handler.Lesson_GridPosition;

/**
 * Created by qidunwei on 2016/1/10.
 */
public class Lesson {

    public String name, place, teacher;
    public int week, time, startweek, endweek;

    public Lesson(String name0, String place0,
                  String teacher0, int startweek0, int endweek0, int week0, int time0) {
        name = name0;
        place = place0;
        teacher = teacher0;
        startweek = startweek0;
        endweek = endweek0;
        week = week0;
        time = time0;
    }

    public boolean isBeforeEnd(int numOfWeek) {
        return numOfWeek <= endweek;
    }

    public boolean isInRange(int numOfWeek) {
        return numOfWeek >= startweek && numOfWeek <= endweek;
    }

    public String getTitle() {
        return name + "(" + teacher + ")";
    }

    public String getDuration() {
        return "第" + startweek + "-" + endweek + "周";
    }

    public boolean atPosition(Lesson_GridPosition grid) {
        return (week == grid.week && time == grid.time);
    }

}
