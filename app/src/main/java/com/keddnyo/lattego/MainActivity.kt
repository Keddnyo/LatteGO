package com.keddnyo.lattego

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import com.keddnyo.lattego.commands.BootExec
import com.keddnyo.lattego.shortcuts.Recovery
import com.keddnyo.lattego.shortcuts.Windows

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BootExec().mountEFI()

        val p = packageManager
        p.setComponentEnabledSetting(
            ComponentName(applicationContext, Windows::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        p.setComponentEnabledSetting(
            ComponentName(applicationContext, Recovery::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    override fun onResume() {
        super.onResume()
        val listView = findViewById<ListView>(R.id.list_boot_options)
        val bootList = resources.getStringArray(R.array.boot_options)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bootList)
        listView.adapter = adapter

        listView.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                val context: Context = applicationContext
                when (adapter.getItem(position)) {
                    bootList[0] -> {
                        dialog(context, bootList[0].toString(), 0, 0)
                    }
                    bootList[1] -> {
                        dialog(context, bootList[1].toString(), 1, 0)
                    }
                    bootList[2] -> {
                        dialog(context, bootList[2].toString(), 2, 0)
                    }
                    bootList[3] -> {
                        dialog(context, bootList[3].toString(), 3, 0)
                    }
                    bootList[4] -> {
                        dialog(context, bootList[4].toString(), 4, 0)
                    }
                    bootList[5] -> {
                        dialog(context, bootList[5].toString(), 5, 0)
                    }
                    bootList[6] -> {
                        dialog(context, bootList[6].toString(), 6, 0)
                    }
                    bootList[7] -> {
                        dialog(context, bootList[7].toString(), 7, 1)
                    }
                }
            }
    }

    private fun dialog(context: Context, choose: String, confirm: Int, cancel: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.confirm))
        builder.setMessage(resources.getString(R.string.choose, choose))
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            when (confirm) {
                0 -> {
                    BootExec().shutdown() // Shutdown
                }
                1 -> {
                    BootExec().recovery() // Recovery
                }
                2 -> {
                    BootExec().bootloader() // Bootloader
                }
                3 -> {
                    BootExec().dnx() // DNX
                }
                4 -> {
                    BootExec().reboot() // Reboot
                }
                5 -> {
                    BootExec().sleep() // Sleep
                }
                6 -> {
                    BootExec().safemode() // Safe mode
                }
                7 -> {
                    BootExec().windows(context) // Windows
                }
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
            when (cancel) {
                0 -> {
                    // None
                }
                1 -> {
                    BootExec().copyFile(context, "android.efi")
                }
            }
        }
        builder.show()
    }
}