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

class NewsRepository private constructor() : MainContract.Repository {

    private var listNews: ArrayList<News>? = null
    private var isLoaded = false
    private var isLoading = false
    private var listener: LoadListener? = null

    companion object {
        private var instance: NewsRepository = NewsRepository()
        fun getInstance() = instance
    }

    override fun loadNews(whatever: Boolean, listener: LoadListener) {
        if (!isLoading)
            if (!isLoaded || whatever) {
                this.listener = listener
                getJson()
            } else
                listener.newsLoaded(listNews!!)
    }

    private fun getJson() {
        isLoading = true
        ApiInterface.getApi().getNews().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    parseJson(response.body()!!)
                } else
                    showError(ErrorView.ErrorType.FATAL)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                showError(ErrorView.ErrorType.FAILURE)
            }

        })
    }

    private fun parseJson(responseBody: String) {
        listNews = ArrayList<News>()
        var rss: JSONObject? = null
        try {
            rss = JSONObject(responseBody)
        } catch (e: JSONException) {
            showError(ErrorView.ErrorType.PARSING)
            return
        }
        if (rss.getString("status").equals("ok")) {
            var jsonNews: JSONArray? = null
            try {
                jsonNews = rss.getJSONArray("articles")
            } catch (e: JSONException) {
                showError(ErrorView.ErrorType.PARSING)
                return
            }
            for (i in 0..(jsonNews.length() - 1)) {
                val currentJson = jsonNews.getJSONObject(i)
                listNews?.add(getNewsFromJsonObject(currentJson))
            }
            isLoading = false
            isLoaded = true
            listener?.newsLoaded(listNews!!)
            return
        }
        showError(ErrorView.ErrorType.PARSING)
    }

    private fun getNewsFromJsonObject(jsonObject: JSONObject): News {
        return News(
            jsonObject.getJSONObject("source").getString("name"),
            jsonObject.getString("author"),
            jsonObject.getString("title"),
            jsonObject.getString("description"),
            jsonObject.getString("url"),
            jsonObject.getString("urlToImage"),
            jsonObject.getString("publishedAt")
        )
    }

    private fun showError(type: ErrorView.ErrorType) {
        isLoading = false
        listener?.errorLoading(type)
    }


}