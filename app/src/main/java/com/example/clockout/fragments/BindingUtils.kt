package com.example.clockout.fragments

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.clockout.*
import com.example.clockout.database.TimeCardEntry

// Select Image to show day of week
@BindingAdapter("dayofweekImage")
fun ImageView.setSleepImage(item: TimeCardEntry?) {
    item?.let {
        setImageResource(
            when (item.dayofweek) {
                // TODO: Update from using clockInTimeMilli to using dayofweek
                7 -> R.drawable.ic_sunday
                1 -> R.drawable.ic_monday
                2 -> R.drawable.ic_tuesday
                3 -> R.drawable.ic_wednesday
                4 -> R.drawable.ic_thursday
                5 -> R.drawable.ic_friday
                6 -> R.drawable.ic_saturday
                else -> R.drawable.icon
            }
        )
    }
}

// Format Date
@BindingAdapter("dateFormatted")
fun TextView.setDateTimeFormatted(item: TimeCardEntry?) {
    item?.let {
        text = item.timeCardDate
    }
}

// Format Start Time
@BindingAdapter("clockInTimeFormatted")
fun TextView.setClockInTimeFormatted(item: TimeCardEntry?) {
    item?.let {
        text = convertLongToTimeString(item.clockInTimeMilli)
    }
}

// Format End Time
@BindingAdapter("clockOutTimeFormatted")
fun TextView.setClockOutTimeFormatted(item: TimeCardEntry?) {
    item?.let {
        text = if (item.clockOutTimeMilli > item.clockInTimeMilli) {
            convertLongToTimeString(item.clockOutTimeMilli)
        } else {
            "--:--"
        }
    }
}

// Format Total Hours worked
@BindingAdapter("totalHoursFormatted")
fun TextView.setTotalHoursFormatted(item: TimeCardEntry?) {
    item?.let {
        text = if (item.clockOutTimeMilli > item.clockInTimeMilli) {
            String.format("%.2f",item.totalHours)

        } else {
            "0.0"
        }
    }
}


// Format Lunch Break Start Time
@BindingAdapter("lunchInTimeFormatted")
fun TextView.setLunchInTimeFormatted(item: TimeCardEntry?) {
    item?.let {
        text = if (item.lunchInTimeMilli > 0) {
            convertLongToTimeString(item.lunchInTimeMilli)
        } else {
            "--:--"
        }
    }
}

// Format Lunch Break End Time
@BindingAdapter("lunchOutTimeFormatted")
fun TextView.setLunchOutTimeFormatted(item: TimeCardEntry?) {
    item?.let {
        text = if (item.lunchOutTimeMilli > 0) {
            convertLongToTimeString(item.lunchOutTimeMilli)
        } else {
            "--:--"
        }
    }
}