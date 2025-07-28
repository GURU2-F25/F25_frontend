package com.example.f25_frontend.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.f25_frontend.R
import com.example.f25_frontend.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        binding.btnMypageFollower.setOnClickListener(){
            findNavController().navigate(R.id.action_mypageFragment_to_mypageFollowerFragment)
        }
        binding.btnMypageFollowing.setOnClickListener(){
            findNavController().navigate(R.id.action_mypageFragment_to_mypageFollowingFragment)
        }
        // 로그인 버튼
//        binding.btnProfileSave.setOnClickListener {
////            val id = binding.editTextId.text.toString().trim()
////            val pw = binding.editTextPassword.text.toString().trim()
//            if (id.isNotEmpty() && pw.isNotEmpty()) {
//                findNavController().navigate(R.id.action_loginFragment_to_todoFragment)
//            }
//        }

//        // 회원가입으로 이동
//        binding.btnGoSignup.setOnClickListener {
//            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    fun getLogin() {
//        var connURL = "https://127.0.0.1/fastapi/user-data";
//        var url = URL(connURL);
//        val conn = url.openConnection() as HttpURLConnection;
//        conn.requestMethod = "GET";
//        conn.connectTimeout = 150000;
//        conn.readTimeout = 150000;
//
//        var charset = Charset.forName("UTF-8")
//        var br= BufferedReader(InputStreamReader(conn.inputStream, charset))
//        var sb = StringBuilder()
//        if (conn.responseCode == HttpURLConnection.HTTP_OK) {
//            while(true) {
//                val line = br.readLine() ?: break;
//                sb.append(line)
//            }
//        }
//
//        println(sb.toString())
//    }
}
