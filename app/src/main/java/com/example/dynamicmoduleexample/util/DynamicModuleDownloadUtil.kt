package com.example.dynamicmoduleexample.util

import android.content.Context
import android.util.Log
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

class DynamicModuleDownloadUtil(
    val context: Context,
    private val mDynamicDownloadListener: DynamicDownloadListener
) {

    lateinit var splitInstallManager: SplitInstallManager
    private var mySessionId = 0

    init {
        if (!::splitInstallManager.isInitialized) {
            splitInstallManager = SplitInstallManagerFactory.create(context)
        }
    }

    fun downloadDynamicModule(moduleName: String) {
        val request = SplitInstallRequest
            .newBuilder()
            .addModule(moduleName)
            .build()

        val listener =
            SplitInstallStateUpdatedListener { splitInstallSessionState ->
                if (splitInstallSessionState.sessionId() == mySessionId) {
                    when (splitInstallSessionState.status()) {
                        SplitInstallSessionStatus.INSTALLED -> {
                            mDynamicDownloadListener.onDynamicModuleDownloaded()
                        }
                        SplitInstallSessionStatus.DOWNLOADING -> {
                        }
                        SplitInstallSessionStatus.DOWNLOADED -> {
                        }
                    }
                }
            }

        splitInstallManager.registerListener(listener)

        splitInstallManager.startInstall(request)
            .addOnFailureListener { e -> Log.d("Error", "Exception: $e") }
            .addOnSuccessListener { sessionId -> mySessionId = sessionId }
    }

    fun isModuleDownloaded(moduleName: String): Boolean {
        return splitInstallManager.installedModules.contains(moduleName)
    }

    interface DynamicDownloadListener {
        fun onDynamicModuleDownloaded()
    }
}