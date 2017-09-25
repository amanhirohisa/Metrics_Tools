package org.computer.aman.metrics.comment;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceFile;
import org.computer.aman.metrics.util.CodeLineMap;
import org.computer.aman.metrics.util.c.CodeLineMapForC;

/**
 * Cソースファイル中のメソッドについてコメント抽出機能を実装
 * <p>
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CommentExtractorForC 
extends CommentExtractor
{

    /**
     * 指定されたソースファイルを測定対象とした CommentExtractorForC オブジェクトを生成する
     * 
     * @param aSourceFile 測定対象のソースファイル
     */
    public CommentExtractorForC(final SourceFile aSourceFile) 
    throws NotSupportedSourceFileExeption, IOException
    {
        super(aSourceFile);
    }
    
    /**
     * コメント文の解析を実行し，結果を CommentLine インスタンスの ArrayList として返す．
     * 
     * @param aBeginLineNumber 解析の開始行
     * @param anEndLineNumber 解析の終了行
     * @return コメント抽出の結果
     */
    public ArrayList<CommentLine> parse(final int aBeginLineNumber, final int anEndLineNumber) 
    throws IOException, NotSupportedSourceFileExeption 
    {
        ArrayList<CommentLine> list = new ArrayList<CommentLine>();

        // (1) 解析対象メソッドの直前に書かれた Javadoc （あるいはそれに準ずるもの）を抽出する
        // (1-1) 解析対象行よりも前に登場する実効コード行の中で一番後(predecessorLine)を見つける
        // (1-2) predecessorLine+1 から aBeginLineNumber-1 までの行に登場するコメントを抽出する          
        Iterator<CodeLineMap> itr = getCodeMap().iterator();
        
        int predecessorLine = -1;
        for ( int i = 1; i < aBeginLineNumber; i++ ){
            if ( itr.next().getCodeCount() > 0 ){
                predecessorLine = i;
            }
        }
        
        LineNumberReader reader = new LineNumberReader(new FileReader(getFilePath()));

        if ( predecessorLine != -1 ){
            itr = getCodeMap().iterator();
            for ( int i = 1; i < aBeginLineNumber; i++ ){
            	String line = reader.readLine();
                CodeLineMapForC lineMap = (CodeLineMapForC)itr.next();
                if ( i <= predecessorLine || lineMap.getCommentCount() == 0 ){
                    continue;
                }
                else if ( lineMap.getEolCommentCount() > 0 ){
                	list.add(new CommentLine(i, 4, extractComments(lineMap,line)));
                }
                else{
                	list.add(new CommentLine(i, 5, extractComments(lineMap,line)));
                }
            }
        }
        
        // (2) (1) の続きから一行ずつマップを見ていく
        for ( int i = aBeginLineNumber; i <= anEndLineNumber; i++ ){
        	String line = reader.readLine();
            CodeLineMapForC lineMap = (CodeLineMapForC)itr.next();                        
            if ( lineMap.getCommentCount() == 0 ){
                continue;
            }

            // コメントの確認:
            // 異なるタイプのコメント文が一行に混在する場合は EOLコメントアウト，Traditional コメントアウト，EOL, Traditional の優先順で抽出する 
            if ( lineMap.getTraditionalCommentOutCount() > 0 ){
            	list.add(new CommentLine(i, 7, extractComments(lineMap,line)));            
            }
            else if ( lineMap.getEolCommentOutCount() > 0 ){
            	list.add(new CommentLine(i, 6, extractComments(lineMap,line)));
            }
            else if ( lineMap.getEolCommentCount() > 0 ){
               	list.add(new CommentLine(i, 1, extractComments(lineMap,line)));
            }
            else{
               	list.add(new CommentLine(i, 2, extractComments(lineMap,line)));
            }
        }
        
        reader.close();
        
        return list;
    }
}
