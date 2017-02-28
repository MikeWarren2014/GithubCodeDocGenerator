package me.mikewarren.documentationPageCreator;

public class DataType {

	private boolean _isBuiltIn;
	private PrimitiveDataType _primitive;
	private String _type;
	
	public DataType()
	{
		// default: nothing
		this._isBuiltIn = false;
		this._primitive = PrimitiveDataType.NONE;
		this._type = "";
	}
	
	public DataType(PrimitiveDataType type)
	{
		// it is built in, since it was specified with PrimitiveDataType
		this._isBuiltIn = true;
		this._primitive = type;
		// ... therefore, the string is simply the string representation of the PrimitiveDataType
		this._type = type.toString().toLowerCase().replaceAll("_", " ");
	}
	
	public DataType(String type)
	{
		// safe to assume that it is not built-in
		this._isBuiltIn = false;
		// ... and doesn't point to a PrimitiveDataType
		this._primitive = PrimitiveDataType.NONE;
		this._type = type;
	}
	
	public DataType(String type, boolean builtIn)
	{
		this._isBuiltIn = builtIn;
		this._primitive = PrimitiveDataType.NONE;
		this._type = type;
	}
	
	public DataType(String type, PrimitiveDataType primitive)
	{
		this._isBuiltIn = true;
		this._primitive = primitive;
		this._type = type;
	}
	
	@Override
	public String toString()
	{
		String str = "";
		// if there is a PrimitiveDataType
		if (_primitive != PrimitiveDataType.NONE)
		{
			// get its string representation
			String primitiveToString = _primitive.toString();
			str += primitiveToString.replaceAll("_", " ");
			// append space
			str += ' ';
		}
		str += _type;
		return str;
	}
	
}
