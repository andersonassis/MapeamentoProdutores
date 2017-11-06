package br.com.shefa.mapeamentoprodutores

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_listar_produtores.*
import java.util.ArrayList
import android.widget.AdapterView
import android.widget.Toast
import br.com.shefa.mapeamentoprodutores.R.id.spinner



class ListarProdutores : AppCompatActivity() {
    var db: SQLiteDatabase? = null
    internal lateinit var cursorSpinner: Cursor
    internal lateinit var cursor: Cursor
    var label3: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_produtores)

         ListagemSpinner()

    }//fim do oncreate


    //metodo para buscar as linhas
    private fun ListagemSpinner() {
        val lables2:ArrayList<String> = subRotaLinhas() as ArrayList<String>
        val arraySpinner = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        arraySpinner.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        arraySpinner.addAll(lables2)
        spinner.adapter = arraySpinner

        //pegando o valor clicado
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
           override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                label3 = parent.getItemAtPosition(position).toString()//valor clicado
                buscarProdutores()
                criarListagem()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    }//fim do metodo ListagemSpinner
    private fun subRotaLinhas(): Any {
        val labels = ArrayList<String>()//para guardar as linhas em um array
        db = openOrCreateDatabase("mapeamento.db", Context.MODE_PRIVATE, null)
        cursorSpinner = db!!.rawQuery("SELECT _subRota FROM  tabela_mapeamento  GROUP BY  _subRota  ",null )
        if (cursorSpinner.moveToFirst()) {
            do {
                try {
                    labels.add(cursorSpinner.getString(0))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } while (cursorSpinner.moveToNext())
        }
        cursorSpinner.close()
        db!!.close()
       return labels
    }

  //função buscar produtores
    fun buscarProdutores() {
         try {
             db = openOrCreateDatabase("mapeamento.db", Context.MODE_PRIVATE, null)
             cursor = db!!.rawQuery("SELECT * FROM  tabela_mapeamento  WHERE   _subRota = '$label3' ORDER BY  _nomeProdutor ", null)//SELECT PARA PEGAR SOMENTE O QUE NÃO FOI ENVIADO e  A LINHA ESCOLHIDA PELO SPINNER
         } catch (e: Exception) {
             Toast.makeText(this@ListarProdutores,"ERROR", Toast.LENGTH_LONG).show()
         }
     }//fim buscarProdutores


    //função para criar a lsiatagem no listview
    fun criarListagem() {


    }//fim criarListagem


}//fim da classe
