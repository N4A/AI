package me.genetic;

/**
 * @author Duocai Wu
 * @date 2016年12月21日
 * @time 下午2:18:05
 *
 */
public class Individual<T> {
	private T[] code;
	
	public Individual(T[] code) {
		this.setCode(code);
	}

	public T[] getCode() {
		return code;
	}

	public void setCode(T[] code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < code.length; i++) {
			str += code[i] + " ";
		}
		return str;
	}
}
