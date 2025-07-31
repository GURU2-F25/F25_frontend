package com.example.f25_frontend.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.R
import com.example.f25_frontend.databinding.FragmentLoginBinding
import com.example.f25_frontend.model.LoginRequest
import com.example.f25_frontend.model.UserDto
import com.example.f25_frontend.utils.ApiClient
import com.example.f25_frontend.utils.RetrofitUtil
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
/*
    @Author 조수연
    로그인 기능 - 화면
*/
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        // 로그인 버튼
        binding.btnLogin.setOnClickListener {
            val id = binding.editTextId.text.toString().trim()
            val pw = binding.editTextPassword.text.toString().trim()
            if (id.isNotEmpty() && pw.isNotEmpty()) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val deviceToken = task.result
                        login(id, pw, deviceToken)
                    } else {
                        Toast.makeText(requireContext(), "FCM 토큰을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                        login(id, pw, "")
                    }
                }
            } else {
                Toast.makeText(requireContext(), "아이디, 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

        }

        // 회원가입으로 이동
        binding.btnGoSignup.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }

        return binding.root
    }
    private fun login(userId: String, password: String, deviceToken: String) {
//        findNavController().navigate(R.id.action_login_to_todo)

        val service: RetrofitUtil = ApiClient.getNoAuthApiClient().create(RetrofitUtil::class.java)
        val newUser = LoginRequest(id = userId, password = password, deviceToken=deviceToken)
        Log.d("userData", Gson().toJson(newUser))
        service.login(newUser)
            .enqueue(object : Callback<UserDto> {
                override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                    if(response.isSuccessful){
                        val result:UserDto? = response.body()
                        Log.d("resultLogin",result.toString())
                        MyApplication.prefs.setString("uId", result?.uId)
                        MyApplication.prefs.setString("id", result?.id)
                        MyApplication.prefs.setString("userName", result?.userName)
                        MyApplication.prefs.setString("access_token", result?.access_token)
                        MyApplication.prefs.setString("token_type", result?.token_type)
                        findNavController().navigate(R.id.action_login_to_todo)
                    }else{
//                        if(response.headers().get("statusCode")==200){
//
//                        }else if(response.headers().get("statusCode")==400){
//
//                        }else if(response.headers().get("statusCode")==404){
//
//                        }
                        Toast.makeText(requireContext(), "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
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
