package me.mikewarren.documentationPageCreator;

import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;
import java.io.File;

public abstract class CodeFileParser {
	private String _markdownFileTemplate;
	private List<Function> _codeFileFunctions;
	private List<Variable> _globalVariables;

	public CodeFileParser()
	{
		// create _markdownFileTemplate
		_markdownFileTemplate = "# Documentation for [codeFileName]\n\n" + 
				"## What This Script Does\n\n" + 
				"[codeFileDescription]\n\n" + 
				"## NOTES:\n\n[codeFileNotes]";
		// initialize _codeFileFunctions,_globalVariables
		_codeFileFunctions = new LinkedList<Function>();
		_globalVariables = new LinkedList<Variable>();
	}
	
	public void parse(String fileToParse) throws Exception
	{
		parse(new File(fileToParse));
	}
	
	public abstract void parse(File file) throws Exception;
	
	protected abstract void parseNextSection(Scanner file) throws Exception;
	
	public final String getParsedContents()
	{
		return "";
	}
	
	public final List<Function> getFunctions() { return _codeFileFunctions; }
	public final List<Variable> getGlobalVariables() { return _globalVariables; }
	
	// returns the position of the first alphabetic character in specified string (or -1 if none found)
	public static int findAlphabeticCharacter(String str)
	{
		return findAlphabeticCharacter(str, 0); 
	}
	
	public static int findAlphabeticCharacter(String str, int fromPos)
	{
		if ((fromPos < 0) || (fromPos >= str.length())) return -1; 
		char[] strArray = str.toCharArray();
		for (int k = fromPos; k < strArray.length; k++)
		{
			if (((strArray[k] >= 'A') && (strArray[k] <= 'Z')) || ((strArray[k] >= 'a') && (strArray[k] <= 'z'))) 
				return k;
		}
		return -1; 
	}
	
	public static int find_first_of(String characters, String str)
	{
		return find_first_of(characters, str, 0);
	}
	
	public static int find_first_of(String characters, String str, int fromPos)
	{
		// make sure fromPos is in bounds
		if ((fromPos < 0) || (fromPos >= str.length())) return -1;
		char[] strArray = str.toCharArray(),
				charactersToFind = characters.toCharArray();
		// implement brute force find
		for (int k = fromPos; k < strArray.length; k++)
		{
			for (char charToFind : charactersToFind)
			{
				if (strArray[k] == charToFind) return k;
			}
		} 
		return -1;
	}
	
	public static int find_first_not_of(String characters, String str)
	{
		return find_first_not_of(characters, str, 0);
	}
	
	public static int find_first_not_of(String characters, String str, int fromPos)
	{
		// make sure fromPos is in bounds
		if ((fromPos < 0) || (fromPos >= str.length())) return -1;
		char[] strArray = str.toCharArray(),
				charactersToAvoid = characters.toCharArray();
		int k = fromPos;
		boolean skipCharacter = true;
		do 
		{
			for (int j = 0; j < charactersToAvoid.length; j++)
			{
				if (strArray[k] == charactersToAvoid[j]) break;
				if ((j == charactersToAvoid.length - 1) && (strArray[k] != charactersToAvoid[j])) skipCharacter = false;
			}
		}
		while ((skipCharacter) && (k++ < str.length()));
		if (!skipCharacter) return k; 
		return -1;
	}
}
