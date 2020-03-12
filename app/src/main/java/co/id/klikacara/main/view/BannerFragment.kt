package co.id.klikacara.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.R
import co.id.klikacara.base.utils.imagehelper.GlideUtils
import co.id.klikacara.base.view.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_banner.*

class BannerFragment : BaseFragment() {

    companion object {

        const val bannerUrl = "BANNER_URL"

        @JvmStatic
        fun newInstance(orderStatus: String) = BannerFragment().apply {
            arguments = Bundle().apply {
                putString(bannerUrl, orderStatus)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getInflate(inflater, R.layout.fragment_banner, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureImagePopUp()
        arguments?.getString(bannerUrl, "")?.let {
            GlideUtils.setFotoRoundedWithUrl(activity!!, it, bannerImageView)
            imagePopup.initiatePopupWithPicasso(it)
        }
    }

    @OnClick(R.id.bannerImageView)
    fun bannerImageViewClicked() {
    }
}