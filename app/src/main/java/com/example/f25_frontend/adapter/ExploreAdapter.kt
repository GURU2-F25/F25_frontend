package com.example.f25_frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.R
import com.example.f25_frontend.model.UserDto

/*
    @Author 조수연
    유저 목록 탐색 기능 - 데이터 바인딩 어댑터
*/
class ExploreAdapter(
    private var userList: List<UserDto>,
    private val onExploreUserClick: (UserDto) -> Unit,
    private val onExploreUserFollowClick: (UserDto) -> Unit
) : RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {
    inner class ExploreViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val itemLayout: LinearLayout = view.findViewById(R.id.exploreItemLayout)
        val userName: TextView = view.findViewById(R.id.exploreUserName)
        val id: TextView = view.findViewById(R.id.exploreId)
        val profileImage: ImageView = view.findViewById(R.id.exploreProfileImage)
        val followBtn: Button = view.findViewById(R.id.exploreFollowBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ExploreViewHolder(view)
    }
//  유저 목록 데이터 바인드
    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val user = userList[position]
        holder.id.text = user.id
        holder.userName.text = user.userName
//  @TODO 백엔드 프로필 이미지 저장 엔드포인트 구현 시 주석 해제
//        holder.profileImage.setImageURI(user.profileImage?.toUri())
        holder.itemLayout.setOnClickListener {
            onExploreUserClick(user)
        }
        holder.followBtn.setOnClickListener{
            onExploreUserFollowClick(user)
        }
//  탐색 해당 유저가 사용자를 팔로우 하고 있을 경우 팔로잉 표시
        holder.followBtn.text = "팔로우"
        if(user.followers!=null) {
            for (index in user.followers.indices) {
                if (user.followers.get(index).equals(MyApplication.prefs.getString("id"))) {
                    holder.followBtn.text = "팔로잉 중"
                }
            }
        }
    }

    override fun getItemCount(): Int = userList.size

//  @Ignore("데이터 수정 시 UI 반영을 위해 반드시 notify")
    fun updateUserList(newUserList: List<UserDto>){
        this.userList = newUserList
        notifyDataSetChanged()
    }
    fun resetUserList(){
        userList = emptyList()
        notifyDataSetChanged()
    }

}

