package com.vmm408.voznickandroid.network.response

import com.google.gson.annotations.SerializedName

open class Base {
    @SerializedName("status_code")
    var status: Int? = 0

    @SerializedName("code")
    var code: Int? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("title")
    var title: String? = null
}
