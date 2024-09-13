package com.syj2024.tpproject.data

data class KaKaoSearchPlaceResponse(var meta:PlaceMeta , var documents: List<Place>)

data class PlaceMeta(var total_count:Int, var pageable_count:Int, var is_end:Boolean)

data class Place(
    var id:String,
    var place_name:String,
    var category_name:String,
    var phone:String,
    var address_name:String,
    var road_address_name:String,
    var x:String, // 경로
    var y:String, // 위도
    var place_url:String, // 장소에 대한 검색 사이트 링크
    var distance:String // 중심좌표까지의 거리. 단, 요청 파라미터 X,Y를 준 경우만 존재 [단위는 meter]
)
