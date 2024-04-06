package com.example.truthordare

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.truthordare.NoInternet.Companion.setTheme
import com.example.truthordare.databinding.PlayersBinding

@Suppress("DEPRECATION")
class Players : AppCompatActivity()
{
    private lateinit var binding: PlayersBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        NoInternet.sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE)
        setTheme(this)
        //Создание самого проекта и привязки!
        super.onCreate(savedInstanceState)
        binding = PlayersBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        //Весь остальной код, выше не трогать!!!
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val myAnimClick = AnimationUtils.loadAnimation(this, R.anim.click)
        val myAnimAddRemove = AnimationUtils.loadAnimation(this, R.anim.add_remove_player)
        val myAnimRemoveChild = AnimationUtils.loadAnimation(this, R.anim.remove_child)
        countPlayers = 1
        editTexts = ArrayList()
        binding.shapeAdd.setOnClickListener {
            binding.shapeRemove.isEnabled = false
            binding.shapeAdd.isEnabled = false
            val cardView = CardView(this)
            cardView.cardElevation = 0f
            cardView.setCardBackgroundColor(getResources().getColor(R.color.main, theme))
            val linearLayout = LinearLayout(this)
            val textView = TextView(this)
            textView.setPadding(convertDpToPixel(16f, this), 0, convertDpToPixel(8f, this), 0)
            textView.text = "Игрок $countPlayers:"
            textView.textSize = 20f
            textView.typeface = Typeface.SANS_SERIF
            linearLayout.addView(textView)
            val editText = EditText(this)
            editText.typeface = Typeface.SANS_SERIF
            editText.setBackgroundColor(getResources().getColor(R.color.empty, theme))
            editText.width = convertDpToPixel(200f, this)
            editText.hint = getResources().getString(R.string.name_player)
            editText.tag = "editText$countPlayers"
            editText.maxLines = 1
            editText.inputType = InputType.TYPE_CLASS_TEXT
            val filter: InputFilter = object : InputFilter
            {
                override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence
                {
                    var keepOriginal = true
                    val sb = StringBuilder(end - start)
                    for (i in start until end)
                    {
                        val c = source[i]
                        if (isCharAllowed(c))
                        { // put your condition here
                            sb.append(c)
                        }
                        else
                        {
                            keepOriginal = false
                        }
                    }
                    if (keepOriginal)
                    {
                        return sb
                    }
                    else
                    {
                        return if (source is Spanned)
                        {
                            val sp = SpannableString(sb)
                            TextUtils.copySpansFrom(source, start, sb.length, null, sp, 0)
                            sp
                        }
                        else
                        {
                            sb
                        }
                    }
                }

                private fun isCharAllowed(c: Char): Boolean
                {
                    return c != ' '
                }
            }
            val filterArray = arrayOfNulls<InputFilter>(1)
            filterArray[0] = LengthFilter(20)
            editText.filters = arrayOf(filter, filterArray[0])
            editTexts.add(editText)
            if (NoInternet.sharedPreferences!!.getBoolean("theme", false))
            {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.blackEmpty, theme))
                editText.setTextColor(getResources().getColor(R.color.white, theme))
                editText.setHintTextColor(getResources().getColor(R.color.whiteEmpty, theme))
            }
            linearLayout.addView(editText)
            cardView.addView(linearLayout)
            val divider = ConstraintLayout(this)
            divider.maxHeight = convertDpToPixel(5f, this)
            divider.minHeight = convertDpToPixel(5f, this)
            divider.setBackgroundColor(getResources().getColor(R.color.empty, theme))
            binding.playersList.addView(cardView)
            binding.playersList.getChildAt(binding.playersList.childCount - 1).startAnimation(myAnimClick)
            binding.playersList.addView(divider)
            countPlayers++
            binding.shapeAdd.startAnimation(myAnimAddRemove)
            binding.shapeAdd.postDelayed({
                binding.shapeRemove.isEnabled = true
                binding.shapeAdd.isEnabled = true
            }, 500)
        }
        binding.shapeAdd.performClick()
        binding.shapeAdd.performClick()
        binding.shapeRemove.setOnClickListener {
            if (binding.playersList.childCount < 5)
            {
                Toast.makeText(this, "Минимальное число игроков!\nУдаление невозможно!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                binding.shapeRemove.isEnabled = false
                binding.shapeAdd.isEnabled = false
                binding.playersList.removeViewAt(binding.playersList.childCount - 1)
                binding.playersList.getChildAt(binding.playersList.childCount - 1).startAnimation(myAnimRemoveChild)
                binding.playersList.getChildAt(binding.playersList.childCount - 1).postDelayed({
                    binding.playersList.removeViewAt(binding.playersList.childCount - 1)
                    binding.shapeRemove.isEnabled = true
                    binding.shapeAdd.isEnabled = true
                }, 500)
                countPlayers--
                editTexts.removeAt(editTexts.size - 1)
                binding.shapeRemove.startAnimation(myAnimAddRemove)
            }
        }
        binding.startGameWithPlayers.setOnClickListener {
            var isCorrect = 0
            for (i in editTexts.indices)
            {
                if (editTexts[i].text.toString().isNotEmpty())
                {
                    isCorrect++
                }
            }
            if (isCorrect == editTexts.size)
            {
                val myIntent = Intent(this@Players, PlayingField::class.java)
                startActivity(myIntent)
                finish()
            }
            else
            {
                Toast.makeText(this@Players, "Ввведите имена всех игроков!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object
    {
        var countPlayers = 1
        @JvmField
        var editTexts = ArrayList<EditText>()
        fun convertDpToPixel(dp: Float, context: Context): Int
        {
            return Math.round(dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
        }
    }
}
