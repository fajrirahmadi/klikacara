package co.id.klikacara.order.presenter

import android.support.v7.widget.AppCompatEditText
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.order.contract.OrderContract
import com.jakewharton.rxbinding2.widget.RxTextView
import org.apache.commons.lang3.StringUtils

class OrderDatePresenter(
    private val view: OrderContract.OrderDateView
) : BasePresenter() {

    fun bindingText(editText: AppCompatEditText, price: Long) {
        val value = arrayOfNulls<String>(1)
        addDisposable(
            RxTextView.textChanges(editText)
                .filter { StringUtils.isNotEmpty(editText.text.toString()) }
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
                .subscribe { values ->
                    if (StringUtils.isNotEmpty(values)) {
                        editText.setText(values)
                        editText.setSelection(editText.text.toString().length)
                        view.setTotalAmount(price * StringHelper.removeDotFromFormatedValue(editText.text.toString()).toLong())
                    }
                }
        )
    }
}