package br.com.shefa.mapeamentoprodutores.ConverteJson

import android.util.Log
import br.com.shefa.mapeamentoprodutores.Objetos.ObjetosPojo
import org.json.JSONStringer
import java.util.ArrayList

/**
 * Created by aassis on 08/11/2017.
 */
class ConverteJson  {
    fun toJson(listaColetas: ArrayList<ObjetosPojo>): String? {
        try {
            val jsonStringer = JSONStringer()
            jsonStringer.`object`().key("coletas").array()
            for (coletaPojo in listaColetas) {
                jsonStringer.`object`()
                        .key("_id").value(coletaPojo.id)
                        .key("_rota").value(coletaPojo.rota)
                        .key("_subRota").value(coletaPojo.subRota)
                        .key("_codProdutor").value(coletaPojo.codProdutor)
                        .key("_nomeProdutor").value(coletaPojo.nomeProdutor)
                        .key("_cidade").value(coletaPojo.cidade)
                        .key("_imei").value(coletaPojo.imei)
                        .key("_obs").value(coletaPojo.obs)
                        .key("_latitude").value(coletaPojo.latitude)
                        .key("_longitude").value(coletaPojo.longitude)
                        .key("_dataHora").value(coletaPojo.datahora)
                        //.key("_confirmaEnvio").value(coletaPojo.getEnviaArquivo())
                        .endObject()
            }
            jsonStringer.endArray().endObject()
            return jsonStringer.toString()

        } catch (e: Exception) {
            Log.i("Mapeamento", e.message)
            return null
        }

    }
}