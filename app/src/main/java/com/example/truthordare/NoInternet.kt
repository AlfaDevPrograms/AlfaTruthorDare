package com.example.truthordare

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.AnimationDrawable
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.truthordare.databinding.NoInternetBinding

@Suppress("DEPRECATION")
class NoInternet : AppCompatActivity()
{
    private lateinit var binding: NoInternetBinding
    private var animation: AnimationDrawable? = null
    private val isOnline: Boolean
        get()
        {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE)
        if (!sharedPreferences!!.contains("theme"))
        {
            when (getResources().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK)
            {
                Configuration.UI_MODE_NIGHT_YES -> sharedPreferences!!.edit().putBoolean("theme", true).apply()
                Configuration.UI_MODE_NIGHT_NO -> sharedPreferences!!.edit().putBoolean("theme", false).apply()
            }
        }
        setTheme(this)
        //Создание самого проекта и привзяки!
        super.onCreate(savedInstanceState)
        binding = NoInternetBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        //Весь остальной код, выше не трогать!!!
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        binding.bgStartAppNoInetAnim.setBackgroundResource(R.drawable.animation)
        animation = binding.bgStartAppNoInetAnim.background as AnimationDrawable
        animation!!.start()
        if (!isOnline)
        {
            Toast.makeText(this@NoInternet, "Проверьте подключение к интернету", Toast.LENGTH_LONG).show()
        }
        val networkRequest = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).addTransportType(NetworkCapabilities.TRANSPORT_WIFI).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build()
        val networkCallback: NetworkCallback = object : NetworkCallback()
        {
            override fun onAvailable(network: Network)
            {
                super.onAvailable(network)
                if (start == 0)
                {
                    try
                    {
                        Thread.sleep(2315)
                    }
                    catch (e: InterruptedException)
                    {
                        throw RuntimeException(e)
                    }
                    val myIntent = Intent(this@NoInternet, Players::class.java)
                    startActivity(myIntent)
                    finish()
                    start = 1
                }
            }

            override fun onLost(network: Network)
            {
                start = 0
                super.onLost(network)
            }

        }
        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    companion object
    {
        @JvmField
        var sharedPreferences: SharedPreferences? = null
        var start = 0
        @JvmStatic
        fun setTheme(context: Context)
        {
            if (sharedPreferences!!.getBoolean("theme", false))
            {
                context.setTheme(R.style.Theme_TruthorDare_Dark)
            }
            else
            {
                context.setTheme(R.style.Theme_TruthorDare)
            }
        }
    }
}
