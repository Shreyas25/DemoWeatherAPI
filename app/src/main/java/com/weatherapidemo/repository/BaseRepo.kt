package com.weatherapidemo.repository

import com.weatherapidemo.others.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

abstract class BaseRepo {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<ApiState<T>> = flow {
        emit(ApiState.Loading)
        val response: Response<T>
        try {
            response = apiCall.invoke()
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    emit(ApiState.Success(data))
                }else{
                    val error = response.errorBody()
                    if(error!=null){
                        emit(ApiState.Failure(Exception(error.toString())))
                    }else{
                        emit(ApiState.Failure(Exception("Something went wrong..")))
                    }
                }
            }else{
                emit(ApiState.Failure(Throwable(response.errorBody().toString())))
            }
        } catch (e: Exception) {
            emit(ApiState.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}