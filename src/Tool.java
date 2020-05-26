import java.util.regex.Pattern;

public class Tool {

    public static boolean isNumeric(String str) {//判断整数
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//        System.out.println();
        return pattern.matcher(str).matches();
    }

    public void judegrNumberWithNoExp(String a){
//        isNumeric(a);
        System.out.println(isNumeric(a));

    }

    public static void main(String[] args) {
//        int a;
//        isNumeric("11");
//        System.out.println(isNumeric("11.2"));

    }

}
