package co.id.klikacara.base.utils.timehelper

import android.app.TimePickerDialog
import android.content.Context
import android.support.v7.widget.AppCompatEditText
import java.util.*

class TimeHelper {

    companion object {

        val TIME_FORMAT = "HH:mm"

        public fun getTime(context: Context, calendar: Calendar, timeEditText: AppCompatEditText) {
            val mHour = calendar.get(Calendar.HOUR_OF_DAY)
            val mMinute = calendar.get(Calendar.MINUTE)

            val mTimePickerDialog =
                TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    timeEditText.setText(TimeUtils.getDateFormated(TIME_FORMAT, calendar.timeInMillis))
                }, mHour, mMinute, true)
            mTimePickerDialog.setTitle("Pilih Waktu")
            mTimePickerDialog.show()
        }
    }
}