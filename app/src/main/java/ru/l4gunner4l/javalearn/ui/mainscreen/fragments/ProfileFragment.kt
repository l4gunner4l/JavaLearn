package ru.l4gunner4l.javalearn.ui.mainscreen.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.data.models.User
import ru.l4gunner4l.javalearn.ui.mainscreen.MainActivity
import ru.l4gunner4l.javalearn.ui.settingsscreen.SettingsActivity
import ru.l4gunner4l.javalearn.ui.signscreens.SignActivity
import ru.l4gunner4l.javalearn.utils.Utils


class ProfileFragment : Fragment() {

    private lateinit var nameTIL: TextInputLayout
    private lateinit var emailTIL: TextInputLayout
    private lateinit var avatarIV: ImageView

    private lateinit var user: User

    companion object {
        fun createInstance() = ProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoadData().execute()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        nameTIL = view.findViewById(R.id.profile_til_name)
        emailTIL = view.findViewById(R.id.profile_til_email)
        avatarIV = view.findViewById(R.id.civ_avatar)
        view.findViewById<Button>(R.id.btn_sign_out).setOnClickListener { signOut() }
        val toolbar = view.findViewById<Toolbar>(R.id.profile_toolbar)
        toolbar.findViewById<ImageView>(R.id.profile_toolbar_iv_settings)
                .setOnClickListener{
                    startActivity(SettingsActivity.createNewInstance(activity as Context, user))
                }
        nameTIL.isEnabled = false
        emailTIL.isEnabled = false

        return view
    }

    private fun signOut(){
        val sureAlert = AlertDialog.Builder(activity as Context)
        sureAlert.setTitle(getString(R.string.label_exit))
                .setMessage(getString(R.string.text_question_sure_sign_out))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.label_yes_sure)){ dialog, which ->
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(activity as Context, SignActivity::class.java))
                    (activity as MainActivity).finish()
                }
                .setNegativeButton(getString(R.string.label_cancel)){ dialog, which ->
                    Utils.showToast(activity as Context,
                            getString(R.string.text_you_stayed), Toast.LENGTH_SHORT)
                }
        sureAlert.create().show()
    }

    private fun updateUI() {
        if (user.avatarName == null || user.avatarName == "null")
            avatarIV.setImageResource(R.drawable.avatar_default)
        else {
            val storageRef = FirebaseStorage.getInstance()
                    .getReferenceFromUrl("gs://l4gunner4l-learn-java.appspot.com")
            val fileName = user.avatarName!!
            storageRef.child("images").child(fileName)
                    .downloadUrl
                    .addOnSuccessListener {
                        Glide.with(activity!!.baseContext).load(it).into(avatarIV)
                    }
        }
        nameTIL.editText!!.setText(user.name)
        emailTIL.editText!!.setText(user.email)
    }


    inner class LoadData : AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
            loadData()
            return null
        }
    }

    private fun loadData() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().reference.child("users").child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val starsList = mutableListOf<Int>()
                        for (starsSnapshot in dataSnapshot.child("starsList").children)
                            starsList.add(starsSnapshot.getValue(Int::class.java)!!)

                        user = User(
                                dataSnapshot.child("id").value.toString(),
                                dataSnapshot.child("name").value.toString(),
                                dataSnapshot.child("email").value.toString(),
                                dataSnapshot.child("avatarName").value.toString(),
                                starsList
                        )
                        updateUI()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.i("M_MAIN", "Failed to read value.", error.toException())
                    }
                })
    }
}