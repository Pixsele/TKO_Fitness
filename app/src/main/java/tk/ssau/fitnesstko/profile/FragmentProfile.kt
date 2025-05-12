package tk.ssau.fitnesstko.profile

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

    private lateinit var binding: ProfileBinding
    private lateinit var prefs: PreferencesManager

    private lateinit var requestPermission: ActivityResultLauncher<String>
    private lateinit var pickImage: ActivityResultLauncher<Array<String>>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileBinding.bind(view)
        prefs = (activity as MainActivity).prefs

        registerActivityResultLaunchers()

        initViews()
        loadUserData()
        loadAvatar()
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
        loadUserData()
        loadAvatar()
        binding.btnProfileAddWeighing.text = "Добавить взвешивание"
    }

    private fun initViews() {
        binding.btnProfileChangeFIO.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, EditProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnProfileAddWeighing.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, AddWeightFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnProfileChangeAvatar.setOnClickListener {
            checkPermissionsAndOpenPicker()
        }
        binding.btnProfileResultKcal.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, CalculateCaloriesFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.btnLogout.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogout() {
        AuthManager(requireContext()).logout()
        PreferencesManager(requireContext()).clearUserData()

        startActivity(Intent(requireContext(), AuthActivity::class.java))
        activity?.finish()
    }

    private fun checkPermissionsAndOpenPicker() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                requestPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            else -> pickImage.launch(arrayOf("image/*"))
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
            showError("Нет прав доступа к файлу")
        } catch (e: Exception) {
            showError("Ошибка загрузки изображения")
        }
    }

    private fun loadUserData() {
        val firstName = prefs.getString("firstName", "")
        val lastName = prefs.getString("lastName", "")
        val birthDate = prefs.getString("birthDay", "")

        binding.fio.text =
            "$firstName $lastName"

        binding.age.text = calculateAge(birthDate)
    }

    private fun loadAvatar() {
        prefs.getAvatarUri()?.let { uriString ->
            try {
                val uri = Uri.parse(uriString)

                if (isUriValid(uri)) {
                    Glide.with(this)
                        .load(uri)
                        .circleCrop()
                        .into(binding.imageAvatar)
                    return
                }
            } catch (_: Exception) {
            }
        }

        binding.imageAvatar.setImageResource(R.drawable.cat)
    }

    private fun isUriValid(uri: Uri): Boolean {
        return try {
            val permissions = requireContext().contentResolver
                .getPersistedUriPermissions()

            permissions.any { it.uri == uri }
        } catch (e: Exception) {
            false
        }
    }

    private fun showError(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun calculateAge(birthDate: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val birthDate = LocalDate.parse(birthDate, formatter)
            val currentDate = LocalDate.now()
            val age = Period.between(birthDate, currentDate).years
            "$age лет"
        } catch (e: Exception) {
            "Возраст не указан"
        }
    }
}