package co.id.klikacara.base.presenter

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter(
) {
    var disposables: CompositeDisposable? = null

    fun addDisposable(disposable: Disposable) {
        if (disposables == null)
            disposables = CompositeDisposable()
        disposables?.add(disposable)
    }

    fun clearDisposable() {
        disposables?.clear()
    }

}