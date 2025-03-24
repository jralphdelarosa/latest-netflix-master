package com.example.myfirstandroidtvapp.data.remote.dto

import android.telephony.SubscriptionPlan
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by John Ralph Dela Rosa on 3/24/2025.
 */
data class AppContentPlanResponse(
    @Expose @SerializedName("plans") var plans: List<SubscriptionPlan> = emptyList(),
    @Expose @SerializedName("credentials") var credentials: CredentialResponse? = null
) {
    override fun toString() = "credentials = $credentials \n plans = " + plans.joinToString(",")
}