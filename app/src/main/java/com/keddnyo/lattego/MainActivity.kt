package com.keddnyo.lattego

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.keddnyo.lattego.commands.Boot
import com.keddnyo.lattego.other.Device
import com.keddnyo.lattego.shortcuts.Recovery
import com.keddnyo.lattego.shortcuts.Windows

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val device = getString(R.string.only_for, Device().model)
        if (!Device().check()) {
            Toast.makeText(this, device, Toast.LENGTH_SHORT).show()
            finish()
        }
        else {
            Boot().mountEFI()
        }
    }

    override fun onResume() {
        super.onResume()
        val listView = findViewById<ListView>(R.id.list_boot_options)
        val bootList = resources.getStringArray(R.array.boot_options)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bootList)
        listView.adapter = adapter

        listView.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                when (adapter.getItem(position)) {
                    bootList[0] -> {
                        dialog(bootList[0].toString(), 0) // DNX
                    }
                    bootList[1] -> {
                        dialog(bootList[1].toString(), 1) // Fastboot
                    }
                    bootList[2] -> {
                        dialog(bootList[2].toString(), 2) // Recovery
                    }
                    bootList[3] -> {
                        dialog(bootList[3].toString(), 3) // Reboot
                    }
                    bootList[4] -> {
                        dialog(bootList[4].toString(), 4)// Safe mode
                    }
                    bootList[5] -> {
                        dialog(bootList[5].toString(), 5) // Sleep
                    }
                    bootList[6] -> {
                        dialog(bootList[6].toString(), 6) // Power off
                    }
                }
            }

        listView.onItemLongClickListener =
            OnItemLongClickListener { _, _, position, _ ->
                val p = packageManager
                val context = applicationContext
                when (adapter.getItem(position)) {
                    bootList[0] -> {
                        Boot().copyFile(context, "android.efi")
                        toast(getString(R.string.attempt_to_fix_efi))
                    }
                    bootList[2] -> {
                        p.setComponentEnabledSetting(
                            ComponentName(context, Recovery::class.java),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        toast((getString(R.string.shortcut_enabled, getString(R.string.recovery))))
                    }
                    bootList[3] -> {
                        p.setComponentEnabledSetting(
                            ComponentName(context, Windows::class.java),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        toast((getString(R.string.shortcut_enabled, getString(R.string.windows))))
                    }
                    bootList[4] -> {
                        p.setComponentEnabledSetting(
                            ComponentName(context, Windows::class.java),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        toast((getString(R.string.shortcut_disabled, getString(R.string.windows))))
                    }
                    bootList[6] -> {
                        p.setComponentEnabledSetting(
                            ComponentName(context, Recovery::class.java),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        toast((getString(R.string.shortcut_disabled, getString(R.string.recovery))))
                    }
                }
                true
            }
    }

    private fun dialog(choice: String, confirm: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(resources.getString(R.string.choice, choice))
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            when (confirm) {
                0 -> {
                    Boot().dnx() // DNX
                }
                1 -> {
                    Boot().bootloader() // Fastboot
                }
                2 -> {
                    Boot().recovery() // Recovery
                }
                3 -> {
                    Boot().reboot() // Reboot
                }
                4 -> {
                    Boot().safemode() // Safe mode
                }
                5 -> {
                    Boot().sleep() // Sleep
                }
                6 -> {
                    Boot().shutdown() // Power off
                }
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
            builder.show().dismiss()
        }
        builder.show().setCanceledOnTouchOutside(false)
    }

    private fun toast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }
}