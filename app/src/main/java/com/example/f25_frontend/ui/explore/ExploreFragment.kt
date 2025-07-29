package com.example.f25_frontend.ui.explore

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.databinding.FragmentExploreBinding
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
                service.getUser(s.toString())
                    .enqueue(object : Callback<JsonElement>{
                        override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                            Log.d("result coroutines", response.body().toString())
                        }

                        override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                            Log.d("result coroutines failed", t.toString())
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
