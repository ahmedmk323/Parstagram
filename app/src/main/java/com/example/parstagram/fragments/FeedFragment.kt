package com.example.parstagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.PostsAdapter
import com.example.parstagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class FeedFragment : Fragment() {
    lateinit var postsRv: RecyclerView
    lateinit var adapter: PostsAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    var allPosts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer)
        postsRv = view.findViewById(R.id.rv_feed)
        adapter = PostsAdapter(requireContext(),allPosts as ArrayList<Post>)

        postsRv.adapter = adapter
        postsRv.layoutManager = LinearLayoutManager(requireContext())

        swipeContainer.setOnRefreshListener{
            queryPosts()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        queryPosts()
    }

    // Query for al posts in the server
    open fun queryPosts(){
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
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
                        // Clear out current fetched tweets
                        adapter.clear()
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                        swipeContainer.setRefreshing(false)
                    }
                }
            }

        })
    }
    companion object{
        const val TAG = "FeedFragment"
    }
}