package com.syj2024.tpproject.network


import com.syj2024.tpproject.data.KaKaoSearchPlaceResponse
import com.syj2024.tpproject.data.NidUserInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitService {

    // 네아로 사용자 프로필 정보 open api 요청하는 작업명세 .. 응답결과 json을 NidUserInfoResponse 라는 데이터클래스 객체로 파싱하여 받아오기
    @GET("/v1/nid/me")
    fun getNidUserInfo(@Header("Authorization") authorization:String):Call<NidUserInfoResponse>

    // 카카오 키워드 장소 검색 API 를 GET방식으로 요청하는 작업 명세 .. 결과를 스트링으로

    @Headers("Authorization: KakaoAK 07539faa40c02dbc3debc4b78abbbcef")
    @GET("/v2/local/search/keyword.json?sort=distance")
    fun searchPlacesFromServer(@Query("query") query:String,@Query("x") longitude:String,@Query("y") latitude:String) : Call<String>

    @Headers("Authorization: KakaoAK 07539faa40c02dbc3debc4b78abbbcef")
    @GET("/v2/local/search/keyword.json?sort=distance")
    fun searchPlacesFromServer2(@Query("query") query:String,@Query("x") longitude:String,@Query("y") latitude:String) : Call<KaKaoSearchPlaceResponse>
}