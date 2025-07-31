package com.example.f25_frontend.ui.explore

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.R
import com.example.f25_frontend.adapter.ExploreAdapter
import com.example.f25_frontend.databinding.FragmentExploreBinding
import com.example.f25_frontend.model.UserDto
import com.example.f25_frontend.utils.ApiClient
import com.example.f25_frontend.utils.RetrofitUtil
import com.example.f25_frontend.viewmodel.ExploreViewModel
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
    @Author 조수연
    유저 목록 탐색 기능 - 화면
*/
class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private lateinit var exploreRecyclerView: RecyclerView
    private lateinit var exploreAdapter: ExploreAdapter
    private val exploreViewModel: ExploreViewModel by activityViewModels()
    private lateinit var nowSearchText: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        exploreRecyclerView = binding.rvExplore

        setupExploreList()
//        아이디 검색창 텍스트 변경 시 비동기로 서버에 요청하여 해당 문자열로 시작하는 ID 검색하여 리스트업
        binding.exploreSearchText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nowSearchText = s.toString()
                exploreAdapter.resetUserList()
                exploreSearchUser(nowSearchText)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    return binding.root
}
//        화면 생성 시 어댑터 연결
    private fun setupExploreList() {
        exploreAdapter = ExploreAdapter(
            userList = emptyList(),
            onExploreUserClick = {
                user -> onExploreUserClick(user)
            },
            onExploreUserFollowClick = {
                user -> onExploreUserFollowClick(user)
            }
        )
        exploreRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        exploreRecyclerView.adapter = exploreAdapter
    }

//  해당 문자열로 시작하는 유저 정보 리스트 서버 조회
    private fun exploreSearchUser(s: String) {
        val service: RetrofitUtil = ApiClient.getAuthApiClient().create(RetrofitUtil::class.java)
        service.searchUser(s)
            .enqueue(object : Callback<List<UserDto>>{
                override fun onResponse(call: Call<List<UserDto>>, response: Response<List<UserDto>>) {
                    if (response.code()==200){
                        if (response.body() != null) {
                            val userList : List<UserDto> = response.body()!!
                            exploreViewModel.updateUserList(userList)
                            exploreAdapter.updateUserList(userList)
                        }
                    }
//                    @TODO 엔드포인트 status_code 명세서 업로드 시 예외 처리 개발 예정
                    /*else {

                    }*/
                }
                override fun onFailure(call: Call<List<UserDto>>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다. 네트워크 통신 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    Log.w("ExploreSearchUser REQUEST ERROR", t.toString())
                }
            })
    }

//    조회 유저 레이어 클릭 시 해당 유저 상세정보페이지로 이동 (UserDto Serialized Data로 데이터 전달)
    private fun onExploreUserClick(user: UserDto) {
        val bundle = Bundle()
        bundle.putSerializable("exploreUser", user)
        findNavController().navigate(R.id.action_explore_to_explore_user, bundle)
    }

//    팔로우 버튼 클릭 시 서버에 해당 유저 팔로우 요청
    private fun actionFollow(user: UserDto) {
        val service: RetrofitUtil = ApiClient.getAuthApiClient().create(RetrofitUtil::class.java)
        service.follow(user.id)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>,
                    response: Response<JsonElement>
                ) {
                    if (response.code() == 200) {
                        exploreSearchUser(nowSearchText)
                    } else if (response.code() == 400) {
//                        @TODO 엔드포인트 status_code 명세서 업로드 시 예외 처리 개발 예정
                        Toast.makeText(requireContext(), response.message().toString(), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다. 네트워크 통신 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    Log.w("Explore follow action REQUEST ERROR", t.toString())
                }
            })
    }
    private fun actionUnFollow(user:UserDto){
        val service: RetrofitUtil = ApiClient.getAuthApiClient().create(RetrofitUtil::class.java)
        service.unFollow(user.id)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>,
                    response: Response<JsonElement>
                ) {
                    if(response.code()==200){
                        exploreSearchUser(nowSearchText)
                    }
                    else if(response.code()==400){
//                        @TODO 엔드포인트 status_code 명세서 업로드 시 예외 처리 개발 예정
                        Toast.makeText(requireContext(), response.message().toString(), Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다. 네트워크 통신 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    Log.w("Explore follow action REQUEST ERROR", t.toString())
                }
            })
    }
//    사용자가 탐색 유저를 팔로우 여부 체크 후 팔로우/언팔로우 요청 전송
    private fun onExploreUserFollowClick(user: UserDto) {
        if (user.followers != null) {
            if(user.followers.isEmpty()) actionFollow(user)
            else {
                for (index in user.followers.indices) {
                    if (! user.followers.get(index).equals(MyApplication.prefs.getString("id"))) {
                        actionFollow(user)
                    } else if (user.followers.get(index).equals(MyApplication.prefs.getString("id"))) {
                        actionUnFollow(user)
                    }
                }
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}