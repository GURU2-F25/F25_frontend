package com.example.f25_frontend.ui.explore

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.R
import com.example.f25_frontend.databinding.FragmentExploreUserBinding
import com.example.f25_frontend.model.CategoryDto
import com.example.f25_frontend.model.UserDto
import com.example.f25_frontend.adapter.WeekAdapter
import com.example.f25_frontend.adapter.CategoryAdapter
import com.example.f25_frontend.adapter.ExploreUserCategoryAdapter
import com.example.f25_frontend.model.TaskDto
import com.example.f25_frontend.utils.ApiClient
import com.example.f25_frontend.utils.RetrofitUtil
import com.example.f25_frontend.viewmodel.CategoryViewModel
import com.example.f25_frontend.viewmodel.ExploreUserViewModel
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.time.LocalDate

/*
    @Author 조수연
    유저 상세 탐색 기능 - 화면
*/
@Suppress("DEPRECATION")
class ExploreUserFragment : Fragment() {
    private lateinit var weekRecyclerView: RecyclerView
    private lateinit var tvMonthYear: TextView
    private lateinit var btnPrevWeek: ImageButton
    private lateinit var btnNextWeek: ImageButton
    private lateinit var btnEditCategory: ImageButton
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryProgressContainer: LinearLayout
    private lateinit var tvSelectedDateProgress: TextView

    private lateinit var weekAdapter: WeekAdapter
    private lateinit var categoryAdapter: ExploreUserCategoryAdapter

    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private var selectedDate: LocalDate = LocalDate.now()
    private var currentWeekStart: LocalDate = LocalDate.now().with(DayOfWeek.MONDAY)

    private var _binding: FragmentExploreUserBinding? = null
    private val binding get() = _binding!!
    private val exploreUserViewModel: ExploreUserViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreUserBinding.inflate(inflater, container, false)
        return binding.root
    }
//    유저 정보 데이터 바인딩 함수
    fun initUserInfoLayout(user: UserDto){
        /*백엔드 이미지 업로드 미구현으로 주석 처리
        binding.imgProfile.setImageURI(Uri.parse(user.profileImage))*/
        binding.tvUserName.text=user.userName
        binding.tvUserId.text=user.id
        binding.tvFollowing.text="팔로잉 "+user.following?.size
        binding.tvFollower.text="팔로워 "+user.followers?.size

        if (user.followers != null) {
            if(user.followers.isNotEmpty()){
                for (index in user.followers.indices)
                    if (user.followers[index] != MyApplication.prefs.getString("id"))
                        setFollowBtn(user)
                    else if (user.followers[index] == MyApplication.prefs.getString("id"))
                        setUnFollowBtn(user)
            }
            else{
                setFollowBtn(user)
            }
        }
    }
    @SuppressLint("SetTextI18n")
    fun setFollowBtn(user: UserDto){
        if(user.followers!=null)
            binding.tvFollower.text="팔로워 "+ user.followers.size.toString()
        binding.btnFollow.text="팔로우 하기"
        binding.btnFollow.setOnClickListener{
            actionFollow(user)
        }
    }
    @SuppressLint("SetTextI18n")
    fun setUnFollowBtn(user: UserDto){
        if(user.followers!=null)
            binding.tvFollower.text="팔로워 "+ user.followers.size.plus(1).toString()
        binding.btnFollow.text="팔로잉 중"
        binding.btnFollow.setOnClickListener{
            actionUnFollow(user)
        }
    }
    private fun actionFollow(user: UserDto) {
        val service: RetrofitUtil = ApiClient.getAuthApiClient().create(RetrofitUtil::class.java)
        service.follow(user.id)
            .enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>,
                    response: Response<JsonElement>
                ) {
                    if (response.code() == 200) {
                        /* 백엔드로부터 요청 성공 시 데이터 무결성을 위해 user 정보를 반환 요청하였으나 엔드포인트 수정이 되지 않음*/
                        setUnFollowBtn(user)
                    } else if (response.code() == 400) {
                        /* 백엔드로부터 statusCode 및 message 반환 요청하였으나 엔드포인트 수정이 되지 않음*/
                        Toast.makeText(requireContext(), response.message().toString(), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다.", Toast.LENGTH_SHORT).show()
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
                        /* 백엔드로부터 요청 성공 시 데이터 무결성을 위해 user 정보를 반환 요청하였으나 엔드포인트 수정이 되지 않음*/
                        setFollowBtn(user)
                    }
                    else if(response.code()==400){
                        /* 백엔드로부터 statusCode 및 message 반환 요청하였으나 엔드포인트 수정이 되지 않음*/
                        Toast.makeText(requireContext(), response.message().toString(), Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버와의 통신에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user: UserDto = arguments?.getSerializable("exploreUser") as UserDto
        weekRecyclerView = view.findViewById(R.id.weekRecyclerView)
        tvMonthYear = view.findViewById(R.id.tvMonthYear)
        btnPrevWeek = view.findViewById(R.id.btnPrevWeek)
        btnNextWeek = view.findViewById(R.id.btnNextWeek)
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)
        categoryProgressContainer = view.findViewById(R.id.categoryProgressContainer)
        tvSelectedDateProgress = view.findViewById(R.id.tvSelectedDateProgress)

        setupWeekView()
        setupCategoryList()

        exploreUserViewModel.updateUser(user)
        initUserInfoLayout(user)

        val service: RetrofitUtil = ApiClient.getAuthApiClient().create(RetrofitUtil::class.java)
        service.getCategory(user.id)
            .enqueue(object : Callback<List<CategoryDto>> {
                override fun onResponse(
                    call: Call<List<CategoryDto>>,
                    response: Response<List<CategoryDto>>
                ) {
                    if (response.body() != null) {
                        val respCategoryList: List<CategoryDto> = response.body()!!
                        /*@FIXME FROM조수연 TO김소연
                                백엔드 반환값 내 tasksByDate가 비직렬화 과정 중 null 발생하여 초기화 작업 필요. iterator를 통해 임시 조치하였음.*/
                        val iteratorCategoryList = respCategoryList.iterator()
                        while (iteratorCategoryList.hasNext()) {
                            val iterCateNext = iteratorCategoryList.next()
                            iterCateNext.tasksByDate = mutableMapOf()
                        }
                        /*@FIXME FROM조수연 TO김소연
                            updateCategories 함수 내에서 불러온 카테고리 name값이 변경되지않음. (ExploreUserCategoryAdapter.kt 확인)
                             또한 백엔드측에서 반환 한 color값이 정상 작동하는 지 확인 후 수정 필요.*/
                        categoryAdapter.updateCategories(respCategoryList)
//                      updateCategoryProgress(respCategoryList)

                        service.getTodo(user.id, selectedDate.toString())
                            .enqueue(object : Callback<List<TaskDto>> {
                                override fun onResponse(
                                    call: Call<List<TaskDto>>,
                                    response: Response<List<TaskDto>>
                                ) {
                                    if (response.body() != null) {
                                        val respTaskList: List<TaskDto> = response.body()!!
                                        val iteratorTaskList = respTaskList.iterator()
                                        while (iteratorTaskList.hasNext()) {
                                            var iterTaskNext = iteratorTaskList.next()
                                            /*@FIXME FROM조수연 TO김소연
                                                respTaskList: List<TaskDto>에서 카테고리별로 dataList를 분리한 후
                                                respTaskList: List<CategoryDto> 내부 CategoryDto들과 매칭되는 dataList를 tasksByDate에 SET해주세요.
                                                TODOFragment쪽도 동일하게 적용되어 해당 부분 해결 되어야 완성이 가능합니다.
                                             */
                                        }
                                    }
                                }
                                override fun onFailure(
                                    call: Call<List<TaskDto>>,
                                    t: Throwable
                                ) {
                                    TODO("Not yet implemented")
                                }
                        })
                    }
                }
                override fun onFailure(call: Call<List<CategoryDto>>, t: Throwable) {
//                    TODO("Not yet implemented")
                }
            })

        categoryViewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            selectedDate = date
            weekAdapter.updateSelectedDate(date)
            categoryAdapter.updateSelectedDate(date)

            Log.d("SELECTEDDATE ::: ", date.toString())

        }

        categoryViewModel.categoriesForSelectedDate.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateCategories(categories)
            updateCategoryProgress(categories)
        }

        btnPrevWeek.setOnClickListener { shiftWeek(-1) }
        btnNextWeek.setOnClickListener { shiftWeek(1) }
    }

    @SuppressLint("SetTextI18n")
    private fun setupWeekView() {
        val weekList = (0 until 7).map { currentWeekStart.plusDays(it.toLong()) }
        tvMonthYear.text = "${currentWeekStart.year}년 ${currentWeekStart.monthValue}월"
        val screenWidth = resources.displayMetrics.widthPixels

        weekAdapter = WeekAdapter(weekList, screenWidth) { date ->
            categoryViewModel.updateSelectedDate(date)
            refreshTaskListForDate(date)
        }

        weekRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        weekRecyclerView.adapter = weekAdapter
    }

    private fun setupCategoryList() {
        categoryAdapter = ExploreUserCategoryAdapter(
            categories = emptyList(),
            selectedDate = selectedDate,
            onAddTaskClick = { },
            onTaskChecked = { },
            onTaskDeleted = { }
        )

        categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        categoryRecyclerView.adapter = categoryAdapter
    }

    private fun shiftWeek(weeks: Long) {
        currentWeekStart = currentWeekStart.plusWeeks(weeks)
        setupWeekView()
        categoryViewModel.updateSelectedDate(currentWeekStart)
    }

    private fun refreshTaskListForDate(date: LocalDate) {}

    @SuppressLint("SetTextI18n")
    private fun updateCategoryProgress(categories: List<CategoryDto>) {
        categoryProgressContainer.removeAllViews()

        // 전체 목표 달성률 계산
        val totalTasks = categories.sumOf { it.tasksByDate[selectedDate]?.size ?: 0 }
        val doneTasks = categories.sumOf { it.tasksByDate[selectedDate]?.count { it -> it.isDone } ?: 0 }

        if (totalTasks > 0) {
            val percent = (doneTasks * 100) / totalTasks
            tvSelectedDateProgress.text = "${selectedDate.dayOfMonth}일의 목표 달성률 ${percent}%"
            tvSelectedDateProgress.setTextColor(Color.BLACK)
        } else {
            tvSelectedDateProgress.text = "${selectedDate.dayOfMonth}일은 등록된 일정이 없습니다"
            tvSelectedDateProgress.setTextColor(Color.GRAY)
        }

        for (category in categories) {
            val tasks = category.tasksByDate[selectedDate] ?: emptyList()
            if (tasks.isEmpty()) continue

            val done = tasks.count { it.isDone }
            val percent = (done * 100) / tasks.size

            val progressLayout = LayoutInflater.from(context)
                .inflate(R.layout.item_category_progress, categoryProgressContainer, false)

            val tvName = progressLayout.findViewById<TextView>(R.id.tvCategoryName)
            val progressBar = progressLayout.findViewById<ProgressBar>(R.id.progressBar)
            val tvPercent = progressLayout.findViewById<TextView>(R.id.tvProgressPercent)

            tvName.text = category.name
            tvName.setTextColor(category.color)

            progressBar.progress = percent
            progressBar.progressTintList = ColorStateList.valueOf(category.color) // ✅ 색상 적용
            tvPercent.text = "$percent%"
            tvPercent.setTextColor(category.color)

            categoryProgressContainer.addView(progressLayout)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
