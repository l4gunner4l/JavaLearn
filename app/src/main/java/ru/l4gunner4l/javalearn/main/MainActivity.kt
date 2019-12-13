package ru.l4gunner4l.javalearn.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.main.fragments.LessonsFragment
import ru.l4gunner4l.javalearn.main.fragments.ProfileFragment
import ru.l4gunner4l.javalearn.main.fragments.ShopFragment
import ru.l4gunner4l.javalearn.models.User

//import android.support.v7.widget.Toolbar;
/**
 * Activity with Bottom NavigationView (Profile, Lessons, Shop).
 * It is host for fragments
 *
 * Экран с нижней навигацией (профилем, уроками, магазином).
 * Является хостом для фрагментов
 */
class MainActivity : AppCompatActivity() {

    private val lessonsFragment: LessonsFragment = LessonsFragment()
    private val shopFragment: ShopFragment = ShopFragment()
    private val profileFragment: ProfileFragment = ProfileFragment()
    private var activeFragment: Fragment = lessonsFragment

    private lateinit var nav: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: to get from Intent current user's id, also init current user
        user = User(0, "Nikola", "qwerty@mail.ru", "qwerty12")
        initViews()
        // TODO: to add method updateUI()
    }

    private fun initViews() {
        initToolbar()

        val fm = supportFragmentManager
        nav = findViewById(R.id.nav_view_main)
        nav.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.nav_lessons -> {
                    fm.beginTransaction()
                            .hide(activeFragment)
                            .show(lessonsFragment)
                            .commit()
                    activeFragment = lessonsFragment
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    fm.beginTransaction()
                            .hide(activeFragment)
                            .show(profileFragment)
                            .commit()
                    activeFragment = profileFragment
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_shop -> {
                    fm.beginTransaction()
                            .hide(activeFragment)
                            .show(shopFragment)
                            .commit()
                    activeFragment = shopFragment
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    fm.beginTransaction()
                            .hide(activeFragment)
                            .show(lessonsFragment)
                            .commit()
                    activeFragment = lessonsFragment
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }

        // set primary fragment - lessonsFrag - on the screen
        nav.selectedItemId = R.id.nav_lessons
        fm.beginTransaction().add(R.id.fragment_container, profileFragment, "3").hide(profileFragment).commit()
        fm.beginTransaction().add(R.id.fragment_container, shopFragment, "2").hide(shopFragment).commit()
        fm.beginTransaction().add(R.id.fragment_container, activeFragment, "1").commit()
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar_main)

        // TODO: to move this line to method updateUI()
        toolbar.findViewById<TextView>(R.id.tv_your_lvl).text = getLevelText(user.currentLvl.toString())
    }

    /**
     * Method for creating colored text with user's level
     * Метод для создания цветного текста с уровнем пользователя
     */
    private fun getLevelText(levelStr: String): SpannableString {
        val spannable = SpannableString("${resources.getString(R.string.label_your_lvl)} $levelStr")
        val startIndex = spannable.length-levelStr.length
        val endIndex = spannable.length
        spannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorAccent)),
                startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(
                RelativeSizeSpan(1.25f),
                startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
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
