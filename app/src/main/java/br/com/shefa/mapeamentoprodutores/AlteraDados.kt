package br.com.shefa.mapeamentoprodutores

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.shefa.mapeamentoprodutores.BD_Interno.DB_Interno
import br.com.shefa.mapeamentoprodutores.Gps.Gps
import kotlinx.android.synthetic.main.activity_altera_dados.*


class AlteraDados : AppCompatActivity() {
    var latitude:String =""
    var longitude:String= ""
    var banco: DB_Interno? = null
    var idProdutor:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_altera_dados)
        banco = DB_Interno(this)//chama o banco
        idProdutor = getIntent().getStringExtra("id_Produtor");


        //botao captrar posicao
        btn_capturaposicao.setOnClickListener{
            val gps  = Gps(this) //inicia a classe do gps
            obtemPosiçoes(gps)
        }

        //botão salvar
        btn_salvar.setOnClickListener{
            salvar(idProdutor)
            latprodutor.setText("")
            longiprodutor.setText("")
        }





    }//fim do oncreate

    //pega as posições
    fun obtemPosiçoes(gps2:Gps)
    {
        latitude = gps2.posicaolatitude()
        longitude = gps2.posicaolongitude()
        latprodutor.setText(latitude)
        longiprodutor.setText(longitude)
    }

    fun salvar(id:String)
    {


    }



}//fim da classe
