package com.rk.xededitor.MainActivity.tabs.media

import android.content.Context
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.rk.file.FileObject
import com.rk.file.FileWrapper
import com.rk.libcommons.CustomScope
import com.rk.runner.commonUtils.getAvailablePort
import com.rk.runner.runners.web.HttpServer
import com.rk.xededitor.MainActivity.tabs.core.CoreFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URLEncoder

class WebFragment(val context: Context) : CoreFragment {
    val scope = CustomScope()
    private var file: File? = null
    private val webView:WebView = WebView(context)
    private var httpServer:HttpServer? = null
    private val port = getAvailablePort()
    
    override fun getView(): View {
        return webView
    }
    
    override fun onDestroy() {
        webView.destroy()
        if (httpServer?.isAlive == true) {
            httpServer?.stop()
        }
        scope.cancel()
    }
    
    override fun onClosed() {
        onDestroy()
    }
    override fun onCreate() {
        webView.setWebChromeClient(WebChromeClient())
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
    }
    
    override fun loadFile(file: com.rk.file.FileObject) {
        file as com.rk.file.FileWrapper
        this.file = file.file
        scope.launch(Dispatchers.IO) {
            httpServer = HttpServer(port, file.file.parentFile)
            withContext(Dispatchers.Main){
                webView.loadUrl("http://localhost:$port/${
                    withContext(Dispatchers.IO) {
                        URLEncoder.encode(file.getName(), "UTF-8")
                    }
                }")
            }
        }
    }
    
    override fun getFile(): com.rk.file.FileObject? {
        return file?.let { com.rk.file.FileWrapper(it) }
    }
}