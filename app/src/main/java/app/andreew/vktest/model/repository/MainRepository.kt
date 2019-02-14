package app.andreew.vktest.model.repository

import app.andreew.vktest.api.ApiInterface
import app.andreew.vktest.model.News
import app.andreew.vktest.ui.contracts.MainContract
import app.andreew.vktest.ui.view.view.ErrorView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository private constructor() : MainContract.Repository {

    var listNews: ArrayList<News>? = null
    var isLoaded = false
    var isLoading = false;

    companion object {
        private var instance: MainRepository = MainRepository()
        fun getInstance() = instance
    }

    override fun loadNews(whatever: Boolean, listener: LoadListener) {
        if (!isLoading)
            if (!isLoaded || whatever)
                getJson(listener)
            else
                listener.newsLoaded(listNews!!)
    }

    private fun getJson(listener: LoadListener) {
        isLoading = true
        ApiInterface.getApi().getNews().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    listNews = ArrayList<News>()
                    var rss: JSONObject? = null
                    try {
                        rss = JSONObject(response.body())
                    } catch (e: JSONException) {
                        showError(ErrorView.ErrorType.PARSING, listener)
                        return
                    }
                    if (rss.getString("status").equals("ok")) {
                        var jsonNews: JSONArray? = null
                        try {
                            jsonNews = rss.getJSONArray("articles")
                        } catch (e: JSONException) {
                            showError(ErrorView.ErrorType.PARSING, listener)
                            return
                        }
                        for (i in 0..(jsonNews.length() - 1)) {
                            val currentJson = jsonNews.getJSONObject(i)
                            var news = News(
                                currentJson.getJSONObject("source").getString("name"),
                                currentJson.getString("author"),
                                currentJson.getString("title"),
                                currentJson.getString("description"),
                                currentJson.getString("url"),
                                currentJson.getString("urlToImage"),
                                currentJson.getString("publishedAt")
                            )
                            listNews?.add(news)
                        }
                        isLoading = false
                        isLoaded = true
                        listener.newsLoaded(listNews!!)
                        return
                    }
                    showError(ErrorView.ErrorType.PARSING, listener)
                }
                showError(ErrorView.ErrorType.FATAL, listener)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                showError(ErrorView.ErrorType.FAILURE, listener)
            }

        })
    }

    fun showError(type: ErrorView.ErrorType, listener: LoadListener) {
        isLoading = false
        listener.errorLoading(type)
    }

}