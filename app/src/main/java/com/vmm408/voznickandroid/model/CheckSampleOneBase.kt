package com.vmm408.voznickandroid.model

data class CheckSampleOne(
    var id: Int = 0,
    var name: String? = null,
    var checkList: ArrayList<SubCheckSampleOne>? = null
)

data class SubCheckSampleOne(
    var id: Int = 0,
    var name: String? = null,
    var isChecked: Boolean = false
)

fun fillListWithData() = ArrayList<CheckSampleOne>().apply {
    for (i in 0 until 20) {
        add(
            CheckSampleOne(
                id = i.plus(1).times(100),
                name = "Check_$i",
                checkList = fillSubList(),
            )
        )
    }
}

fun fillSubList() = ArrayList<SubCheckSampleOne>().apply {
    for (j in 0 until 10) {
        add(
            SubCheckSampleOne(
                id = j.plus(1).times(1000),
                name = "Sub check $j",
            )
        )
    }
}