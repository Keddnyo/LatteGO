package com.keddnyo.lattego.shortcuts

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.keddnyo.lattego.R
import com.keddnyo.lattego.commands.Boot

class Recovery : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(resources.getString(R.string.choice, getString(R.string.recovery)))
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            Boot().recovery()
            finish()
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
            finish()
        }
        builder.show().setCanceledOnTouchOutside(false)
        super.onCreate(savedInstanceState)
    }
}