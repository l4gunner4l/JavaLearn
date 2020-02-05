package ru.l4gunner4l.javalearn.ui.mainscreen

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.ui.mainscreen.fragments.LessonsFragment
import ru.l4gunner4l.javalearn.ui.mainscreen.fragments.ProfileFragment
import ru.l4gunner4l.javalearn.ui.mainscreen.fragments.ShopFragment
import ru.l4gunner4l.javalearn.ui.signscreens.SignActivity
import ru.l4gunner4l.javalearn.utils.Utils

/**
 * Activity with Bottom NavigationView (Profile, Lessons, Shop).
 * It is host for fragments
 *
 * Экран с нижней навигацией (профилем, уроками, магазином).
 * Является хостом для фрагментов
 */
class MainActivity : AppCompatActivity() {

    private lateinit var lessonsFragment: LessonsFragment
    private lateinit var shopFragment: ShopFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var activeFragment: Fragment

    private lateinit var nav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

    }

    fun signOut(view: View){
        val sureAlert = AlertDialog.Builder(this@MainActivity)
        sureAlert.setTitle(getString(R.string.label_exit))
                .setMessage(getString(R.string.text_question_sure_sign_out))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.label_yes_sure)){ dialog, which ->
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, SignActivity::class.java))
            finish()
        }
                .setNegativeButton(getString(R.string.label_cancel)){ dialog, which ->
                Utils.showToast(applicationContext,
                    getString(R.string.text_you_stayed), Toast.LENGTH_SHORT)
        }
        sureAlert.create().show()
    }

    private fun initView() {
        profileFragment = ProfileFragment()
        profileFragment.ctx = this
        lessonsFragment = LessonsFragment()
        lessonsFragment.ctx = this
        shopFragment = ShopFragment()
        activeFragment = lessonsFragment

        val fm = supportFragmentManager
        nav = findViewById(R.id.main_nav_view_main)
        nav.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.nav_lessons -> {
                    changeFragment(fm, lessonsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    changeFragment(fm, profileFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_shop -> {
                    changeFragment(fm, shopFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    changeFragment(fm, lessonsFragment)
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
        // set primary fragment - lessonsFrag - on the screen
        nav.selectedItemId = R.id.nav_lessons
        fm.beginTransaction()
                .add(R.id.main_fragment_container, profileFragment, "3")
                .hide(profileFragment)
                .commitAllowingStateLoss()
        fm.beginTransaction()
                .add(R.id.main_fragment_container, shopFragment, "2")
                .hide(shopFragment)
                .commitAllowingStateLoss()
        fm.beginTransaction()
                .add(R.id.main_fragment_container, lessonsFragment, "1")
                //.hide(lessonsFragment)
                .commitAllowingStateLoss()

        //changeFragment(supportFragmentManager, lessonsFragment)
    }


    fun endLoading() {
        lessonsFragment.updateUI()
        findViewById<ProgressBar>(R.id.main_pb_progress).visibility = GONE
        findViewById<ImageView>(R.id.main_iv_splash).visibility = GONE
    }

    private fun changeFragment(fm: FragmentManager, fragment: Fragment) {
        fm.beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commitAllowingStateLoss()
        activeFragment = fragment
    }

    companion object {
        fun createNewInstance(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            // all previous activities will be finished
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }
    }

}
