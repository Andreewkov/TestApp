package app.andreew.vktest.model.repository

import app.andreew.vktest.model.News
import app.andreew.vktest.ui.view.view.ErrorView

interface LoadListener {
    fun newsLoaded(news: ArrayList<News>)
    fun errorLoading(type: ErrorView.ErrorType)
}