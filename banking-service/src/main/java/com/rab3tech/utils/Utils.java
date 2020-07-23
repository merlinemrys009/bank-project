package com.rab3tech.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
	
	public static String genRandomAlphaNum() {
		// pseudo code
		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)) + "-");
		for (int i = 0; i < 5; i++)
		    sb.append(chars[rnd.nextInt(chars.length)]);
		return sb.toString();
	}
	
	public static String generateCustomerAccount() {
	    Random r = new Random(System.currentTimeMillis());
	    return "00"+(1000000000 + r.nextInt(2000000000))+"";
	}
	
	public static void main(String[] args) {
		System.out.println(generateCustomerAccount());
	}
	
	public static String[] ignoreNullData(Object sources) {
		BeanWrapper wrapper = new BeanWrapperImpl(sources);
		PropertyDescriptor[] pDescriptors = wrapper.getPropertyDescriptors();
		Set<String> nullValue = new HashSet<String>();
		for (PropertyDescriptor pd : pDescriptors) {
			Object object = wrapper.getPropertyValue(pd.getName());
			if (object == null)
				nullValue.add(pd.getName());

		}
		String[] ignoredData = new String[nullValue.size()];
		return nullValue.toArray(ignoredData);
	}
	

}
