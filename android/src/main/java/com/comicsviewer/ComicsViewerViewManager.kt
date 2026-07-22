package com.comicsviewer

import android.graphics.Color
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.ViewManager

@ReactModule(name = ComicsViewerViewManager.NAME)
class ComicsViewerViewManager : SimpleViewManager<ComicsViewerView>() {

  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): ComicsViewerView {
    return ComicsViewerView(context)
  }

  @ReactProp(name = "filePath")
  fun setFilePath(view: ComicsViewerView, filePath: String?) {
    view.setFilePath(filePath)
  }

  @ReactProp(name = "languageIndex")
  fun setLanguageIndex(view: ComicsViewerView, index: Int) {
    view.setLanguageIndex(index)
  }

  @ReactProp(name = "soundEnabled")
  fun setSoundEnabled(view: ComicsViewerView, enabled: Boolean) {
    view.setSoundEnabled(enabled)
  }

  // Export commands for imperative API
  override fun getCommandsMap(): Map<String, Int> {
    return mapOf(
      "loadComics" to COMMAND_LOAD_COMICS,
      "play" to COMMAND_PLAY,
      "pause" to COMMAND_PAUSE,
      "setScrollPosition" to COMMAND_SET_SCROLL_POSITION,
      "togglePreview" to COMMAND_TOGGLE_PREVIEW,
      "toggleSounds" to COMMAND_TOGGLE_SOUNDS
    )
  }

  override fun receiveCommand(
    root: ComicsViewerView,
    commandId: String,
    args: ReadableArray?
  ) {
    when (commandId) {
      "loadComics" -> {
        val path = args?.getString(0)
        if (path != null) {
          root.loadComics(path)
        }
      }
      "play" -> root.play()
      "pause" -> root.pause()
      "setScrollPosition" -> {
        val position = args?.getDouble(0) ?: 0.0
        root.setScrollPosition(position)
      }
      "togglePreview" -> {
        val show = args?.getBoolean(0) ?: false
        root.togglePreview(show)
      }
      "toggleSounds" -> {
        val enabled = args?.getBoolean(0) ?: false
        root.toggleSounds(enabled)
      }
    }
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any> {
    return mapOf(
      "onScrollChanged" to mapOf("registrationName" to "onScrollChanged"),
      "onLoaded" to mapOf("registrationName" to "onLoaded"),
      "onError" to mapOf("registrationName" to "onError")
    )
  }

  companion object {
    const val NAME = "ComicsViewerView"
    private const val COMMAND_LOAD_COMICS = 1
    private const val COMMAND_PLAY = 2
    private const val COMMAND_PAUSE = 3
    private const val COMMAND_SET_SCROLL_POSITION = 4
    private const val COMMAND_TOGGLE_PREVIEW = 5
    private const val COMMAND_TOGGLE_SOUNDS = 6
  }
}
