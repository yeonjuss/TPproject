package com.syj2024.tpproject.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.MarkerIcons
import com.syj2024.tpproject.R
import com.syj2024.tpproject.activities.MainActivity
import com.syj2024.tpproject.activities.PlaceUrlActivity
import com.syj2024.tpproject.data.Place
import com.syj2024.tpproject.databinding.FragmentPlaceListBinding
import com.syj2024.tpproject.databinding.FragmentPlaceMapBinding

class PlaceMapFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPlaceMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var binding:FragmentPlaceMapBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 여기서 작업 시작.
        // 네이버지도 불러오기..작업 실행하기 위해 MapFragment 객체 참조하기
        val mapFragment: MapFragment= childFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        mapFragment.getMapAsync { naverMap -> setMapAndMarkers(naverMap) }


        }

       // 지도 관련 설정( 지도 위치, 마커들 추가 등...)
      private fun setMapAndMarkers(naverMap: NaverMap) {
          // 내 위치로 맵의 카메라를 이동
          // 현재 내위치 위도/경도 좌표는 MainActivity의 멤버로 저장되어 있음.
          var lat: Double= (activity as MainActivity).mylocation?.latitude ?: 37.5472
          var lng: Double= (activity as MainActivity).mylocation?.longitude ?: 127.0520

           var myPosition: LatLng= LatLng(lat, lng)
           naverMap.moveCamera( CameraUpdate.scrollAndZoomTo(myPosition, 14.0))

           // 내 위치를 표시
           val locationOverlay= naverMap.locationOverlay
           locationOverlay.isVisible=true
           locationOverlay.icon= LocationOverlay.DEFAULT_ICON

           //내 위치를 마커로 표시
           val marker= Marker()
           marker.position= myPosition
           marker.map= naverMap

           // 마커들을 클릭했을때 보여질 정보창 객체
           val infoWindow: InfoWindow= InfoWindow()
           infoWindow.adapter= object : InfoWindow.DefaultTextAdapter(requireContext()){
               override fun getText(p0: InfoWindow): CharSequence {
                   return p0.marker!!.captionText
               }

           }

           infoWindow.setOnClickListener { overlay->
               val place_url= infoWindow.marker!!.tag.toString()

               val intent= Intent(requireContext(), PlaceUrlActivity::class.java)
               intent.putExtra("place_url", place_url )
               startActivity(intent)


               true // 리턴값 true


           }

           // 검색결과 장소정보들을 마커로 표시..
           val documents: List<Place>? = (activity as MainActivity).searchPlaceResponse?.documents
           documents?.forEach { place ->

               // 마커 객체 만들기
               val marker= Marker()
               marker.position= LatLng(place.y.toDouble(), place.x.toDouble()) // 마커위치(위도,경도)
               marker.icon= MarkerIcons.BLACK // 마커핀 기본 검정색 마커지정
               marker.iconTintColor= Color.MAGENTA // 덧입힐 색상. 지정...

               marker.captionText= place.place_name
               marker.map= naverMap
               marker.tag= place.place_url // 장소에 대한 상세정보 url 정보를 tag 값으로 저장

               marker.onClickListener= object : Overlay.OnClickListener{
                   override fun onClick(p0: Overlay): Boolean {
                       // 정보창 띄우기..
                       infoWindow.open(p0 as Marker)


                       // 맵 자체를 클릭하는 이벤트로 넘어가지 않도록..
                       return true
                   }

               }


           } // forEach...............



    }

}