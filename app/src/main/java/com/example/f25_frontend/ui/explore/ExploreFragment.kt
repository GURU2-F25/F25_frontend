package com.example.f25_frontend.ui.explore

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.R
import com.example.f25_frontend.databinding.FragmentExploreBinding
import com.example.f25_frontend.model.UserDto
import com.example.f25_frontend.utils.ApiClient
import com.example.f25_frontend.utils.retrofitUtil
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        Log.d("resultExplore:", MyApplication.prefs.getString("access_token"))

//        아이디 검색창 텍스트 변경 시 비동기로 서버에 요청하여 해당 문자열로 시작하는 ID 검색하여 리스트업
        binding.exploreSearchText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("searchIdTextOnChanged", s.toString())

                val service: retrofitUtil = ApiClient.getAuthApiClient().create(retrofitUtil::class.java)

                service.searchUser(s.toString())
                    .enqueue(object : Callback<List<UserDto>>{
                        override fun onResponse(call: Call<List<UserDto>>, response: Response<List<UserDto>>) {
                            Log.d("result coroutines", response.body().toString())

                            /*
                            val len = response.body().count()
                            mutableListOf()를 사용하는 Adapter 만들어서 데이터 소팅
                            https://velog.io/@simsubeen/Android-Kotlin-ListView

                            onClickListener(){
                                val intent = setIntent()
                                intent.set("id")
                                intent.set("userName")
                                intent.put("follower")
                                intent.put("following")
                                intent.put("profileImage")

                                findNavController().navigate(R.id.action_explore_to_explore_user, intent)                            }
                            */
//                            binding.userListLayout.addView()
//                            BINDING LAYOUT && ADD ITEM (USER LIST)
//                            onClickListener SET && request UserDetail -> onResponse NAVIGATE DETAIL PAGE

                        }

                        override fun onFailure(call: Call<List<UserDto>>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
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
