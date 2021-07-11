package com.example.secretdiary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val firstNumberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.firstNumberPicker).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val secondNumberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.secondNumberPicker).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val thirdNumberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.thirdnumberPicker).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changeButton)
    }

    private var changePasseordMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstNumberPicker
        secondNumberPicker
        thirdNumberPicker

        openButton.setOnClickListener{

            if(changePasseordMode){
                Toast.makeText(this, "패스워드 변경 중에는 잠금을 해제할수 없습니다.", Toast.LENGTH_SHORT).show()
            }else {
                val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

                val passwordFromUser = "${firstNumberPicker.value}${secondNumberPicker.value}${thirdNumberPicker.value}"

                if(passwordPreferences.getString("password", "000").equals(passwordFromUser)){
                    //TODO 다이어리 페이지 작성 후에 넘겨주어야함
                    startActivity(Intent(this, DiaryActivity::class.java))
                }else {
                    showErrorAlertDialog()
                }
            }

        }

        changePasswordButton.setOnClickListener {
            //예외처리 : 패스워드 버튼을 누르고 있을 때는 openButton이 활성화되지 않게
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${firstNumberPicker.value}${secondNumberPicker.value}${thirdNumberPicker.value}"

            if(changePasseordMode) {
                // 저장
                passwordPreferences.edit(true){
                    putString("password", passwordFromUser)

                }
                Toast.makeText(this, "패스워드가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                changePasseordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)

            }else {
                //패스워드 확인

                if(passwordPreferences.getString("password", "000").equals(passwordFromUser)){
                    changePasseordMode = true
                    Toast.makeText(this, "패스워드 변경 모드가 활성화 되었습니다.", Toast.LENGTH_SHORT).show()
                    changePasswordButton.setBackgroundColor(Color.RED)

                }else {
                    showErrorAlertDialog()
                }
            }
        }


    }

    private fun showErrorAlertDialog(){
        AlertDialog.Builder(this)
            .setTitle("실패!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}