package aSD.transformers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.iharder.Base64;

import org.jf.dexlib2.iface.Method;

/**
 * @author Gorav Gupta
 *
 */
public class StringDecryptor {

	// Note: Do not change field
	public static List<Method> method = new ArrayList<>();

	// Note: Don't change method name and parameter
	public static String decryptor(String str) {

		String StackTraceElement = getMethod().getName()
				+ getMethod().getDefiningClass().replaceFirst("L", "").replace("/", ".").replaceFirst(";", "");

		String StackTraceElement2 = getMethod().getDefiningClass().replaceFirst("L", "").replace("/", ".")
				.replaceFirst(";", "") + getMethod().getName();

		// Replace with your code

		try {
			return new String(Base64.decode(str));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

		// End
	}

	// Note: Do not change method
	private static Method getMethod() {
		return method.get(0);
	}

}
