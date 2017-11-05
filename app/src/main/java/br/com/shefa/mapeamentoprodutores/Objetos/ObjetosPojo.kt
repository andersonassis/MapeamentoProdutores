package br.com.shefa.mapeamentoprodutores.Objetos

/**
 * Created by AndersonLuis on 02/11/2017.
 */

class ObjetosPojo {

    //variaveis
    var id: Int = 0  //id do banco
    var dataColeta: String? = null  //data da coleta no sitio
    var rota: String? = null        // rota exemplo LEIITAP
    var subRota: String? = null   // Linha
    var codProdutor:String? = null   // codigo do produtor
    var nomeProdutor: String? = null  // nome do produtor
    var enderecoProdutor:String? = null // endereço do produtor
    var cidade:String? = null  // cidade
    var imei:String? = null   //imei do aparelho
    var latitude:String? = null  // latitude
    var longitude:String? = null  // longitude
    var obs:String? = null        //obs
    var datahora:String? = null    // data e hora da coleta
    var salvou:String? = null



    constructor(){
    }

    constructor(id: Int, rota: String, dataColeta:String, subRota:String, codProdutor:String,nomeProdutor:String,
                enderecoProdutor:String, cidade:String, imei:String, latitude:String, longitude:String, dataHora:String, obs:String, salvou:String) {
        this.id = id
        this.rota = rota
        this.dataColeta = dataColeta
        this.subRota    = subRota
        this.codProdutor = codProdutor
        this.nomeProdutor = nomeProdutor
        this.enderecoProdutor = enderecoProdutor
        this.cidade = cidade
        this.imei   = imei
        this.latitude = latitude
        this.longitude = longitude
        this.obs  = obs
        this.datahora  = datahora
        this.salvou  = salvou
    }


}//fim da classe
