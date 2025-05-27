package tk.ssau.fitnesstko.profile
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import tk.ssau.fitnesstko.AddWeightFragment
import tk.ssau.fitnesstko.AuthActivity
import tk.ssau.fitnesstko.AuthManager
import tk.ssau.fitnesstko.MainActivity
import tk.ssau.fitnesstko.PreferencesManager
import tk.ssau.fitnesstko.R
import tk.ssau.fitnesstko.databinding.ProfileBinding
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class FragmentProfile : Fragment(R.layout.profile) {
    private var _binding: ProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: PreferencesManager
    private lateinit var requestPermission: ActivityResultLauncher<String>
    private lateinit var pickImage: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ProfileBinding.bind(view)
        prefs = (activity as? MainActivity)?.prefs
            ?: throw IllegalStateException("PreferencesManager not available")

        try {
            registerActivityResultLaunchers()
            initViews()
            loadUserData()
            loadAvatar()
        } catch (e: Exception) {
            Log.e("ProfileFrag", "Error initializing profile view", e)
        }
    }

    private fun registerActivityResultLaunchers() {
        requestPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) pickImage.launch(arrayOf("image/*"))
        }

        pickImage = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri?.let { handleImageSelection(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            loadUserData()
            loadAvatar()
        } catch (e: Exception) {
            Log.e("ProfileFrag", "Error in onResume", e)
        }
    }

    private fun initViews() {
        binding.btnProfileChangeFIO.setOnClickListener {
            safeFragmentTransaction { EditProfileFragment() }
        }

        binding.btnProfileAddWeighing.setOnClickListener {
            safeFragmentTransaction { AddWeightFragment() }
        }

        binding.btnProfileChangeAvatar.setOnClickListener {
            checkPermissionsAndOpenPicker()
        }

        binding.btnProfileResultKcal.setOnClickListener {
            safeFragmentTransaction { CalculateCaloriesFragment() }
        }

        binding.btnLogout.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogout() {
        try {
            AuthManager(requireContext()).logout()
            prefs.clearUserData()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            activity?.finish()
        } catch (e: Exception) {
            Log.e("ProfileFrag", "Logout failed", e)
        }
    }

    private fun checkPermissionsAndOpenPicker() {
        try {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> requestPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                else -> pickImage.launch(arrayOf("image/*"))
            }
        } catch (e: Exception) {
            Log.e("ProfileFrag", "Error requesting permissions", e)
        }
    }

    private fun handleImageSelection(uri: Uri) {
        try {
            requireContext().contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            prefs.saveAvatarUri(uri.toString())
            loadAvatar()
        } catch (e: SecurityException) {
            Log.e("ProfileFrag", "SecurityException on image selection", e)
        } catch (e: Exception) {
            Log.e("ProfileFrag", "Image loading error", e)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadUserData() {
        try {
            val firstName = prefs.getString("firstName", "")
            val lastName = prefs.getString("lastName", "")
            val birthDate = prefs.getString("birthDay", "")
            binding.fio.text = "$firstName $lastName"
            binding.age.text = calculateAge(birthDate)
        } catch (e: Exception) {
            Log.e("ProfileFrag", "Error loading user data", e)
        }
    }

    private fun loadAvatar() {
        prefs.getAvatarUri()?.let { uriString ->
            try {
                val uri = uriString.toUri()
                if (isUriValid(uri)) {
                    Glide.with(this)
                        .load(uri)
                        .circleCrop()
                        .into(binding.imageAvatar)
                    return
                }
            } catch (e: Exception) {
                Log.e("ProfileFrag", "Invalid avatar URI", e)
            }
        }
        binding.imageAvatar.setImageResource(R.drawable.cat)
    }

    private fun isUriValid(uri: Uri): Boolean {
        return try {
            requireContext().contentResolver.persistedUriPermissions
                .any { it.uri == uri }
        } catch (e: Exception) {
            false
        }
    }

    private fun calculateAge(birthDate: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(birthDate, formatter)
            val age = Period.between(date, LocalDate.now()).years
            "$age"
        } catch (e: Exception) {
            Log.e("ProfileFrag", "Age parse error", e)
        }.toString()
    }

    private fun safeFragmentTransaction(createFragment: () -> Fragment) {
        try {
            val fragment = createFragment()
            (activity as? MainActivity)?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.flFragment, fragment)
                ?.addToBackStack(null)
                ?.commit()
        } catch (e: Exception) {
            Log.e("ProfileFrag", "Fragment transaction failed", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
