package com.example.dynamicmoduleexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.dynamicmoduleexample.util.DynamicModuleDownloadUtil

class MainActivity : AppCompatActivity(),  DynamicModuleDownloadUtil.DynamicDownloadListener{
    companion object {
        const val DYNAMIC_MODULE_NAME = "dynamicfeature"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    lateinit var dynamicModuleDownloadUtil: DynamicModuleDownloadUtil
    private fun init() {
        initDynamicModuleDownloadUtil()
        initClickListener()
    }

    private fun initDynamicModuleDownloadUtil() {
        dynamicModuleDownloadUtil = DynamicModuleDownloadUtil(baseContext, this)
    }

    private fun initClickListener() {
        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            openDynamicActivityIfDownloaded()
        }
    }

    private fun openDynamicActivityIfDownloaded() {
        if (dynamicModuleDownloadUtil.isModuleDownloaded(DYNAMIC_MODULE_NAME)) {
            openDynamicActivity()
        } else {
            dynamicModuleDownloadUtil.downloadDynamicModule(DYNAMIC_MODULE_NAME)
        }
    }

    override fun onDynamicModuleDownloaded() {
        openDynamicActivity()
    }

    private fun openDynamicActivity() {
        val intent = Intent()
        intent.setClassName(BuildConfig.APPLICATION_ID, "com.example.dynamicfeature.DynamicActivity")
        startActivity(intent)
    }
}