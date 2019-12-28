package ru.l4gunner4l.javalearn.main.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.main.MainActivity
import ru.l4gunner4l.javalearn.models.User
import ru.l4gunner4l.javalearn.utils.extensions.hideKeyboard
import ru.l4gunner4l.javalearn.utils.extensions.showKeyboard

class ProfileFragment : Fragment() {

    private lateinit var editSaveBtn: ImageButton
    private lateinit var nameTIL: TextInputLayout
    private lateinit var emailTIL: TextInputLayout

    private var isEditMode = false
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = (activity as MainActivity).getUser()
        Log.i("M_MAIN", "(activity as MainActivity).getUser()=$user")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        nameTIL = view.findViewById(R.id.profile_til_name)
        emailTIL = view.findViewById(R.id.profile_til_email)
        emailTIL.isEnabled = false
        editSaveBtn = view.findViewById(R.id.btn_edit)
        editSaveBtn.setOnClickListener {
            if (this.isEditMode) {
                val isSuccessful = updateDBUser(user)
                Toast.makeText(
                        activity,
                        if (isSuccessful) "Данные успешно обновлены!"
                        else "Данные имеют неверный формат! Обновление не завершено!",
                        Toast.LENGTH_SHORT).show()
            }
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }
        showCurrentMode(isEditMode)
        return view
    }

    fun setUserUI(user:User){
        this.user = user
        nameTIL.editText!!.setText(user.name)
        emailTIL.editText!!.setText(user.email)
    }

    private fun updateDBUser(user:User): Boolean {
        val newName = nameTIL.editText!!.text.toString().trim()
        //val newEmail = emailTIL.editText!!.text.toString().trim()
        return if (
                isValidName(newName)
                //&& isValidEmail(newEmail)
        ){
            val userDbRef = FirebaseDatabase.getInstance().getReference("users").child(user.id)
            if (user.name != newName) user.name = newName
            //if (user.email != newEmail) user.email = newEmail
            userDbRef.setValue(user)
            true
        } else {
            nameTIL.editText!!.setText(user.name)
            //emailTIL.editText!!.setText(user.email)
            nameTIL.error = null
            //emailTIL.error = null
            false
        }
    }

    private fun showCurrentMode(isEditMode: Boolean){
        nameTIL.isFocusable = true
        nameTIL.isFocusableInTouchMode = isEditMode
        nameTIL.isEnabled = isEditMode
        // emailTIL.isFocusable = isEditMode
        // emailTIL.isFocusableInTouchMode = isEditMode
        // emailTIL.isEnabled = isEditMode
        editSaveBtn.setImageResource(if (!isEditMode) R.drawable.ic_edit_light_24dp else R.drawable.ic_save_light_24dp)
        if (isEditMode) activity!!.showKeyboard()
        else activity!!.hideKeyboard()
    }


    private fun isValidEmail(emailInput: String): Boolean {
        return if (emailInput.isEmpty()) {
            emailTIL.error = getString(R.string.text_error_fill_field)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailTIL.error = getString(R.string.text_error_wrong_email)
            false
        } else {
            emailTIL.error = null
            true
        }
    }
    private fun isValidName(nameInput: String): Boolean {
        return when {
            nameInput.isEmpty() -> {
                nameTIL.error = getString(R.string.text_error_fill_field)
                false
            }
            nameInput.length < 3 -> {
                nameTIL.error = getString(R.string.text_error_too_little_name)
                false
            }
            else -> {
                nameTIL.error = null
                true
            }
        }
    }
    /*private fun isValidPassword(passwordInput: String, passwordInput2: String): Boolean {
        return if (passwordInput.isEmpty()) {
            passwordTIL.error = getString(R.string.text_error_fill_field)
            false
        } else if (passwordInput.length < 8) {
            passwordTIL.error = getString(R.string.text_error_too_little_password)
            false
        } else if (!Pattern.compile(Utils.PASSWORD_PATTERN).matcher(passwordInput).matches()) {
            passwordTIL.error = getString(R.string.text_error_wrong_password)
            false
        } else if (passwordInput != passwordInput2) {
            passwordTIL.error = null
            passwordRepeatTIL.error = getString(R.string.text_error_wrong_repeat_password)
            false
        } else {
            passwordTIL.error = null
            passwordRepeatTIL.error = null
            true
        }
    }*/

}