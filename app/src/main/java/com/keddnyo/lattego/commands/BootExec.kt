package com.keddnyo.lattego.commands

import android.annotation.SuppressLint
import android.content.Context
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class BootExec {
    private val file = "bootx64.efi"
    private val efiPath = "/mnt/cifs/efi"

    fun shutdown() {
        Runtime.getRuntime().exec(arrayOf("reboot", "-p"))
    }
    fun recovery() {
        Runtime.getRuntime().exec(arrayOf("reboot", "recovery"))
    }
    fun bootloader() {
        Runtime.getRuntime().exec(arrayOf("reboot", "bootloader"))
    }
    fun dnx() {
        Runtime.getRuntime().exec(arrayOf("reboot", "dnx"))
    }
    fun reboot() {
        Runtime.getRuntime().exec("reboot")
    }
    fun sleep() {
        Runtime.getRuntime().exec(arrayOf("su", "-c", "input", "keyevent", "KEYCODE_POWER"))
    }
    fun safemode() {
        Runtime.getRuntime().exec(arrayOf("su", "-c", "setprop", "persist.sys.safemode", "1"))
        reboot()
    }
    fun windows(context: Context) {
        copyFile(context, "windows.efi")
        reboot()
    }
    fun mountEFI() {
        Runtime.getRuntime().exec(arrayOf("su", "-c", "mount -o remount,rw /")).waitFor()
        Runtime.getRuntime().exec(arrayOf("su", "-c", "mkdir /mnt/cifs")).waitFor()
        Runtime.getRuntime().exec(arrayOf("su", "-c", "mkdir $efiPath")).waitFor()
        Runtime.getRuntime().exec(arrayOf("su", "-c", "umount $efiPath")).waitFor()
        Runtime.getRuntime().exec(arrayOf("su", "-c", "mount | grep $efiPath > /dev/null 2>&1 || mount -t vfat /dev/block/by-name/*loader $efiPath")).waitFor() // Root
    }
    private fun copyEFI() {
        Runtime.getRuntime().exec(arrayOf("su", "-c", "mv /sdcard/.$file $efiPath/EFI/BOOT/$file"))
    }

    @SuppressLint("SdCardPath")
    fun copyFile(context: Context, fileName: String) {
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