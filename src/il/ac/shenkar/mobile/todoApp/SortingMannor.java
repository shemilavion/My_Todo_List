package il.ac.shenkar.mobile.todoApp;

/**
 * SORTING MANNOR ENUM
 * */
public enum SortingMannor 
{
	BY_DUE_DATE(0), BY_HIGHER_IMPORTANCY(1), BY_NEAREST_LOCATION(2);
    private final int value;
    private SortingMannor(int value)
    {
        this.value = value;
    }
    //get the enum value
    public int getValue() 
    {
        return value;
    }
}
