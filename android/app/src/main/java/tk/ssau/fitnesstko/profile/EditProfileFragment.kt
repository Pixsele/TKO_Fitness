package tk.ssau.fitnesstko.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.ApiService
import tk.ssau.fitnesstko.AuthManager
import tk.ssau.fitnesstko.PreferencesManager
import tk.ssau.fitnesstko.R
import tk.ssau.fitnesstko.UpdateUserRequest
import tk.ssau.fitnesstko.model.dto.user.UserDTO
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {
    private lateinit var prefs: PreferencesManager
    private lateinit var authManager: AuthManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PreferencesManager(requireContext())
        authManager = AuthManager(requireContext())

        val etFirstName = view.findViewById<TextInputEditText>(R.id.etFirstName)
        val etLastName = view.findViewById<TextInputEditText>(R.id.etLastName)
        val etBirthDay = view.findViewById<TextInputEditText>(R.id.etAge)

        view.findViewById<MaterialButton>(R.id.btnSave).setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val birthDay = etBirthDay.text.toString()

            if (isValidDate(birthDay)) {
                prefs.saveUserData(firstName, lastName, birthDay)

                sendUserDataToServer(firstName, lastName, birthDay)
            } else {
                Toast.makeText(context, "Некорректный формат даты", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendUserDataToServer(firstName: String, lastName: String, birthDay: String) {
        val userId = authManager.getUserId()
        val login = authManager.getLogin()

        if (userId == null || login == null) {
            Toast.makeText(requireContext(), "Ошибка авторизации", Toast.LENGTH_SHORT).show()
            return
        }

        val request = UpdateUserRequest(
            name = "$firstName $lastName",
            login = login,
            birthDay = birthDay
        )

        ApiService.authService.updateUser(userId, request).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Ошибка обновления", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                Toast.makeText(requireContext(), "Ошибка сети", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
            true
        } catch (_: Exception) {
            false
        }
    }
}