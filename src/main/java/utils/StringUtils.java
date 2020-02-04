package utils;

public class StringUtils {

    public static String deleteStringCharacter(String str, int index) {
        StringBuilder builder = new StringBuilder();
        builder.append(str.substring(0, index)).
                append("?").
                append(str.substring(index + 1, str.length()));
        return builder.toString();
    }

    public static String deleteStringCharacter(String str, int[] indexes) {
        StringBuilder builder = new StringBuilder();
        char[] array = str.toCharArray();
        for (int i = 0; i < indexes.length; i++) {
            array[indexes[i]] = '?';
        }
        for (int i = 0; i < array.length; i++) {
            builder.append(String.valueOf(array[i]));
        }
        return builder.toString();
    }
}
