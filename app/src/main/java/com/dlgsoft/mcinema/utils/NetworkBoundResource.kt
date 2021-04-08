package com.dlgsoft.mcinema.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * [networkBoundResource] starts by observing database for the resource. When the entry is loaded
 * from the database for the first time, [networkBoundResource] checks whether the result is good
 * enough to be dispatched and/or it should be fetched from network. If the network call completes
 * successfully, it saves the response into the database and re-initializes the stream. If network
 * request fails, we dispatch a failure directly.
 *
 * [networkBoundResource] has the following parameters:
 *
 * - [saveFetchResult]: This method is responsible for updating/inserting the result of the API into
 * the local database. This method will be called when the data from the remote server is
 * successfully fetched.
 * - [shouldFetch]: Based on implementation, this method should return true if it is needed to fetch
 * the data from a remote server. For example when the data is outdated. Else it should return
 * false.
 * - [query]: This method is responsible for returning the data from the local database.
 * - [fetch]: This method is responsible for creating a remote server call which is responsible
 * for fetching the data from the server.
 */

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = channelFlow {
    val data = query().first()

    if (shouldFetch(data)) {
        val loading = launch {
            query().collect { send(Resource.Loading(it)) }
        }
        try {
            saveFetchResult(fetch())
            loading.cancel()
            query().collect { send(Resource.Success(it)) }
        } catch (t: Throwable) {
            loading.cancel()
            query().collect { send(Resource.Error(t, it)) }
        }
    } else {
        query().collect { send(Resource.Success(it)) }
    }
}