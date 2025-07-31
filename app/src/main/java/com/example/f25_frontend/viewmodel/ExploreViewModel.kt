package com.example.f25_frontend.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.f25_frontend.model.UserDto
/*
    @Author 조수연
    탐색 뷰모델
*/
class ExploreViewModel: ViewModel() {
    val itemList = MutableLiveData<List<UserDto>?>()
    fun updateUserList(newUserList: List<UserDto>){
        itemList.value = newUserList
    }
    fun resetUserList(){
        itemList.value = null
    }
}