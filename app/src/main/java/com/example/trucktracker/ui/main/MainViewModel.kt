package com.example.trucktracker.ui.main

import androidx.lifecycle.*
import com.example.trucktracker.data.Truck
import com.example.trucktracker.model.TruckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val truckRepository = TruckRepository()

    private val truckListLiveData = MutableLiveData<List<Truck>>()
    private var truckList: List<Truck> ? = null

    fun getTrucks() = liveData(Dispatchers.IO) {
        emit(truckRepository.getTruckList().truckList)
    }

    fun getTruckList(): LiveData<List<Truck>> {
        if(truckListLiveData.value == null){
            viewModelScope.launch(Dispatchers.IO) {
                val response  = truckRepository.getTruckList()
                truckList = response.truckList
                truckListLiveData.postValue(truckList!!)
            }
        }
        return truckListLiveData
    }

    fun searchTruckList(str: String){
        if(truckListLiveData.value != null){
            truckListLiveData.value = truckList?.filter { truck ->
                truck.truckNumber.contains(str,ignoreCase = true)
            }
        }
    }

    fun clearSearch(){
        truckList?.let{
            truckListLiveData.value = it
        }
    }

    fun updateTruckList(){
        viewModelScope.launch(Dispatchers.IO) {
            val response  = truckRepository.getTruckList()
            truckList = response.truckList
            truckListLiveData.postValue(truckList!!)
        }
    }
}