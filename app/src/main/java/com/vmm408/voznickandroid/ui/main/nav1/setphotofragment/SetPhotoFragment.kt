package com.vmm408.voznickandroid.ui.main.nav1.setphotofragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import com.bumptech.glide.Glide
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import com.vmm408.voznickandroid.R
import com.vmm408.voznickandroid.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_set_photo_into_view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

enum class ChoosePhoto { CAMERA, GALLERY }

class SetPhotoFragment : BaseFragment() {
    companion object {
        fun newInstance() = SetPhotoFragment()
    }

    override val layoutRes = R.layout.fragment_set_photo_into_view
    override val TAG = "SetPhotoFragment"

    private var choosePhotoFrom: ChoosePhoto? = null
    private var imageFile: File? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openCamera?.setOnClickListener {
            choosePhotoFrom = ChoosePhoto.CAMERA
            if (permissionListApproved()) openCamera()
            else requestPermissionLauncher.launch(permissionListToCheck)
        }
        openGallery?.setOnClickListener {
            choosePhotoFrom = ChoosePhoto.GALLERY
            if (permissionListApproved()) openGallery()
            else requestPermissionLauncher.launch(permissionListToCheck)
        }
    }


    /** Image listener **/

    private val imagePickerLauncher = registerImagePicker { images ->
        if (images.isNotEmpty()) {
            textFieldOne?.text = images[0].uri.toString()
            imageFile = File(context?.let { getRealPathImageFromUri(it, images[0].uri) } ?: "")
            Glide.with(this).load(imageFile).into(imageContainer)
        }
    }

    private fun openCamera() {
        choosePhotoFrom = null
        imagePickerLauncher.launch(
            ImagePickerConfig(
                isCameraOnly = true
            )
        )
    }

    private fun openGallery() {
        choosePhotoFrom = null
        imagePickerLauncher.launch(
            ImagePickerConfig(
                isMultipleMode = false,
                isShowCamera = false
            )
        )
    }

    /** Permissions **/

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { list ->
        list?.forEach { if (it.value == false) return@registerForActivityResult }

        when (choosePhotoFrom) {
            ChoosePhoto.CAMERA -> openCamera()
            ChoosePhoto.GALLERY -> openGallery()
        }
    }

    private fun permissionListApproved(): Boolean {
        permissionListToCheck.forEach { permission ->
            if (PackageManager.PERMISSION_GRANTED !=
                context?.let { checkSelfPermission(it, permission) }
            ) return false
        }
        return true
    }

    private val permissionListToCheck = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    /** Path to photo **/

    private fun getRealPathImageFromUri(context: Context, uri: Uri?): String? {
        val file = File(
            context.applicationInfo.dataDir +
                    File.separator +
                    System.currentTimeMillis()
        )

        textFieldTwo?.text = context.applicationInfo.dataDir
        textFieldThree?.text = file.absolutePath
        textFieldFour?.text = file.canonicalPath
        textFieldFive?.text = file.path
        try {
            val inputStream = context.contentResolver.openInputStream(uri!!) ?: return null
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream.close()
        } catch (ignore: IOException) {
            return null
        }
        return file.absolutePath
    }
}