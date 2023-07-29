package com.sakal.playlistmaker.search.data.network

import com.sakal.playlistmaker.ApiConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RetrofitClient(private val service: ApiService, private val checker: ConnectionChecker) :
    NetworkClient {

    override suspend fun doRequest(dto: Any): Response {

        if (!checker.isConnected()) {
            return Response().apply { resultCode = ApiConstants.NO_INTERNET_CONNECTION_CODE }
        }
        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = ApiConstants.BAD_REQUEST_CODE }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = service.search(dto.query)
                response.apply { resultCode = ApiConstants.SUCCESS_CODE }
            } catch (e: Throwable) {
                Response().apply { resultCode = ApiConstants.INTERNAL_SERVER_ERROR }
            }
        }

    }

}