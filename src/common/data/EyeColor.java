package common.data;

public enum EyeColor {
    GREEN,
    BLUE,
    YELLOW,
    BROWN;

    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (EyeColor category : values()) {
            nameList.append(category.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
