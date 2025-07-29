package com.example.f25_frontend.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.databinding.FragmentExploreUserBinding
import com.example.f25_frontend.utils.ApiClient
import com.example.f25_frontend.utils.retrofitUtil
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreUserFragment : Fragment() {

    private var _binding: FragmentExploreUserBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreUserBinding.inflate(inflater, container, false)

        /*
        val id = this.arguments?.get("id").toString()
        val userName = this.arguments?.get("userName").toString()
        val follower:List<String> = this.arguments?.get("follower")
        val following:List<String> = this.arguments?.get("following")
        val profileImage = this.arguments?.get("profileImage").toString()

        binding.viewId.text="\@"+id
        binding.viewUserName.text=userName
        binding.followerCount=follower.count()
        binding.followingCount=following.count()
        binding.profileImage.url=profileImage

//        default btnFollow = 팔로우&black
        for(i in 0..following.count()){
            if(following[i]==MyApplication.prefs.getString("id")){
                binding.btnFollow.color=white
                binding.btnFollow.text="팔로잉"
            }
        }

        val service: retrofitUtil = ApiClient.getAuthApiClient().create(retrofitUtil::class.java)
        service.getTasks(id).enqueue(object : Callback<List<Task>>{
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                TaskAdapter 사용해서 데이터 소팅 (todoFragment의 레이아웃 구조 가져오기)
            }
            override fun onFailure(call: Call<List<Task>>, t: Throwable) {

            }
        })

        binding.btnFollow.onClickListener(){
            if(binding.btnFollow.text="팔로우"){
                service.follow(id).enqueue(object : Callback<JsonElement>{
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
            else{
                service.unFollow(id).enqueue(object : Callback<JsonElement>{
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }*/


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
