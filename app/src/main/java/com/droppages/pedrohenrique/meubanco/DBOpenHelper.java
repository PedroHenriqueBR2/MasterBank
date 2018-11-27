package com.droppages.pedrohenrique.meubanco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBOpenHelper extends SQLiteOpenHelper {
    // Dados iniciais
    private static int DATABASE_VERSION = 2;
    private static String DATABASE_NOME = "bancodb";

    // Usuário
    private static String USUARIO_TABLE = "usuario";
    private static String USUARIO_ID = "id";
    private static String USUARIO_NOME = "nome";
    private static String USUARIO_LOGIN = "login";
    private static String USUARIO_SENHA = "senha";

    // Banco
    private static String BANCO_TABLE = "banco";
    private static String BANCO_ID = "id";
    private static String BANCO_NOME = "nome";
    private static String BANCO_USUARIO = "usuario";

    // Movimentacao
    private static String MOV_TABLE = "movimentacao";
    private static String MOV_ID = "id";
    private static String MOV_BANCO = "banco";
    private static String MOV_USUARIO = "usuario";
    private static String MOV_VALOR = "valor";

    public DBOpenHelper(Context context){
        super(context, DATABASE_NOME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABELA_USUARIO = "create table if not exists usuario (id integer primary key autoincrement, nome varchar(50), login varchar(50), senha varchar(50));";
        String TABELA_BANCO = "create table if not exists banco (id integer primary key autoincrement, nome varchar(50), usuario integer, foreign key(usuario) references usuario(id));";
        String TABELA_MOVIMENTACAO = "create table if not exists movimentacao (id integer primary key autoincrement, banco integer, usuario integer, valor float, foreign key(banco) references banco(id), foreign key(usuario) references usuario(id));";
        db.execSQL(TABELA_USUARIO);
        db.execSQL(TABELA_BANCO);
        db.execSQL(TABELA_MOVIMENTACAO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists usuario;");
        db.execSQL("drop table if exists banco;");
        db.execSQL("drop table if exists movimentacao;");
        this.onCreate(db);
    }

    // Cadastrar dados nas tabelas
    public void novoUsuario(UsuarioDB usuario){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USUARIO_NOME, usuario.getNome());
        values.put(USUARIO_LOGIN, usuario.getLogin());
        values.put(USUARIO_SENHA, usuario.getSenha());

        db.insert(USUARIO_TABLE, null, values);
        db.close();
    }

    public void novoBanco(BancoDB banco){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BANCO_NOME, banco.getNome());
        values.put(BANCO_USUARIO, banco.getUsuario());

        db.insert(BANCO_TABLE, null, values);
        db.close();
    }

    public void novaMovimentacao(MovimentacaoDB movimentacao){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MOV_BANCO, movimentacao.getBanco());
        values.put(MOV_USUARIO, movimentacao.getUsuario());
        values.put(MOV_VALOR, movimentacao.getValor());

        db.insert(MOV_TABLE, null, values);
        db.close();
    }

    // Conversor de dados
    private UsuarioDB cursorToUsuario(Cursor cursor){
        UsuarioDB usuario = new UsuarioDB();
        usuario.setId(cursor.getInt(0));
        usuario.setNome(cursor.getString(1));
        usuario.setLogin(cursor.getString(2));
        usuario.setSenha(cursor.getString(3));
        return usuario;
    }

    private BancoDB cursorToBanco(Cursor cursor){
        BancoDB banco = new BancoDB();
        banco.setId(cursor.getInt(0));
        banco.setNome(cursor.getString(1));
        banco.setUsuario(cursor.getInt(2));
        return banco;
    }

    private MovimentacaoDB cursorToMovimentacao(Cursor cursor){
        MovimentacaoDB mov = new MovimentacaoDB();
        mov.setId(cursor.getInt(0));
        mov.setBanco(cursor.getInt(1));
        mov.setValor(cursor.getFloat(2));
        return mov;
    }

    // Selecionar todos os dados
    public ArrayList<UsuarioDB> todosOsUsuarios(){
        ArrayList<UsuarioDB> usuario = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "Select * from " + USUARIO_TABLE;

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do {
                UsuarioDB user = cursorToUsuario(cursor);
                usuario.add(user);
            } while (cursor.moveToNext());
        }
        return usuario;
    }


    public ArrayList<BancoDB> todosOsBancos(int usuario){
        ArrayList<BancoDB> banco = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select * from banco as b where b.usuario = "+ usuario +";";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do {
                BancoDB bancoDB = cursorToBanco(cursor);
                banco.add(bancoDB);
            } while (cursor.moveToNext());
        }

        return banco;
    }

    public ArrayList<MovimentacoesRealizadas> todasAsMovimentacoes(int usuario){
        ArrayList<MovimentacoesRealizadas> mov = new ArrayList<>();
        MovimentacoesRealizadas realizadas;
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select m.id, b.nome, m.valor from banco as b inner join movimentacao as m on m.banco = b.id where m.usuario = "+usuario+" order by m.id desc limit 20;";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do {
                realizadas = new MovimentacoesRealizadas(cursor.getInt(0) ,cursor.getString(1), Float.toString(cursor.getFloat(2)));
                mov.add(realizadas);
            } while (cursor.moveToNext());
        }

        return mov;
    }

    // Atualizar dados
    public boolean updateUsuario(UsuarioDB usuario){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USUARIO_NOME, usuario.getNome());
        values.put(USUARIO_LOGIN, usuario.getLogin());
        values.put(USUARIO_SENHA, usuario.getSenha());

        int retorno = db.update(USUARIO_TABLE,
                values,
                USUARIO_ID + " = ?",
                new String[]{String.valueOf(usuario.getId())});

        db.close();
        return retorno > 0;
    }

    public boolean updateBanco(BancoDB banco){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BANCO_NOME, banco.getNome());

        int retorno = db.update(BANCO_TABLE,
                values,
                BANCO_ID + " = ?",
                new String[]{String.valueOf(banco.getId())});

        db.close();
        return retorno > 0;
    }

    // Atualizar dados
    public boolean updateMovimentacao(MovimentacaoDB mov){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MOV_VALOR, mov.getValor());

        int retorno = db.update(MOV_TABLE,
                values,
                MOV_ID + " = ?",
                new String[]{String.valueOf(mov.getId())});

        db.close();
        return retorno > 0;
    }


    // Deletar dados
    public boolean deleteUsuario(UsuarioDB usuario){
        SQLiteDatabase db = this.getWritableDatabase();
        int retorno = db.delete(USUARIO_TABLE,
                USUARIO_ID + " = ?",
                new String[] {String.valueOf(usuario.getId())});
        db.close();
        return retorno > 0;
    }

    public boolean deleteBanco(BancoDB banco){
        SQLiteDatabase db = this.getWritableDatabase();
        int retorno = db.delete(BANCO_TABLE,
                BANCO_ID + " = ?",
                new String[] {String.valueOf(banco.getId())});
        db.close();
        return retorno > 0;
    }

    public boolean deleteMovimentacao(MovimentacaoDB mov){
        SQLiteDatabase db = this.getWritableDatabase();
        int retorno = db.delete(MOV_TABLE,
                MOV_ID + " = ?",
                new String[] {String.valueOf(mov.getId())});
        db.close();
        return retorno > 0;
    }


    // Métodos do sistemas
    // Login
    public boolean loginUsuario(String login, String senha){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from usuario where login = '"+ login +"' and senha = '"+ senha +"';";
        Cursor cursor = db.rawQuery(sql, null);

        int t = cursor.getCount();
        return t > 0;
    }
    // Seleciona dados por login
    public UsuarioDB dadosPorLogin(String login, String senha){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from usuario where login = '"+ login +"' and senha = '"+ senha +"';";

        UsuarioDB usuario = new UsuarioDB();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            usuario.setId(cursor.getInt(0));
            usuario.setNome(cursor.getString(1));
            usuario.setLogin(cursor.getString(2));
            usuario.setSenha(cursor.getString(3));
        } else {
            usuario.setId(1);
            usuario.setNome("not found");
            usuario.setLogin("not found");
            usuario.setSenha("not found");
        }

        return usuario;
    }
    public boolean repeteUsuario(String login){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from usuario where login = '"+ login +"';";
        Cursor cursor = db.rawQuery(sql, null);

        int t = cursor.getCount();
        return t > 0;
    }


    // Calcula saldo de banco
    public float calculaSaldoDeBanco(int banco, int usuario){
        float resultado;
        String sql = "select sum(valor) as soma from movimentacao where banco = "+ banco +" and usuario = "+ usuario +";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            resultado = cursor.getFloat(0);
        } else {
            resultado = 0;
        }

        return resultado;
    }
    // Calcula saldo total
    public float saldoTotal(int usuario){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select sum(valor) as soma from movimentacao where usuario = "+usuario+";";
        Cursor cursor = db.rawQuery(sql, null);

        float resultado;
        if (cursor.moveToFirst()){
            resultado = cursor.getFloat(0);
        } else {
            resultado = 0;
        }

        return resultado;
    }

    // Seleciona dados por banco
    public int idPorBanco(String banco){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select id from banco where nome = '"+ banco +"';";

        int resultado;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            resultado = cursor.getInt(0);
        } else {
            resultado = 1;
        }

        return resultado;
    }
    public boolean repeteBanco(String banco){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from banco where nome = '"+ banco +"';";
        Cursor cursor = db.rawQuery(sql, null);

        int t = cursor.getCount();
        return t > 0;
    }

}
