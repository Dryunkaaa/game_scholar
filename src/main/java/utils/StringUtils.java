package utils;

public class StringUtils {

    public static String deleteStringCharacter(String str, int index) {
        return str.substring(0, index) + "?" + str.substring(index + 1);
    }

    public static String deleteStringCharacter(String str, int[] indexes) {
        StringBuilder builder = new StringBuilder();
        char[] array = str.toCharArray();

        for (int i = 0; i < indexes.length; i++) {
            array[indexes[i]] = '?';
        }

        builder.append(array);
        return builder.toString();
    }
}
