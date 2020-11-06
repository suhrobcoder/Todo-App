package uz.suhrob.todoapp.ui.home

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.Resource
import uz.suhrob.todoapp.databinding.FragmentProfileBinding
import uz.suhrob.todoapp.ui.auth.AuthActivity
import uz.suhrob.todoapp.ui.base.BaseFragment
import uz.suhrob.todoapp.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel: HomeViewModel by activityViewModels()
    private var imageUri: Uri? = null
    private var isImageUploading = false

    @Inject
    lateinit var glide: RequestManager
    @Inject lateinit var firebaseAuth: FirebaseAuth

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(binding.quickNotesToolbar)
        setHasOptionsMenu(true)
        viewModel.userName.observe(viewLifecycleOwner) {
            binding.userName.text = it
        }
        viewModel.userEmail.observe(viewLifecycleOwner) {
            binding.userEmail.text = it
        }
        viewModel.userProfilePicture.observe(viewLifecycleOwner) {
            glide.load(it).into(binding.profileImage)
        }
        viewModel.createdTasksCount.observe(viewLifecycleOwner) {
            binding.createTasks.text = it.toString()
        }
        viewModel.completedTasksCount.observe(viewLifecycleOwner) {
            binding.completedTasks.text = it.toString()
        }
        viewModel.uploadPictureState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.uploadImageProgress.visibility = View.GONE
                    isImageUploading = false
                    binding.uploadRefresh.visibility = View.GONE
                }
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
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA
                            ), CAMERA_PERMISSION_REQUEST_CODE
                        )
                    }
                },
                fromStorageListener = {
                    if (checkStoragePermission()) {
                        pickFromStorage()
                    } else {
                        requestPermissions(
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
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
                firebaseAuth.signOut()
                startNewActivity(AuthActivity::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
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
                    binding.profileImage.setImageURI(imageUri)
                    viewModel.uploadProfilePicture(imageUri!!)
                }
                GALLERY_REQUEST_CODE -> {
                    imageUri = data?.data
                    binding.profileImage.setImageURI(imageUri)
                    viewModel.uploadProfilePicture(imageUri!!)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}