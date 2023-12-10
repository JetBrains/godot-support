# Parser test

Parser, which builds PsiTree, is tested against pure GdScript code and comparing expected PsiTree with parsed one.  

Easiest way to create new test is by extending ParsingTestCase and supplying both .gd (source code) and .txt (expected PsiTree) files - sharing same name with test function.  
f.e.:
```
@Test fun testClassName() = doTest(true)
```
will look for files `ClassName.gd` and `ClassName.txt`

Let .txt empty and run test first.  
When testing against a valid code, check that result does not contain any PsiErrorElement,
then by eye check if resulting PsiTree seems ok.  
Then copy resulting tree into .txt file.

Currently, there are 2 sets of parser tests:  

- GdParserTest reading `data` folder for simple valid code blocks
- GdRecoveryParserTest reading `recoveryData` for expected Errors of invalid scripts

For more complex test cases (large scripts) I'd recommend 3rd set, something like GdComplexParserTest

Official [tutorial](https://plugins.jetbrains.com/docs/intellij/parsing-test.html)
