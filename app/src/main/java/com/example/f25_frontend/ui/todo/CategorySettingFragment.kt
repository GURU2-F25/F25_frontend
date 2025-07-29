package com.example.f25_frontend.ui.todo

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.R
import com.example.f25_frontend.model.Category
import com.example.f25_frontend.ui.category.CategorySettingAdapter
import com.example.f25_frontend.viewmodel.CategoryViewModel

class CategorySettingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddCategory: Button
    private lateinit var btnBack: ImageButton
    private lateinit var adapter: CategorySettingAdapter

    private val viewModel: CategoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_category_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerCategory)
        btnAddCategory = view.findViewById(R.id.btnAddCategory)

        adapter = CategorySettingAdapter(
            mutableListOf(),
            onEditClick = { showEditCategoryDialog(it) },
            onDeleteClick = { viewModel.deleteCategory(it) }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

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

        // 모든 FrameLayout 모아둠
        val allFrames = colorMap.keys.map { view.findViewById<FrameLayout>(it) }

        for ((id, color) in colorMap) {
            val frame = view.findViewById<FrameLayout>(id)
            val colorView = frame.getChildAt(0)

            // 초기 선택된 색상 테두리 적용
            if (defaultColor == color) {
                frame.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_color_circle_selected)
            } else {
                frame.foreground = null
            }

            // 클릭 리스너: 내부 뷰에 달아도 되고, frame에도 달아도 됨
            colorView.setOnClickListener {
                onColorSelected(color)

                allFrames.forEach { it.foreground = null } // 모든 테두리 제거
                frame.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_color_circle_selected) // 선택된 테두리
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
                val category = Category(name = name, color = selectedColor[0])
                viewModel.addCategory(category)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "카테고리 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun showEditCategoryDialog(category: Category) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_category, null)
        val etCategoryName = dialogView.findViewById<EditText>(R.id.etCategoryName)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSaveCategory)

        etCategoryName.setText(category.name)
        val selectedColor = intArrayOf(category.color)

        setupColorPickers(dialogView, selectedColor[0]) { selectedColor[0] = it }

        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        btnSave.setOnClickListener {
            val newName = etCategoryName.text.toString().trim()
            if (newName.isNotEmpty()) {
                val updated = category.copy(name = newName, color = selectedColor[0])
                viewModel.updateCategory(category, updated)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "카테고리 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
