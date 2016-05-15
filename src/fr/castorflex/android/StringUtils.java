package fr.castorflex.android;

public class StringUtils {
	/**
	 * ���������ʽ�Ƿ���ȷ
	 * @param target
	 * @return
	 */
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}
}
