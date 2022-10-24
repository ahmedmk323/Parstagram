package com.example.parstagram.fragments

import android.util.Log
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment: FeedFragment() {
    override fun queryPosts() {
        // Specify which class query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Return posts from currently signed in user
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        // Find all posts
        query.include(Post.KEY_USER)
        // Pull up the most recent posts first
        query.addDescendingOrder("createdAt")


        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null){
                    Log.e(MainActivity.TAG, "Error fetching posts")
                } else{
                    if (posts != null){
                        for (post in posts){
                            Log.i(MainActivity.TAG, "Post: "+ post.getDescription() +  ", username: "+ post.getUser()?.username)
                        }
                        adapter.clear()
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                        swipeContainer.setRefreshing(false)
                    }
                }
            }

        })
    }
}