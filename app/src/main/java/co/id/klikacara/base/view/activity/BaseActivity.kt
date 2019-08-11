package co.id.klikacara.base.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.base.contract.BaseContract
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.dialog.BaseJavaDialog
import co.id.klikacara.base.view.dialog.ProgressDialog
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import id.zelory.compressor.Compressor
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.io.IOException
import java.util.*


abstract class BaseActivity : AppCompatActivity(), BaseContract.View {

    var progressDialog: ProgressDialog? = null
    lateinit var infoDialog: BaseJavaDialog

    private var isProgressShown = false

    override fun setContentView(layout: Int) {
        super.setContentView(layout)
        ButterKnife.bind(this)
        initProgressDialog()
        initInfoDialog()
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog()
    }

    private fun initInfoDialog() {
        infoDialog = BaseJavaDialog()
        infoDialog.hideBtnCancel(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun showProgressDialog(message: String) {
        progressDialog?.title = message
        progressDialog?.show(supportFragmentManager)
    }

    override fun showProgressDialog() {
        if (!isProgressShown) {
            initProgressDialog()
            isProgressShown = true
            progressDialog?.show(supportFragmentManager, "PROGRESS_DIALOG")
        }
    }

    override fun dismissProgressDialog() {
        if (isProgressShown) {
            isProgressShown = false
            progressDialog?.dismissAllowingStateLoss()
        }
    }

    override fun showSuccess(message: String) {
        showSuccess(message, View.OnClickListener { infoDialog.dismissAllowingStateLoss() })
    }

    fun showSuccess(message: String, okListener: View.OnClickListener) {
        if (infoDialog.isAdded)
            initInfoDialog()
        infoDialog.hideBtnCancel(true)
        infoDialog.okClickListener = okListener
        infoDialog.description = message
        infoDialog.showAllowingStateLoss(supportFragmentManager, "TAG")
    }

    override fun showError(message: String) {
        showError(message, View.OnClickListener { infoDialog.dismissAllowingStateLoss() })
    }

    fun showError(message: String, okListener: View.OnClickListener) {
        if (infoDialog.isAdded)
            initInfoDialog()
        infoDialog.hideBtnCancel(true)
        infoDialog.okClickListener = okListener
        infoDialog.description = message
        infoDialog.showAllowingStateLoss(supportFragmentManager, "TAG")
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
        infoDialog.showAllowingStateLoss(supportFragmentManager, "TAG")
    }

    fun showInfoWithCancel(message: String, okListener: View.OnClickListener) {
        if (infoDialog.isAdded)
            initInfoDialog()
        infoDialog.okClickListener = okListener
        infoDialog.hideBtnCancel(false)
        infoDialog.description = message
        infoDialog.showAllowingStateLoss(supportFragmentManager, "TAG")
    }

    fun showActivityAndFinishCurent(intent: Intent) {
        showActivity(intent)
        finish()
    }

    fun showActivityAndFinishAllActivity(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        showActivityAndFinishCurent(intent)
    }

    fun showActivity(intent: Intent) {
        startActivity(intent)
    }

    fun getIntent(context: Context, cls: Class<*>): Intent {
        return Intent(context, cls)
    }

    fun configureItemAdapter(
        adapter: FastItemAdapter<*>,
        recyclerView: androidx.recyclerview.widget.RecyclerView
    ) {
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.isFocusable = false
        if (!adapter.isSelectable)
            adapter.withSelectable(true)
    }

    fun configureHorizontalItemAdapter(
        adapter: FastItemAdapter<*>,
        recyclerView: androidx.recyclerview.widget.RecyclerView
    ) {
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.isFocusable = false
        if (!adapter.isSelectable)
            adapter.withSelectable(true)
    }

    protected fun configureToolbarWithHomeAndTitle(title: String) {
        configureToolbarWithHomeAndTitle(findViewById(R.id.toolbar), title)
    }

    protected fun configureToolbarNoHomeAndTitle(title: String) {
        findViewById<AppCompatTextView>(R.id.titleToolbar).text = title
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    @Suppress("DEPRECATION")
    protected fun configureToolbarWithHomeAndTitle(toolbar: Toolbar, title: String?) {
        findViewById<AppCompatTextView>(R.id.titleToolbar).text = title
        toolbar.title = ""
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        Objects.requireNonNull<Drawable>(toolbar.navigationIcon).setColorFilter(
            resources.getColor(R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    @Suppress("DEPRECATION")
    protected fun configureToolbarEmpty() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        this.setSupportActionBar(toolbar)
        Objects.requireNonNull(this.supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setDisplayShowTitleEnabled(true)
        this.supportActionBar?.setDisplayShowHomeEnabled(true)
        Objects.requireNonNull<Drawable>(toolbar.navigationIcon).setColorFilter(
            resources.getColor(R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    @Suppress("DEPRECATION")
    protected fun configureToolbarEmptyNoHome() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        this.setSupportActionBar(toolbar)
        Objects.requireNonNull(this.supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setDisplayShowTitleEnabled(false)
        this.supportActionBar?.setDisplayShowHomeEnabled(false)
        Objects.requireNonNull<Drawable>(toolbar.navigationIcon).setColorFilter(
            resources.getColor(R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    protected fun configureEasyImage() {
        EasyImage.configuration(this)
            .setImagesFolderName(BuildConfig.APPLICATION_ID)
            .saveInAppExternalFilesDir()
            .saveInRootPicturesDirectory()
    }

    protected fun removeEasyImageConfiguration() {
        EasyImage.clearConfiguration(this)
    }

    @Throws(IOException::class)
    protected fun compressFile(file: File): File {
        return Compressor(this)
            .setQuality(50)
            .compressToFile(file)
    }

    protected fun configureBackButton() {
        val view = findViewById<AppCompatImageView>(R.id.backToolbarButton)
        ViewHelper.showView(view)
        view.setOnClickListener { onBackPressed() }
    }
}