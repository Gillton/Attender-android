package org.attendr.utils.network

/**
 * @author paul@quarkworks.co (Paul Gillis)
 */
class Promise<T> {
    var child: Promise<T>? = null
    private var successListener: ((T) -> Unit)? = null
    private var failureListener: ((Exception) -> Unit)? = null

    var isResolved = false

    fun then(listener: (T) -> Unit): Promise<T> {
        successListener = listener
        child = Promise()
        return child!!
    }

    fun failure(listener: (Exception) -> Unit) {
        failureListener = listener
    }

    fun resolve(result: T) {
        if (!isResolved) {
            isResolved = true
            successListener?.invoke(result)
            child?.resolve(result)
        }
    }

    fun reject(exception: Exception) {
        if (!isResolved) {
            isResolved = true
            failureListener?.invoke(exception)
            child?.reject(exception)
        }
    }
}