package com.shuttlemap.web.common;

import java.util.Map;

public interface IScripting {
	String getLanguage();
	/**
	 * String을 compile함. compile한 script은 evaluate method의 script로 넘길 수 있음.
	 * @param scriptString
	 * @return
	 */
	Object compile(Object script);
	
	/**
	 * Context 를 현 thread 에 시작함. destroyContext가 호출 될 때까지의
	 * 모든 evaluate method 호출은 thread 와 associated 된 Context 를 사용함.
	 * 여기서 Context 라 함은 global 변수 Map 임.
	 * @return 만약 context 기능을 지원하지 않으면 UnsupportedOperationException 던짐. 
	 */
	void startContext(Object globalScript);
	
	/**
	 * 현 thread 와 연결된 context 를 끝냄.
	 */
	void exitContext();
	
	Object evaluate(Object script);
	
	/**
	 * 
	 * @param script compile된 script아니면 그냥 script String
	 * @param variables scripting에서 활용 할 수 있는 변수들
	 * @return
	 */
	Object evaluate(Object script, Map<String,? extends Object> variables);
	
	/**
	 * Evaluates the script
	 * @param <T> The return type parameter
	 * @param script compile된 script아니면 그냥 script String/Reader
	 * @param resultTypes Must not be null
	 * @return
	 * @exception IllegalArgumentException If resultClass is null
	 */
	<T> T evaluate(Object script, Class<T> ... resultTypes);

	/**
	 * Evaluates the script
	 * @param <T> The return type parameter
	 * @param script compile된 script아니면 그냥 script String/Reader
	 * @param variables scripting에서 활용 할 수 있는 변수들
	 * @param resultTypes Must not be null
	 * @return
	 * @exception IllegalArgumentException If resultClass is null
	 */
	<T> T evaluate(Object script, Map<String,? extends Object> variables, Class<T> ... resultTypes);

}
