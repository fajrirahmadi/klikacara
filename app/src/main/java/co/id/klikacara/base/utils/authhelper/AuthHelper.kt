package co.id.klikacara.base.utils.authhelper

import android.content.Context
import co.id.klikacara.BuildConfig
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth

class AuthHelper(val context: Context) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    var gso: GoogleSignInOptions? = null
    var googleApiClient: GoogleApiClient? = null

    init {
        initSignInOption()
    }

    private fun initSignInOption() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.gmailKey)
            .requestEmail()
            .build()
        if (gso != null)
            googleApiClient = GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso!!)
                .build()
        googleApiClient?.connect()
    }

    fun signOut() {
        if (googleApiClient != null)
            Auth.GoogleSignInApi.signOut(googleApiClient)
        firebaseAuth.signOut()
    }

}