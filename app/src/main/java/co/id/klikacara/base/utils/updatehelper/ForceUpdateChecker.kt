package co.id.klikacara.base.utils.updatehelper

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.NonNull
import co.id.klikacara.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.apache.commons.lang3.StringUtils


class ForceUpdateChecker(
    @param:NonNull private val context: Context,
    private val onUpdateNeededListener: OnUpdateNeededListener?
) {

    interface OnUpdateNeededListener {
        fun onUpdateNeeded(updateUrl: String)
    }

    fun check() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        val currentVersion = remoteConfig.getString(BuildConfig.currentVersionKey)
        val appVersion = getAppVersion(context)
        val updateUrl = remoteConfig.getString(BuildConfig.playStoreUrlKey)

        if (appVersion.replace(".", "") < currentVersion.replace(".", "") && onUpdateNeededListener != null) {
            onUpdateNeededListener.onUpdateNeeded(updateUrl)
        }
    }

    private fun getAppVersion(context: Context): String {
        var result = ""

        try {
            result = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0)
                .versionName
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return result
    }

    class Builder(private val context: Context) {
        private var onUpdateNeededListener: OnUpdateNeededListener? = null

        fun onUpdateNeeded(onUpdateNeededListener: OnUpdateNeededListener): Builder {
            this.onUpdateNeededListener = onUpdateNeededListener
            return this
        }

        fun build(): ForceUpdateChecker {
            return ForceUpdateChecker(context, onUpdateNeededListener)
        }

        fun check(): ForceUpdateChecker {
            val forceUpdateChecker = build()
            forceUpdateChecker.check()

            return forceUpdateChecker
        }
    }

    companion object {

        fun with(@NonNull context: Context): Builder {
            return Builder(context)
        }
    }
}