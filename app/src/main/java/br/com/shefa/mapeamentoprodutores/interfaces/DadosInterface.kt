package br.com.shefa.mapeamentoprodutores.interfaces

import br.com.shefa.mapeamentoprodutores.Objetos.ObjetosPojo
import java.util.ArrayList

/**
 * Created by AndersonLuis on 02/11/2017.
 */
interface DadosInterface  {
    fun addColeta(objetos: ObjetosPojo);
    fun getALLColeta(): ArrayList<ObjetosPojo>

}