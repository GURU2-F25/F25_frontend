package com.example.f25_frontend.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.f25_frontend.R
import com.example.f25_frontend.databinding.FragmentProfileBinding
/*
    @Author 조수연
    회원가입 후 프로필 이미지, 닉네임 설정 화면
*/
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        //        @TODO 무결성 직렬화 데이터 반환 및 user PUT 엔드포인트 추가 시 주석 해제
        /*var user: UserDto = arguments?.getSerializable("user") as UserDto
        val service: retrofitUtil = ApiClient.getNoAuthApiClient().create(retrofitUtil::class.java)
        service.update(user)
            .enqueue(object : Callback<UserDto> {
                override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                    if(response.isSuccessful){
                        val result = response.body()
//                        @TODO 현재 백엔드에서 저장 후 성공 여부만 반환. 데이터 무결성을 위하여 수정 요청.
                        var user = response : <UserDto> -- Deserialization
                        findNavController().navigate(R.id.action_profile_to_main, bundle)
                        Log.d("resultSuccess",result.toString())
                    }else{
//                        @TODO 엔드포인트 status_code 명세서 업로드 시 예외 처리 개발 예정
                        Toast.makeText(requireContext(), "회원가입에 실패하였습니다", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                        Log.w("resultFail", response.toString())
                    }
                }
                override fun onFailure(call: Call<UserDto>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다. 네트워크 통신 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    Log.w("requestFail", t.toString())
                }
            })*/

        binding.btnProfileSave.setOnClickListener {
            val name = binding.editTextProfileName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                findNavController().navigate(R.id.action_profile_to_todo)
            }
        }
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
