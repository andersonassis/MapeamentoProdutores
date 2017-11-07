package br.com.shefa.mapeamentoprodutores

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SimpleCursorAdapter
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_listar_produtores.*
import java.util.ArrayList
import android.widget.AdapterView
import android.widget.Toast
import br.com.shefa.mapeamentoprodutores.Toast.ToastManager


class ListarProdutores : AppCompatActivity() {
    var db: SQLiteDatabase? = null
    internal lateinit var cursorSpinner: Cursor
    internal lateinit var cursor: Cursor
    var label3: String? = null
    var ad: SimpleCursorAdapter? = null
    internal var posicao: Int = 0


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

                //aqui tentar fazer a escolha da linha

                buscarProdutores()
                criarListagem()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    }//fim do metodo ListagemSpinner
    private fun subRotaLinhas(): Any {
        val labels = ArrayList<String>()//para guardar as linhas em um array
        val num = ""
        db = openOrCreateDatabase("mapeamento.db", Context.MODE_PRIVATE, null)
        cursorSpinner = db!!.rawQuery("SELECT _subRota  FROM tabela_mapeamento     GROUP BY  _subRota  ", null);//SELECT PARA PEGAR
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
        val from = arrayOf("_id","_subRota", "_nomeProdutor", "_enderecoProdutor", "_salvou")
        val to = intArrayOf(R.id.txtId, R.id.txtsuRota, R.id.txtNomeProdutor, R.id.txtendereco, R.id.star)
        try {
            ad = SimpleCursorAdapter(applicationContext, R.layout.itens_produtores, cursor, from, to, 0);
            ad!!.setViewBinder(CustomViewBinder())//chamando este adaptador para acrescentar o check caso o produtor ja foi salvo
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //habilita o click no item da lista
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            val sqlCursor = ad!!.getItem(position) as SQLiteCursor
            val nomeProdutor = sqlCursor.getString(sqlCursor.getColumnIndex("_nomeProdutor"))
            val idProdutor = sqlCursor.getString(sqlCursor.getColumnIndex("_id"))
            ToastManager.show(applicationContext, "selecionou: " + nomeProdutor, ToastManager.INFORMATION)

            //chama a tela para inserir os dados
            val altera = Intent(applicationContext, AlteraDados::class.java)
            altera.putExtra("id_Produtor", idProdutor)
            startActivity(altera)

        })
        listView.setAdapter(ad)//chama o adaptador que monta a lista
    }//fim criarListagem

    // coloca o check na lista se o produtor foi salvo
    inner class CustomViewBinder : android.widget.SimpleCursorAdapter.ViewBinder, SimpleCursorAdapter.ViewBinder {
        override fun setViewValue(view: View, cursor: Cursor, columnIndex: Int): Boolean {
            if (columnIndex == cursor.getColumnIndex("_salvou")) {  // obs: o campo  _salvou serve para verifica se o produtor foi preenchido e salvo
                posicao = cursor.position
                val sqlCursor = ad!!.getItem(posicao) as SQLiteCursor
                val gravou = sqlCursor.getString(sqlCursor.getColumnIndex("_salvou")) // obs: o campo  _salvo serve para verifica se o produtor foi preenchido e salvo
                if (gravou != "1") {
                    view.visibility = View.GONE//  esconde o check
                } else {
                    view.visibility = View.VISIBLE// MOSTRA o check
                }
                return true
            }
            return false
        }
    }//fim CustomViewBinder




}//fim da classe
