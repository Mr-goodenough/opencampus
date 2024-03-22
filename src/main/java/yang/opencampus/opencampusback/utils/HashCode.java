package yang.opencampus.opencampusback.utils;

public class HashCode {
    public static int StringToInt(String input) {
        int originCode=input.hashCode();
        return (Math.abs(originCode)%Integer.MAX_VALUE)%100000;
    }
    public boolean checkCode(String input,int code){
        int newcode=StringToInt(input);
        return (newcode==code);
    }
}
