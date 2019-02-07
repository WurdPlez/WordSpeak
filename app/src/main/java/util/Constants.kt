package util

import com.android.wordspeak.R
import data.models.Models

object Constants {

    fun keyWordList(): MutableList<List<String>> {
        val list = mutableListOf<List<String>>()
        list.add(listOf("name"))
        list.add(listOf("old", "age"))
        list.add(listOf("okay", "ok"))
        list.add(listOf("live", "home"))
        list.add(listOf("level", "grade"))
        list.add(listOf("day", "today"))
        list.add(listOf("time"))
        return list
    }

    fun videoMappedList(): MutableList<Models.Video> {
        val videoList = mutableListOf<Models.Video>()
        /*1*/ videoList.add(Models.Video("What is your name", R.raw.what_is_your_name))
        /*2*/ videoList.add(Models.Video("How old are you", R.raw.how_old_are_you))
        /*3*/ videoList.add(Models.Video("How are you", R.raw.how_are_you))
        /*4*/ videoList.add(Models.Video("Where do you live", R.raw.where_do_you_live))
        /*5*/ videoList.add(Models.Video("What school level are you", R.raw.what_school_level_are_you))
        /*6*/ videoList.add(Models.Video("What day is today", R.raw.what_day_today))
        /*7*/ videoList.add(Models.Video("What time is it", R.raw.what_time_is_it))
        return videoList
    }
}