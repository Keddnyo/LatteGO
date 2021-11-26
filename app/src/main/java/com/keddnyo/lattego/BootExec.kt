package com.keddnyo.lattego

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
        Runtime.getRuntime().exec(arrayOf("input", "keyevent", "KEYCODE_POWER"))
    }
    fun safemode() {
        Runtime.getRuntime().exec(arrayOf("setprop", "persist.sys.safemode", "1"))
        reboot()
    }
    fun mountEFI() {
        Runtime.getRuntime().exec(arrayOf("su", "-c", "mount -o remount,rw /")).waitFor()
        Runtime.getRuntime().exec(arrayOf("su", "-c", "mkdir /mnt/cifs")).waitFor()
        Runtime.getRuntime().exec(arrayOf("su", "-c", "mkdir $efiPath")).waitFor()
        Runtime.getRuntime().exec(arrayOf("su", "-c", "umount $efiPath")).waitFor()
        Runtime.getRuntime().exec(arrayOf("su", "-c", "mount | grep $efiPath > /dev/null 2>&1 || mount -t vfat /dev/block/by-name/*loader $efiPath")).waitFor() // Root
    }
    fun copyEFI() {
        Runtime.getRuntime().exec(arrayOf("su", "-c", "mv /sdcard/.$file $efiPath/EFI/BOOT/$file"))
    }
}