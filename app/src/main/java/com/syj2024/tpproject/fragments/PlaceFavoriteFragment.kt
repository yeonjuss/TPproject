package com.syj2024.tpproject.fragments

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.syj2024.tpproject.G
import com.syj2024.tpproject.activities.LoginActivity
import com.syj2024.tpproject.adapter.PlaceListAdapter
import com.syj2024.tpproject.data.Place
import com.syj2024.tpproject.databinding.FragmentPlaceFavoriteBinding
import com.syj2024.tpproject.databinding.FragmentPlaceListBinding
import com.syj2024.tpproject.databinding.FragmentPlaceMapBinding

class PlaceFavoriteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPlaceFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var binding:FragmentPlaceFavoriteBinding

    // 찜 목록에 저장된 place 정보들을 가진 List
    val placeList: MutableList<Place> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 여기서 작업 시작
        // SQLite DB에 저장된 찜 목록 place 들 가져오기
        if (G.userAccount!=null) loadDB()
        else {
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }


    }

    private fun loadDB() {
        // "data.db" 파일 열기
        val db: SQLiteDatabase= requireContext().openOrCreateDatabase("data", Context.MODE_PRIVATE, null)
        // "favor" 테이블 없을 수도 있기에..
        db.execSQL("CREATE TABLE IF NOT EXISTS favor(id VARCHAR(80) PRIMARY KEY,place_name VARCHAR(80),category_name VARCHAR(80),phone VARCHAR(80),address_name VARCHAR(80),road_address_name VARCHAR(80),x VARCHAR(80),y VARCHAR(80),place_url VARCHAR(80),distance VARCHAR(80))")

        //"favor" 테이블에 저장된 모든 place 정보들 가져오기..[ 모든 레코드(한줄 row) 가져오는 쿼리문 실행 ]
        val cursor:Cursor= db.rawQuery("SELECT * FROM favor", null)
        // 한 줄씩 읽어오기..
        cursor?.apply {
            moveToFirst()

            // cursor.count:  총 레코드 수 ...
            for ( i in 0 until count) {
                var id= getString(0) // 0번칸에 있는 string 값 얻어오기
                var place_name=getString(1)
                var category_name= getString(2)
                var phone= getString(3)
                var address_name= getString(4)
                var road_address_name= getString(5)
                var x= getString(6)
                var y = getString(7)
                var place_url= getString(8)
                var distance= getString(9)

                // 리사이클러가 보여줄 대량의 데이터에 추가
                placeList.add( Place(id, place_name, category_name, phone, address_name, road_address_name, x, y, place_url, distance))

                moveToNext()
            }//for

            // 리사이클러뷰에 보여주기 !!
            if (placeList.size==0) binding.tv.visibility= View.VISIBLE
            else{
                binding.tv.visibility=  View.GONE

                binding.recyclerView.adapter= PlaceListAdapter(requireContext(), placeList)
            }




        }
    }

}