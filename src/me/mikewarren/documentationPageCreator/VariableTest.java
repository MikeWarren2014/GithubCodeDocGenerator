package me.mikewarren.documentationPageCreator;

public class VariableTest {
	public VariableTest()
	{
		// make a Variable with a name and a description
		try 
		{
			Variable testVariable = new Variable("testVariable", "some description");
			// output the description
			String testDescription = testVariable.getDescription();
			System.out.printf("testVariable.getDescription() == %s\n", testDescription);
			// try appending a string with that description
			String test = "test ";
			test += testDescription;
			System.out.println("test== " + test);
			// try writing that description to a variable and manipulating that variable
			testDescription = "// " + testVariable.getDescription();
			System.out.println("testDescription == " + testDescription);
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
		testEmptyVariable();
		
	}
	
	public void testEmptyVariable()
	{
		Variable emptyVariable = new Variable();
		assert emptyVariable == new Variable();
		System.out.println("emptyVariable == newVariable()");
		assert emptyVariable.equals(new Variable());
		System.out.println("emptyVariable.equals(new Variable())");
		assert emptyVariable.toString().equals("");
		System.out.println("String representation of empty Variable is empty string");
		System.out.printf("String representation of emptyVariable : %s\n", emptyVariable.toString());
		
	}
}
