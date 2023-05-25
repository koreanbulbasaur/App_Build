package com.example.practice

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.Random
import java.util.Timer
import kotlin.math.abs
import java.time.LocalDateTime
import kotlin.random.Random.Default.nextInt

class MainActivity : AppCompatActivity() {
    var p_num = 2
    var k = 1
    val score_list = mutableListOf<Float>()
    var isBlind = false

    fun start() {
        setContentView((R.layout.activity_main_start))
        val tv_pnum: TextView = findViewById(R.id.tv_peoplenum)
        val btn_minus: Button = findViewById(R.id.btn_minus)
        val btn_plus: Button = findViewById(R.id.btn_plus)
        val btn_start: Button = findViewById(R.id.btn_start)
        val btn_blind: Button = findViewById(R.id.btn_hard)

        btn_blind.setOnClickListener {
            isBlind = !isBlind
            if (isBlind == true){
                btn_blind.text = "Hard Mode"
            }
            else{
                btn_blind.text = "Easy Mode"
            }
        }

        tv_pnum.text = p_num.toString()

        btn_minus.setOnClickListener{
            p_num --
            if (p_num == 0){
                p_num = 1
            }
            tv_pnum.text = p_num.toString()
        }
        btn_plus.setOnClickListener{
            p_num ++
            tv_pnum.text = p_num.toString()
        }
        btn_start.setOnClickListener {
            main()
        }
    }

    fun main() {
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.tv_random)
        val btn: Button = findViewById(R.id.btn_plus)
        val tv_t: TextView = findViewById(R.id.tv_timer)
        val tv_s: TextView = findViewById(R.id.tv_score)
        val tv_n: TextView = findViewById(R.id.tv_now)
        val tv_people: TextView = findViewById(R.id.tv_peo)
        val btn_first: Button = findViewById(R.id.btn_first)

        var timerTask: Timer? = null

        var sec = 0

        var stage = 1

        val random_box = Random()
        val num = random_box.nextInt(1001)

        val bg_main: ConstraintLayout = findViewById(R.id.bg_main)

        val randomColor = generateRandomNonBlackHexColor()
        bg_main.setBackgroundColor(Color.parseColor(randomColor))

        tv.text = ((num.toFloat()) / 100).toString()
        btn.text = "시작"
        tv_people.text = "참가자 $k"

        btn_first.setOnClickListener {
            score_list.clear()
            k = 1
            p_num = 2
            start()
        }

        btn.setOnClickListener {


            stage ++
            if (stage == 2) {
                timerTask = kotlin.concurrent.timer(period = 10) {
                    sec++
                    runOnUiThread {
                        if (isBlind == false) {
                            tv_t.text = (sec.toFloat() / 100).toString()
                        }
                        else if (isBlind == true && stage == 2)
                            tv_t.text = "???"
                    }
                }
                val currentTime = LocalDateTime.now()
                tv_n.text = currentTime.toString()

                btn.text = "정지"
            }
            else if (stage == 3) {
                tv_t.text = (sec.toFloat() / 100).toString()
                timerTask?.cancel()
                val score = abs(sec - num).toFloat()/100
                score_list.add(score)
                tv_s.text = score.toString()
                btn.text = "다음"
                stage = 0
            }
            else if (stage == 1){
                if (k < p_num) {
                    k++
                    main()
                }
                else{
                    end()
                }
            }

        }
    }

    fun generateRandomNonBlackHexColor(): String {
        val hexDigits = "123456789ABCDEF"
        val sb = StringBuilder(7)

        sb.append("#")
        for (i in 0 until 6) {
            val randomIndex = nextInt(hexDigits.length)
            val digit = hexDigits[randomIndex]
            sb.append(digit)
        }

        return sb.toString()
    }

    fun end() {
        setContentView(R.layout.activity_end)
        val tv_last: TextView = findViewById(R.id.tv_last)
        val tv_lpoint: TextView = findViewById(R.id.tv_lpoint)
        val btn_init: Button = findViewById(R.id.btn_init)

        val last_score = score_list.maxOrNull()
        tv_lpoint.text = last_score.toString()
        val last_user = score_list.indexOf(last_score)
        tv_last.text = "참가자 " + (last_user+1).toString()

        btn_init.setOnClickListener {
            score_list.clear()
            k = 1
            start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        start()
    }
}