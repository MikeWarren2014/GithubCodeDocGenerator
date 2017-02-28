package me.mikewarren.documentationPageCreator;

import java.util.List;
import java.util.LinkedList;

public class Function {

	private String _name = "";
	private List<Variable> _arguments;
	private Variable _returnValue; // should this really be a DataType? 
	
	public Function(String name) throws Exception
	{
		// a function with no arguments or return value
		this(name, new LinkedList<Variable>(), new Variable());
	}
	
	public Function(String name, List<Variable> arguments) throws Exception
	{
		// function with no return value
		this(name, arguments, new Variable());
	}
	
	public Function(String name, List<Variable> arguments, Variable returnVal) throws Exception
	{
		// name cannot contain spaces
		if (name.contains(" ")) throw new IllegalArgumentException("name of Function cannot contain spaces.");
		this._name = name;
		this._arguments = arguments;
		this._returnValue = returnVal;
	}
	
	public final String getName() { return _name; }
	public void setName(String newName)
	{
		_name = newName;
	}
	
	public final String toString()
	{
		return ""; // mock
	}
	
	public final String toString(String typeOfString)
	{
		if (typeOfString == DocumentationPageCreator.LIST_ITEM)
		{
			return String.format("+ [%s()](#%s)", _name, _name.toLowerCase());
		}
		if (typeOfString == DocumentationPageCreator.HEADER)
		{
			return String.format("### %s()", _name);
		}
		if (typeOfString == DocumentationPageCreator.SECTION)
		{
			String functionHeader = toString(DocumentationPageCreator.HEADER),
					section = functionHeader;
			section += "\n_**Parameters**_\n\n";
			for (Variable argument : _arguments)
			{
				section += argument.toString(DocumentationPageCreator.SECTION) + "\n";
			}
			section += String.format("\n_**Returns**_: %s\n", _returnValue.toString());
			return section;
		}
		return "";	// mock section. For now, if the code reaches here, there is problem.
	}
}
