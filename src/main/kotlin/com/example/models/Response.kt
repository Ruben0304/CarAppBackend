package com.example.models

import com.example.enums.Status
import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> success(data: T): Response<T> =
            Response(status = Status.SUCCESS, data = data)

        fun error(message: String): Response<Nothing> =
            Response(status = Status.ERROR, message = message)
    }


}
