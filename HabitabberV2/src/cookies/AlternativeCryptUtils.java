package cookies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AlternativeCryptUtils {

	private static String chromeKeyringPassword;

	public static byte[] unprotect(byte[] data) {
		byte[] decryptedBytes = null;
		if (SQLiteCookieReader.OS.startsWith("Windows")) {
			try {
				decryptedBytes = JNACrypt32Utils.unprotect(data);
			} catch (Exception e) {
				decryptedBytes = null;
			}
		} else if (SQLiteCookieReader.OS.startsWith("Linux")){
			try {
				byte[] salt = "saltysalt".getBytes();
				char[] password = "peanuts".toCharArray();
				char[] iv = new char[16];
				Arrays.fill(iv, ' ');
				int keyLength = 16;

				int iterations = 1;

				PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
				SecretKeyFactory pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

				byte[] aesKey = pbkdf2.generateSecret(spec).getEncoded();

				SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");

				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new String(iv).getBytes()));

				// if cookies are encrypted "v10" is a the prefix (has to be removed before decryption)
				if (new String(data).startsWith("v10")) {
					data = Arrays.copyOfRange(data, 3, data.length);
				}
				decryptedBytes = cipher.doFinal(data);
			} catch (Exception e) {
				decryptedBytes = null;
			}
		} else if(SQLiteCookieReader.OS.startsWith("Mac")) {
			// access the decryption password from the keyring manager
			if(chromeKeyringPassword == null){
				try {
					chromeKeyringPassword = getMacKeyringPassword("Chrome Safe Storage");
				} catch (IOException e) {
					decryptedBytes = null;
				}
			}
			try {
				byte[] salt = "saltysalt".getBytes();
				char[] password = chromeKeyringPassword.toCharArray();
				char[] iv = new char[16];
				Arrays.fill(iv, ' ');
				int keyLength = 16;

				int iterations = 1003;

				PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
				SecretKeyFactory pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

				byte[] aesKey = pbkdf2.generateSecret(spec).getEncoded();

				SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");

				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new String(iv).getBytes()));

				// if cookies are encrypted "v10" is a the prefix (has to be removed before decryption)
				if (new String(data).startsWith("v10")) {
					data = Arrays.copyOfRange(data, 3, data.length);
				}
				decryptedBytes = cipher.doFinal(data);
			} catch (Exception e) {
				decryptedBytes = null;
			}
		}
		return decryptedBytes;
	}

	private static String getMacKeyringPassword(String application) throws IOException {
		Runtime rt = Runtime.getRuntime();
		String[] commands = {"security", "find-generic-password","-w", "-s", application};
		Process proc = rt.exec(commands);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String result = "";
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			result += s;
		}
		return result;
	}
}
