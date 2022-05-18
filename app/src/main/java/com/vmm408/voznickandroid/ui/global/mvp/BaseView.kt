package com.vmm408.voznickandroid.ui.global.mvp

import com.vmm408.voznickandroid.network.response.Base
import com.vmm408.voznickandroid.ui.global.API_INTERNET_ERROR

interface BaseView {
    fun apiError(
        error: String = API_INTERNET_ERROR,
        apiException: Base? = null,
        function: (() -> Unit)?
    ) {
    }

    fun startLoading() {}
    fun stopLoading() {}
}