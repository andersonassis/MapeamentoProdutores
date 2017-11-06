package br.com.shefa.mapeamentoprodutores

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.shefa.mapeamentoprodutores.BD_Interno.DB_Interno
import br.com.shefa.mapeamentoprodutores.Gps.Gps


class AlteraDados : AppCompatActivity() {
    var latitude:String =""
    var longitude:String= ""
    var banco: DB_Interno? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_altera_dados)
        val gps  = Gps(this) //inicia a classe do gps
        banco = DB_Interno(this)//chama o banco


        obtemPosiçoes(gps)

    }//fim do oncreate



    //pega as posições
    fun obtemPosiçoes(gps2:Gps)
    {
        latitude = gps2.posicaolatitude()
        longitude = gps2.posicaolongitude()

    }



}//fim da classe
