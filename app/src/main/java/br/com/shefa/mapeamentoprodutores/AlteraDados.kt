package br.com.shefa.mapeamentoprodutores

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteCursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.shefa.mapeamentoprodutores.BD_Interno.DB_Interno
import br.com.shefa.mapeamentoprodutores.Gps.Gps
import kotlinx.android.synthetic.main.activity_altera_dados.*
import java.text.SimpleDateFormat
import java.util.*


class AlteraDados : AppCompatActivity() {
    var latitude:String =""
    var longitude:String= ""
    var banco: DB_Interno? = null
    var idProdutor:String = ""
    var datasistema:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_altera_dados)
        banco = DB_Interno(this)//chama o banco
        idProdutor = getIntent().getStringExtra("id_Produtor");
        buscarDados(idProdutor)

        //DATA E HORA DO SISTEMA
        val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val data = Date()
        val cal = Calendar.getInstance()
        cal.time = data
        val data_atual = cal.time
        val data_sistema2 = date.format(data_atual)
        datasistema = data_sistema2

        //botao captrar posicao
        btn_capturaposicao.setOnClickListener{
            val gps  = Gps(this) //inicia a classe do gps
            obtemPosiçoes(gps)
        }

        //botão salvar
        btn_salvar.setOnClickListener{
            salvar(idProdutor)
            buscarDados(idProdutor)
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

    //funçao para salvar
    fun salvar(id:String)
    {
        val db2 = openOrCreateDatabase("mapeamento.db", Context.MODE_PRIVATE, null)

       try {
           val salvou:String = "1"
           val datahora: String? = datasistema
           val latitudeLocal   = latprodutor.getText().toString()
           val longitudeLocal  = longiprodutor.getText().toString()
           val obs             = edit_obs.getText().toString()

           val ctv = ContentValues()
           ctv.put("_salvou",salvou)
           ctv.put("_latitude",  latitudeLocal)//insere latitude
           ctv.put("_longitude", longitudeLocal)//insere longitude
           ctv.put("_dataHora",datahora)
           ctv.put("_obs",obs)
           val res = db2.update("tabela_mapeamento", ctv, "_id=?", arrayOf(id))
           db2.close()
           Toast.makeText(this@AlteraDados,"SALVO COM SUCESSO", Toast.LENGTH_LONG).show()
           latprodutor.setText("")
           longiprodutor.setText("")

       } catch (e: Exception){
           e.printStackTrace()
           Toast.makeText(this@AlteraDados,"ERRO AO SALVAR", Toast.LENGTH_LONG).show()

       }

    }//fim da função salvar

    fun buscarDados(id2:String)
    {
        val db3 = openOrCreateDatabase("mapeamento.db", Context.MODE_PRIVATE, null)//abrindo conexão com banco
        val sql = "SELECT * FROM  tabela_mapeamento  where _id = ?"//select para pegar o produtor clicado de acordo com o ID
        val c = db3.rawQuery(sql, arrayOf<String>(id2)) as SQLiteCursor
        if (c.moveToFirst()) {

            val nome = c.getString(c.getColumnIndex("_nomeProdutor"))
            val lati = c.getString(c.getColumnIndex("_latitude"))
            val long = c.getString(c.getColumnIndex("_longitude"))
            val linha = c.getString(c.getColumnIndex("_subRota"))

            txt_nome.setText(nome.toString())
            txtlati1.setText(lati.toString())
            txtlongi1.setText(long.toString())
            txt_linha.setText(linha.toString())

        }
        c.close()
        db3.close()//fecha a conexão com o banco
    }//fim da funcao buscar dados



}//fim da classe
