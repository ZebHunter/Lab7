package common.data;

public enum Position {
    HUMAN_RESOURSES,
    LEAD_DEVELOPER,
    BAKER;



    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (Position category : values()) {
            nameList.append(category.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}