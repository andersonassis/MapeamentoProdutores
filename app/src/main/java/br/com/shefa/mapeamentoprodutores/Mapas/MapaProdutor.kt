package br.com.shefa.mapeamentoprodutores.Mapas

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.shefa.mapeamentoprodutores.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaProdutor : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var nomeProdutor:String? = null
    var lat:String= ""
    var longi:String=""
    var latitude:Double = 0.0
    var longitude:Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_produtor)

        //pegando os valores da tela dos dados
        nomeProdutor = getIntent().getStringExtra("nome")
        lat          = getIntent().getStringExtra("lat")
        longi        = getIntent().getStringExtra("long")

        //converte string para double
        latitude  = lat.toDouble()
        longitude = longi.toDouble()

        // chama o fragmento do mapa
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Adiciona marcador e move a camera com zoom automatico
        val produtor = LatLng(latitude,longitude)
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
        mMap.addMarker(MarkerOptions().position(produtor).title(nomeProdutor + " Latitude: "+latitude + " Longitude: "+longitude ))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(produtor))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(produtor,17.0f))

    }
}
