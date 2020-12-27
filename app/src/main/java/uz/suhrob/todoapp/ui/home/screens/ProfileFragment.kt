package uz.suhrob.todoapp.ui.home.screens

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.Resource
import uz.suhrob.todoapp.databinding.FragmentProfileBinding
import uz.suhrob.todoapp.ui.auth.AuthActivity
import uz.suhrob.todoapp.ui.base.BaseFragment
import uz.suhrob.todoapp.ui.home.HomeViewModel
import uz.suhrob.todoapp.ui.home.dialogs.ImageChangeDialog
import uz.suhrob.todoapp.util.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel: HomeViewModel by activityViewModels()
    private var imageUri: Uri? = null
    private var isImageUploading = false

    @Inject
    lateinit var glide: RequestManager
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(binding.quickNotesToolbar)
        setHasOptionsMenu(true)
        binding.quickNotesToolbar.overflowIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_more_vert)
        lifecycleScope.launchWhenStarted {
            viewModel.userName.collect {
                binding.userName.text = it
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.userEmail.collect {
                binding.userEmail.text = it
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.userProfilePicture.collect {
                glide.load(it).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean = false

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.uploadImageProgress.visibility = View.GONE
                        isImageUploading = false
                        binding.uploadRefresh.visibility = View.GONE
                        return false
                    }
                }).into(binding.profileImage)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.createdTasksCount.collect {
                binding.createTasks.text = it.toString()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.completedTasksCount.collect {
                binding.completedTasks.text = it.toString()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.uploadPictureState.collect {
                when (it) {
                    is Resource.Success -> Unit
                    is Resource.Error -> {
                        binding.uploadImageProgress.visibility = View.GONE
                        toast(it.error)
                        isImageUploading = false
                        binding.uploadRefresh.visibility = View.VISIBLE
                    }
                    is Resource.Loading -> {
                        binding.uploadImageProgress.visibility = View.VISIBLE
                        isImageUploading = true
                        binding.uploadRefresh.visibility = View.GONE
                    }
                }
            }
        }
        binding.uploadRefresh.setOnClickListener {
            viewModel.retryUploadProfilePicture()
        }
        binding.profileImage.setOnClickListener {
            if (isImageUploading) {
                return@setOnClickListener
            }
            ImageChangeDialog(
                fromCameraListener = {
                    if (checkCameraPermission()) {
                        pickFromCamera()
                    } else {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            ), CAMERA_PERMISSION_REQUEST_CODE
                        )
                    }
                },
                fromStorageListener = {
                    if (checkStoragePermission()) {
                        pickFromStorage()
                    } else {
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION_REQUEST_CODE
                        )
                    }
                },
                removeListener = {
                    viewModel.removeProfilePicture()
                }
            ).show(childFragmentManager, "ProfileImageChangeDialog")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out_menu -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("Sign out")
                    .setMessage("Are you sure you want to sign out?")
                    .setPositiveButton("Sign out"
                    ) { _, _ ->
                        firebaseAuth.signOut()
                        viewModel.clearAllData().invokeOnCompletion {
                            startNewActivity(AuthActivity::class.java)
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                var cameraAccepted = true
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) cameraAccepted = false
                }
                if (cameraAccepted) {
                    pickFromCamera()
                } else {
                    toast("Camera permissions are necessary")
                }
            }
            STORAGE_PERMISSION_REQUEST_CODE -> {
                var storageAccepted = true
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) storageAccepted = false
                }
                if (storageAccepted) {
                    pickFromStorage()
                } else {
                    toast("Storage permissions are necessary")
                }
            }
        }
    }

    private fun pickFromCamera() {
        val cv = ContentValues()
        cv.put(MediaStore.Images.Media.TITLE, "Image title")
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Image description")
        imageUri =
            activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }.also {
            startActivityForResult(it, CAMERA_REQUEST_CODE)
        }
    }

    private fun pickFromStorage() {
        Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }.also {
            startActivityForResult(it, GALLERY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    glide.load(imageUri).into(binding.profileImage)
                    viewModel.uploadProfilePicture(compressImage(imageUri!!))
                }
                GALLERY_REQUEST_CODE -> {
                    imageUri = data?.data
                    glide.load(imageUri).into(binding.profileImage)
                    viewModel.uploadProfilePicture(compressImage(imageUri!!))
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun compressImage(imageUri: Uri): ByteArray {
        val inputStream: InputStream? =
            (activity as AppCompatActivity).contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        return out.toByteArray()
    }
}