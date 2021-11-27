package com.keddnyo.lattego.shortcuts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.keddnyo.lattego.commands.BootExec

class Recovery : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        BootExec().recovery()
        super.onCreate(savedInstanceState)
        finish()
    }
}