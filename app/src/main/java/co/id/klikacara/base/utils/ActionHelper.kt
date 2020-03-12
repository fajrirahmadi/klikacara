package co.id.klikacara.base.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import co.id.klikacara.`object`.constanta.DefaultConstanta

class ActionHelper {

    companion object {
        fun openWa(context: Context, number: String, text: String) {
            val smsNumber = number
                .replaceFirst("0", DefaultConstanta.TAG_PREFIX_PHONE_NUMBER)
                .replace("+", "")
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("http://api.whatsapp.com/send?phone=$smsNumber&text=$text")
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}