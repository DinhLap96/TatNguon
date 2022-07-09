package com.dinhlap.menu.power

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import java.util.*


class MenuPower : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(isRoot()){
            deviceRoot()
        }else{
            deviceNotRoot()
        }
    }

    //DeviceRoot
    private fun deviceRoot (){
        val power = "reboot -p"
        val reboot = "reboot"
        val recovery = "reboot recovery"
        val fastboot = "reboot bootloader"
        val key = "key"
        val iv_power :ImageView
        val iv_reboot :ImageView
        val iv_recovery :ImageView
        val iv_fastboot :ImageView
        val view: LinearLayout
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        setContentView(R.layout.activity_menu_power)
        view = findViewById(R.id.view)
        iv_power = findViewById(R.id.iv_power)
        iv_reboot = findViewById(R.id.iv_reboot)
        iv_recovery = findViewById(R.id.iv_recovery)
        iv_fastboot = findViewById(R.id.iv_fastboot)
        view.setOnClickListener({finish()})
        iv_power.setOnClickListener( {
            focusStatus(iv_power)
            setReboot(key,power)
        })
        iv_reboot.setOnClickListener( {
            focusStatus(iv_reboot)
            setReboot(key,reboot) })
        iv_recovery.setOnClickListener( {
            focusStatus(iv_recovery)
            setReboot(key,recovery) })

        iv_fastboot.setOnClickListener( {
            focusStatus(iv_fastboot)
            setReboot(key,fastboot) })


    }

    //Device Not-Root

    private fun deviceNotRoot(){
        if (isAccessibilityServiceEnabled(this,PowerMenuService::class.java)){
            //Show Menu POWER_DIALOG
            val intent = Intent("com.dinhlap.menu.power.ACCESSIBILITY_ACTION")
            intent.putExtra("action", AccessibilityService.GLOBAL_ACTION_POWER_DIALOG)
            sendBroadcast(intent)
            finish()
        }else{
            //Show Activity Enable Accessibilit
            viewDeviceNotRoot()

        }
    }
    private fun viewDeviceNotRoot(){
        val tv :TextView
        val iv :ImageView
        val btn_VN : Button
        val btn_EN : Button
        val btn_accessibility :Button
        val btn_Autostart :Button
        supportActionBar?.hide()
        setContentView(R.layout.activity_enabled_accessibility)
        var dialog = DialogPermission()
        dialog.show(supportFragmentManager,"dhssd")
        tv = findViewById(R.id.text_view)
        iv = findViewById(R.id.iv_tutorial)
        btn_EN = findViewById(R.id.btn_EN)
        btn_VN = findViewById(R.id.btn_VN)
        btn_Autostart = findViewById(R.id.btn_enabled_autoStart)
        btn_accessibility = findViewById(R.id.btn_enabled_accessibility)
        btn_accessibility.isEnabled =false
        if(checkLocale().equals("vi")) iv.setImageResource(R.drawable.iv_vi) else iv.setImageResource(R.drawable.iv_en)
        btn_EN.setOnClickListener({
            iv.setImageResource(R.drawable.iv_en)
            setAppLocale("en",tv,btn_Autostart,btn_accessibility)
        })
        btn_VN.setOnClickListener({
            iv.setImageResource(R.drawable.iv_vi)
            setAppLocale("vi",tv,btn_Autostart, btn_accessibility)
        })

        btn_accessibility.setOnClickListener({
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        })
        btn_Autostart.setOnClickListener({
            btn_accessibility.isEnabled =true
            btn_accessibility.text = getString(R.string.next)
            btn_accessibility.setTextColor(Color.BLUE)
            btn_accessibility.textSize =14.0f
            autoStart()
        })

    }
    fun setAppLocale(language: String, tv:TextView,btn_Autostart :Button, btn_btn_accessibility:Button) {
        val config = resources.configuration
        val locale = Locale(language)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(locale)
        else
            config.locale = locale

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
        tv.text = resources.getString(R.string.tutorial)
        btn_Autostart.text = resources.getString(R.string.autoStart)
        if(!btn_btn_accessibility.text.equals("0")){
            btn_btn_accessibility.text= resources.getString(R.string.next)
        }
    }
    private fun autoStart(){
        try {
            val intent = Intent()
            val manufacturer = Build.MANUFACTURER
            when {
                "xiaomi".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                    )
                }
                "oppo".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                    )
                }
                "vivo".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                    )
                }
                "letv".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.letv.android.letvsafe",
                        "com.letv.android.letvsafe.AutobootManageActivity"
                    )
                }
                "honor".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.optimize.process.ProtectActivity"
                    )
                }
                "asus".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.asus.mobilemanager",
                        "com.asus.mobilemanager.powersaver.PowerSaverSettings"
                    )
                }
                "nokia".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.evenwell.powersaving.g3",
                        "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity"
                    )
                }
                "huawei".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
                    )
                }
            }
            startActivity(intent)
        } catch (e: Exception) {
            /*Timber.e(e)*/
        }
    }

    private fun setReboot(key:String,reboot: String){
        var intent = Intent(this,Reboot::class.java)
        intent.putExtra(key,reboot)
        startActivity(intent)
        finish()
    }
    private fun focusStatus(itemView: View) {
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.animate(itemView).scaleX(1.15f).scaleY(1.2f).translationZ(1f).setDuration(100).start()
        } else {
            ViewCompat.animate(itemView).scaleX(1.15f).scaleY(1.2f).setDuration(100).start()
            val parent = itemView.parent as ViewGroup
            parent.requestLayout()
            parent.invalidate()
        }
    }

    override fun onBackPressed() {
        finish()
    }
    private fun isRoot(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec("su")
            true
        } catch (e: Exception) {
            false
        } finally {
            if (process != null) {
                try {
                    process.destroy()
                } catch (e: Exception) {
                }
            }
        }
    }
    private fun isAccessibilityServiceEnabled(context: Context, accessibilityService: Class<*>?): Boolean {
        val expectedComponentName = ComponentName(context, accessibilityService!!)
        val enabledServicesSetting =
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: return false
        val colonSplitter = SimpleStringSplitter(':')
        colonSplitter.setString(enabledServicesSetting)
        while (colonSplitter.hasNext()) {
            val componentNameString = colonSplitter.next()
            val enabledService = ComponentName.unflattenFromString(componentNameString)
            if (enabledService != null && enabledService == expectedComponentName) return true
        }
        return false
    }
    private fun checkLocale() :String{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return resources.configuration.locales.get(0).language;
        } else{
            return resources.configuration.locale.language
        }
    }
}