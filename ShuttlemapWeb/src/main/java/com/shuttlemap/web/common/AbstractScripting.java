package com.shuttlemap.web.common;

import java.util.Map;

abstract public class AbstractScripting implements IScripting {

	public Object compile(Object script) {
		return null;
	}

	
	public Object evaluate(Object script) {
		return evaluate(script, (Map<String, ? extends Object>)null);
	}

	@SuppressWarnings("unchecked")
	
	public Object evaluate(Object script,
			Map<String, ? extends Object> variables) {
		return evaluate(script, variables, new Class[]{});
	}

	
	public <T> T evaluate(Object script, Class<T>... resultTypes) {
		return evaluate(script, null, resultTypes);
	}

}
