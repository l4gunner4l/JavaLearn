package ru.l4gunner4l.javalearn.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.hideKeyboard
import ru.l4gunner4l.javalearn.main.MainActivity
import ru.l4gunner4l.javalearn.models.User
import ru.l4gunner4l.javalearn.showKeyboard

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
        editSaveBtn = view.findViewById(R.id.btn_edit)
        editSaveBtn.setOnClickListener {
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }
        nameTIL = view.findViewById(R.id.profile_til_name)
        emailTIL = view.findViewById(R.id.profile_til_email)
        showCurrentMode(isEditMode)
        return view
    }

    fun setUser(user:User){
        this.user = user
        updateUI()
    }

    private fun updateUI() {
        nameTIL.editText!!.setText(user.name)
        emailTIL.editText!!.setText(user.email)
    }

    private fun showCurrentMode(isEditMode: Boolean){
        nameTIL.isFocusable = isEditMode
        nameTIL.isFocusableInTouchMode = isEditMode
        nameTIL.isEnabled = isEditMode
        emailTIL.isFocusable = isEditMode
        emailTIL.isFocusableInTouchMode = isEditMode
        emailTIL.isEnabled = isEditMode
        editSaveBtn.setImageResource(if (!isEditMode) R.drawable.ic_edit_light_24dp else R.drawable.ic_save_light_24dp)
        if (isEditMode) activity!!.showKeyboard()
        else activity!!.hideKeyboard()
    }


}