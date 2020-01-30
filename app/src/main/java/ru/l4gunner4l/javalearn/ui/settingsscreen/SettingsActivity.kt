package ru.l4gunner4l.javalearn.ui.settingsscreen

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.settings_activity.*
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.data.models.User
import ru.l4gunner4l.javalearn.utils.Utils
import ru.l4gunner4l.javalearn.utils.extensions.hideKeyboard

class SettingsActivity : AppCompatActivity() {

    private lateinit var nameOld: String
    private lateinit var emailOld: String
    private var avatarNew: String? = null

    private lateinit var nameTIL: TextInputLayout
    private lateinit var emailTIL: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        nameOld = intent.getStringExtra("name")
        emailOld = intent.getStringExtra("email")
        initToolbar()
        initViews()
    }

    private fun initViews() {
        nameTIL = findViewById(R.id.settings_til_name)
        emailTIL = findViewById(R.id.settings_til_email)
        nameTIL.editText!!.setText(nameOld)
        emailTIL.editText!!.setText(emailOld)
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.settings_toolbar))
        supportActionBar!!.title = null
        settings_toolbar.findViewById<ImageView>(R.id.settings_toolbar_iv_back)
                .setOnClickListener { finish(); this@SettingsActivity.hideKeyboard() }
        settings_toolbar.findViewById<ImageView>(R.id.settings_toolbar_iv_save)
                .setOnClickListener { SaveData().execute(); this@SettingsActivity.hideKeyboard() }
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

    companion object {
        fun createNewInstance(context: Context, user: User): Intent {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("email", user.email)
            return intent
        }
    }


    inner class SaveData : AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
            saveProfileData()
            return null
        }
    }

    private fun saveProfileData() {
        FirebaseDatabase.getInstance().reference.child("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.i("M_MAIN", "LessonActivity: Updating stars count failed")
                    }
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var isSuccesful = false
                        val nameNew = nameTIL.editText!!.text.toString().trim()
                        if (isValidName(nameNew) && nameOld != nameNew){
                            dataSnapshot.child("name").ref.setValue(nameNew)
                            isSuccesful = true
                        }
                        val emailNew = emailTIL.editText!!.text.toString().trim()
                        if (isValidEmail(emailNew) && emailOld != emailNew){
                            dataSnapshot.child("email").ref.setValue(emailNew)
                            isSuccesful = true
                        }
                        if (avatarNew != null){
                            dataSnapshot.child("avatar").ref.setValue(avatarNew)
                            isSuccesful = true
                        }
                        if (isSuccesful)
                            Utils.showToast(this@SettingsActivity, R.string.text_changes_updated, Toast.LENGTH_LONG)
                        else Utils.showToast(this@SettingsActivity, R.string.text_saving_failed, Toast.LENGTH_LONG)
                    }

                })

    }
}
