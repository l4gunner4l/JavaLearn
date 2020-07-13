package ru.l4gunner4l.javalearn.ui.mainscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.ui.mainscreen.fragments.LessonsFragment
import ru.l4gunner4l.javalearn.ui.mainscreen.fragments.ProfileFragment
import ru.l4gunner4l.javalearn.ui.mainscreen.fragments.ShopFragment
import ru.l4gunner4l.javalearn.utils.Utils

/**
 * Activity with Bottom NavigationView (Profile, Lessons, Shop).
 * It is host for fragments
 *
 * Экран с нижней навигацией (профилем, уроками, магазином).
 * Является хостом для фрагментов
 */
class MainActivity : AppCompatActivity(), HostActivity {

    companion object {
        const val EXTRA_IS_ADMIN = "EXTRA_IS_ADMIN"

        fun createNewInstance(context: Context, isAdmin: Boolean): Intent {
            val intent = Intent(context, MainActivity::class.java)
            // all previous activities will be finished
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(EXTRA_IS_ADMIN, isAdmin)
            return intent
        }
    }

    private lateinit var lessonsFragment: LessonsFragment
    private lateinit var shopFragment: ShopFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var activeFragment: Fragment
    private lateinit var nav: BottomNavigationView
    private var isAdmin: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isAdmin = intent.getBooleanExtra(EXTRA_IS_ADMIN, false)

        if (Utils.isInternetConnection(this))
            initView()
        else Utils.showToast(this, R.string.text_no_internet, Toast.LENGTH_LONG)

    }


    private fun initView() {
        profileFragment = ProfileFragment.createInstance()
        lessonsFragment = LessonsFragment.createInstance(isAdmin)
        shopFragment = ShopFragment.createInstance()
        activeFragment = lessonsFragment

        val fm = supportFragmentManager
        nav = findViewById(R.id.main_nav_view_main)
        nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
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

    override fun endLoading() {
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

    override fun finishActivity() {
        finish()
    }

}

interface HostActivity {
    fun endLoading()
    fun finishActivity()
}
