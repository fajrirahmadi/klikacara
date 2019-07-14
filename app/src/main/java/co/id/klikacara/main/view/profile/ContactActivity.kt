package co.id.klikacara.main.view.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import butterknife.BindString
import butterknife.OnClick
import co.id.klikacara.R
import co.id.klikacara.`object`.constanta.DefaultConstanta
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.base.view.activity.BaseActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : BaseActivity() {

    @BindString(R.string.value_phone)
    lateinit var valuePhone: String
    @BindString(R.string.value_whatsapp)
    lateinit var valueWhatsApp: String
    @BindString(R.string.value_email)
    lateinit var valueEmail: String

    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    lateinit var phone: String
    lateinit var email: String
    lateinit var wa: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        initValueContact()
    }

    private fun initValueContact() {
        phone = remoteConfig.getString(DefaultConstanta.PHONE_CS_KEY)
        wa = remoteConfig.getString(DefaultConstanta.WA_CS_KEY)
        email = remoteConfig.getString(DefaultConstanta.EMAIL_CS_KEY)
        valuePhoneNumberTextView.text = valuePhone.replace("$", phone)
        whatsappTextView.text = valueWhatsApp.replace("$", wa)
        emailtextView.text = valueEmail.replace("$", email)
    }

    @OnClick(R.id.areaPhone)
    fun onPhoneTextView() {
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse(StringHelper.getStringBuilderToString("tel:", phone)))
        startActivity(callIntent)
    }

    @OnClick(R.id.areaWa)
    fun onWhatsAppTextView() {
        val smsNumber = wa
            .replaceFirst("0", DefaultConstanta.TAG_PREFIX_PHONE_NUMBER)
            .replace("+", "")
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://api.whatsapp.com/send?phone=$smsNumber")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @OnClick(R.id.areaEmail)
    fun onEmailTextView() {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "plain/text"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        try {
            startActivity(i)
        } catch (ex: android.content.ActivityNotFoundException) {
            showInfo("Anda belum memiliki aplikasi email")
        }

    }
}