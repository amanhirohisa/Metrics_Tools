package org.computer.aman.metrics.comment;

/**
 * 1 行分のコメントのモデル
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class CommentLine 
{
	/**
	 * CommentLine インスタンスを生成する．
	 * 
	 * @param aLineNumber 行番号
	 * @param aType タイプ番号（1--7）
	 * @param aText コメントの内容（コメントの開始・終了を意味する文字列を除く）
	 */
	public CommentLine(final int aLineNumber, final int aType, final String aText)
	{
		lineNumber = aLineNumber;
		type = aType;
		text = new String(aText);
	}
	
	public String toString()
	{
		return (lineNumber + "\t" + type + "\t" + text);
	}
	
	/** コメントが書かれている行番号 */
	private int lineNumber;
	
	/** コメントの内容（コメントの開始・終了を意味する文字列を除く） */
	private String text;
	
	/** コメントのタイプ（番号: 1--7） */
	private int type;
}
