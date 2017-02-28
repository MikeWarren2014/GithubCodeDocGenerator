package me.mikewarren.documentationPageCreator;

import java.util.Arrays;
import java.util.TreeMap;

public class DocumentationPageCreator {
	private String _projectDirectory;
	private String _documentationDirectory;
	private TreeMap<String, String> _codeFileMetaData;	// useless 
	
	public static String LIST_ITEM = "list item";
	public static String HEADER = "header";
	public static String SECTION = "section";
	
	public DocumentationPageCreator()
	{
		this("C:/Users/MikeW/Desktop/GroupManagementAppServer", "documentation");
	}
	
	public DocumentationPageCreator(String project)
	{
		this(project, "documentation");
	}
	
	public DocumentationPageCreator(String project, String documentation)
	{
		this._projectDirectory = project;
		this._documentationDirectory = documentation;
	}
	
	
	public static void main(String[] args) 
	{
		/* This program is to be called from either the command line or via GUI (clicking on the icon to this program). 
		 * If there are parameters, they used the command line to call this program. Else, treat it as if they are trying 
		 * to use the GUI instead. 
		 */
		System.out.printf("args == %s\n", Arrays.toString(args));
		// if arguments have been passed
		if (args.length > 0)
		{
			// run command-line version of this program
			// TODO: copy args into array of code files
		}
		// else
		else
		{
			// fire up the GUI
		}
		
		//new VariableTest();
		BasicFileParserTest parserTest = new BasicFileParserTest();
		/*parserTest.testGetCommentTypeAt();
		parserTest.testIsComment();
		parserTest.testFindFirstCommentSymbolIndex();*/
		parserTest.testParse();
		//parserTest.testFindAlphabeticCharacter();
	}

}
