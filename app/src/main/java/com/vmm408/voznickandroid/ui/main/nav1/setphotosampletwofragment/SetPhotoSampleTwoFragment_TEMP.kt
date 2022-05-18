package com.vmm408.voznickandroid.ui.main.nav1.setphotosampletwofragment
//
//import android.Manifest
//import android.app.Activity
//import android.content.Context
//import android.content.pm.PackageManager
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.view.View
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.core.content.ContextCompat.checkSelfPermission
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.DataSource
//import com.bumptech.glide.load.engine.GlideException
//import com.bumptech.glide.request.RequestListener
//import com.bumptech.glide.request.target.Target
//import com.github.dhaval2404.imagepicker.ImagePicker
//import com.makeramen.roundedimageview.RoundedImageView
//import com.vmm408.voznickandroid.R
//import com.vmm408.voznickandroid.ui.global.BaseFragment
//import kotlinx.android.synthetic.main.fragment_set_photo_into_view.*
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//import java.io.OutputStream
//
//enum class ChoosePhoto { CAMERA, GALLERY }
//
//class SetPhotoSampleTwoFragment : BaseFragment() {
//    override val layoutRes = R.layout.fragment_set_photo_into_view
//    override val TAG = "SetPhotoSampleTwoFragment"
//
//    private var choosePhotoFrom: ChoosePhoto? = null
//    private var imageFile: File? = null
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        openCamera?.setOnClickListener {
//            choosePhotoFrom = ChoosePhoto.CAMERA
//            if (permissionListApproved()) openCamera()
//            else requestPermissionLauncher.launch(permissionListToCheck)
//        }
//        openGallery?.setOnClickListener {
//            choosePhotoFrom = ChoosePhoto.GALLERY
//            if (permissionListApproved()) openGallery()
//            else requestPermissionLauncher.launch(permissionListToCheck)
//        }
//    }
//
//
//    /** Image listener **/
//
//    private val imagePickerLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            val data = result.data
//
//            when (result.resultCode) {
//                Activity.RESULT_OK -> {
//                    val fileUri = data?.data!!
//                    imageFile = File(context?.let { getRealPathImageFromUri(it, fileUri) } ?: "")
//                    loadImageIntoView()
//
//                }
//                ImagePicker.RESULT_ERROR -> {
//                    showLotti(false)
//                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//                }
//                else -> {
//                    showLotti(false)
//                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//    private fun loadImageIntoView() {
//        Glide.with(this)
//            .load(imageFile)
//            .listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    showLotti(false)
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    showLotti(false)
//                    return false
//                }
//            })
//            .into(imageContainer)
//    }
//
//    private fun openCamera() {
//        choosePhotoFrom = null
//        ImagePicker.with(this)
//            .cameraOnly()
//            .compress(1024)         //Final image size will be less than 1 MB(Optional)
//            .maxResultSize(
//                1080,
//                1080
//            )  //Final image resolution will be less than 1080 x 1080(Optional)
//            .saveDir(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
//            .createIntent { intent ->
//                imagePickerLauncher.launch(intent)
//                showLotti(true)
//            }
//    }
//
//    private fun openGallery() {
//        choosePhotoFrom = null
//        ImagePicker.with(this)
//            .galleryOnly()
//            .compress(1024)         //Final image size will be less than 1 MB(Optional)
//            .maxResultSize(
//                1080,
//                1080
//            )  //Final image resolution will be less than 1080 x 1080(Optional)
//            .galleryMimeTypes(  //Exclude gif images
//                mimeTypes = arrayOf(
//                    "image/png",
//                    "image/jpg",
//                    "image/jpeg"
//                )
//            )
//            .createIntent { intent ->
//                imagePickerLauncher.launch(intent)
//                showLotti(true)
//            }
//    }
//
//
//    /** Permissions **/
//
//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { list ->
//        list?.forEach { if (it.value == false) return@registerForActivityResult }
//
//        when (choosePhotoFrom) {
//            ChoosePhoto.CAMERA -> openCamera()
//            ChoosePhoto.GALLERY -> openGallery()
//        }
//    }
//
//    private fun permissionListApproved(): Boolean {
//        permissionListToCheck.forEach { permission ->
//            if (PackageManager.PERMISSION_GRANTED !=
//                context?.let { checkSelfPermission(it, permission) }
//            ) return false
//        }
//        return true
//    }
//
//    private val permissionListToCheck = arrayOf(
//        Manifest.permission.CAMERA,
//        Manifest.permission.READ_EXTERNAL_STORAGE
//    )
//
//
//    /** Path to photo **/
//
//    private fun getRealPathImageFromUri(context: Context, uri: Uri?): String? {
//        val file = File(
//            context.applicationInfo.dataDir +
//                    File.separator +
//                    System.currentTimeMillis()
//        )
//
//        textFieldTwo?.text = context.applicationInfo.dataDir
//        textFieldThree?.text = file.absolutePath
//        textFieldFour?.text = file.canonicalPath
//        textFieldFive?.text = file.path
//        try {
//            val inputStream = context.contentResolver.openInputStream(uri!!) ?: return null
//            val outputStream: OutputStream = FileOutputStream(file)
//
//            val buf = ByteArray(1024)
//            var len: Int
//
//            while (inputStream.read(buf).also { len = it } > 0)
//                outputStream.write(buf, 0, len)
//
//            outputStream.close()
//            inputStream.close()
//        } catch (ignore: IOException) {
//            return null
//        }
//        return file.absolutePath
//    }
//
//
//    /** Show lotti **/
//
//    private fun showLotti(isShow: Boolean) {
//        if (isShow) {
//            imageLotti?.playAnimation()
//            imageLotti?.visibility = View.VISIBLE
////            imageLotti?.addValueCallback(
////                KeyPath("**"), LottieProperty.COLOR_FILTER
////            ) {
////                PorterDuffColorFilter(
////
////                    imageContainer.background,
////                    PorterDuff.Mode.SRC_ATOP
////                )
////            }
//        } else {
//            imageLotti?.cancelAnimation()
//            imageLotti?.visibility = View.GONE
//        }
//    }
//}