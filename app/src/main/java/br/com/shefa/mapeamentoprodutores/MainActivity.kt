package br.com.shefa.mapeamentoprodutores

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.telephony.TelephonyManager
import br.com.shefa.mapeamentoprodutores.BD_Interno.DB_Interno
import br.com.shefa.mapeamentoprodutores.Conexao.TestarConexao
import br.com.shefa.mapeamentoprodutores.Toast.ToastManager
import kotlinx.android.synthetic.main.activity_main.*

import android.widget.Toast


class MainActivity : AppCompatActivity() {
    var conexao:Boolean = false
    var numeroImei:String = ""
    private val MY_PERMISSIONS_REQUEST_CODE = 1
    var telephonyManager: TelephonyManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val banco = DB_Interno(this)//chama o banco
        conexao = TestarConexao().verificaConexao(this)

        //PERMISSÃO PARA PEGAR O IMEI
        if (checkPermissions()) {
            numeroImei = imei()

        } else {
            setPermissions()
        }

        //click botao baixar linhas
        btn_baixar_linhas.setOnClickListener{
            if (conexao) {
                ToastManager.show(this@MainActivity, "click botao baixar", ToastManager.INFORMATION)
                banco.deletar()//deleta todos os registros
                importaLinhas(numeroImei)

            }else{
                ToastManager.show(this@MainActivity, "SEM CONEXÃO COM INTERNET, VERIFIQUE", ToastManager.INFORMATION)
            }
        }//fim botao baixar linhas

        //click botao exibir linhas
        btn_exibir_linhas.setOnClickListener{
            ToastManager.show(this@MainActivity, "click botao exibir", ToastManager.INFORMATION)
        }//fim botao exibir linhas


    }//fim do oncreate

    //funçao importar linhas
    private fun importaLinhas(imei: String) {


    }

    //permissão para o imei
    fun checkPermissions(): Boolean {
            return if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                false
            } else
                true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != MY_PERMISSIONS_REQUEST_CODE) {
            return
        }
        var isGranted = true
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false
                break
            }
        }
        if (isGranted) {
            imei()
        } else {
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
        }
    }
    fun setPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), MY_PERMISSIONS_REQUEST_CODE)
    }

    //permissoes para IMEI
    @SuppressLint("MissingPermission")
    fun imei():String{
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val deviceId = telephonyManager!!.getDeviceId()
        return  deviceId
    }

    //fim da permissão imei*******************************************************************************



}//fim da classe main
