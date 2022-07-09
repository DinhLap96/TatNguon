package com.dinhlap.menu.power

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast

class PowerMenuService : AccessibilityService() {

    private val powerMenuReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!performGlobalAction(intent.getIntExtra("action", -1))){
                Toast.makeText(context, getString(R.string.not_supported), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onCreate() {
        super.onCreate()
        registerReceiver(
            powerMenuReceiver,
            IntentFilter("com.dinhlap.menu.power.ACCESSIBILITY_ACTION")
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(powerMenuReceiver)
    }



}