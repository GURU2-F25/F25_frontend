package com.example.f25_frontend.ui.todo

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.R
import com.example.f25_frontend.model.CategoryDto
import com.example.f25_frontend.adapter.CategorySettingAdapter
import com.example.f25_frontend.utils.ApiClient
import com.example.f25_frontend.utils.RetrofitUtil
import com.example.f25_frontend.viewmodel.CategoryViewModel
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDate.*

/*
    @Author 김소연, 조수연
    @TODO 카테고리 CRUD 서버 연동 개발 예정
*/
class CategorySettingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddCategory: Button
    private lateinit var adapter: CategorySettingAdapter

    private val viewModel: CategoryViewModel by activityViewModels()
    private var selectedDate: LocalDate = now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_category_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerCategory)
        btnAddCategory = view.findViewById(R.id.btnAddCategory)

        // activity의 상단바에 있는 뒤로가기 버튼을 제어
        requireActivity().findViewById<ImageButton>(R.id.btnPopBackStack)?.setOnClickListener {
            requireActivity().onBackPressed()
        }

        adapter = CategorySettingAdapter(
            mutableListOf(),
            onEditClick = { showEditCategoryDialog(it) },
            onDeleteClick = { viewModel.deleteCategory(selectedDate, it) }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.selectedDate.observe(viewLifecycleOwner) {
            selectedDate = it
        }

        viewModel.categoriesForSelectedDate.observe(viewLifecycleOwner) {
            adapter.updateCategories(it)
        }

        btnAddCategory.setOnClickListener {
            showAddCategoryDialog()
        }
    }

    private fun setupColorPickers(view: View, defaultColor: Int, onColorSelected: (Int) -> Unit) {
        val colorMap = mapOf(
            R.id.colorOption1 to Color.parseColor("#bd413b"),
            R.id.colorOption2 to Color.parseColor("#d07192"),
            R.id.colorOption3 to Color.parseColor("#e69a98"),
            R.id.colorOption4 to Color.parseColor("#f5df8f"),
            R.id.colorOption5 to Color.parseColor("#b7dfc2"),
            R.id.colorOption6 to Color.parseColor("#639c6f"),
            R.id.colorOption7 to Color.parseColor("#8ea5f1"),
            R.id.colorOption8 to Color.parseColor("#6782eb"),
            R.id.colorOption9 to Color.parseColor("#3b3b3b")
        )
        Log.d("colorOption",colorMap.toString())

        val allFrames = colorMap.keys.map { view.findViewById<FrameLayout>(it) }

        for ((id, color) in colorMap) {
            val frame = view.findViewById<FrameLayout>(id)
            val colorView = frame.getChildAt(0)

            if (defaultColor == color) {
                frame.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_color_circle_selected)
            } else {
                frame.foreground = null
            }

            colorView.setOnClickListener {
                onColorSelected(color)
                allFrames.forEach { it.foreground = null }
                frame.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_color_circle_selected)
            }
        }
    }

    private fun showAddCategoryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null)
        val etCategoryName = dialogView.findViewById<EditText>(R.id.etCategoryName)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSaveCategory)

        val selectedColor = intArrayOf(Color.parseColor("#bd413b"))
        setupColorPickers(dialogView, selectedColor[0]) { selectedColor[0] = it }

        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        btnSave.setOnClickListener {
            val name = etCategoryName.text.toString().trim()
            if (name.isNotEmpty()) {
                val categoryDto = CategoryDto(name = name, color = selectedColor[0], id= System.currentTimeMillis().toString(), userId = "devId")
                viewModel.addCategory(selectedDate, categoryDto)
                val service: RetrofitUtil = ApiClient.getNoAuthApiClient().create(RetrofitUtil::class.java)
                service.addCategory(categoryDto)
                    .enqueue(object : Callback<JsonElement> {
                        override fun onResponse(
                            call: Call<JsonElement>,
                            response: Response<JsonElement>
                        ) {
                            Log.d("result category add::",response.body().toString())
                        }

                        override fun onFailure(call: Call<JsonElement>, t: Throwable) {

                        }

                    })
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "카테고리 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun showEditCategoryDialog(categoryDto: CategoryDto) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_category, null)
        val etCategoryName = dialogView.findViewById<EditText>(R.id.etCategoryName)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSaveCategory)

        etCategoryName.setText(categoryDto.name)
        val selectedColor = intArrayOf(categoryDto.color)

        setupColorPickers(dialogView, selectedColor[0]) { selectedColor[0] = it }

        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        btnSave.setOnClickListener {
            val newName = etCategoryName.text.toString().trim()
            if (newName.isNotEmpty()) {
                val updated = categoryDto.copy(name = newName, color = selectedColor[0])
                viewModel.updateCategory(selectedDate, categoryDto, updated)

                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "카테고리 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
