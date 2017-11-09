package br.com.shefa.mapeamentoprodutores.SalvaDIR

import android.os.Environment
import java.io.File
import java.io.FileOutputStream

/**
 * Created by aassis on 08/11/2017.
 */
class SalvarDiretorio  {

    fun SalvarDiretorio(json: String?, data:String){ //construtor

        val fileName = data +".txt"
        val myFile = File(Environment.getExternalStorageDirectory(), fileName)//grava na memoria
        try {
            myFile.createNewFile()//cria o arquivo
            val stream = FileOutputStream(myFile)
            stream.write(json!!.toByteArray()) //escreve no arquivo
            stream.close()//fecha a criação
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }//fim do constutor
}