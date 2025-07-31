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
import kotlin.random.Random

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

    private var globalCategories: List<CategoryDto> = listOf()
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

        testAssetsInit()

       /*
        val service: RetrofitUtil = ApiClient.getAuthApiClient().create(RetrofitUtil::class.java)
        service.getCategory(user.id)
            .enqueue(object : Callback<List<CategoryDto>> {
                override fun onResponse(
                    call: Call<List<CategoryDto>>,
                    response: Response<List<CategoryDto>>
                ) {
                    if (response.body() != null) {
                        val respCategoryList: List<CategoryDto> = response.body()!!
                        *//*@FIXME FROM조수연 TO김소연
                                백엔드 반환값 내 tasksByDate가 비직렬화 과정 중 null 발생하여 초기화 작업 필요. iterator를 통해 임시 조치하였음.*//*
                        val iteratorCategoryList = respCategoryList.iterator()
                        while (iteratorCategoryList.hasNext()) {
                            val iterCateNext = iteratorCategoryList.next()
                            iterCateNext.tasksByDate = mutableMapOf()
                        }
                        *//*@FIXME FROM조수연 TO김소연
                            updateCategories 함수 내에서 불러온 카테고리 name값이 변경되지않음. (ExploreUserCategoryAdapter.kt 확인)
                             또한 백엔드측에서 반환 한 color값이 정상 작동하는 지 확인 후 수정 필요.*//*
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
                                            *//*@FIXME FROM조수연 TO김소연
                                                respTaskList: List<TaskDto>에서 카테고리별로 dataList를 분리한 후
                                                respTaskList: List<CategoryDto> 내부 CategoryDto들과 매칭되는 dataList를 tasksByDate에 SET해주세요.
                                                TODOFragment쪽도 동일하게 적용되어 해당 부분 해결 되어야 완성이 가능합니다.
                                             *//*
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
            })*/

        categoryViewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            selectedDate = date
            weekAdapter.updateSelectedDate(date)
            categoryAdapter.updateSelectedDate(date)

            Log.d("SELECTEDDATE ::: ", date.toString())

        }

        categoryViewModel.categoriesForSelectedDate.observe(viewLifecycleOwner) { categories ->
            testAssetsInit()
            categoryAdapter.updateCategories(globalCategories)
            updateCategoryProgress(globalCategories)
//            categoryAdapter.updateCategories(categories)
//            updateCategoryProgress(categories)
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
            categories = globalCategories,
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
    fun testAssetsInit(){
        val colorArr :List<Int> = mutableListOf<Int>(Color.parseColor("#FCDC58"), Color.parseColor("#DCDC85"), Color.parseColor("#9B9BFF"), Color.parseColor("#A7D397"), Color.parseColor("#FF9B9B"))

        val c1 = CategoryDto(id = "1a", userId = "allenjeffrey4971", name = "과제(전공)", color = colorArr[0], tasksByDate = mutableMapOf())
        val c2 = CategoryDto(id = "2a", userId = "allenjeffrey4971", name = "과제(교양)", color = colorArr[1], tasksByDate = mutableMapOf())
        val c3 = CategoryDto(id = "3a", userId = "allenjeffrey4971", name = "자기계발", color = colorArr[2], tasksByDate = mutableMapOf())
        val c4 = CategoryDto(id = "4a", userId = "allenjeffrey4971", name = "운동", color = colorArr[3], tasksByDate = mutableMapOf())
        val c5 = CategoryDto(id = "5a", userId = "allenjeffrey4971", name = "취미", color = colorArr[4], tasksByDate = mutableMapOf())

        val t1 = TaskDto(title= "팀플 A", date= LocalDate.parse("2025-07-31"), categoryDto = c1, isDone=true)
        val t2 = TaskDto(title= "팀플 B", date= LocalDate.parse("2025-07-31"), categoryDto = c1, isDone=true)
        val t3 = TaskDto(title= "팀플 C", date= LocalDate.parse("2025-07-31"), categoryDto = c1)
        val t4 = TaskDto(title= "협업 A", date= LocalDate.parse("2025-07-31"), categoryDto = c2)
        val t5 = TaskDto(title= "협업 B", date= LocalDate.parse("2025-07-31"), categoryDto = c2, isDone=true)
        val t6 = TaskDto(title= "협업 C", date= LocalDate.parse("2025-07-31"), categoryDto = c2)
        val t7 = TaskDto(title= "솔플 A", date= LocalDate.parse("2025-07-31"), categoryDto = c3, isDone=true)
        val t8 = TaskDto(title= "솔플 A", date= LocalDate.parse("2025-07-31"), categoryDto = c3, isDone=true)
        val t9 = TaskDto(title= "솔플 A", date= LocalDate.parse("2025-07-31"), categoryDto = c3, isDone=true)
        val t10 = TaskDto(title= "갠플 A", date= LocalDate.parse("2025-07-31"), categoryDto = c4)
        val t11 = TaskDto(title= "갠플 B", date= LocalDate.parse("2025-07-31"), categoryDto = c4)
        val t12 = TaskDto(title= "갠플 C", date= LocalDate.parse("2025-07-31"), categoryDto = c4)
        val t13 = TaskDto(title= "필수 수행 A", date= LocalDate.parse("2025-07-31"), categoryDto = c5)
        val t14 = TaskDto(title= "필수 수행 B", date= LocalDate.parse("2025-07-31"), categoryDto = c5, isDone=true)
        val t15 = TaskDto(title= "필수 수행 C", date= LocalDate.parse("2025-07-31"), categoryDto = c5)

        val allTaskList:MutableList<TaskDto> = mutableListOf(t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15)


        var mL1:MutableList<TaskDto> = mutableListOf()
        for(index in 0..Random.nextInt(5)){
            mL1.add(allTaskList[Random.nextInt(15)])
        }
        var mL2:MutableList<TaskDto> = mutableListOf()
        for(index in 0..Random.nextInt(5)){
            mL2.add(allTaskList[Random.nextInt(15)])
        }
        var mL3:MutableList<TaskDto> = mutableListOf()
        for(index in 0..Random.nextInt(5)){
            mL3.add(allTaskList[Random.nextInt(15)])
        }
        var mL4:MutableList<TaskDto> = mutableListOf()
        for(index in 0..Random.nextInt(5)){
            mL4.add(allTaskList[Random.nextInt(15)])
        }
        var mL5:MutableList<TaskDto> = mutableListOf()
        for(index in 0..Random.nextInt(5)){
            mL5.add(allTaskList[Random.nextInt(15)])
        }
        c1.tasksByDate.put(LocalDate.parse("2025-07-31"),mL1)
        c2.tasksByDate.put(LocalDate.parse("2025-07-31"),mL2)
        c3.tasksByDate.put(LocalDate.parse("2025-07-31"),mL3)
        c4.tasksByDate.put(LocalDate.parse("2025-07-31"),mL4)
        c5.tasksByDate.put(LocalDate.parse("2025-07-31"),mL5)

        mL1 = mutableListOf()
        mL2 = mutableListOf()
        mL3 = mutableListOf()
        mL4 = mutableListOf()
        mL5 = mutableListOf()
        for(index in 0..Random.nextInt(5)){
            mL1.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL2.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL3.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL4.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL5.add(allTaskList[Random.nextInt(15)])
        }
        c1.tasksByDate.put(LocalDate.parse("2025-07-30"),mL1)
        c2.tasksByDate.put(LocalDate.parse("2025-07-30"),mL2)
        c3.tasksByDate.put(LocalDate.parse("2025-07-30"),mL3)
        c4.tasksByDate.put(LocalDate.parse("2025-07-30"),mL4)
        c5.tasksByDate.put(LocalDate.parse("2025-07-30"),mL5)

        mL1 = mutableListOf()
        mL2 = mutableListOf()
        mL3 = mutableListOf()
        mL4 = mutableListOf()
        mL5 = mutableListOf()
        for(index in 0..Random.nextInt(5)){
            mL1.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL2.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL3.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL4.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL5.add(allTaskList[Random.nextInt(15)])
        }
        c1.tasksByDate.put(LocalDate.parse("2025-07-29"),mL1)
        c2.tasksByDate.put(LocalDate.parse("2025-07-29"),mL2)
        c3.tasksByDate.put(LocalDate.parse("2025-07-29"),mL3)
        c4.tasksByDate.put(LocalDate.parse("2025-07-29"),mL4)
        c5.tasksByDate.put(LocalDate.parse("2025-07-29"),mL5)
        mL1 = mutableListOf()
        mL2 = mutableListOf()
        mL3 = mutableListOf()
        mL4 = mutableListOf()
        mL5 = mutableListOf()
        for(index in 0..Random.nextInt(5)){
            mL1.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL2.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL3.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL4.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL5.add(allTaskList[Random.nextInt(15)])
        }
        c1.tasksByDate.put(LocalDate.parse("2025-07-28"),mL1)
        c2.tasksByDate.put(LocalDate.parse("2025-07-28"),mL2)
        c3.tasksByDate.put(LocalDate.parse("2025-07-28"),mL3)
        c4.tasksByDate.put(LocalDate.parse("2025-07-28"),mL4)
        c5.tasksByDate.put(LocalDate.parse("2025-07-28"),mL5)
        mL1 = mutableListOf()
        mL2 = mutableListOf()
        mL3 = mutableListOf()
        mL4 = mutableListOf()
        mL5 = mutableListOf()
        for(index in 0..Random.nextInt(5)){
            mL1.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL2.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL3.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL4.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL5.add(allTaskList[Random.nextInt(15)])
        }
        c1.tasksByDate.put(LocalDate.parse("2025-08-01"),mL1)
        c2.tasksByDate.put(LocalDate.parse("2025-08-01"),mL2)
        c3.tasksByDate.put(LocalDate.parse("2025-08-01"),mL3)
        c4.tasksByDate.put(LocalDate.parse("2025-08-01"),mL4)
        c5.tasksByDate.put(LocalDate.parse("2025-08-01"),mL5)
        mL1 = mutableListOf()
        mL2 = mutableListOf()
        mL3 = mutableListOf()
        mL4 = mutableListOf()
        mL5 = mutableListOf()
        for(index in 0..Random.nextInt(5)){
            mL1.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL2.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL3.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL4.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL5.add(allTaskList[Random.nextInt(15)])
        }
        c1.tasksByDate.put(LocalDate.parse("2025-08-02"),mL5)
        c2.tasksByDate.put(LocalDate.parse("2025-08-02"),mL4)
        c3.tasksByDate.put(LocalDate.parse("2025-08-02"),mL3)
        c4.tasksByDate.put(LocalDate.parse("2025-08-02"),mL2)
        c5.tasksByDate.put(LocalDate.parse("2025-08-02"),mL1)
        mL1 = mutableListOf()
        mL2 = mutableListOf()
        mL3 = mutableListOf()
        mL4 = mutableListOf()
        mL5 = mutableListOf()
        for(index in 0..Random.nextInt(5)){
            mL1.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL2.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL3.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL4.add(allTaskList[Random.nextInt(15)])
        }
        for(index in 0..Random.nextInt(5)){
            mL5.add(allTaskList[Random.nextInt(15)])
        }
        c1.tasksByDate.put(LocalDate.parse("2025-08-03"),mL3)
        c2.tasksByDate.put(LocalDate.parse("2025-08-03"),mL1)
        c3.tasksByDate.put(LocalDate.parse("2025-08-03"),mL5)
        c4.tasksByDate.put(LocalDate.parse("2025-08-03"),mL2)
        c5.tasksByDate.put(LocalDate.parse("2025-08-03"),mL4)

        globalCategories = listOf(c1,c2,c3,c4,c5)
        categoryAdapter.setTestObjects(globalCategories)
    }

}
