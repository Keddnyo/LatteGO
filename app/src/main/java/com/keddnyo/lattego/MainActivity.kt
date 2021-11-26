package com.keddnyo.lattego

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import android.widget.AdapterView.OnItemClickListener
import java.io.*
import android.content.res.AssetManager
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val listView = findViewById<ListView>(R.id.list_boot_options)

        val bootList = listOf (
            "Shutdown",
            "Recovery",
            "Fastboot",
            "DNX",
            "Reboot",
            "Screen off",
            "Safe mode",
            "Windows"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bootList)
        listView.adapter = adapter

        listView.onItemClickListener =
            OnItemClickListener { _, _, position, id ->
                Toast.makeText(baseContext, adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show()

                val context: Context = applicationContext
                when (adapter.getItem(position)) {
                    bootList[0] -> {
                        Runtime.getRuntime().exec(arrayOf("reboot", "-p")) // Shutdown
                        Toast.makeText(baseContext, "I'm happy", Toast.LENGTH_SHORT).show()
                    }
                    bootList[1] -> {
                        Runtime.getRuntime().exec(arrayOf("reboot", "recovery")) // Recovery
                    }
                    bootList[2] -> {
                        Runtime.getRuntime().exec(arrayOf("reboot", "bootloader")) // Fastboot
                    }
                    bootList[3] -> {
                        Runtime.getRuntime().exec(arrayOf("reboot", "dnx")) // DNX
                    }
                    bootList[4] -> {
                        Runtime.getRuntime().exec("reboot") // Reboot
                    }
                    bootList[5] -> {
                        Runtime.getRuntime().exec(arrayOf("input", "keyevent", "KEYCODE_POWER")) // Screen off
                    }
                    bootList[6] -> {
                        Runtime.getRuntime().exec(arrayOf("setprop", "persist.sys.safemode", "1")) // Safemode preparation
                        Runtime.getRuntime().exec("reboot") // Reboot safemode
                    }
                    bootList[7] -> {
                        copyFile(context, "windows.efi")

                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Title")
                        builder.setMessage("Msg")
                        builder.setPositiveButton("Yes") { _, _ ->
                            Runtime.getRuntime().exec("reboot") // Reboot
                        }
                        builder.setNegativeButton("No") { _, _ ->
                            copyFile(context, "android.efi")
                        }
                        builder.show()
                    }
                }
            }
    }





    private fun copyFile(context: Context, fileName: String) {
        val assetManager = context.assets
        try {
            val `in`: InputStream = assetManager.open(fileName)
            val out: OutputStream = FileOutputStream("/sdcard/"+"bootx64.efi")
            val buffer = ByteArray(1024)
            var read: Int
            while (`in`.read(buffer).also { read = it } != -1) {
                out.write(buffer, 0, read)
            }
        } catch (e: Exception) {
            // None
        }
    }
}