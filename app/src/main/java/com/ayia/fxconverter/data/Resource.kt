package com.ayia.fxconverter.data


sealed class Resource<out R> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val txtId: Int) : Resource<Nothing>()
    data class Empty(val txtId: Int) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$txtId]"
            is Empty -> "Not found"
            Loading -> "Loading"
        }
    }
}
