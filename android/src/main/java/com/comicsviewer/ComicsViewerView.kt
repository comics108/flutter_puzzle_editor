package com.comicsviewer

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ScrollView
import net.nativemind.comics.viewer.ComicsViewController
import net.nativemind.comics.viewer.comics.view.LayersView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter

class ComicsViewerView : FrameLayout {
    private val scrollView: ScrollView
    private lateinit var layersView: LayersView
    private lateinit var controller: ComicsViewController

    private var filePath: String? = null

    constructor(context: Context?) : super(context!!) {
        scrollView = ScrollView(context)
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        scrollView = ScrollView(context)
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        scrollView = ScrollView(context)
        init(context)
    }

    private fun init(context: Context) {
        // Initialize views
        layersView = LayersView(context, null)

        scrollView.addView(
            layersView,
            ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT
            )
        )

        addView(
            scrollView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        // Initialize controller
        controller = ComicsViewController(context, layersView)

        // Setup scroll listener
        controller.setScrollListener { position ->
            onScrollChanged(position)
        }
    }

    // Props
    fun setFilePath(path: String?) {
        if (path != null && path != filePath) {
            filePath = path
            loadComics(path)
        }
    }

    fun setLanguageIndex(index: Int) {
        controller.setLanguage(index)
    }

    fun setSoundEnabled(enabled: Boolean) {
        controller.toggleSounds(enabled)
    }

    // Methods
    fun loadComics(path: String) {
        controller.loadComics(path, object : ComicsViewController.ComicsLoadListener {
            override fun onLoaded() {
                onComicsLoaded()
            }

            override fun onError(error: String) {
                onComicsError(error)
            }
        })
    }

    fun play() {
        controller.play()
    }

    fun pause() {
        controller.pause()
    }

    fun setScrollPosition(position: Double) {
        controller.setScrollPosition(position.toFloat())
    }

    fun getScrollPosition(): Double {
        return controller.getScrollPosition().toDouble()
    }

    fun togglePreview(show: Boolean) {
        controller.togglePreview(show)
    }

    fun toggleSounds(enabled: Boolean) {
        controller.toggleSounds(enabled)
    }

    fun isPlaying(): Boolean {
        return controller.isPlaying()
    }

    fun getDuration(): Double {
        return controller.getDuration().toDouble()
    }

    fun getCurrentPosition(): Double {
        return controller.getCurrentPosition().toDouble()
    }

    fun cleanup() {
        controller.dispose()
    }

    // Events
    private fun onScrollChanged(position: Float) {
        val event = Arguments.createMap().apply {
            putDouble("position", position.toDouble())
        }
        val reactContext = context as ReactContext
        reactContext
            .getJSModule(RCTEventEmitter::class.java)
            .receiveEvent(id, "onScrollChanged", event)
    }

    private fun onComicsLoaded() {
        val reactContext = context as ReactContext
        reactContext
            .getJSModule(RCTEventEmitter::class.java)
            .receiveEvent(id, "onLoaded", null)
    }

    private fun onComicsError(error: String) {
        val event = Arguments.createMap().apply {
            putString("error", error)
        }
        val reactContext = context as ReactContext
        reactContext
            .getJSModule(RCTEventEmitter::class.java)
            .receiveEvent(id, "onError", event)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cleanup()
    }
}
