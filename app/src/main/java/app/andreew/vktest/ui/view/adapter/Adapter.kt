package app.andreew.vktest.ui.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.andreew.vktest.R
import app.andreew.vktest.model.News
import app.andreew.vktest.ui.view.activity.OpenUrlListener
import app.andreew.vktest.util.FormatDate
import com.bumptech.glide.Glide

class Adapter(news: ArrayList<News>?, listener: OpenUrlListener) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private var news:ArrayList<News>? = news
    private var listener: OpenUrlListener? = listener

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.item, p0, false)
        return ViewHolder(view, listener!!)
    }

    override fun getItemCount(): Int {
        return if (news == null) 0 else news!!.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(news!![p1])
    }

    fun addNews(news: ArrayList<News>) {
        if (this.news == null)
            this.news = ArrayList<News>()
        this.news!!.addAll(news)
        notifyDataSetChanged()
    }

    class ViewHolder : RecyclerView.ViewHolder {
        val view: View
        val textSource: TextView
        val textTitle: TextView
        val textDescription: TextView
        val textPublished: TextView
        val textAuthor: TextView
        val image: ImageView
        val listener: OpenUrlListener

        constructor(view: View, listener: OpenUrlListener) : super(view) {
            textSource = view.findViewById(R.id.textSource)
            textTitle = view.findViewById(R.id.textTitle)
            textDescription = view.findViewById(R.id.textDescription)
            textPublished = view.findViewById(R.id.textPublished)
            textAuthor = view.findViewById(R.id.textAuthor)
            image = view.findViewById(R.id.image)
            this.view = view
            this.listener = listener
        }

        fun bind(news: News) {
            textSource!!.text = news.source
            textTitle!!.text = news.title
            textDescription!!.text = news.description
            textPublished!!.text = FormatDate.format(news.published)
            textAuthor!!.text = news.author
            if (textAuthor!!.text == "null")
                textAuthor!!.visibility = View.GONE
            Glide.with(this.view!!)
                .load(news.urlImage)
                .into(image!!)
            this.view?.setOnClickListener {
                this.listener?.open(news.url)
            }
        }
    }
}