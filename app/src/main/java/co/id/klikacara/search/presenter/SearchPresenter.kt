package com.sajaksenja.search.presenter

import android.widget.EditText
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.base.utils.SchedulersFacade
import co.id.klikacara.search.contract.SearchContract
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.concurrent.TimeUnit

class SearchPresenter(val view: SearchContract.View) : BasePresenter() {

    val schedulerFacade = SchedulersFacade()

    fun bindingSearchEditText(editText: EditText) {
        addDisposable(
            RxTextView.textChanges(editText)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map { _ -> true }
                .observeOn(schedulerFacade.ui())
                .subscribeOn(schedulerFacade.io())
                .subscribe { valid ->
                    view.searchValid(editText.text.toString())
                }
        )
    }
}