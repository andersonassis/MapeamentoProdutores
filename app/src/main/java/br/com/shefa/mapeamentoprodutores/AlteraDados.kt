package br.com.shefa.mapeamentoprodutores

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteCursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import br.com.shefa.mapeamentoprodutores.BD_Interno.DB_Interno
import br.com.shefa.mapeamentoprodutores.Gps.Gps
import br.com.shefa.mapeamentoprodutores.Mapas.MapaProdutor
import br.com.shefa.mapeamentoprodutores.Toast.ToastManager
import kotlinx.android.synthetic.main.activity_altera_dados.*
import java.text.SimpleDateFormat
import java.util.*


class AlteraDados : AppCompatActivity() {
    var latitude:String =""
    var longitude:String= ""
    var banco: DB_Interno? = null
    var idProdutor:String = ""
    var datasistema:String?=null
    var nome:String = ""
    var lati:String  = ""
    var long:String  = ""
    var linha:String = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_altera_dados)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Voltar"

        banco = DB_Interno(this)//chama o banco
        idProdutor = getIntent().getStringExtra("id_Produtor");
        linha      = getIntent().getStringExtra("linha");

        buscarDados(idProdutor)

        //DATA E HORA DO SISTEMA
        val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val data = Date()
        val cal = Calendar.getInstance()
        cal.time = data
        val data_atual = cal.time
        val data_sistema2 = date.format(data_atual)
        datasistema = data_sistema2

        //botao capturar posicao
        btn_capturaposicao.setOnClickListener{
            val gps  = Gps(this) //inicia a classe do gps
            obtemPosiçoes(gps)
        }

        //botão salvar
        btn_salvar.setOnClickListener{
            salvar(idProdutor)
            buscarDados(idProdutor)
        }

        //botão visualizar MAPA
        btn_mapa_produtor.setOnClickListener{
            if(!lati.equals("")) {
                val altera = Intent(applicationContext, MapaProdutor::class.java)
                altera.putExtra("nome", nome)
                altera.putExtra("lat", lati)
                altera.putExtra("long", long)
                startActivity(altera)
            }else{
                ToastManager.show(this@AlteraDados, "SEM POSIÇÃO GPS !!!, CAPTURE A POSIÇÃO PARA VISUALIZAR O MAPA", ToastManager.INFORMATION)
            }
        }//FIM DO BOTÃO VISUALIZAR MAPA

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
           ToastManager.show(this@AlteraDados, "SALVO COM SUCESSO", ToastManager.INFORMATION)
           latprodutor.setText("")
           longiprodutor.setText("")
           edit_obs.setText("")
           banco!!.updateLinhas(linha)// aqui vai escoher apenas a linha faz um update
       } catch (e: Exception){
           e.printStackTrace()
           ToastManager.show(this@AlteraDados, "ERRO AO SALVAR", ToastManager.ERROR)
       }

    }//fim da função salvar

    //função para fazer um select no banco para mostrar na tela o produtor
    fun buscarDados(id2:String)
    {
        val db3 = openOrCreateDatabase("mapeamento.db", Context.MODE_PRIVATE, null)//abrindo conexão com banco
        val sql = "SELECT * FROM  tabela_mapeamento  where _id = ?"//select para pegar o produtor clicado de acordo com o ID
        val c = db3.rawQuery(sql, arrayOf<String>(id2)) as SQLiteCursor
        if (c.moveToFirst()) {
            nome = c.getString(c.getColumnIndex("_nomeProdutor"))
            lati = c.getString(c.getColumnIndex("_latitude"))
            long = c.getString(c.getColumnIndex("_longitude"))
            val linha = c.getString(c.getColumnIndex("_subRota"))
            txt_nome.setText(nome.toString())
            txt_linha.setText(linha.toString())
        }
        c.close()
        db3.close()//fecha a conexão com o banco
    }//fim da funcao buscar dados



    //menu voltar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }




}//fim da classe
