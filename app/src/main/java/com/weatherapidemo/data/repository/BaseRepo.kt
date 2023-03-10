package com.weatherapidemo.data.repository

import com.weatherapidemo.others.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

abstract class BaseRepo {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<Result<T>> = flow {
        emit(Result.Loading)
        val response: Response<T>
        try {
            response = apiCall.invoke()
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    emit(Result.Success(data))
                }else{
                    val error = response.errorBody()
                    if(error!=null){
                        emit(Result.Failure(Exception(error.toString())))
                    }else{
                        emit(Result.Failure(Exception("Something went wrong..")))
                    }
                }
            }else{
                emit(Result.Failure(Throwable(response.errorBody().toString())))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}