//import java.io.*;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//public class DemoOne {
//    public Map<String,String> map;
//    public int flag=0;
//    public String IDENFER="INDEFER";
//    public String INTCON="INTCON";
//    public String CHARCON="CHARCON";
//    public String STRCON="STRCON";
//
//
//
//
//
//    public DemoOne(){
//
//        map=new HashMap<>();
//
//        map.put("const", "CONSTTK");
//        map.put("int", "INTTK");
//        map.put("char", "CHARTK");
//        map.put("void", "VOIDTK");
//        map.put("main", "MAINTK");
//        map.put("if", "IFTK");
//        map.put("else", "ELSETK");
//        map.put("do", "DOTK");
//        map.put("while", "WHILETK");
//        map.put("for", "FORTK");
//        map.put("scanf", "SCANFTK");
//        map.put("printf", "PRINTFTK");
//        map.put("return", "RETURNTK");
//        map.put("+", "PLUS");
//        map.put("-", "MINU");
//        map.put("*", "MULT");
//        map.put("/", "DIV");
//        map.put("<", "LSS");
//        map.put(">", "GRE");
//        map.put("<=", "LEQ");
//        map.put(">=", "GEQ");
//        map.put("!=", "NEQ");
//        map.put("=", "ASSIGN");
//        map.put("==", "EQL");
//        map.put(";", "SEMICN");
//        map.put(",", "COMMA");
//        map.put("(", "LPARENT");
//        map.put(")", "RPARENT");
//        map.put("[", "LBRACK");
//        map.put("]", "RBRACK");
//        map.put("{", "LBRACE");
//        map.put("}", "RBRACE");
//
//
//    }
//    public void ReadTxt(String filePath) throws IOException {
//        File testFile=new File(filePath);
////        FileWriter fileWriter=new FileWriter(testFile);
//        BufferedReader bufferedReader=new BufferedReader(new
//                FileReader(testFile));
//        String strLine=null;
//        String judge []= null;
//        String alltext=null;
//        int lineCount=1;
//        StringBuilder sBuilder = new StringBuilder();
//
//        while (null != (strLine = bufferedReader.readLine())) {
//            sBuilder.append(strLine+"\n");
//        }
//
//        trans(sBuilder.toString());
////        String result=sBuilder.toString();
////
////        System.out.println(result);
//
//    }
//    public boolean isInMap(String a){
//        boolean flag=false;
//        isNumber(a);
//        if (map.containsKey(a)){
//            System.out.print(map.get(a));
//            System.out.print(" ");
//            System.out.print(a);
//            System.out.println();
//
//        }
//        return false;
//    }
//    public boolean isNumber(String a){
//        boolean flag=false;
//        Pattern pattern = Pattern.compile("[0-9]*");
//        flag=pattern.matcher(a).matches();
//        System.out.println(flag);
//        return flag;
//    }
//
//
//    public void trans(String a){
//        boolean isTrans=false;
//        int first=0;
//        int last=0;
//        char now ;
//        System.out.println("a的长度"+a.length());
//        while (last<a.length()){
//            while (last<a.length()&&(a.charAt(last)==' '||a.charAt(last)=='\n')) {
//                last++;
//            }
//            if (last>=a.length()){
//                System.out.println("读取完成");
//                System.out.println("last"+last+"长度"+a.length());
//                break;
//            }
////            System.out.println("charAt "+a.charAt(last));
//
////            first=last;
//            isTrans=false;
//            while (last<a.length()&&(now=a.charAt(last))!=' '){
//                //判断是否为括号
//                if (isKuoHao(now)==true){
//                    System.out.println(map.get(String.valueOf(now)));
//                    System.out.println(now);
//                    break;
//                }else if (){
//
//                }
//                break;
//            }
//            last++;
//        }
//    }
//
//    public boolean isKuoHao(char a){
//        if (a=='{'||a=='}' ||a=='('||a==')' ||a=='['||a==']'){
//            return true;
//        }
//        return false;
//    }
//
//    public static void main(String[] args) throws IOException {
//        DemoOne demoOne=new DemoOne();
//        String testFile="/Users/zhangtong/Downloads/testGrammer1/testfile.txt";
//        demoOne.ReadTxt(testFile);
//
//    }
//
//
//
//}
