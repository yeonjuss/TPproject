package com.syj2024.tpproject.adapter

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.syj2024.tpproject.G
import com.syj2024.tpproject.R
import com.syj2024.tpproject.activities.MainActivity
import com.syj2024.tpproject.activities.PlaceUrlActivity
import com.syj2024.tpproject.data.Place
import com.syj2024.tpproject.databinding.RecyclerItemListFragmentBinding
import com.syj2024.tpproject.fragments.PlaceFavoriteFragment

class PlaceListAdapter(val context: Context, val documents:List<Place>) : Adapter<PlaceListAdapter.VH>() {

    inner class VH(var binding:RecyclerItemListFragmentBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding= RecyclerItemListFragmentBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int {
        return documents.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val place:Place = documents[position]

        holder.binding.tvPlaceName.text= place.place_name
        holder.binding.tvDistance.text= place.distance + "m"
        holder.binding.tvAddress.text= if (place.road_address_name=="")place.address_name else place.road_address_name

        holder.itemView.setOnClickListener {
            val intent= Intent(context, PlaceUrlActivity::class.java)
            intent.putExtra("place_url", place.place_url)
            context.startActivity(intent)

        }

        holder.itemView.setOnLongClickListener {

            // 팝업메뉴를 보이기
            val popupMenu: PopupMenu= PopupMenu(context, holder.itemView)
            // res폴더 안에 menu폴더의 xml문서를 읽어와서 메뉴객체로 만들어주는 (부풀려주는) 객체 이용
            popupMenu.menuInflater.inflate(R.menu.popup, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->

                when( menuItem.itemId ) {
                    R.id.popup_favor-> addFavorite(place)
                    R.id.popup_delete-> removePlace(place)
                }

                false
            }

            popupMenu.show()

            true // 리턴값을 true로 하여.. click이벤트가 추가로 발동하지 않도록..
        }


    } // bindViewholder

    fun addFavorite(place: Place){

        if(G.userAccount==null) {
            Toast.makeText(context, "로그인 후 찜기능을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return

        }


        // 클릭한 place 정보를 SQLite DB에 저장하기

        // "data.db"라는 이름의 데이터베이스 파일 열기
        val db:SQLiteDatabase= context.openOrCreateDatabase("data", Context.MODE_PRIVATE, null)
        // "favor" 라는 이름의 테이블(표) 만들기
        db.execSQL("CREATE TABLE IF NOT EXISTS favor(id VARCHAR(80) PRIMARY KEY,place_name VARCHAR(80),category_name VARCHAR(80),phone VARCHAR(80),address_name VARCHAR(80),road_address_name VARCHAR(80),x VARCHAR(80),y VARCHAR(80),place_url VARCHAR(80),distance VARCHAR(80))")

        // favor 라는 테이블에 장소정보 place의 값들을 저장 (삽입)
        db.execSQL("INSERT INTO favor VALUES(?,?,?,?,?,?,?,?,?,?)", arrayOf(place.id,place.place_name,place.category_name,place.phone,place.address_name,place.road_address_name,place.x,place.y,place.place_url,place.distance))

        db.close()

        Toast.makeText(context, "찜 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()

    }
    fun removePlace(place: Place) {
        // 선택한 장소 정보를 찜 목록 db 에서 제거

        // "data.db"라는 이름의 데이터베이스 파일 열기
        val db:SQLiteDatabase= context.openOrCreateDatabase("data", Context.MODE_PRIVATE, null)

        // "favor" 테이블에서 파라미터로 받은 place를 제거하는 쿼리문 실행
        db.execSQL("DELETE FROM favor WHERE id=?" , arrayOf(place.id))
        db.close()

       // 프레그먼트의 화면 갱신 -- MainActivity 에서 FavorFragment 를 다시 붙이면 됨 ..
        (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container,PlaceFavoriteFragment()).commit()



    }

}

