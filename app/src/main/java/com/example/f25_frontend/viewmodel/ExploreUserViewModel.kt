package com.example.f25_frontend.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.f25_frontend.model.UserDto
/*
    @Author 조수연
    탐색 유저 뷰모델
*/
class ExploreUserViewModel: ViewModel() {
    val item = MutableLiveData<UserDto>()
    fun updateUser(newUser: UserDto){
        item.value = newUser
    }

}