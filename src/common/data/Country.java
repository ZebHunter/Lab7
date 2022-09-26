package common.data;

public enum Country {
    GERMANY,
    SPAIN,
    THAILAND;

    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (Country category : values()) {
            nameList.append(category.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
