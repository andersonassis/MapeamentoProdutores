package br.com.shefa.mapeamentoprodutores.BD_Interno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import br.com.shefa.mapeamentoprodutores.Objetos.ObjetosPojo;
import br.com.shefa.mapeamentoprodutores.interfaces.DadosInterface;

/**
 * Created by AndersonLuis on 02/11/2017.
 */

public class DB_Interno extends SQLiteOpenHelper implements DadosInterface {
    //Campos da tabela principal
    private static final int DB_VERSION            = 1;
    private static final String DB_NAME            = "mapeamento.db";
    private static final String TABLE_NAME         = "tabela_mapeamento";
    private static final String ID                 = "_id";
    private static final String DATACOLETA         = "_dataColeta";
    private static final String ROTA               = "_rota";
    private static final String SUBROTA            = "_subRota";
    private static final String COD_PRODUTOR       = "_codProdutor";
    private static final String NOME_PRODUTOR      = "_nomeProdutor";
    private static final String ENDERECO_PRODUTOR  = "_enderecoProdutor";
    private static final String CIDADE           = "_cidade";
    private static final String IMEI             = "_imei";
    private static final String LATITUDE         = "_latitude";
    private static final String LONGITUDE        = "_longitude";
    private static final String OBS              = "_obs";
    private static final String DATAHORA         = "_dataHora";
    private static final String SALVOU           = "_salvou";


    //criando a tabela que vai conter os dados em geral
    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY," + DATACOLETA + " TEXT," + ROTA + " TEXT," + SUBROTA + " TEXT," + COD_PRODUTOR + " TEXT," + NOME_PRODUTOR + " TEXT,"
            + ENDERECO_PRODUTOR + " TEXT," + CIDADE + " TEXT," + IMEI + " INT," + LATITUDE + " REAL," + LONGITUDE + " REAL,"
            + OBS + " CHAR(150)," + DATAHORA + " TEXT" + SALVOU+ "TEXT  )";

    String DROP_TABLE  = "DROP TABLE IF EXISTS " + TABLE_NAME;

    //construtor
    public DB_Interno(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.e("criar",   "banco criado com sucesso");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);

    }

    @Override
    public void addColeta(@NotNull ObjetosPojo objetos) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(ID, objetos.getId());
            values.put(DATACOLETA, objetos.getDataColeta());
            values.put(ROTA, objetos.getRota());
            values.put(SUBROTA, objetos.getSubRota());
            values.put(COD_PRODUTOR, objetos.getCodProdutor());
            values.put(NOME_PRODUTOR, objetos.getNomeProdutor());
            values.put(ENDERECO_PRODUTOR, objetos.getEnderecoProdutor());
            values.put(CIDADE, objetos.getCidade());
            values.put(IMEI, objetos.getImei());
            values.put(LATITUDE, objetos.getLatitude());
            values.put(LONGITUDE, objetos.getLongitude());
            values.put(OBS, objetos.getObs());
            values.put(DATAHORA, objetos.getDatahora());
            db.insert(TABLE_NAME, null, values);
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Problema", e + "Problema ao gravar a tabela");
        }

    }// fim addColeta

    @NotNull
    @Override
    public ArrayList<ObjetosPojo> getALLColeta() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ObjetosPojo> objetos = null;

        try {
            objetos = new ArrayList<ObjetosPojo>();
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    ObjetosPojo coleta = new ObjetosPojo();
                    coleta.setId(cursor.getInt(0));
                    coleta.setDataColeta(cursor.getString(1));
                    coleta.setRota(cursor.getString(2));
                    coleta.setSubRota(cursor.getString(3));
                    coleta.setCodProdutor(cursor.getString(4));
                    coleta.setNomeProdutor(cursor.getString(5));
                    coleta.setEnderecoProdutor(cursor.getString(6));
                    coleta.setCidade(cursor.getString(7));
                    coleta.setImei(cursor.getString(8));
                    coleta.setLatitude(cursor.getString(9));
                    coleta.setLongitude(cursor.getString(10));
                    coleta.setObs(cursor.getString(11));
                    coleta.setDatahora(cursor.getString(12));
                    objetos.add(coleta);
                }
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Problemas", e + "Problema ao ler a tabela");
        }
        return objetos;
    }//FIM ArrayList<ObjetosPojo> getALLColeta()



    //funcao deletar
    public String deletar(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String QUERY = ("DELETE  FROM " + TABLE_NAME );
            db.execSQL(QUERY );
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }




















} //fim da classe DB
