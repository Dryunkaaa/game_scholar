package utils;

public class StringUtils {

    public static String deleteStringCharacter(String str, int index) {
        StringBuilder builder = new StringBuilder();
        builder.append(str, 0, index).
                append("?").
                append(str.substring(index + 1));
        return builder.toString();
    }

    public static String deleteStringCharacter(String str, int[] indexes) {
        StringBuilder builder = new StringBuilder();
        char[] array = str.toCharArray();

        for (int i = 0; i < indexes.length; i++) {
            array[indexes[i]] = '?';
        }

        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
        }

        return builder.toString();
    }
}
