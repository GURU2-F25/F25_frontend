package com.example.f25_frontend.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.f25_frontend.R
import com.example.f25_frontend.databinding.FragmentSignupBinding

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
            // 회원가입 완료 → 프로필 설정 화면으로 이동
            findNavController().navigate(R.id.action_signupFragment_to_profileFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 아이디 입력값이 변경되면 다시 중복확인 필요 (초기화)
//        asdf asdf1 asdf12 asdf123 asdf1234
//        중복검사 버튼 누를 시 체크 or onFocus가 해제될 때 그러니까 다른 쪽으로 focus가 옮겨 갈 때 중복 검사
        /*binding.editTextSignupId.add온클릭이나 포커스Listener {
        isExistId? == true false 반환 해주는 callBack 짜주기
            isIdAvailable = false
        }*/


    }

    //회원 정보 Firestore에 저장
    private fun createUser(userId: String, password: String) {
        val newUser = hashMapOf(
            "userId" to userId,
            "password" to password, // 실제 배포 시 해싱 필요
            "nickname" to userId    // 초기 닉네임 = 아이디
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
