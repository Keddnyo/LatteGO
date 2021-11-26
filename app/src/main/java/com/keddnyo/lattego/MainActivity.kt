package com.keddnyo.lattego

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.AdapterView.OnItemClickListener
import java.io.*
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
            OnItemClickListener { _, _, position, _ ->
                val context: Context = applicationContext
                when (adapter.getItem(position)) {
                    bootList[0] -> {
                        BootExec().shutdown() // Shutdown
                    }
                    bootList[1] -> {
                        BootExec().recovery() // Recovery
                    }
                    bootList[2] -> {
                        BootExec().bootloader() // Bootloader
                    }
                    bootList[3] -> {
                        BootExec().dnx() // DNX
                    }
                    bootList[4] -> {
                        BootExec().reboot() // Reboot
                    }
                    bootList[5] -> {
                        BootExec().sleep() // Sleep
                    }
                    bootList[6] -> {
                        BootExec().safemode() // Safe mode
                    }
                    bootList[7] -> {
                        BootExec().mountEFI()

                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Confirm")
                        builder.setMessage("Do you want to reboot into Windows?")
                        builder.setPositiveButton("Yes") { _, _ ->
                            copyFile(context, "windows.efi")
                            BootExec().reboot()
                        }
                        builder.setNegativeButton("No") { _, _ ->
                            copyFile(context, "android.efi")
                        }
                        builder.show()
                    }
                }
            }
    }
    @SuppressLint("SdCardPath")
    private fun copyFile(context: Context, fileName: String) {
        val assetManager = context.assets
        try {
            val `in`: InputStream = assetManager.open(fileName)
            val out: OutputStream = FileOutputStream("/sdcard/"+".bootx64.efi")
            val buffer = ByteArray(1024)
            var read: Int
            while (`in`.read(buffer).also { read = it } != -1) {
                out.write(buffer, 0, read)
            }
            BootExec().copyEFI()
        } catch (e: Exception) {
            // None
        }
    }
}