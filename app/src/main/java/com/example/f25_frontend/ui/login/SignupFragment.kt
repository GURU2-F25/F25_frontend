package com.example.f25_frontend.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.f25_frontend.R
import com.example.f25_frontend.databinding.FragmentSignupBinding
import com.example.f25_frontend.model.UserDto
import com.example.f25_frontend.utils.ApiClient
import com.example.f25_frontend.utils.RetrofitUtil
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
    @Author 조수연
    회원가입 화면
*/
class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private var isIdAvailable = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        initValidationCheck()

        return binding.root
    }
    private fun initValidationCheck(){
//        회원가입 정보 검증
        binding.btnSignupConfirm.setOnClickListener {
            val id = binding.editTextSignupId.text.toString().trim()
            val pw = binding.editTextSignupPassword.text.toString().trim()
            val pwConfirm = binding.editTextSignupPasswordConfirm.text.toString().trim()
            val agreed = binding.checkBoxTerms.isChecked

            if (id.isEmpty() || pw.isEmpty() || pwConfirm.isEmpty()) {
                Toast.makeText(requireContext(), "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pw != pwConfirm) {
                Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!agreed) {
                Toast.makeText(requireContext(), "약관에 동의해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!isIdAvailable){
                Toast.makeText(requireContext(), "아이디 중복검사가 필요합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createUser(id, pw)
        }

        binding.btnCheckId.setOnClickListener{
            val id = binding.editTextSignupId.text.toString().trim()
            val service: RetrofitUtil = ApiClient.getNoAuthApiClient().create(RetrofitUtil::class.java)
            service.isAvailableId(id)
                .enqueue(object : Callback<JsonElement> {
                    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                        if(response.code()==200){
                            isIdAvailable = true
                            Toast.makeText(requireContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_LONG).show()
                        }
                        else if(response.code()==400){
                            Toast.makeText(requireContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                        }
//                        @TODO 엔드포인트 status_code 명세서 업로드 시 예외 처리 개발 예정
                    }
                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다. 네트워크 통신 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        Log.w("ID VALIDATION REQUEST ERROR", t.toString())
                    }

                })
        }
    }
    private fun createUser(userId: String, password: String) {
        val newUser = UserDto(id = userId, password = password, "","","","","", emptyList(),
            emptyList(), "")
        val service: RetrofitUtil = ApiClient.getNoAuthApiClient().create(RetrofitUtil::class.java)
        service.join(newUser)
            .enqueue(object : Callback<UserDto> {
                override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                    if(response.isSuccessful){
                        val result = response.body()
//                        @TODO 현재 백엔드에서 저장 후 성공 여부만 반환. 프로필 작성 시 데이터 무결성을 위하여 수정 요청.
                        val bundle = Bundle()
                        bundle.putSerializable("user", newUser)
                        /*
                        var user = response : <UserDto> -- Deserialization
                        bundle.putSerializable("user", user)
                        */
                        findNavController().navigate(R.id.action_signup_to_profile, bundle)
                        Log.d("resultSuccess",result.toString())
                    }else{
                        Toast.makeText(requireContext(), "회원가입에 실패하였습니다", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                        Log.w("resultFail", response.toString())
                    }
                }
                override fun onFailure(call: Call<UserDto>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다. 네트워크 통신 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    Log.w("requestFail", t.toString())
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
