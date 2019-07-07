package co.id.klikacara.validator

import android.support.v4.util.PatternsCompat
import android.support.v7.widget.AppCompatEditText
import co.id.klikacara.base.utils.stringhelper.StringHelper
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidatorUsecase @Inject constructor() {

    fun isEmailValid(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun isIdentityNumberValid(identityNumber: String?): Boolean {
        return StringUtils.isNotBlank(identityNumber) && identityNumber!!.length > 15
    }

    fun isPhoneValid(phone: String): Boolean {
        return phone.length > 10
    }

    fun bindingCurrency(nominalEditText: AppCompatEditText): Observable<CharSequence> {
        val value = arrayOfNulls<String>(1)
        return RxTextView.textChanges(nominalEditText)
            .filter { StringUtils.isNotEmpty(nominalEditText.text.toString()) }
            .map { char ->
                if ((StringUtils.isNotEmpty(value[0]) && char.toString() != value[0])
                    || !StringUtils.isNotEmpty(value[0])
                ) {
                    value[0] = StringHelper.getDecimalFormatter(
                        char.toString()
                            .replace(".", "")
                    )
                    value[0]
                } else
                    ""
            }
    }
}