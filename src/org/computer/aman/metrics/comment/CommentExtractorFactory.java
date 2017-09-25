package org.computer.aman.metrics.comment;

import java.io.IOException;

import org.computer.aman.io.sourcecode.NotSupportedSourceFileExeption;
import org.computer.aman.io.sourcecode.SourceFile;

/**
 * 指定されたソースファイルを解析対象とした CommentExtractor オブジェクトを生成するファクトリ
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CommentExtractorFactory
{
    /**
     * 指定されたソースファイルを解析対象とした CommentExtractor オブジェクトを生成する
     * 
     * @param aFilePath 解析対象のソースファイルパス
     */
    public static CommentExtractor create(final String aFilePath) 
    throws SecurityException, NotSupportedSourceFileExeption, IOException
    {
        SourceFile file = new SourceFile(aFilePath);
        
        if ( file.isJavaFile() ){
            return new CommentExtractorForJava(file);
        }
        if ( file.isCFile() ){
            return new CommentExtractorForC(file);
        }
        
        throw new NotSupportedSourceFileExeption("not supported file type: " + aFilePath);    
    }
    
}
