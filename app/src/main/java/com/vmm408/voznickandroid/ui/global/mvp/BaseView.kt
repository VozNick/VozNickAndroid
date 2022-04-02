package com.vmm408.voznickandroid.ui.global.mvp

import com.vmm408.voznickandroid.network.response.Base
import com.vmm408.voznickandroid.ui.global.API_INTERNET_ERROR

interface BaseView {
    fun apiError(
        error: String = API_INTERNET_ERROR,
        function: (() -> Unit)?,
        apiException: Base? = null
    ) {
    }

    fun startLoading() {}
    fun stopLoading() {}
}