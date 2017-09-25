package org.computer.aman.metrics.comment;

import java.io.IOException;
import java.util.ArrayList;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.util.CodeLineMap;
import org.computer.aman.metrics.util.CodeMap;
import org.computer.aman.metrics.util.CodeMapFactory;

/**
 * メソッド単位でのコメント抽出器
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public abstract class CommentExtractor
{
    /**
     * 指定されたソースファイルを対象とした CommentExtractor オブジェクトを生成する
     * （このクラスは抽象クラスであるため，このコンストラクタはサブクラスのコンストラクタからのみ呼び出される）
     * 
     * @param aSourceFile 対象のソースファイル
     * @throws IOException 
     * @throws NotSupportedSourceFileExeption 
     */
    public CommentExtractor (final SourceFile aSourceFile) 
    throws NotSupportedSourceFileExeption, IOException
    {
        sourceFile = aSourceFile;
        codeMap = CodeMapFactory.create(aSourceFile);
    }
    
    /**
     * 対象ソースファイルのパスを返す
     * 
     * @return 対象ソースファイルのパス
     */
    public String getFilePath()
    {
        return sourceFile.getPath();
    }
    
    /**
     * コメント文の解析を実行し，結果を CommentLine インスタンスの ArrayList として返す．
     * 
     * @param aBeginLineNumber 解析の開始行
     * @param anEndLineNumber 解析の終了行
     * @return コメント抽出の結果
     */
    public abstract ArrayList<CommentLine> parse(final int aBeginLineNumber, final int anEndLineNumber)
    throws IOException, NotSupportedSourceFileExeption ;
    
    
    /**
     * 指定されたコード行マップに従って，指定されたコード行のコメントに該当する箇所のみを抜き出した文字列を返す．
     * 
     * @param aLineMap コード行マップ
     * @param aCodeLine コード行
     * @return 抜き出されたコメント
     */
    protected static String extractComments ( final CodeLineMap aLineMap, final String aCodeLine )
    {
        final int MAP_LENGTH = aLineMap.getMap().length();
        StringBuffer comments = new StringBuffer();
        for ( int j = 0; j < MAP_LENGTH; j++ ){
        	if ( aLineMap.isComment(j) ){
        		comments.append(aCodeLine.charAt(j));
        	}
        }
    	return new String(comments);
    }
    
    /**
     * 対象としているソースコードのコードマップを返す（サブクラス向け）
     * 
     * @return 対象としているソースコードのコードマップ
     */
    protected CodeMap getCodeMap()
    {
        return codeMap;
    }
    
    /** 測定対象ソースファイルのコードマップ */
    private CodeMap codeMap;
    
    /** 測定対象のソースファイル */
    private SourceFile sourceFile;

}
