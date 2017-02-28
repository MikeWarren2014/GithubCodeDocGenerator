package me.mikewarren.documentationPageCreator;

public class Variable {
	private String _name = "";
	private DataType _type;
	private String _description = "";
	
	// default type of Variable. This is effectively an alternative to just using "null" (and subjecting yourself to NullPointerException later on in the code)
	public Variable()
	{
		
	}

	public Variable(String name) throws Exception
	{
		// just a name, no type or description
		this(name, "");
	}
	
	public Variable(String name, String desc) throws Exception
	{
		// just a name and a description
		this(new DataType(), name, desc);
	}
	
	public Variable(DataType type, String name) throws Exception 
	{
		// just a type and a name
		this(type, name, "");
	}
	
	public Variable(DataType type, String name, String desc) throws Exception
	{
		// variables cannot contain a space
		if (name.contains(" ")) throw new IllegalArgumentException("Variable cannot contain any spaces");
		
		this._name = name;
		this._type = type;
		this._description = desc;
	}
	
	public final String getDescription() { return _description; }
	
	public void setDescription(String newDesc)
	{
		_description = newDesc;
	}
	
	public static String toMarkdownNameString(String varName)
	{
		return "**" + varName + "**";
	}
	
	public final String toString()
	{
		return toString(DocumentationPageCreator.SECTION);
	}
	
	public final String toString(String type)
	{
		// if the string to be returned is a HEADER or SECTION string
		if ((type == DocumentationPageCreator.SECTION) || (type == DocumentationPageCreator.HEADER))
		{
			// if this is empty, its string representation must also be
			if (this == new Variable()) return "";
			// if this has no _name, we simply return it like "`[_type]` [_description]"
			if (_name == "") return String.format("`%s` %s", _type, _description);
			// return something like "**[_name]** : `[_type]` [_description]"
			return String.format("%s : `%s` %s", Variable.toMarkdownNameString(_name), _type, _description);
		}
		return "";	// subject to change. for now, it's just a mock
	}
}
