# ApkStringDecryptor
Decrypt String in classes.dex without decompile

### Example

#### asd/Main.java:->
   
   - Class name: Lcom/example/app/MyClass; 
   - method name: MyMethod 
   - list of parameters: Ljava/lang/String; 
   - return type: Ljava/lang/String;
	
	ImmutableMethodReference immutableMethodReference = new ImmutableMethodReference("Lcom/example/app/MyClass;", "MyMethod", Lists.newArrayList("Ljava/lang/String;"), "Ljava/lang/String;");
	
   - Keep Call
   
	decryptString("test\\classes.dex", "test\\classes-D.dex", 15, immutableMethodReference.toString(), false, immutableMethodReference);
	
   - Remove Call
   
	decryptString("test\\classes.dex", "test\\classes-DRC.dex", 15, immutableMethodReference.toString(), true, immutableMethodReference);
	
#### asd/transformers/StringDecryptor.java:->
 
   // Note: Don't change method name and parameter
   
	public static String decryptor(String str) {
		
		// Replace with your code
	      ...
		// Code
	}

### Support
	(Ljava/lang/String;)Ljava/lang/String;
	Custom StackTraceElement
	
### License
    dexlib2: The 3-Clause BSD License
    guava: http://www.apache.org/licenses/LICENSE-2.0.txt
    jsr350: http://www.apache.org/licenses/LICENSE-2.0.txt
