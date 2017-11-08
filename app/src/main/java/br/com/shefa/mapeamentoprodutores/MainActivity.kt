package br.com.shefa.mapeamentoprodutores

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import br.com.shefa.mapeamentoprodutores.BD_Interno.DB_Interno
import br.com.shefa.mapeamentoprodutores.Conexao.TestarConexao
import br.com.shefa.mapeamentoprodutores.Toast.ToastManager
import kotlinx.android.synthetic.main.activity_main.*
import br.com.shefa.mapeamentoprodutores.Permissoes.PermissionUtils
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import br.com.shefa.mapeamentoprodutores.Gps.Gps
import br.com.shefa.mapeamentoprodutores.Objetos.ObjetosPojo
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var requestQueue: RequestQueue
    lateinit var coletaArrayList: ArrayList<ObjetosPojo>
    var conexao:Boolean = false
    var numeroImei:String = ""
    var telephonyManager: TelephonyManager? = null
    var latitude:String =""
    var longitude:String= ""
    var progress: ProgressDialog? = null
    var banco: DB_Interno? = null
    var contando_registros:Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        banco = DB_Interno(this)//chama o banco
        val gps   = Gps(this) //inicia a classe do gps
        conexao = TestarConexao().verificaConexao(this)
        contando_registros = banco!!.contandoregistros()

        // Solicita as permissoes
        val permissoes = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET)
        PermissionUtils.validate(this, 0, *permissoes)

        //click botao baixar linhas
            btn_baixar_linhas.setOnClickListener {
                if (conexao) {
                    numeroImei = imei()
                    if (contando_registros<=0) {
                        importaLinhas(numeroImei)
                    }else{
                        val alert = AlertDialog.Builder(this)
                        alert.setTitle("ATENÇÃO!! LINHAS JA IMPORTADAS")
                        alert.setMessage("Deseja atualizar as LINHAS ?")
                        alert.setPositiveButton("ATUALIZAR", DialogInterface.OnClickListener { dialog, whichButton ->
                            banco!!.deletar()//deleta todos os registros
                            importaLinhas(numeroImei)
                        })
                        alert.setNegativeButton("CANCELAR") { dialog, which ->  }
                        alert.show()

                    }//fim do else contando registros
                } else {
                    ToastManager.show(this@MainActivity, "SEM CONEXÃO COM INTERNET, VERIFIQUE", ToastManager.INFORMATION)
                }
            }//fim botao baixar linhas




        //click botao exibir linhas vai para a tela listar produtores
        btn_exibir_linhas.setOnClickListener{
            val intent = Intent(this@MainActivity, ListarProdutores::class.java)
            startActivity(intent)


            //botao enviar
            btn_enviar_dados.setOnClickListener{



            }






        }//fim botao exibir linhas


    }//fim do oncreate

    //subescreve o metodo para as permissoes
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (result in grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Alguma permissÃ£o foi negada, agora Ã© com vocÃª :-)
                alertAndFinish()
                return
            }
        }
    }

    //entra aqui se o usuario não conceder alguma permissão
    private fun alertAndFinish() {
        run {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.app_name).setMessage("Para utilizar este aplicativo, voce precisa aceitar as permissoes.")
            // Add the buttons
            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id -> finish() })
            val dialog = builder.create()
            dialog.show()
        }
    }

    //funçao importar as linhas
    private fun importaLinhas(imei: String) {
        progress = ProgressDialog(this);
        progress!!.setMessage("Baixando as linhas por favor aguarde");
        progress!!.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress!!.show();//inicio progress
        requestQueue = Volley.newRequestQueue(this)//inicio volley
        val url = "http://www.shefa-comercial.com.br:8080/coleta/ArquivoEnvio/$imei/$imei.txt"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    try {
                        coletaArrayList = ArrayList<ObjetosPojo>()
                        val jsonArray = response.getJSONArray("rotas")
                        for (i in 0 until jsonArray.length()) {
                            val rotas = jsonArray.getJSONObject(i)
                            val idJson = rotas.getString("sid")
                            val idt = rotas.getString("id")
                            val dataColetaJson = rotas.getString("datacoleta")
                            val rotaJson = rotas.getString("rota")
                            val subRotaJson = rotas.getString("subrota")
                            val codTransportadoraJson = rotas.getString("codTransportadora")
                            val codProdutorJson = rotas.getString("codProdutor")
                            val nomeProdutorJson = rotas.getString("nomeProdutor")
                            val enderecoProdutorJson = rotas.getString("enderecoProdutor")
                            val cidadeJson = rotas.getString("cidade")
                            val qtdJson = rotas.getString("qtd")
                            val imeiJson = rotas.getString("imei")
                            val temperaturaJson = rotas.getString("temperatura")
                            val latitudeJson = rotas.getString( "latitude")
                            val longitudeJson = rotas.getString("longitude")
                            val alisarolJson = rotas.getString("alisarol")
                            val obsJson = rotas.getString("obs")
                            val latitudeLocalJson = rotas.getString("latitudeLocal")
                            val longitudeLocalJson = rotas.getString("longitudeLocal")
                            var origemLatJosn = rotas.getString("origemlat")
                            var origemLongJson = rotas.getString("origemlog")
                            val datahoraJson = rotas.getString("datahora")

                            val coleta = ObjetosPojo()
                            coleta.id = idJson.toInt()
                            coleta.dataColeta =  ""         //dataColetaJson
                            coleta.rota = rotaJson
                            coleta.subRota = subRotaJson
                            coleta.codProdutor = codProdutorJson
                            coleta.nomeProdutor = nomeProdutorJson
                            coleta.enderecoProdutor = enderecoProdutorJson
                            coleta.cidade = cidadeJson
                            coleta.imei   = imeiJson
                            coleta.latitude = latitudeJson
                            coleta.longitude = longitudeJson
                            coleta.obs       = obsJson
                            coleta.datahora = datahoraJson
                            coleta.salvou   = ""

                            //aqui vai salvar no banco
                             banco!!.addColeta(coleta)

                        }//fim do for
                        progress!!.dismiss();//encerra progress

                    } catch (e: JSONException) {
                        ToastManager.show(this@MainActivity, "Falha no arquivo,favor entrar em contato com a TI", ToastManager.ERROR)
                        progress!!.dismiss();//encerra progress
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    Log.e("Falha", "ERRO")
                    ToastManager.show(this@MainActivity, "Falha na conexão ou arquivo não existe,por favor tentar Novamente", ToastManager.ERROR)
                    progress!!.dismiss();//encerra progress
                }
        ) //fim do volley
           requestQueue.add(jsonObjectRequest)
    }// fim funçao importar as linhas


    //pegar  IMEI
    @SuppressLint("MissingPermission")
    fun imei():String{
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val deviceId = telephonyManager!!.getDeviceId()
        return  deviceId
    }


}//fim da classe main
