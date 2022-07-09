package com.dinhlap.menu.power

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment

class DialogPermission : DialogFragment() {

    private var count =15
    private lateinit var  ok:Button
    private lateinit var  close:Button

    @Nullable
    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.dialog_permission, container, false)
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false);
        }
        close = view.findViewById<Button>(R.id.btn_cancel)
        ok = view.findViewById<Button>(R.id.btn_ok)
        ok.text = count.toString()
        ok.setOnClickListener{
            dismiss()
        }
        close.setOnClickListener { activity?.finish()}
        setCount()
        return view
    }

    private fun setCount(){
        ok.isEnabled = false
        val handler = Handler(Looper.getMainLooper())
        val runnableCode = object: Runnable {
            override fun run() {
                count= count -1
                ok.text = count.toString()
                if (count == 0){
                    ok.isEnabled = true
                    ok.text = getText(R.string.ok)
                }else{
                    handler.postDelayed(this, 1000)
                }
            }
        }
        handler.postDelayed(runnableCode,1000)
    }
}