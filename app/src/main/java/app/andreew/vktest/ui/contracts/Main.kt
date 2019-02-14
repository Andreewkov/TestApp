package app.andreew.vktest.ui.contracts

import app.andreew.vktest.model.News
import app.andreew.vktest.model.repository.LoadListener
import app.andreew.vktest.ui.view.activity.CurrentState
import app.andreew.vktest.ui.view.view.ErrorView


interface MainContract {

    interface View {
        fun showNews(news: ArrayList<News>)
        fun showError(type: ErrorView.ErrorType, callback: ErrorView.OnErrorButtonClickListener)
        fun setState(state: CurrentState, whatever: Boolean?)
    }

    interface Presenter {
        var isAttached: Boolean
        fun attachView(view: MainContract.View)
        fun detachView()
        fun loadNews()
        fun refresh()
    }

    interface Repository {
        fun loadNews(whatever: Boolean, listener: LoadListener)
    }
}