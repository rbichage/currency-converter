package com.reuben.core_model.currency

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> apiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
) = withContext(dispatcher){
    try {
        return@withContext apiCall.invoke()
    } catch (e:Exception){
      throw e
    }

}

data class ErrorHolder(
    override val message: String,
    val statusCode: Int,
    val body: String = ""
) : Exception(message)

//TODO: Move this to core-common

fun Throwable.extractNetworkExceptions(): ErrorHolder {
    return when (this) {
        is UnknownHostException -> ErrorHolder("Unable to connect, check your connection", 1)
        is SocketTimeoutException -> ErrorHolder("Unable to connect, check your connection", 1)
        is ConnectException -> ErrorHolder("unable to connect : ${this.localizedMessage}", 1)
        is IOException -> ErrorHolder("Unable to connect, check your connection", 1)
        is HttpException -> extractHttpExceptions(this)
        else -> ErrorHolder(this.message.orEmpty(), 1)
    }
}

private fun extractHttpExceptions(e: HttpException): ErrorHolder {
    val body = e.response()?.errorBody()
    val jsonString = body?.string()

    val message = try {
        val jsonObject = JSONObject(jsonString.orEmpty())
        jsonObject.getString("message")
    } catch (exception: JSONException) {
        when (e.code()) {
            500 -> {
                "Unable to complete request your request, try again later"
            }

            503 -> {
                "Service temporarily unavailable, try again in a few minutes"
            }

            else -> {
                "Unable to complete request your request, try again later"
            }
        }
    }

    val errorCode = e.response()?.code() ?: 0
    return ErrorHolder(message, errorCode, jsonString.orEmpty())
}


sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null, val errorHolder: ErrorHolder) : Result<Nothing>
    object Loading : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch {
            Timber.e(it)
            emit(Result.Error(it, it.extractNetworkExceptions()))
        }
}

data class CurrencyException(
    override val message: String
) : Exception(message)