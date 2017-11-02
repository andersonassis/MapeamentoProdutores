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
import br.com.shefa.mapeamentoprodutores.Permissoes.PermissionUtils
import android.content.DialogInterface
import android.support.v7.app.AlertDialog


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

        // Solicita as permissÃµes
        val permissoes = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET)
        PermissionUtils.validate(this, 0, *permissoes)

        //click botao baixar linhas
        btn_baixar_linhas.setOnClickListener{
            if (conexao) {
                banco.deletar()//deleta todos os registros
                numeroImei = imei()
                importaLinhas(numeroImei)

                imei.setText(numeroImei)//textview

            }else{
                ToastManager.show(this@MainActivity, "SEM CONEXÃO COM INTERNET, VERIFIQUE", ToastManager.INFORMATION)
            }
        }//fim botao baixar linhas

        //click botao exibir linhas
        btn_exibir_linhas.setOnClickListener{
            ToastManager.show(this@MainActivity, "click botao exibir", ToastManager.INFORMATION)
        }//fim botao exibir linhas


    }//fim do oncreate

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

    //funçao importar linhas
    private fun importaLinhas(imei: String) {

    }



    //pegar  IMEI
    @SuppressLint("MissingPermission")
    fun imei():String{
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val deviceId = telephonyManager!!.getDeviceId()
        return  deviceId
    }




}//fim da classe main
