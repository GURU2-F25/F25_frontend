package com.example.f25_frontend.ui.explore

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.R
import com.example.f25_frontend.databinding.FragmentExploreBinding
import com.example.f25_frontend.model.UserDto
import com.example.f25_frontend.utils.ApiClient
import com.example.f25_frontend.utils.retrofitUtil
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val LifecycleOwner.lifecycleScope: LifecycleCoroutineScope
        get() = lifecycle.coroutineScope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        Log.d("resultExplore:", MyApplication.prefs.getString("access_token"))

        binding.exploreSearchText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("searchIdTextOnChanged", s.toString())

                val service: retrofitUtil = ApiClient.getAuthApiClient().create(retrofitUtil::class.java)
                lifecycleScope.launch {
                    val response = service.getUser(s.toString())
                    Log.d("coroutines result", response.toString())
                    val tv: TextView = TextView(inflater.context)
                    tv.setText(response.toString())
                    binding.layoutResultContainer.addView(tv)
                }
            }
//
//                    service.getUser(s.toString())
//                    .enqueue(object : Callback<UserDto> {
//                    override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
//                        if(response.isSuccessful){
//                            val result = response.body()
//                            findNavController().navigate(R.id.action_login_to_todo)
//                            Log.d("resultSuccess",result.toString())
//                        }else{
//                    //                        if(response.headers().get("statusCode")==200){
//                    //
//                    //                        }else if(response.headers().get("statusCode")==400){
//                    //
//                    //                        }else if(response.headers().get("statusCode")==404){
//                    //
//                    //                        }
//                            Toast.makeText(requireContext(), "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
//                            findNavController().popBackStack()
//                            Log.w("resultFail", response.toString())
//                        }
//                    }
//                    override fun onFailure(call: Call<UserDto>, t: Throwable) {
//                        Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다. 네트워크 통신 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
//                        Log.w("requestFail", t.toString())
//                    }
//                    })
//
//                    }

            override fun afterTextChanged(s: Editable?) {
            //                TODO("Not yet implemented")
            }
        })
// 로그인 버튼
//        binding.btnProfileSave.setOnClickListener {
//            val id = binding.editTextId.text.toString().trim()
//            val pw = binding.editTextPassword.text.toString().trim()
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
