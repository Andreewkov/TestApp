package app.andreew.vktest.ui.presenter

import app.andreew.vktest.model.News
import app.andreew.vktest.model.repository.LoadListener
import app.andreew.vktest.model.repository.MainRepository
import app.andreew.vktest.ui.contracts.MainContract
import app.andreew.vktest.ui.view.activity.CurrentState
import app.andreew.vktest.ui.view.view.ErrorView

class MainPresenter : MainContract.Presenter {

    private var view: MainContract.View? = null
    override var isAttached: Boolean = view != null

    override fun attachView(view: MainContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadNews() {
        loadNews(true)
    }

    override fun refresh() {
        loadNews(false)
    }

    private fun loadNews(whatever: Boolean) {
        Thread {
            view?.setState(CurrentState.LOADING, whatever)
            MainRepository.getInstance().loadNews(whatever, object : LoadListener {
                override fun newsLoaded(news: ArrayList<News>) {
                    view?.setState(CurrentState.NEWS, null)
                    view?.showNews(news)
                }

                override fun errorLoading(type: ErrorView.ErrorType) {
                    view?.setState(CurrentState.ERROR, null)
                    view?.showError(type, object : ErrorView.OnErrorButtonClickListener {
                        override fun repeat() {
                            view?.setState(CurrentState.LOADING, false)
                            refresh()
                        }
                    })
                }
            })
        }.start()
    }
}