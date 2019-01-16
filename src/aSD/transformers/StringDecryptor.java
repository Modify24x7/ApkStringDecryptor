package aSD.transformers;

import java.io.IOException;

import net.iharder.Base64;

/**
 * @author Gorav Gupta
 *
 */
public class StringDecryptor {

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

}
