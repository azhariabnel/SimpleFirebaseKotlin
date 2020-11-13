package com.ari.firebaseloginapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ari.firebaseloginapp.R
import com.ari.firebaseloginapp.model.User
import com.ari.firebaseloginapp.utils.InfiniteRecycleViewPagingAdapter
import com.ari.firebaseloginapp.view.adapter.UsersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UsersAdapter
    private lateinit var listUser: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        getUser()
        btnLogout.setOnClickListener {
            doLogout()
        }
    }

    private fun initView() {
        adapter = UsersAdapter().apply {
            setRecyclerView(rvListUser)
            listener = object: InfiniteRecycleViewPagingAdapter.PagingAdapterListener<User> {
                override fun loadMore(page: Int) {

                }

                override fun onItemClick(item: User) {
                }

                override fun onStateLoadingChange(isLoading: Boolean) {
                }

            }
        }
    }

    private fun getUser() {
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(s: DataSnapshot) {

                s.children.forEach {
                    Log.d("Testing", it.toString())
                    val user = it.child("users").getValue(User::class.java)
                    if (user != null) {
                        adapter.addData(listUser)
                    }
                }
                rvListUser.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun onBackPressed() {
        doLogout()
    }

    private fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


}