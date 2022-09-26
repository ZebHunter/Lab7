package common.data;

public enum Status {
    FIRED,
    RECOMEND_FOR_PROMOTION,
    REGULAR,
    PROBATION;

    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (Status category : values()) {
            nameList.append(category.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
