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
   
	decryptString("test\\classes.dex", "test\\classes-decrypt.dex", 16, immutableMethodReference.toString(), false, immutableMethodReference);
	
   - Remove Call
   
	decryptString("test\\classes.dex", "test\\classes-decrypt2.dex", 16, immutableMethodReference.toString(), true, immutableMethodReference);
	
#### asd/transformers/StringDecryptor.java:->
 
   // Note: Don't change method name and parameter
   
	public static String decryptor(String str) {
		
		// Replace with your code
		
		try {
			return new String(Base64.decode(str));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

### Support
	(Ljava/lang/String;)Ljava/lang/String;
	
### License
    dexlib2-2.2.5: The 3-Clause BSD License
