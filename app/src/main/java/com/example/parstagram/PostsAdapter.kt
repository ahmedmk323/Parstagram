package com.example.parstagram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class PostsAdapter(private val context: Context, private val posts: ArrayList<Post>): RecyclerView.Adapter<PostsAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvUsername = itemView.findViewById<TextView>(R.id.tv_username)
        val tvUsername2 = itemView.findViewById<TextView>(R.id.tv_username2)
        val tvPostBody = itemView.findViewById<TextView>(R.id.tv_postBody)
        val relativeTime = itemView.findViewById<TextView>(R.id.tv_creationTime)
        val ivPostImage = itemView.findViewById<ImageView>(R.id.iv_postImage)
        val ivAvatar = itemView.findViewById<ImageView>(R.id.iv_avatar)

        fun bind(post: Post){
            tvUsername.text = post.getUser()?.username
            tvUsername2.text = post.getUser()?.username
            relativeTime.text = post.getFormattedTimeStamp(post.getCreatedAt().toString())
            tvPostBody.text = post.getDescription()
            Glide.with(itemView.context)
                .load(post.getImage()?.url)
                .into(ivPostImage)

            Glide.with(itemView.context)
                .load(R.drawable.image_placeholder)
                .transform(CircleCrop())
                .into(ivAvatar)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val postView = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false)
        return ViewHolder(postView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        return holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size

    // Clean all elements of the recycler
    fun clear() {
        posts.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(postList: List<Post>) {
        posts.addAll(postList)
        notifyDataSetChanged()
    }
}