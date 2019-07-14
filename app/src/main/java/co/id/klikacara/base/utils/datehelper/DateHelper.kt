package co.id.klikacara.base.utils.datehelper

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import co.id.klikacara.base.utils.timehelper.TimeUtils
import java.util.*

class DateHelper {

    companion object {

        fun getDate(context: Context, calendar: Calendar, dateEditText: AppCompatEditText) {
            getDate(context, calendar, dateEditText, 0L, System.currentTimeMillis())
        }

        fun getDateWithMax(context: Context, calendar: Calendar, dateEditText: AppCompatEditText, maxDate: Long) {
            getDate(context, calendar, dateEditText, 0L, maxDate)
        }

        fun getDateWithMin(context: Context, calendar: Calendar, dateEditText: AppCompatEditText, minDate: Long) {
            getDate(context, calendar, dateEditText, minDate, 0L)
        }

        private fun getDate(
            context: Context,
            calendar: Calendar,
            dateEditText: AppCompatEditText,
            minDate: Long,
            maxDate: Long
        ) {
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            val mDatePicker = DatePickerDialog(
                context,
                { _, selectedyear, selectedmonth, selectedday ->
                    calendar.set(selectedyear, selectedmonth, selectedday)
                    dateEditText.setText(TimeUtils.getDateFormated("dd-MM-yyyy", calendar.timeInMillis))
                }, mYear, mMonth, mDay
            )
            mDatePicker.setTitle("Pilih Tanggal")
            if (minDate != 0L)
                mDatePicker.datePicker.minDate = minDate
            if (maxDate != 0L)
                mDatePicker.datePicker.maxDate = maxDate
            mDatePicker.show()
        }
    }
}