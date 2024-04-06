package com.example.truthordare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.example.truthordare.NoInternet.Companion.setTheme
import com.example.truthordare.databinding.SettingsBinding

@Suppress("DEPRECATION")
class Settings : AppCompatActivity()
{
    private lateinit var binding: SettingsBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        NoInternet.sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE)
        setTheme(this)
        //Создание самого проекта и привязки!
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        //Весь остальной код, выше не трогать!!!
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val hide = AnimationUtils.loadAnimation(this, R.anim.hide)
        val show = AnimationUtils.loadAnimation(this, R.anim.show)
        openCloseSettings(show, View.VISIBLE)
        binding.changeTheme.setChecked(NoInternet.sharedPreferences!!.getBoolean("theme", false))
        binding.changeTheme.setOnCheckedChangeListener { _: CompoundButton?, b: Boolean ->
            themeChange = 1
            NoInternet.sharedPreferences!!.edit().putBoolean("theme", b).apply()
            recreate()
        }
        binding.closeSettingsButton.setOnClickListener {
            if (themeChange == 0)
            {
                finish()
            }
            else
            {
                val myIntent = Intent(this@Settings, PlayingField::class.java)
                startActivity(myIntent)
                finishAffinity()
                themeChange = 0
            }
        }
        binding.returnBackButton.setOnClickListener {
            if (themeChange == 0)
            {
                finish()
            }
            else
            {
                val myIntent = Intent(this@Settings, PlayingField::class.java)
                startActivity(myIntent)
                finishAffinity()
                themeChange = 0
            }
        }
        binding.exitButton.setOnClickListener {
            val myIntent = Intent(this@Settings, Players::class.java)
            startActivity(myIntent)
            PlayingField.adIsShow = false
            finishAffinity()
        }
        binding.rulesButton.setOnClickListener {
            openCloseSettings(hide, View.INVISIBLE)
            binding.closeSettingsButton.startAnimation(show)
            binding.rulesScroll.startAnimation(show)
            binding.exitButton.postDelayed({
                binding.closeSettingsButton.visibility = View.VISIBLE
                binding.rulesScroll.visibility = View.VISIBLE
                binding.info.text = getResources().getString(R.string.rules_info)
            }, 500)
        }
        binding.wCv.setOnClickListener {
            val vk = Intent(Intent.ACTION_VIEW, Uri.parse("https://apps.rustore.ru/app/com.example.alfaweather"))
            startActivity(vk)
        }
        binding.acCv.setOnClickListener {
            val tg = Intent(Intent.ACTION_VIEW, Uri.parse("https://apps.rustore.ru/app/com.example.alfacalendar"))
            startActivity(tg)
        }
        binding.ccCv.setOnClickListener {
            val tg = Intent(Intent.ACTION_VIEW, Uri.parse("https://apps.rustore.ru/app/com.example.caloriecounter"))
            startActivity(tg)
        }
        binding.awCv.setOnClickListener {
            val tg = Intent(Intent.ACTION_VIEW, Uri.parse("https://apps.rustore.ru/app/com.example.alfaworkingshift"))
            startActivity(tg)
        }
        binding.vk.setOnClickListener {
            val vk = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/powerkiller"))
            startActivity(vk)
        }
        binding.tg.setOnClickListener {
            val tg = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/powerkiller"))
            startActivity(tg)
        }
    }

    private fun openCloseSettings(animation1: Animation, visibility1: Int)
    {
        binding.closeSettingsButton.startAnimation(animation1)
        binding.returnBackButton.startAnimation(animation1)
        binding.rulesButton.startAnimation(animation1)
        binding.exitButton.startAnimation(animation1)
        binding.cardViewTheme.startAnimation(animation1)
        binding.ourPrograms.startAnimation(animation1)
        binding.exitButton.postDelayed({
            binding.closeSettingsButton.visibility = visibility1
            binding.returnBackButton.visibility = visibility1
            binding.rulesButton.visibility = visibility1
            binding.exitButton.visibility = visibility1
            binding.cardViewTheme.visibility = visibility1
            binding.ourPrograms.visibility = visibility1
        }, 500)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            binding.closeSettingsButton.performClick()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    companion object
    {
        var themeChange = 0
    }
}
