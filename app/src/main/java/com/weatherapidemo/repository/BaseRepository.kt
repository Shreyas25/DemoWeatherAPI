package com.weatherapidemo.repository

import androidx.lifecycle.liveData
import com.weatherapidemo.others.Resource
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

open class BaseRepository {

    suspend fun <T > apiCallRequest(call: suspend () -> Response<T>)= liveData(Dispatchers.IO) {

        val response: Response<T>
        try {
            response = call.invoke()  //or
            if (response!!.isSuccessful) {
                emit(Resource.success(response.body()))
            }
            else{
                emit(Resource.error(UnknownError("ERROR_WENT_WRONG"), null))
            }
        }catch (e:Exception)
        {
            emit(Resource.error(e, null))
        }
    }
}