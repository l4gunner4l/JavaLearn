package ru.l4gunner4l.javalearn.ui.settingsscreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.settings_activity.*
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.data.models.User
import ru.l4gunner4l.javalearn.utils.Utils
import ru.l4gunner4l.javalearn.utils.extensions.hideKeyboard
import java.io.ByteArrayOutputStream


class SettingsActivity : AppCompatActivity() {

    private lateinit var nameOld: String
    private lateinit var emailOld: String
    private lateinit var avatarIV: ImageView
    private var avatarNew: Bitmap? = null

    private lateinit var nameTIL: TextInputLayout
    private lateinit var emailTIL: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        nameOld = intent.getStringExtra("name") ?: ""
        emailOld = intent.getStringExtra("email") ?: ""
        initToolbar()
        initViews()
    }

    private fun initViews() {
        nameTIL = findViewById(R.id.settings_til_name)
        emailTIL = findViewById(R.id.settings_til_email)
        nameTIL.editText!!.setText(nameOld)
        emailTIL.editText!!.setText(emailOld)
        avatarIV = settings_iv_avatar
        settings_btn_change_photo.setOnClickListener { chooseImage() }
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.settings_toolbar))
        supportActionBar!!.title = null
        settings_toolbar.findViewById<ImageView>(R.id.settings_toolbar_iv_back)
                .setOnClickListener { finish(); this@SettingsActivity.hideKeyboard() }
        settings_toolbar.findViewById<ImageView>(R.id.settings_toolbar_iv_save)
                .setOnClickListener { onSaveClick() }
    }

    private fun onSaveClick() {
        val nameNew = nameTIL.editText!!.text.toString().trim()
        val emailNew = emailTIL.editText!!.text.toString().trim()
        if (!isValidName(nameNew) || !isValidEmail(emailNew))
            Utils.showToast(this@SettingsActivity, getString(R.string.text_error_valid), Toast.LENGTH_LONG)
        else if (avatarNew == null && nameOld == nameNew && emailOld == emailNew)
            Utils.showToast(this@SettingsActivity, getString(R.string.text_no_updates), Toast.LENGTH_LONG)
        else SaveData().execute()
        this@SettingsActivity.hideKeyboard()
    }

    private fun chooseImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK &&
                imageReturnedIntent != null
        ) {
            val selectedImageUri: Uri? = imageReturnedIntent.data
            avatarNew = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            avatarIV.setImageBitmap(avatarNew)
        }
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
        private const val PICK_IMAGE_REQUEST = 71
        fun createNewInstance(context: Context, user: User): Intent {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("email", user.email)
            return intent
        }
    }


    inner class SaveData : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            saveProfileData()
            return null
        }
    }

    private fun saveProfileData() {
        if (avatarNew != null) {
            val fileName = System.currentTimeMillis().toString() + ".png"
            val stream = ByteArrayOutputStream()
            avatarNew!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            FirebaseStorage.getInstance()
                    .getReferenceFromUrl("gs://l4gunner4l-learn-java.appspot.com")
                    .child("images/$fileName")
                    .putBytes(stream.toByteArray())
                    .addOnSuccessListener {
                        FirebaseDatabase.getInstance().reference
                                .child("users")
                                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .child("avatarName")
                                .setValue(fileName)
                        Utils.showToast(this@SettingsActivity, getString(R.string.text_avatar_updated), Toast.LENGTH_LONG)
                    }
                    .addOnFailureListener {
                        Log.i("M_MAIN", "saveProfileData: $it")
                        Utils.showToast(this@SettingsActivity, getString(R.string.text_avatar_updated), Toast.LENGTH_LONG)
                    }
        }
        FirebaseDatabase.getInstance().reference.child("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.i("M_MAIN", "saveProfileData:$p0")
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val nameNew = nameTIL.editText!!.text.toString().trim()
                        if (isValidName(nameNew) && nameOld != nameNew) {
                            dataSnapshot.child("name").ref.setValue(nameNew)
                            nameOld = nameNew
                            Utils.showToast(this@SettingsActivity, getString(R.string.text_name_updated), Toast.LENGTH_LONG)
                        }
                        val emailNew = emailTIL.editText!!.text.toString().trim()
                        if (isValidEmail(emailNew) && emailOld != emailNew) {
                            dataSnapshot.child("email").ref.setValue(emailNew)
                            emailOld = emailNew
                            Utils.showToast(this@SettingsActivity, getString(R.string.text_email_updated), Toast.LENGTH_LONG)
                        }

                    }

                })

    }
}
