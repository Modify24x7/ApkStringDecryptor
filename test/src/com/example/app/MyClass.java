package com.example.app;
import android.util.*;


/**
 * @author Gorav Gupta
 *
 */
public class MyClass
{

	public static String MyMethod(String str) {
        return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
	}

	public static String test2() {
		return MyMethod("aHR0cHM6Ly9naXRodWIuY29tL01vZGlmeTI0eDc=");
	}

}
