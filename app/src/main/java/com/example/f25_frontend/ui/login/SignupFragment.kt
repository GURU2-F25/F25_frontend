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
import com.example.f25_frontend.utils.retrofitUtil
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private var isIdAvailable = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

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

            if(id.equals("test1234")){
                Toast.makeText(requireContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // createUser(id, pw) 사용자 정보 저장
            createUser(id, pw)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun createUser(userId: String, password: String) {
        val service: retrofitUtil = ApiClient.getNoAuthApiClient().create(retrofitUtil::class.java)
        val newUser = UserDto(id=userId, password=password, userName="userNameTest",profileImage="string", "","","")
        Log.d("userData",Gson().toJson(newUser))
        service.join(newUser)
            .enqueue(object : Callback<UserDto> {
                override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                    if(response.isSuccessful){
                        val result = response.body()
                        findNavController().navigate(R.id.action_signup_to_profile)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
