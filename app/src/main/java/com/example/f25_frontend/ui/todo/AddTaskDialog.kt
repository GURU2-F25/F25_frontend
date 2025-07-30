package com.example.f25_frontend.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.f25_frontend.R
import com.example.f25_frontend.model.Category
import com.example.f25_frontend.model.Task
import java.io.Serializable
import java.time.LocalDate

class AddTaskDialog : DialogFragment() {

    private var onTaskAdded: ((Task, Boolean, Boolean) -> Unit)? = null

    companion object {
        fun newInstance(category: Category, date: LocalDate): AddTaskDialog {
            val fragment = AddTaskDialog()
            val args = Bundle()
            args.putString("categoryName", category.name)
            args.putInt("categoryColor", category.color)
            args.putSerializable("category", category as Serializable)
            args.putSerializable("date", date)
            fragment.arguments = args
            return fragment
        }
    }

    fun setOnTaskAddedListener(listener: (Task, Boolean, Boolean) -> Unit) {
        onTaskAdded = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val categoryName = arguments?.getString("categoryName") ?: "카테고리"
        val categoryColor = arguments?.getInt("categoryColor") ?: Color.GRAY
        val category = arguments?.getSerializable("category") as? Category
        val date = arguments?.getSerializable("date") as? LocalDate

        val tvCategoryTitle = view.findViewById<TextView>(R.id.tvCategoryTitle)
        val etTaskTitle = view.findViewById<EditText>(R.id.etTaskTitle)
        val cbRepeatDaily = view.findViewById<CheckBox>(R.id.cbRepeatDaily)
        val cbRepeatFriday = view.findViewById<CheckBox>(R.id.cbRepeatFriday)
        val btnAddTaskConfirm = view.findViewById<Button>(R.id.btnAddTaskConfirm)

        tvCategoryTitle.text = categoryName
        tvCategoryTitle.setBackgroundColor(categoryColor)

        btnAddTaskConfirm.setOnClickListener {
            val taskTitle = etTaskTitle.text.toString().trim()
            val repeatDaily = cbRepeatDaily.isChecked
            val repeatWeekly = cbRepeatFriday.isChecked

            if (taskTitle.isEmpty()) {
                Toast.makeText(requireContext(), "일정명을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (category == null || date == null) {
                Toast.makeText(requireContext(), "카테고리 정보가 없습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task = Task(
                title = taskTitle,
                date = date,
                isDone = false,
                category = category
            )

            onTaskAdded?.invoke(task, repeatDaily, repeatWeekly)
            dismiss()
        }
    }
}
