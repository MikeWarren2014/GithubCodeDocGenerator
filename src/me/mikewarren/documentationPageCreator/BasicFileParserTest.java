package me.mikewarren.documentationPageCreator;

import java.util.Scanner;
import java.io.File;

public class BasicFileParserTest {
 
	public BasicFileParserTest()
	{
		BasicFileParser parser = new BasicFileParser();
	}
	
	public void testGetCommentTypeAt()
	{
		System.out.println();
		BasicFileParser parser = new BasicFileParser();
		// write up some dummy code
		String lineComment = "// This is a line comment",
				multilineComment = "/* This is supposed to be a multiline comment */",
				poundSignComment = "# You\'ll see comment like this in Python or PHP",
				nonComment = "string regular = \"line of code\";";
		// identify the type of comment the dummy code is (if one at all!) by the first character of the dummy code
		assert parser.getCommentTypeAt(0, lineComment).equals(CommentType.DOUBLE_FORWARD_SLASH);
		System.out.println("Successfully identified the double-forward-slash comment");
		assert parser.getCommentTypeAt(0, multilineComment).equals(CommentType.FORWARD_SLASH_ASTERISK);
		System.out.println("Successfully identified the forward-slash-asterisk comment");
		assert parser.getCommentTypeAt(0, poundSignComment).equals(CommentType.HASHTAG);
		System.out.println("Successfully identified the hashtag comment");
		assert parser.getCommentTypeAt(0, nonComment) == null;
		System.out.println("Successfully identified the non-commment!");
	}
	
	public void testIsComment()
	{
		System.out.println();
		BasicFileParser parser = new BasicFileParser();
		// write up some dummy code
		String lineComment = "// This is a line comment",
				multilineComment = "/* This is supposed to be a multiline comment */",
				poundSignComment = "# You\'ll see comment like this in Python or PHP",
				nonComment = "string regular = \"line of code\";";
		// perform tests!
		assert parser.isComment(lineComment);
		System.out.println("lineComment is comment");
		assert parser.isComment(multilineComment);
		System.out.println("multilineComment is comment");
		assert parser.isComment(poundSignComment);
		System.out.println("poundSignComment is comment");
		assert !parser.isComment(nonComment);
		System.out.println("nonComment is not a comment");
		// now let's test with a line of code that is suffixed by a line of code!
		String codeWithComment = "int x = 5; // test variable";
		assert !parser.isComment(codeWithComment);
		System.out.println("codeWithComment is not a comment");
		// now let's test with some commented out code that contains an inline comment at the end
		String commentedOutCode = "/* int y = 10; // variable that is being commented out because its use is causing errors */";
		assert parser.isComment(commentedOutCode);
		System.out.println("commentedOutCode is a comment");
		// test with another commented-out line of code
		String anotherCommentedOutLOC = "# $var = $_POST[\'data\']; // an ID";
		assert parser.isComment(anotherCommentedOutLOC);
		System.out.println("anotherCommentedOutLOC is a comment");
		// test with even more commented-out code
		String moreCommentedOutCode = "/* int y = 10; // variable that is being commented out because its use is causing errors */";
		assert parser.isComment(moreCommentedOutCode);
		System.out.println("moreCommentedOutCode is a comment");
	}
	
	public void testFindFirstCommentSymbolIndex()
	{
		System.out.println();
		BasicFileParser parser = new BasicFileParser();
		// write up some dummy code
		String lineComment = "// This is a line comment",
				multilineComment = "/* This is supposed to be a multiline comment */",
				poundSignComment = "# You\'ll see comment like this in Python or PHP",
				nonComment = "string regular = \"line of code\";";
		// perform tests!
		int index = parser.findFirstCommentSymbolIndex(lineComment);
		assert index == 0;
		System.out.printf("First comment symbol in lineComment found at %d\n", index);
		assert (index = parser.findFirstCommentSymbolIndex(multilineComment)) == 0;
		System.out.printf("First comment symbol in multilineComment found at %d\n", index);
		assert (index = parser.findFirstCommentSymbolIndex(poundSignComment)) == 0;
		System.out.printf("First comment symbol in poundSignComment found at %d\n", index);
		// test another non-comment
		String anotherNonComment = "int duration = 30 * 60 * 1000; // 30 minutes";
		assert (index = parser.findFirstCommentSymbolIndex(anotherNonComment)) == -1;
		System.out.println("comment was found after code in anotherNonComment");
	}
	
	public void testParse()
	{
		System.out.println();
		File file = new File("testCodeFiles/BaseCodeFile.txt");
		assert file.exists();
		BasicFileParser parser = new BasicFileParser();
		try 
		{
			parser.parse(file);
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void testFindAlphabeticCharacter()
	{
		// test on line of code that is multi-variable definition
		String multiVariableDeclaration = "int a = 1, b = 1, c = 1;";
		int firstComma = multiVariableDeclaration.indexOf(",");
		assert multiVariableDeclaration.indexOf("b") == BasicFileParser.findAlphabeticCharacter(multiVariableDeclaration, firstComma);
		System.out.println("First alphabetic character after firstComma is \'b\'");
		assert multiVariableDeclaration.indexOf("b") == BasicFileParser.findAlphabeticCharacter(multiVariableDeclaration, firstComma + 1);
		System.out.println("First alphabetic character after character after firstComma is \'b\'");
	}
}
