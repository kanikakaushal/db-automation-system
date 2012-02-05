/**
 * 
 */
package org.satsang.sms.core.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.util.MethodInvoker;

/**
 * @author Default
 * 
 */
public class RequestInvoker {

	public static Object invoke(Object targetObject, String targetMethod,
			List<Object> arguments) throws Exception {

		
		Object[] params = arguments!= null ? arguments.toArray(): null;

		// Invoke the request method using Spring MethodInvoker
		MethodInvoker methodInvoker = new MethodInvoker();
		methodInvoker.setTargetObject(targetObject);
		methodInvoker.setTargetMethod(targetMethod);
		methodInvoker.setArguments(params);

		Object result = null;
		try {
			methodInvoker.prepare();
			result = methodInvoker.invoke();
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
			throw iae;
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
			throw ite;
		} catch (NoSuchMethodException nsme) {
			nsme.printStackTrace();
			throw nsme;
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			throw cnfe;
		}

		return result;
	}
}
