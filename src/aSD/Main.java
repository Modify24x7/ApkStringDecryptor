
package aSD;

import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;

import com.google.common.collect.Lists;

import aSD.transformers.Transformers;

/**
 * @author Gorav Gupta
 *
 */
public class Main {

	public static void main(String[] args) {
		try {
			test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void test() throws Exception {
		
		/***
		 * Class name: Lcom/example/app/MyClass; 
		 * method name: MyMethod 
		 * list of parameters: Ljava/lang/String; 
		 * return type: Ljava/lang/String;
		 */
		ImmutableMethodReference immutableMethodReference = new ImmutableMethodReference("Lcom/example/app/MyClass;",
				"MyMethod", Lists.newArrayList("Ljava/lang/String;"), "Ljava/lang/String;");
		
		// Keep call
		decryptString("test\\classes.dex", "test\\classes-D.dex", 15, immutableMethodReference.toString(), false,
				immutableMethodReference);
		
		// Remove call
		decryptString("test\\classes.dex", "test\\classes-DRC.dex", 15, immutableMethodReference.toString(), true,
				immutableMethodReference);
		
	}

	/***
	 * @param inputDex
	 * @param outputDex
	 * @param api -> minSDKVersion
	 * @param callStrDecryptMethodRef
	 * @param removeCallStrDecryptMethod
	 * @param immutableMethodReference
	 * @throws Exception
	 */
	static void decryptString(String inputDex, String outputDex, int api, String callStrDecryptMethodRef,
			boolean removeCallStrDecryptMethod, ImmutableMethodReference immutableMethodReference) throws Exception {
		Transformers.decryptStrings(inputDex, outputDex, api, callStrDecryptMethodRef, removeCallStrDecryptMethod,
				immutableMethodReference);
	}

}
