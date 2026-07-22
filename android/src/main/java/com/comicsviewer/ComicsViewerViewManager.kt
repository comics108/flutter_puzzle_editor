package com.comicsviewer

import android.graphics.Color
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.viewmanagers.ComicsViewerViewManagerInterface
import com.facebook.react.viewmanagers.ComicsViewerViewManagerDelegate

@ReactModule(name = ComicsViewerViewManager.NAME)
class ComicsViewerViewManager : SimpleViewManager<ComicsViewerView>(),
  ComicsViewerViewManagerInterface<ComicsViewerView> {
  private val mDelegate: ViewManagerDelegate<ComicsViewerView>

  init {
    mDelegate = ComicsViewerViewManagerDelegate(this)
  }

  override fun getDelegate(): ViewManagerDelegate<ComicsViewerView>? {
    return mDelegate
  }

  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): ComicsViewerView {
    return ComicsViewerView(context)
  }

  @ReactProp(name = "color")
  override fun setColor(view: ComicsViewerView?, color: Int?) {
    view?.setBackgroundColor(color ?: Color.TRANSPARENT)
  }

  companion object {
    const val NAME = "ComicsViewerView"
  }
}
