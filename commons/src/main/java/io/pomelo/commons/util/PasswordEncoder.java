package io.pomelo.commons.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import io.pomelo.commons.util.security.IrreversibleEncryptor;
import io.pomelo.commons.util.security.IrreversibleEncryptor.Algorithm;

public class PasswordEncoder {

	/**
	 * @param rawPassword
	 * @param salt
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String rawPassword, String salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return IrreversibleEncryptor.encrypt(rawPassword, Algorithm.MD5,
				IrreversibleEncryptor.encrypt(salt, Algorithm.SHA));
	}

	/**
	 * @param rawPassword
	 * @param salt
	 * @param encodedPassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean matches(String rawPassword, String salt, String encodedPassword)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return encode(rawPassword, salt).equals(encodedPassword);
	}

}
