package tk.ssau.fitnesstko

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import tk.ssau.fitnesstko.databinding.ProfileBinding
import android.Manifest
import androidx.activity.result.ActivityResultLauncher

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
        loadAvatar() // Обновляем при каждом возврате
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
            // Сохраняем права доступа
            requireContext().contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            // Сохраняем URI
            prefs.saveAvatarUri(uri.toString())

            // Обновляем UI
            loadAvatar()
        } catch (e: SecurityException) {
            showError("Нет прав доступа к файлу")
        } catch (e: Exception) {
            showError("Ошибка загрузки изображения")
        }
    }

    private fun loadUserData() {
        val name = prefs.getString("user_name", "")
        val birthDay = prefs.getString("birthDay", "")

        binding.fio.text = name.ifEmpty { "Имя Фамилия" }
        binding.age.text = calculateAge(birthDay)
    }

    private fun loadAvatar() {
        prefs.getAvatarUri()?.let { uriString ->
            try {
                val uri = Uri.parse(uriString)

                // Проверка действительности URI
                if (isUriValid(uri)) {
                    Glide.with(this)
                        .load(uri)
                        .circleCrop()
                        .into(binding.imageAvatar)
                    return
                }
            } catch (e: Exception) {
                // Ошибка парсинга
            }
        }

        // Установка изображения по умолчанию
        binding.imageAvatar.setImageResource(R.drawable.cat)
    }

    private fun isUriValid(uri: Uri): Boolean {
        return try {
            // Проверка сохраненных прав
            val permissions = requireContext().contentResolver
                .getPersistedUriPermissions()

            permissions.any { it.uri == uri }
        } catch (e: Exception) {
            false
        }
    }

    private fun calculateAge(birthDate: String): String {
        return try {
            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val birthYear = dateFormat.parse(birthDate)?.year ?: return ""
            val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            "${currentYear - birthYear} лет"
        } catch (_: Exception) {
            "Возраст"
        }
    }

    private fun showError(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}