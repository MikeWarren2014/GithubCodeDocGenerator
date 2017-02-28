package me.mikewarren.documentationPageCreator;

public class CommentType 
{
	private String _startSymbol = "";
	private boolean _isMultiline = false;
	private String _endSymbol = "";
	
	public CommentType(String symbol)
	{
		this(symbol, false, "");
	}
	
	public CommentType(String start, String end)
	{
		this(start, true, end);
	}
	
	private CommentType(String start, boolean multiline, String end)
	{
		_startSymbol = start;
		_isMultiline = multiline;
		_endSymbol = end;
	}
	
	public final String getStartSymbol() {  return _startSymbol; }
	public final String getEndSymbol() { return _endSymbol; }
	public final boolean isMultiline() { return _isMultiline; }
	
	/* some default CommentTypes */
	public static CommentType DOUBLE_FORWARD_SLASH = new CommentType("//");
	public static CommentType FORWARD_SLASH_ASTERISK = new CommentType("/*", "*/");
	public static CommentType HASHTAG = new CommentType("#");
}
