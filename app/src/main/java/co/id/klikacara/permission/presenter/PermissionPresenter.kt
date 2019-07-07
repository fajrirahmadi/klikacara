package co.id.klikacara.permission.presenter

import android.Manifest
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.permission.contract.PermissionContract
import co.id.klikacara.permission.usecase.PermissionUseCase
import com.tbruyelle.rxpermissions2.RxPermissions

class PermissionPresenter(
    val view: PermissionContract.View,
    val permissionUseCase: PermissionUseCase
) : BasePresenter() {
    lateinit var rxPermission: RxPermissions;

    fun init(rxPermissions: RxPermissions) {
        rxPermission = rxPermissions
    }

    fun getCameraPermission() {
        addDisposable(
            rxPermission
                .request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .subscribe { granted ->
                    if (granted)
                        view.doOnPermissionGranted()
                    else
                        view.doOnPermissionRejected()
                }
        )
    }

}