package co.id.klikacara.order.view

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.Rating
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.main.view.MainActivity
import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.presenter.RatingPresenter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_ulasan.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels
import javax.inject.Inject

class RatingActivity : BaseActivity(), OrderContract.RatingView {

    @Inject
    lateinit var ratingPresenter: RatingPresenter

    lateinit var order: Order
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ulasan)
        ratingPresenter.getProfile()
        order = Parcels.unwrap<Order>(intent?.extras?.getParcelable(BuildConfig.orderDb))
        configureRatingBarListener()
    }

    private fun configureRatingBarListener() {
        ulasanRatingBar.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    val touchPositionX = event.x
                    val width = ulasanRatingBar.width
                    val stars = (touchPositionX / width * 5.0f)
                    val star = stars.toInt() + 1
                    ulasanRatingBar.rating = star.toFloat()
                    v.isPressed = false
                }
                MotionEvent.ACTION_DOWN -> v.isPressed = true
                MotionEvent.ACTION_CANCEL -> v.isPressed = false
            }
            true
        }
    }

    @OnClick(R.id.submitButton)
    fun onSubmitButtonClicked() {
        if (ulasanRatingBar.rating == 0f)
            showInfo("Anda belum menambahkan rating")
        else if (StringUtils.isBlank(ulasanEditText.text.toString().trim()))
            showInfo(("Anda belum memberikan ulasan"))
        else {
            val rating = Rating()
            rating.nama = user.name
            rating.url = user.url
            rating.rating = ulasanRatingBar.rating
            rating.ulasan = ulasanEditText.text.toString().trim()
            ratingPresenter.submitRating(rating, order)
        }
    }

    override fun doOnSubmitRatingSuccess() {
        showInfo("Terimakasih terlah memberikan penilaian kepada mitra kami", View.OnClickListener {
            showActivityAndFinishAllActivity(getIntent(this, MainActivity::class.java))
        })
    }

    override fun doOnSubmitRatingFailed() {
        showInfo("Maaf, penilaian Anda belum berhasil dilakukan. Cobalah beberapa saat lagi.")
    }

    override fun setUserData(user: User) {
        this.user = user
    }
}