package common.data;

public enum Color {
    GREEN,
    RED,
    BLACK,
    BLUE,
    BROWN;

    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (Color category : values()) {
            nameList.append(category.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
