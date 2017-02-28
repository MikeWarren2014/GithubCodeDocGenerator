package me.mikewarren.documentationPageCreator;

import java.io.File;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/* A basic general-purpose parser for non-OOP code files */
public class BasicFileParser extends CodeFileParser {

	public static final String[] KEYWORDS = { "var", "function", "set" };
	public static final CommentType[] TYPES_OF_COMMENTS = { CommentType.DOUBLE_FORWARD_SLASH,
			CommentType.FORWARD_SLASH_ASTERISK, 
			CommentType.HASHTAG 
	};
	public static final String FUNCTION_KEYWORD = "function";
	
	protected DataType dataType;
	protected Variable returnType;
	protected String line;
	
	private boolean DEBUG = true;
	private int lineNumber = 0; // possibly to be thrown away later. For right now, there is test purpose behind this field. 
	// finds functions and global variables in file
	@Override
	public void parse(File file) throws Exception 
	{
		// TODO: use 1-based counting for lineNumber, and in nextCodeLine(), use post-incrementation
		if (DEBUG) lineNumber = 0;
		// open input file stream with file
		Scanner ifstream = new Scanner(file);
		while ((ifstream.hasNextLine()) && (lineNumber <= 6))
		{
			parseNextSection(ifstream);
		}
	}
	
	@Override
	/* Parses the next section of a code file. A section is defined as any sequence of global variable declarations under the same keyword, or any function declaration signature */
	// TODO: implement this function to detect functions in fileStream
	/* TODO: refactor this function
	 * 	proposed refactoring: 
	 * 	  - move some members of this function to class scope (possibly protected) 
	 *    - define protected List<Variable> parseVariables()
	 *    -	first version: 
	 *    	-	parameters: 
	 *      >		lineOfCode    : String 
	 *    	> 		partOfFunction: boolean
	 *    - second version:
	 *    	- 	parameters:
	 *    	>		lineOfCode      : String
	 *      >		startPos        : int
	 *    - returns : 
	 *      >		listOfVariables : List<Variable>
	 *    - move some of the logic in this function to protected void parseVariable()
	 * 
	 */
	// TODO: add logic at the end to handle comments
	protected void parseNextSection(Scanner fileStream) throws Exception
	{
		boolean reachedEndOfSection = false,	// put these in protected scope?
				onFirstLine = true;
		line = ""; 
		dataType = null;		
		returnType = null;		
		String nameOfFunctionOrVariable;
		LinkedList<Variable> functionArguments = new LinkedList<Variable>();
		CommentType currentComment = null;
		while ((fileStream.hasNextLine()) && (!reachedEndOfSection))
		{
			line = nextCodeLine(fileStream);
			String extractedWord = "";
			int fromPos = 0, toPos = 0;
			// while not at end of first line
			while (onFirstLine)
			{
				// find next delimiting character (delimiting characters in " ,(;{")
				toPos = BasicFileParser.find_first_of(" ,(;{", line, 
						fromPos);
				if (DEBUG) 
				{
					System.out.printf("fromPos = %d\ntoPos = %d\n", fromPos, toPos);	
				}
				// extract substring from last position of delimiter to current position of delimiter
				extractedWord = (toPos == -1) ? line.substring(fromPos) : line.substring(fromPos, toPos);
				if (DEBUG) System.out.printf("extractedWord == %s\n", extractedWord);
				// TODO: refactor this part (the while-loop)
				/* Proposed algorithm: 
				 * 
				 * if dataType is null
				 * 	if returnType is not null
				 * 		we are dealing with function declaration (advance toPos to '(')
				 * 	else 
				 * 		advance toPos to first character not in " ="
				 * else if returnType is null
				 * 	we are dealing with variable declaration (advance toPos to first character found of ",;")
				 */
				// while character after position toPos is in " ="
				/*char c = ' ';
				while (((c = line.charAt(toPos + 1)) == ' ') || (c == '='))
				{
					// advance toPos to the position after that
					toPos += 2;	// may not be safe because of the case where it is on whitespace at the end 
				}*/
				if (dataType == null)
				{
					if (returnType != null)
					{
						if (DEBUG) System.out.println("Currently dealing with function declaration....");
						toPos = line.indexOf('(', toPos);
						if (DEBUG) System.out.printf("toPos == %d\n", toPos);
					}
					else
					{
						int spotBeforeDelimiters = BasicFileParser.find_first_not_of(" =", line, toPos);
						toPos = (toPos < spotBeforeDelimiters) ? spotBeforeDelimiters - 1 : toPos;
					}
				}
				else if (returnType == null)
				{
					if (DEBUG) System.out.println("Currently dealing with variable declaration....");
					toPos = BasicFileParser.find_first_of(",;", line, toPos);
					if (DEBUG) System.out.printf("toPos == %d\n", toPos);
				}
				
				if (DEBUG)
				{
					System.out.printf("toPos == %d\n", toPos);
					if (toPos != -1)	System.out.printf("character at toPos : \'%c\'\n", line.charAt(toPos));
				}
				// if delimiting character is a space
				if (line.charAt(toPos) == ' ')
				{
					// if it is "=" operator...
					if (extractedWord == "=")
					{
						// ...skip over it by moving fromPos to next ',' or ';', or end-of-line (-1) if neither of those symbols found
						int nextPos = BasicFileParser.find_first_of(",;", line, toPos);
						fromPos = (nextPos == -1) ? -1 : (nextPos + 1);
						//continue;
					}
					// else
					else
					{
						// make sure it is not keyword
						// if it is
						if (isKeyword(extractedWord))
						{
							// it's not a DataType
							if (DEBUG) System.out.printf("Detected keyword : %s\n", extractedWord);
						}
						// else 
						else
						{
							if (DEBUG) System.out.printf("Detected DataType : %s\n", extractedWord);
							// check to see if it's a PrimitiveDataType
							if (isPrimitiveDataType(extractedWord))
							{
								if (DEBUG) System.out.println("extractedWord is PrimitiveDataType");
								dataType = new DataType(PrimitiveDataType.valueOf(extractedWord.toUpperCase()));
							}
							// if it's not a PrimitiveDataType
							else
							{
								// it is a user-specified DataType
								if (DEBUG) System.out.println("extractedWord is user-specified DataType");
								dataType = new DataType(extractedWord);
							}
						}
					}
				} 
				// else
				else
				{
					// we have function or variable name
					nameOfFunctionOrVariable = extractedWord;
					// if it contains '='
					if (extractedWord.contains("="))
					{
						// it's a Variable name
						// throw away everything from that '=' onwards
						nameOfFunctionOrVariable = nameOfFunctionOrVariable.substring(0, 
								nameOfFunctionOrVariable.indexOf('='));
						// if we found a DataType
						// the Variable has that DataType
						// if not in argument list of function
							// this is global Variable. Add it to list of global Variables
					}
					// if delimiter is '('
						// we have function
							// everything after delimiter, before ')', is parameter
				}
				// TODO: advance fromPos,toPos 
				if (toPos != -1) 
				{
					//fromPos = toPos + 1;
					fromPos = BasicFileParser.findAlphabeticCharacter(line, toPos + 1);
				}
				// TODO: refactor condition for onFirstLine
				/* We move to next line (we are no longer on first line) iff one of these cases hold:
				 * 	> index to extract characters from is out-of-bounds
				 *  > index to extract characters to is out-of-bounds
				 *  > index to extract characters to is in-bounds but points to semi-colon
				 * if the last case holds, we check past it for any multiline comments
				 */
				/*onFirstLine = (toPos == -1) || (line.charAt(toPos) != ';');
				if (!onFirstLine)
				{
					
				}*/
				//onFirstLine = false;
				onFirstLine = (fromPos != -1) && !((toPos != -1) && (line.charAt(toPos) == ';'));
				// if we hit semicolon
				if (DEBUG)
				{
					System.out.printf("onFirstLine == %b\n", onFirstLine);
					System.out.printf("fromPos == %d\n", fromPos);
					System.out.printf("toPos == %d\n", toPos);
					System.out.printf("line.charAt(%d) == \'%c\'\n", toPos, line.charAt(toPos));
				}
			}  
			reachedEndOfSection = true;
		}
	}
	
	
	protected String nextCodeLine(Scanner fileStream)
	{
		return nextCodeLine(fileStream, null);
	}
	
	protected String nextCodeLine(Scanner fileStream, CommentType comment)
	{
		String line = "";
		CommentType currentComment = comment;
		if (fileStream.hasNextLine())
		{
			if (DEBUG) System.out.printf("Now on line number: %d\n", ++lineNumber);
			line = fileStream.nextLine();
			// if there is no currentComment
			if (currentComment == null)
			{
				// verify that line isn't a comment
				if (isComment(line))
				{
					currentComment = getCommentTypeAt(findFirstCommentSymbolIndex(line), line);
					if (DEBUG) System.out.printf("Current line is a comment that is indicated by the symbol: \"%s\"\n", currentComment.getStartSymbol());
					if (DEBUG) System.out.printf("This comment is %smultiline\n", (currentComment.isMultiline()) ? "" : "not ");
				}
			}
			// skip over lines that have no alphabetic characters on them, and comments
			boolean currentLineIsComment = false;
			while ((currentLineIsComment = isComment(line)) || (CodeFileParser.findAlphabeticCharacter(line) == -1) || (currentComment != null))
			{
				// if there is no currentComment and line is comment
				if ((currentComment == null) && (currentLineIsComment))
				{
					// get CommentType of line and set currentComment to that
					currentComment = getCommentTypeAt(findFirstCommentSymbolIndex(line), line);
					if (DEBUG) System.out.printf("Found comment that is indicated by the symbol: \"%s\"\n", currentComment.getStartSymbol());
				}
				// if currentComment is a multiline comment and we found end symbol of it in line
				if (currentComment != null)
				{
					if ((currentComment.isMultiline()) && (line.contains(currentComment.getEndSymbol())))
					{
						// we will be no longer on a comment
						currentComment = null;
						if (DEBUG) System.out.println("Reached end of multiline comment");
					}
				}
				if (fileStream.hasNextLine())
				{
					// move onto next line
					line = fileStream.nextLine();
					// reset commment if last line was single-line comment
					if ((currentComment != null) && (!currentComment.isMultiline())) currentComment = null;
					if (DEBUG) System.out.printf("Now on line number: %d\n", ++lineNumber);
				}
			}
		}
		return line;
	}
	
	// Probably no use for this function
	protected void parseFunction(Scanner fileStream, int startOfFunctionArgs) throws Exception
	{

	}
	
	protected List<Variable> parseVariables(String lineOfCode, boolean partOfFunction)
	{
		if (partOfFunction)
		{
			// find '('
			int leftParenthesesPos = lineOfCode.indexOf('(');
			// if '(' found
			if (leftParenthesesPos != -1)
			{
				// parseVariables, starting at first alphabetic character after '(', and push any found variables to -
				return parseVariables(lineOfCode, BasicFileParser.findAlphabeticCharacter(lineOfCode, leftParenthesesPos));
			}
			// else
			else
			{
				// parseVariables, starting at first alphabetic character
				return parseVariables(lineOfCode, BasicFileParser.findAlphabeticCharacter(lineOfCode));
			}
		}
		return parseVariables(lineOfCode, 0);
	}
	
	protected List<Variable> parseVariables(String lineOfCode, int startPos)
	{
		// TODO: ensure startPos is within bounds (return or throw exception if it isn't)
		if ((startPos < 0) || (startPos >= lineOfCode.length())) return null;
		LinkedList<Variable> listOfVariables = new LinkedList<Variable>();
		return listOfVariables;
	}
	
	protected boolean isKeyword(String word)
	{
		for (String keyword : BasicFileParser.KEYWORDS)
		{
			if (word == keyword)
			{
				return true; 
			}
		}
		return false;
	}
	
	protected boolean isPrimitiveDataType(String phrase)
	{
		try 
		{
			PrimitiveDataType.valueOf(phrase.replace(' ', '_').toUpperCase());
			return true;
		}
		catch (Exception e) {}
		return false;
	}

	public boolean isComment(String lineOfCode)
	{
		// lineOfCode is a comment iff there exist comment symbol before its first alphabetic character
		return (findFirstCommentSymbolIndex(lineOfCode) != -1);
	}
	
	// This function finds the position of the first comment symbol before the first alphabetic character, if it exists
	public int findFirstCommentSymbolIndex(String lineOfCode)
	{
		return findFirstCommentSymbolIndex(lineOfCode, 0);
	}
	
	// TODO: Find if another approach, walking from the start of the string to the first encountered comment symbol, would be cheaper than this algorithm.
	// Current algorithmic complexity: O(l*m), where l is length of lineOfCode and m is number of CommentTypes
	public int findFirstCommentSymbolIndex(String lineOfCode, int fromPos)
	{
		/* Alternate algorithm: 
		 * 1.) Copy the start symbols of BasicFileParser.TYPES_OF_COMMENTS into temp array, sorted lexicographically (can possibly use brute force to do this, since number of possible CommentTypes expected to be small)
		 * 2.) Walk along lineOfCode until you find a character that is the start of at least one of the symbols. Store the index of the first possible match to temp variable. 
		 * 3.) While there is any candidates for matches and you haven't found a match
		 * 	compare the next character in lineOfCode to the next in the possible matches, discarding any candidates that don't match
		 *  if match found 
		 *   return current position in lineOfCode 
		 * 4.) Repeat 2 until either match found or end of lineOfCode reached
		 * 5.) if end of lineOfCode reached, there is no match (return -1)
		 */
		int firstAlphabeticPos = BasicFileParser.findAlphabeticCharacter(lineOfCode, fromPos),
				firstCommentSymbolIndex = lineOfCode.length();
		for (CommentType commentType : BasicFileParser.TYPES_OF_COMMENTS)
		{
			int index = lineOfCode.indexOf(commentType.getStartSymbol()); 
			if ((index != -1) && (index < firstAlphabeticPos) && (index < firstCommentSymbolIndex)) firstCommentSymbolIndex = index;
		}
		return (firstCommentSymbolIndex == lineOfCode.length()) ? -1 : firstCommentSymbolIndex; 
	}
	
	public CommentType getCommentTypeAt(int pos, String lineOfCode)
	{
		// sort BasicFileParser.TYPES_OF_COMMENTS by start symbol
		Arrays.sort(BasicFileParser.TYPES_OF_COMMENTS,
				new Comparator<CommentType>()
				{

					@Override
					public int compare(CommentType a, CommentType b) 
					{
						// compare start symbols lexicographically
						return a.getStartSymbol().compareTo(b.getStartSymbol());
					}
			
				});
		// start at pos
		int index = pos;
		int indexOfFirstPossibleMatch = 0,
				indexOfLastPossibleMatch = BasicFileParser.TYPES_OF_COMMENTS.length - 1;
		// while [indexOfFirstPossibleMatch, indexOfLastPossibleMatch] is a valid mathematical interval
		while (indexOfFirstPossibleMatch <= indexOfLastPossibleMatch)
		{
			// find all comments symbols for which the character at (index - pos) is the character at index in lineOfCode (update indexOfFirstPossibleMatch,indexOfLastPossibleMatch to point to them all)
			while ((indexOfFirstPossibleMatch < BasicFileParser.TYPES_OF_COMMENTS.length) && 
					(BasicFileParser.TYPES_OF_COMMENTS[indexOfFirstPossibleMatch].getStartSymbol().charAt(index - pos) != lineOfCode.charAt(index))) 
				indexOfFirstPossibleMatch++;
			while ((BasicFileParser.TYPES_OF_COMMENTS[indexOfLastPossibleMatch].getStartSymbol().charAt(index - pos) != lineOfCode.charAt(index)) && (indexOfFirstPossibleMatch <= --indexOfLastPossibleMatch))
			{
				
			}
			// if there is only one such comment symbol (i.e. if indexOfFirstPossibleMatch == indexOfLastPossibleMatch )
			if (indexOfFirstPossibleMatch == indexOfLastPossibleMatch)
				// return the CommentType at that index in BasicFileParser.TYPES_OF_COMMENTS
				return BasicFileParser.TYPES_OF_COMMENTS[indexOfFirstPossibleMatch];
			// move on to next character in lineOfCode
			++index;
		}
		return null;
	}
	
}
