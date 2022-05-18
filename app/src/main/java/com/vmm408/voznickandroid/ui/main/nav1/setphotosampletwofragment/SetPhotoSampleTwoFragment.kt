package com.vmm408.voznickandroid.ui.main.nav1.setphotosampletwofragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment
import com.vmm408.voznickandroid.ui.main.nav1.setphotosampletwofragment.ImageHelper.setImage
import com.vmm408.voznickandroid.ui.main.nav1.setphotosampletwofragment.ImageHelper.imagePickerBuilder
import com.vmm408.voznickandroid.ui.main.nav1.setphotosampletwofragment.ImageHelper.showAnimation
import kotlinx.android.synthetic.main.fragment_set_photo_into_view.*
import java.io.File

class SetPhotoSampleTwoFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_set_photo_into_view
    override val TAG = "SetPhotoSampleTwoFragment"

    private var imageFile: File? = null
    private var choosePhotoFrom = ChoosePhoto.GALLERY

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openCamera?.setCustomClickListener(ChoosePhoto.CAMERA)
        openGallery?.setCustomClickListener(ChoosePhoto.GALLERY)
    }

    private fun View.setCustomClickListener(choosePhoto: ChoosePhoto) {
        setOnClickListener {
            choosePhotoFrom = choosePhoto
            if (ImageHelper.permissionListApproved(context)) pickImage()
            else resultPermissionLauncher.launch(ImageHelper.permissionListToCheck)
        }
    }

    private fun pickImage() {
        imageLotti?.showAnimation(true)
        imagePickerBuilder(this.requireActivity(), choosePhotoFrom).createIntent {
            imagePickerLauncher.launch(it)
        }
    }

    /** Permission listener **/

    private val resultPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { list ->
        list?.forEach { if (!it.value) return@registerForActivityResult }
        pickImage()
    }

    /** Image listener **/

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> result.data?.data?.let { fileUri ->
                    imageFile = File(ImageHelper.getRealPathImageFromUri(context, fileUri) ?: "")
                    imageContainer?.setImage(imageFile) { imageLotti?.showAnimation(false) }
                }
                ImagePicker.RESULT_ERROR -> {
                    imageLotti?.showAnimation(false)
                    Toast.makeText(context, ImagePicker.getError(result.data), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    imageLotti?.showAnimation(false)
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
}