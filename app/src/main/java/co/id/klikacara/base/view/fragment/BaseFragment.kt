package co.id.klikacara.base.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import butterknife.ButterKnife
import butterknife.Unbinder
import co.id.klikacara.R
import co.id.klikacara.base.contract.BaseContract
import co.id.klikacara.base.utils.compressor.Compressor
import co.id.klikacara.base.utils.listhelper.CustomEndlessRecyclerViewScrollListener
import co.id.klikacara.base.view.dialog.BaseJavaDialog
import co.id.klikacara.base.view.dialog.ProgressDialog
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.io.IOException
import java.util.*

abstract class BaseFragment : Fragment(), BaseContract.View {

    private var progressDialog: ProgressDialog? = null
    lateinit var infoDialog: BaseJavaDialog

    lateinit var unbinder: Unbinder

    private var isProgressShown = false

    override fun showError(message: String) {
        showError(message, View.OnClickListener { infoDialog.dismissAllowingStateLoss() })
    }

    fun showError(message: String, okListener: View.OnClickListener) {
        showError("Gagal", message, okListener)
    }

    fun showError(title: String, message: String, okListener: View.OnClickListener) {
        if (infoDialog.isAdded)
            initInfoDialog()
        infoDialog.okClickListener = okListener
        infoDialog.description = message
        infoDialog.title = title
        infoDialog.showAllowingStateLoss(childFragmentManager, "TAG")
    }

    override fun showSuccess(message: String) {
        showSuccess(message, View.OnClickListener { infoDialog.dismissAllowingStateLoss() })
    }

    fun showSuccess(message: String, okListener: View.OnClickListener) {
        if (infoDialog.isAdded)
            initInfoDialog()
        infoDialog.title = "Berhasil"
        infoDialog.okClickListener = okListener
        infoDialog.description = message
        infoDialog.showAllowingStateLoss(childFragmentManager, "TAG")
    }

    override fun showInfo(message: String) {
        showInfo(message, View.OnClickListener { infoDialog.dismissAllowingStateLoss() })
    }

    fun showInfo(message: String, okListener: View.OnClickListener) {
        if (infoDialog.isAdded)
            initInfoDialog()
        infoDialog.hideBtnCancel(true)
        infoDialog.okClickListener = okListener
        infoDialog.description = message
        infoDialog.showAllowingStateLoss(childFragmentManager, "FRAGMENT")
    }

    fun showInfoWithCancel(message: String, okListener: View.OnClickListener) {
        if (infoDialog.isAdded)
            initInfoDialog()
        infoDialog.okClickListener = okListener
        infoDialog.hideBtnCancel(false)
        infoDialog.description = message
        infoDialog.showAllowingStateLoss(childFragmentManager, "TAG")
    }

    override fun showProgressDialog() {
        if (!isProgressShown) {
            initProgressDialog()
            isProgressShown = true
            progressDialog?.show(childFragmentManager, "PROGRESS_DIALOG")
        }
    }

    override fun dismissProgressDialog() {
        if (isProgressShown) {
            isProgressShown = false
            progressDialog?.dismissAllowingStateLoss()
        }
    }

    fun showActivityAndFinishCurent(intent: Intent) {
        showActivity(intent)
        activity?.finish()
    }

    fun showActivity(intent: Intent) {
        startActivity(intent)
    }

    fun showActivityAndFinishAllActivity(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        showActivityAndFinishCurent(intent)
    }

    fun getIntent(context: Context, cls: Class<*>): Intent {
        return Intent(context, cls)
    }

    fun getInflate(layoutInflater: LayoutInflater, layoutResID: Int, container: ViewGroup?): View {
        val inflate = layoutInflater.inflate(layoutResID, container, false)
        unbinder = ButterKnife.bind(this, inflate)
        initProgressDialog()
        initInfoDialog()
        return inflate
    }

    private fun initInfoDialog() {
        infoDialog = BaseJavaDialog()
        infoDialog.hideBtnCancel(true)
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    fun navigateTo(navigationContainerId: Int, actionId: Int, bundle: Bundle?) {
        activity?.let {
            Navigation.findNavController(it, navigationContainerId)
                .navigate(actionId, bundle)
        }
    }

    fun navigateTo(navigationContainerId: Int, actionId: Int) {
        navigateTo(navigationContainerId, actionId, null)
    }

    fun navigateToAndFinishCurrent(navigationContainerId: Int, actionId: Int) {
        navigateTo(navigationContainerId, actionId)
        activity?.finish()
    }

    fun configureItemAdapter(
        adapter: FastItemAdapter<*>,
        recyclerView: androidx.recyclerview.widget.RecyclerView
    ) {
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        if (!adapter.isSelectable)
            adapter.withSelectable(true)
    }

    fun configureItemAdapter(
        adapter: FastItemAdapter<*>,
        recyclerView: androidx.recyclerview.widget.RecyclerView,
        layout: androidx.recyclerview.widget.LinearLayoutManager,
        scrollListener: CustomEndlessRecyclerViewScrollListener
    ) {
        recyclerView.layoutManager = layout
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(scrollListener)
        if (!adapter.isSelectable)
            adapter.withSelectable(true)
    }

    fun configureHorizontalItemAdapter(
        adapter: FastItemAdapter<*>,
        recyclerView: androidx.recyclerview.widget.RecyclerView
    ) {
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            activity,
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        if (!adapter.isSelectable)
            adapter.withSelectable(true)
    }

    fun configureGridItemAdapter(
        adapter: FastItemAdapter<*>,
        recyclerView: androidx.recyclerview.widget.RecyclerView,
        span: Int
    ) {
        recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, span)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        if (!adapter.isSelectable)
            adapter.withSelectable(true)
    }

    protected fun configureToolbarWithTitle(view: View, title: String) {
        configureEmptyToolbar(view)
        view.findViewById<AppCompatTextView>(R.id.titleToolbar).text = title
    }

    @Suppress("DEPRECATION")
    protected fun configureEmptyToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        val activities = (activity as AppCompatActivity)
        activities.setSupportActionBar(toolbar)
        Objects.requireNonNull(activities.supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        activities.supportActionBar?.setDisplayShowTitleEnabled(true)
        activities.supportActionBar?.setDisplayShowHomeEnabled(true)
        Objects.requireNonNull<Drawable>(toolbar.navigationIcon).setColorFilter(
            resources.getColor(R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    protected fun configureToolbarNoHome(view: View, title: String) {
        view.findViewById<AppCompatTextView>(R.id.titleToolbar).text = title
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        val activities = (activity as AppCompatActivity)
        activities.setSupportActionBar(toolbar)
        Objects.requireNonNull(activities.supportActionBar)?.setDisplayHomeAsUpEnabled(false)
        activities.supportActionBar?.setDisplayShowTitleEnabled(true)
        activities.supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    protected fun configureEasyImage() {
        EasyImage.configuration(activity)
            .setImagesFolderName("KlikAcaraApp")
            .saveInAppExternalFilesDir()
            .saveInRootPicturesDirectory()
    }

    protected fun removeEasyImageConfiguration() {
        EasyImage.clearConfiguration(activity)
    }

    @Throws(IOException::class)
    protected fun compressFile(file: File): File {
        return Compressor(activity)
            .setQuality(50)
            .compressToFile(file)
    }
}