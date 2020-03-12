package co.id.klikacara.createevent.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.authentication.view.AuthenticationActivity
import co.id.klikacara.base.utils.imagehelper.GlideUtils
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.base.utils.timehelper.TimeUtils
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.base.view.activity.KlikWeb
import co.id.klikacara.base.view.adapter.ViewPagerAdapter
import co.id.klikacara.createevent.contract.EventContract
import co.id.klikacara.createevent.presenter.EventDetailPresenter
import co.id.klikacara.main.view.BannerFragment
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_detail_event.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels
import javax.inject.Inject

class DetailEventActivity : BaseActivity(), EventContract.DetailEventView {

    @Inject
    lateinit var detailEventPresenter: EventDetailPresenter

    private lateinit var event: Order
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)
        configureImagePopUp()
        event = Parcels.unwrap(intent.extras?.getParcelable(BuildConfig.orderDb))
        detailEventPresenter.getUserData()
        configureViewPager()
        showDetailEvent()
    }

    private fun configureViewPager() {
        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        bannerPager.adapter = pagerAdapter
        tabDots.setupWithViewPager(bannerPager, true)
    }

    private fun showDetailEvent() {
//        pagerAdapter.clearFragment()
//        pagerAdapter.addFragment(BannerFragment.newInstance(event.posterUrl), "")
//        pagerAdapter.notifyDataSetChanged()
//        ViewHelper.showView(areaBanner)
        imagePopup.initiatePopupWithPicasso(event.posterUrl)
        GlideUtils.setFotoWithUrl(this, event.posterUrl, posterAcaraImageView)
        ViewHelper.hideView(bannerLoading)

        namaAcaraTextView.text = event.name
        tanggalAcaraTextView.text = if (!TimeUtils.isSameDate(
                event.startDate,
                event.endDate
            )
        ) StringHelper.getStringBuilderToString(
            TimeUtils.getDateFormated("dd MMMM yyyy", event.startDate),
            " - ",
            TimeUtils.getDateFormated("dd MMMM yyyy", event.endDate)
        ) else TimeUtils.getDateFormated("dd MMMM yyyy", event.endDate)
        deskripsiAcaraTextView.text = event.deskripsi.replace("\\n", "\n")
        lokasiAcaraTextView.text = StringHelper.getStringBuilderToString(
            event.address, ", ",
            event.district?.description, ", ",
            event.city?.description, ", ",
            event.province?.description
        )
        waktuAcaraTextView.text = StringHelper.getStringBuilderToString(
            TimeUtils.getDateFormated("dd MMMM yyyy", event.startDate),
            " pukul ", TimeUtils.getDateFormated("HH:mm", event.startDate),
            "\ns/d\n",
            TimeUtils.getDateFormated("dd MMMM yyyy", event.endDate),
            " pukul ", TimeUtils.getDateFormated("HH:mm", event.endDate)
        )
        hargaTiketAcaraTextView.text =
            if (event.tiketPrice == 0L) "Gratis"
            else StringHelper.getPriceInRp(event.tiketPrice)
    }

    override fun setUser(user: User) {
        if (user.uid == event.uid)
            ViewHelper.showView(viewParticipantButton)
        else
            ViewHelper.hideView(viewParticipantButton)
    }

    @OnClick(R.id.joinEventButton)
    fun onJoinEventButtonClicked() {
        detailEventPresenter.checkLoginStatus()
    }

    @OnClick(R.id.viewParticipantButton)
    fun onParticipantButtonClicked() {
        val intent = getIntent(this, ViewParticipantActivity::class.java)
        intent.putExtra(BuildConfig.eventDb, Parcels.wrap(event))
        showActivity(intent)
    }

    override fun showLoginDialog() {
        showInfoWithCancel(
            "Anda harus login terlebih dahulu untuk mendaftar Acara",
            View.OnClickListener {
                showActivity(getIntent(this, AuthenticationActivity::class.java))
            })
    }

    override fun showConfirmationDialog() {
        showInfoWithCancel("Yakin ingin mendaftar di Acara ini?", View.OnClickListener {
            infoDialog.dismissDialog()
            detailEventPresenter.checkIsUserJoin(event)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && data != null) {
            detailEventPresenter.doJoin(event)
        }
    }

    override fun doOnUserAlreadyJoined() {
        showInfo("Anda telah terdaftar sebagai peserta acara")
    }

    override fun doOnUserNotJoined() {
        if (StringUtils.isNotBlank(event.linkAcara)) {
            val intentToWeb = getIntent(this, KlikWeb::class.java)
            intentToWeb.putExtra(KlikWeb.URL, event.linkAcara)
            startActivityForResult(intentToWeb, 0)
        } else {
            detailEventPresenter.doJoin(event)
        }
    }

    override fun doOnJoinEventSuccess() {
        showInfo("Anda berhasil mendaftar Acara")
    }

    @OnClick(R.id.posterAcaraImageView)
    fun posterAcaraImageViewClicked() {
        if (imagePopup.isImageIsSet)
            imagePopup.viewPopup()
    }
}