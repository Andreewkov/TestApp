package app.andreew.vktest.ui.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import app.andreew.vktest.App
import app.andreew.vktest.R
import app.andreew.vktest.model.News
import app.andreew.vktest.ui.contracts.MainContract
import app.andreew.vktest.ui.presenter.MainPresenter
import app.andreew.vktest.ui.view.adapter.Adapter
import app.andreew.vktest.ui.view.view.ErrorView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainPresenter
    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.getInstance().injector.inject(this)

        adapter = Adapter(null, object : OpenUrlListener {
            override fun open(url: String) {
                var intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        })

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            presenter.refresh()
        }

        presenter.attachView(this)
        presenter.loadNews()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun showError(type: ErrorView.ErrorType, callback: ErrorView.OnErrorButtonClickListener) {
        runOnUiThread {
            errorView.showError(type, callback)
        }
    }

    override fun showNews(news: ArrayList<News>) {
        runOnUiThread {
            adapter.addNews(news)
        }
    }

    override fun setState(state: CurrentState, whatever: Boolean?) {
        runOnUiThread {
            when (state) {
                CurrentState.LOADING -> {
                    if (!whatever!!) {
                        news.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        errorView.visibility = View.GONE
                    }
                }
                CurrentState.ERROR -> {
                    news.visibility = View.GONE
                    errorView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                CurrentState.NEWS -> {
                    swipeRefresh.isRefreshing = false
                    news.visibility = View.VISIBLE
                    errorView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

}

enum class CurrentState {
    LOADING, ERROR, NEWS
}

interface OpenUrlListener {
    fun open(url: String)
}
