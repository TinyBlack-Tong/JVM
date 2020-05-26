
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {

    int index2 = 0;
    int flag;
    String token[];//
    String name[];//
    List<String> ReturnFunc;
    List<String> NoReturnFun;
    public Map<String, String> map;
    public String IDENFR = "IDENFR";
    public String INTCON = "INTCON";
    public String CharString = "CHARCON";
    public String StrString = "STRCON";
    char brackets[] = {'(',')','[',']','{','}'};
    char quotation[] = {'\'','\"'};
    public StringBuilder outputStringBuilder; //输出
    public List<String> orignList = null;
    public List<String> afterList = null;
    File outputFile = null;
    OutputStream outPutStream = null;
    public Main() {
        map = new HashMap<String, String>();
        map.put("const", "CONSTTK");
        map.put("int", "INTTK");
        map.put("char", "CHARTK");
        map.put("void", "VOIDTK");
        map.put("main", "MAINTK");
        map.put("if", "IFTK");
        map.put("else", "ELSETK");
        map.put("do", "DOTK");
        map.put("while", "WHILETK");
        map.put("for", "FORTK");
        map.put("scanf", "SCANFTK");
        map.put("printf", "PRINTFTK");
        map.put("return", "RETURNTK");
        map.put("+", "PLUS");
        map.put("-", "MINU");
        map.put("*", "MULT");
        map.put("/", "DIV");
        map.put("<", "LSS");
        map.put(">", "GRE");
        map.put("<=", "LEQ");
        map.put(">=", "GEQ");
        map.put("!=", "NEQ");
        map.put("=", "ASSIGN");
        map.put("==", "EQL");
        map.put(";", "SEMICN");
        map.put(",", "COMMA");
        map.put("(", "LPARENT");
        map.put(")", "RPARENT");
        map.put("[", "LBRACK");
        map.put("]", "RBRACK");
        map.put("{", "LBRACE");
        map.put("}", "RBRACE");
        orignList = new ArrayList<String>();
        afterList = new ArrayList<String>();
        //start from reading file
        readFile();
    }

    public void readFile(){

        File file = new File("testfile.txt");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {	//read file
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sBuilder = new StringBuilder();
        String strLine = "";

        try {
            while ((strLine = bufferedReader.readLine()) != null) {
                sBuilder.append(strLine + "\n");
            }
            analyse(sBuilder.toString());
            bufferedReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    //检视此行字符
    public void analyse(String line) throws IOException {
        boolean parsed = false;
        int left = 0, isFinish = 0;
        char index;
        while(isFinish < line.length()) {
            while(isFinish < line.length()
                    && (line.charAt(isFinish) == ' '
                    || line.charAt(isFinish) == '\n'))
                isFinish++;
            if(isFinish >= line.length())	//每行读取完成
                break;
            left = isFinish;
            parsed = false;
            //过滤空格
            while(isFinish < line.length() && (index = line.charAt(isFinish))!=' ') {
                //如果当前的字符是（）{} []
                if(contains(brackets, index)) {
                    //进行解析
                    toTranse(line.substring(left,isFinish), null);
                    orignList.add(map.get(String.valueOf(index)));
                    afterList.add(String.valueOf(index));
                    System.out.println("inex"+index);
                    System.out.println(map.get(String.valueOf(index)));
                    parsed = true;
                    break;
                }else if(contains(quotation, index)) {	//对引号进行解析
                    int nextQuo = findNext(line, index , left+1);
                    if(nextQuo == -1) {		//引号未成对
                        return;
                    }
                    toTranse(line.substring(left+1,nextQuo),
                            index == '\'' ? CharString : StrString);
                    isFinish = nextQuo;
                    parsed = true;
                    break;

                }else if(index == '<' || index == '>') {
                    if(left != isFinish)	// 尚未解析完
                        toTranse(line.substring(left,isFinish), null);
                    if(line.charAt(isFinish+1) == '=') { //<= or >=的情况
                        orignList.add(map.get(index+"="));
                        afterList.add(index+"=");
                        isFinish += 1;
                    }else { //> or <
                        orignList.add(map.get(String.valueOf(index)));
                        afterList.add(String.valueOf(index));
                    }
                    parsed = true;
                    break;
                }else if(index== '='){
                    if(left != isFinish)	//
                        toTranse(line.substring(left,isFinish), null);
                    if(isFinish+1 < line.length() && line.charAt(isFinish+1) == '=') {	// token is ==
                        orignList.add(map.get("=="));
                        afterList.add("==");
                        isFinish += 1;
                    }else {	//=
                        orignList.add(map.get("="));
                        afterList.add("=");
                    }
                    parsed = true;
                    break;
                }else if(index == '!') { //!
                    if(left != isFinish)
                        toTranse(line.substring(left,isFinish), null);
                    if(isFinish+1 < line.length() &&
                            line.charAt(isFinish+1) == '=') { //!=
                        orignList.add(map.get("!="));
                        afterList.add("!=");
                        isFinish += 1;
                        parsed = true;
                    }else {
                        parsed = true;
                    }
                    break;
                }else if(index == ','
                        || index == '+'
                        || index == '-'
                        ||index == '*'
                        ||index == '/'
                        ||index == ';') {//上述符号进行解析
                    toTranse(line.substring(left,isFinish), null);
                    orignList.add(map.get(String.valueOf(index)));
                    afterList.add(String.valueOf(index));
                    parsed = true;
                    break;
                }
                isFinish++;
            }
            if(!parsed)
                toTranse(line.substring(left,isFinish), null);
            isFinish++;

        }

    }
    //包含
    public boolean contains(char[] list,char val) {
        for(int i = 0;i<list.length;i++) {
            if(list[i] == val)
                return true;
        }
        return false;
    }

    //从开始位置后寻找指定字符
    public int findNext(String line, char c,int start) {
        if(c == '?') return -1;
        for(int i = start+1;i<line.length();i++) {
            if(line.charAt(i) == c)
                return i;
        }
        return -1;
    }
    //解析
    public void toTranse(String token,String exp) {
        if(token.length() == 0) return;
        if(exp == null) {
            if(token.charAt(0) >='0' && token.charAt(0) <= '9') {
                boolean isNumber = true;
                for(int i = 1;i < token.length();i++) {
                    if(token.charAt(i) < '0' || token.charAt(i) > '9') {
                        isNumber = false;
                        break;
                    }
                }
                if (isNumber==true){
                    exp=INTCON;
                }
            }else {	//
                exp = map.get(token);
                if(exp == null)
                    exp = IDENFR;
            }
        }
        orignList.add(exp);
        afterList.add(token);
    }

    public void outputToken() {
        System.out.println("lex[tix]的值为"+name[index2]+" "+"token[tix]的值为"+token[index2]);
        outputStringBuilder.append(name[index2]+" "+token[index2]+"\n");
    }

    //判断字符串是否为Token
    public boolean isEqual(String s) {
        return s.equals(token[index2]);
    }
    // 如果字符串为Token则加入输出流
    public boolean isTrue(String s) {
        if(isEqual(s)) {
            outputToken();
            index2++;
            return true;
        }
        return false;
    }

    // 如果字符串不为Token则抛出异常
    public void jump(String s) {
        if(!isTrue(s)) {
            // 错误处理：退出
            throwError();
        }
    }

    //变量
    public boolean isNotFunc() {
        return (isEqual("int") || isEqual("char")) && !token[index2+2].equals("(");
    }
    // ＜加法运算符＞ ::= +｜-
    public boolean isAdd() {
        return isTrue("+") || isTrue("-");
    }

    // ＜乘法运算符＞  ::= *｜/
    public boolean isMul() {
        return isTrue("*") || isTrue("/");
    }

    // ＜关系运算符＞  ::=  <｜<=｜>｜>=｜!=｜==
    public boolean isRelation() {
        return isTrue("<") || isTrue(">") || isTrue("<=") || isTrue(">=") || isTrue("!=") || isTrue("==");
    }

    // <关系运算符> ++ --
    public boolean beforeCheckRelation(String s) {
        return s.equals("<") || s.equals(">") || s.equals("<=") || s.equals(">=") || s.equals("!=") || s.equals("==");
    }

    // ＜字母＞   ::= ＿｜a｜．．．｜z｜A｜．．．｜Z
    public boolean isWord() {
        String s = token[index2];
        if(s.length() > 1)
            throwError();
        char c = s.charAt(0);
        if(c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            isTrue(s);
            return true;
        }
        return false;
    }

    public boolean isWord(char c) {
        return c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }



    // ＜数字＞ ::= ０｜＜非零数字＞
    public boolean isNumber() {
        String s = token[index2];
        if(s.length() > 1)
            return false;
        if(s.charAt(0) >= '0' && s.charAt(0) <= '9') {
            isTrue(s);
            return true;
        }
        return false;
    }


    public boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }


    //判断字符串是否为字母或数字
    public boolean isIden() {
        String idenString = token[index2];
        char c = idenString.charAt(0);
        if(!isWord(c))
            return false;
        for(int i = 0;i<idenString.length();i++) {
            c = idenString.charAt(i);
            if(!isWord(c) && !isNumber(c))
                return false;
        }
        return true;
    }

    //判断字符串是否为字母或数字
    public boolean isIden(String idenString) {
        char c = idenString.charAt(0);
        if(!isWord(c)) {
            throwError();
        }
        for(int i = 1;i<idenString.length();i++) {
            c = idenString.charAt(i);
            if(!isWord(c) && !isNumber(c)) {
                throwError();
            }
        }
        return true;
    }

    private void throwError() {
        System.out.println(token[index2]);
        System.out.println("error!");
        System.exit(1);
    }

    // ＜非零数字＞  ::= １｜．．．｜９
    public boolean isNonZeroNumber() {
        String s = token[index2++];
        return s.length() == 1 && (s.charAt(0) >= '1' && s.charAt(0) <= '9');
    }

    // ＜字符＞::=  '＜加法运算符＞'｜'＜乘法运算符＞'｜'＜字母＞'｜'＜数字＞'
    public boolean charaExpr() {
        return isAdd() || isMul() || isWord() || isNumber();
    }

    // ＜字符串＞   ::=  "｛十进制编码为32,33,35-126的ASCII字符｝"
    public boolean isString() {
        if(!name[index2].equals("STRCON"))
            return false;				// 字符串与表达式token分辨
        String s = token[index2];
        for(int i = 0;i<s.length();i++) {
            char c = s.charAt(i);
            if(c != 32 && c != 33 && (c < 35 || c > 126)){
                return false;
            }
        }
        isTrue(s);
        System.out.println("<字符串>");
        outputStringBuilder.append("<字符串>\n");
        return true;
    }

    // ＜类型标识符＞ ::=  int | char
    public boolean isTypeSpecfier() {
        return isTrue("int") || isTrue("char");
    }
    // ＜标识符＞ ::=  ＜字母＞｛＜字母＞｜＜数字＞｝---标识符串
    public boolean idenExpr() {
        if(isIden(token[index2])) {
            isTrue(token[index2]);
            return true;
        }
        return false;
    }

    //＜无符号整数＞  ::= ＜非零数字＞｛＜数字＞｝| 0
    public void unsignedNumExpr() {
        String uint = token[index2];
        char c = uint.charAt(0);
        if(!isNumber(c))
            throwError();
        //判断ASCII值isNumber
        for(int i = 0;i<uint.length();i++){
            if(!isNumber(uint.charAt(i)))
                throwError();
        }
        jump(uint);
        System.out.println("<无符号整数>");
        outputStringBuilder.append("<无符号整数>\n");
    }

    // ＜整数> ::= ［＋｜－］＜无符号整数＞
    public void numExpr() {
        if(isTrue("+") || isTrue("-")) {

        }
        unsignedNumExpr();
        System.out.println("<整数>");
        outputStringBuilder.append("<整数>\n");
    }


    // ＜常量说明＞ ::=  const＜常量定义＞;{ const＜常量定义＞;}
    public void parseConst() {
        while(isTrue("const")) {
            parseTypeSpecifier();
            isTrue(";");
        }
        System.out.println("<常量说明>");
        outputStringBuilder.append("<常量说明>\n");
    }
    // ＜常量定义＞::= int＜标识符＞＝＜整数＞{,＜标识符＞＝＜整数＞} | char＜标识符＞＝＜字符＞{,＜标识符＞＝＜字符＞}
    public void parseTypeSpecifier() {
        //  int
        if(isTrue("int")) {
            do {
                //标识符
                idenExpr();
                jump("=");
                numExpr();
            }while(isTrue(","));
        }else if(isTrue("char")){//char
            do {
                idenExpr();
                jump("=");
                charaExpr();
            }while(isTrue(","));
        }else {
            throwError();
        }
        System.out.println("<常量定义>");
        outputStringBuilder.append("<常量定义>\n");
    }


    // ＜变量说明＞  ::= ＜变量定义＞;{＜变量定义＞;}
    public void parseVariable() {
        while(isNotFunc()) {
            parseVariableDefination();
            isTrue(";");
        }
        System.out.println("<变量说明>");
        outputStringBuilder.append("<变量说明>\n");
    }

    //＜变量定义＞  ::= ＜类型标识符＞ (＜标识符＞|＜标识符＞'['＜无符号整数＞']') {,(＜标识符＞|＜标识符＞'['＜无符号整数＞']' )}
    //＜无符号整数＞表示数组元素的个数，其值需大于0
    public void parseVariableDefination() {
        while(isTypeSpecfier()) {	// <类型标识符>
            do {
                idenExpr();//标识符
                if(isTrue("[")) {
                    unsignedNumExpr();
                    jump("]");
                }
            }while(isTrue(","));
            System.out.println("<变量定义>");
            outputStringBuilder.append("<变量定义>\n");
        }
    }


    // ＜声明头部＞ ::=  int＜标识符＞ |char＜标识符＞
    public void parseHead() {
        if(isTrue("int") || isTrue("char"))
            idenExpr();
        System.out.println("<声明头部>");
        outputStringBuilder.append("<声明头部>\n");
    }

    // 有返回值的函数 int char
    public boolean isICFunc() {
        return
                index2 + 2 < flag
                        &&(isEqual("int") || isEqual("char"))
                        && !token[index2+1].equals("main")//如果后面接的不是main
                        && isIden(token[index2+1]) //判断后面的是否为数字或者字母
                        && token[index2+2].equals("(");//后面的第二个元素是（
    }

    // void 开头的函数方法
    public boolean isVoidFunc() {
        return
                index2 + 2 < flag
                        && isEqual("void")
                        && isIden(token[index2+1])
                        && !token[index2+1].equals("main")
                        && token[index2+2].equals("(");
    }

    // main函数入口
    public boolean isMainFunc() {
        return index2 + 2 < flag
                && isEqual("void")
                && token[index2+1].equals("main")
                && token[index2+2].equals("(");
    }

    // ＜参数表＞ ::=  ＜类型标识符＞＜标识符＞{,＜类型标识符＞＜标识符＞}| ＜空＞
    public void parseExpression() {
        if(isEqual(")")) {
            //空
        }
        else {
            do {
                isTypeSpecfier();//类型标识符
                idenExpr();//标识符
            } while (isTrue(","));
        }
        System.out.println("<参数表>");
        outputStringBuilder.append("<参数表>\n");
    }


    //＜条件语句＞  ::= if '('＜条件＞')'＜语句＞［else＜语句＞］
    public void ifSentense() {
        jump("if");
        jump("(");
        Condition(); //条件
        jump(")");
        Sentence();//语句
        if(isTrue("else"))
            Sentence();//语句
        System.out.println("<条件语句>");
        outputStringBuilder.append("<条件语句>\n");
    }


    //＜因子＞  ::= ＜标识符＞｜＜标识符＞'['＜表达式＞']'|'('＜表达式＞')'｜＜整数＞|＜字符＞｜＜有返回值函数调用语句＞
    public void yinZi() {
        if(isIden() && token[index2+1].equals("(")) {	//＜有返回值函数调用语句＞
            ReturnFunctionCall();
        }else if(isTrue("(")){				// '('＜表达式＞')'
            Expression();
            isTrue(")");
        }else {
            char c = token[index2].charAt(0);
            char nc = token[index2+1].charAt(0);
            if(token[index2].length() == 1 && name[index2].equals(CharString)) {
                charaExpr();				//字符
            }else if((isEqual("+") || isEqual("-") && nc >= '0' && nc<= '9') || (c >= '0' && c<='9')) {
                numExpr();					//整数
            }
            else if(idenExpr()) {				// ＜标识符＞
                if(isTrue("[")) {				// ＜标识符＞'['＜表达式＞']'
                    Expression();
                    isTrue("]");
                }
            }
        }
        System.out.println("<因子>");
        outputStringBuilder.append("<因子>\n");
    }
    // ＜项＞     ::= ＜因子＞{＜乘法运算符＞＜因子＞}
    public void Item() {
        do {
            yinZi();
        } while (isMul());
        System.out.println("<项>");
        outputStringBuilder.append("<项>\n");
    }

    // ＜表达式＞    ::= ［＋｜－］＜项＞{＜加法运算符＞＜项＞}
    public void Expression() {
        if((isEqual("+")||isEqual("-")) && name[index2].equals(CharString)) {
        }else {
            boolean firstPM = false;
            if((isEqual("+") || isEqual("-")) && beforeCheckRelation(token[index2-1]) && (token[index2+1].equals("+") || token[index2+1].equals("-"))) {
                name[index2] = CharString;
                firstPM = true;
            }
            if(isTrue("+") || isTrue("-"));
            if(firstPM) {
                System.out.println("<因子>");
                System.out.println("<项>");
                outputStringBuilder.append("<因子>\n");
                outputStringBuilder.append("<项>\n");
            }
        }
        do {
            Item();
        } while (isTrue("+") || isTrue("-"));
        System.out.println("<表达式>");
        outputStringBuilder.append("<表达式>\n");
    }

    // ＜条件＞    ::=  ＜表达式＞＜关系运算符＞＜表达式＞
    public void Condition() {
        Expression();
        if(isRelation()) {
            Expression();
        }
        System.out.println("<条件>");
        outputStringBuilder.append("<条件>\n");
    }


    //＜步长＞::= ＜无符号整数＞
    public void parseSteps() {
        unsignedNumExpr();
        System.out.println("<步长>");
        outputStringBuilder.append("<步长>\n");
    }


    //＜循环语句＞   ::=  while '('＜条件＞')'＜语句＞| do＜语句＞while '('＜条件＞')'
    //                 |for'('＜标识符＞＝＜表达式＞;＜条件＞;＜标识符＞＝＜标识符＞(+|-)＜步长＞')'＜语句＞
    public void For() {
        if(isTrue("while")) {		// while
            jump("(");
            Condition();
            jump(")");
            Sentence();
        }else if(isTrue("do")){	// do while
            Sentence();
            jump("while");
            jump("(");
            Condition();
            jump(")");
        }else if(isTrue("for")) {	// for
            jump("(");
            idenExpr();
            jump("=");
            Expression();
            jump(";");
            Condition();
            jump(";");
            idenExpr();
            jump("=");
            idenExpr();
            isAdd();
            parseSteps();
            jump(")");
            Sentence();
        }						// err
        System.out.println("<循环语句>");
        outputStringBuilder.append("<循环语句>\n");
    }

    // ＜赋值语句＞   ::=  ＜标识符＞＝＜表达式＞|＜标识符＞'['＜表达式＞']'=＜表达式＞
    public void parseAssign() {
        idenExpr();
        if(isTrue("[")) {
            Expression();
            jump("]");
        }
        jump("=");
        Expression();
        System.out.println("<赋值语句>");
        outputStringBuilder.append("<赋值语句>\n");
    }

    // ＜读语句＞    ::=  scanf '(' ＜标识符＞ {,＜标识符＞} ')'
    public void Read() {
        jump("scanf");
        jump("(");
        do {
            idenExpr();
        } while (isTrue(","));
        jump(")");
        System.out.println("<读语句>");
        outputStringBuilder.append("<读语句>\n");
    }

    // ＜写语句＞::= printf '(' ＜字符串＞,＜表达式＞ ')'| printf '('＜字符串＞ ')'| printf '('＜表达式＞')'
    public void Write() {
        jump("printf");
        jump("(");
        if(isString()) {				// '('＜字符串＞ ')'
            if(isTrue(",")) {
                Expression();	// '(' ＜字符串＞,＜表达式＞ ')'
            }
        }else {
            Expression();	// '('＜表达式＞')'
        }

        jump(")");
        System.out.println("<写语句>");
        outputStringBuilder.append("<写语句>\n");
    }

    // ＜返回语句＞::=  return['('＜表达式＞')']
    public void Return() {
        isTrue("return");
        if(isTrue("(")) {
            Expression();
            jump(")");
        }
        System.out.println("<返回语句>");
        outputStringBuilder.append("<返回语句>\n");
    }

    //＜语句＞ ::= ＜条件语句＞｜＜循环语句＞| '{'＜语句列＞'}'| ＜有返回值函数调用语句＞; |＜无返回值函数调用语句＞;
    //           ｜＜赋值语句＞;｜＜读语句＞;｜＜写语句＞;｜＜空＞;|＜返回语句＞;
    public void Sentence() {
        if(isEqual("if")) {
            ifSentense();	//＜条件语句＞
        }else if(isEqual("do") || isEqual("while") || isEqual("for")) {
            For();			//＜循环语句＞
        }else if(isEqual("{")) {					//'{'＜语句列＞'}'
            jump("{");
            SentenceList();
            jump("}");
        }else if(isEqual("printf")) {			//＜读语句＞
            Write();
            jump(";");
        }else if(isEqual("scanf")) {				//＜写语句＞
            Read();
            jump(";");
        }else if(isEqual("return")) {			//＜返回语句＞
            Return();
            jump(";");
        }else if(ReturnFunc.contains(token[index2])) {	//有返回
            ReturnFunctionCall();
            jump(";");
        }else if(NoReturnFun.contains(token[index2])) {	//无返回
            NotHaveReturnFunc();
            jump(";");
        }
        else if(isEqual(";")){					//＜空＞
            jump(";");
        }else if(isIden()) {				//＜赋值语句＞
            parseAssign();
            jump(";");
        }
        System.out.println("<语句>");
        outputStringBuilder.append("<语句>\n");
    }

    //＜语句列＞ ::= ｛＜语句＞｝
    public void SentenceList() {
        do {
            Sentence();
        }while(!isEqual("}"));
        System.out.println("<语句列>");
        outputStringBuilder.append("<语句列>\n");
    }

    //＜复合语句＞::=  ［＜常量说明＞］［＜变量说明＞］＜语句列＞
    public void SomeSentence() {
        if(isEqual("const"))			//［＜常量说明＞］
            parseConst();
        if(isNotFunc())
            parseVariable();	//［＜变量说明＞］

        SentenceList();	// ＜语句列＞

        System.out.println("<复合语句>");
        outputStringBuilder.append("<复合语句>\n");
    }

    // ＜有返回值函数定义＞  ::=  ＜声明头部＞'('＜参数表＞')' '{'＜复合语句＞'}'
    public void HaveReturnFunc() {
        parseHead();
        ReturnFunc.add(token[index2-1]);
        jump("(");
        parseExpression();
        jump(")");
        jump("{");
        SomeSentence();
        jump("}");
        System.out.println("<有返回值函数定义>");
        outputStringBuilder.append("<有返回值函数定义>\n");
    }

    // ＜无返回值函数定义＞  ::= void＜标识符＞'('＜参数表＞')''{'＜复合语句＞'}'
    public void parseNonReturnFunctionDefination() {
        jump("void");
        idenExpr();
        NoReturnFun.add(token[index2-1]);
        jump("(");
        parseExpression();
        jump(")");
        jump("{");
        SomeSentence();
        jump("}");
        System.out.println("<无返回值函数定义>");
        outputStringBuilder.append("<无返回值函数定义>\n");
    }


    // ＜值参数表＞   ::= ＜表达式＞{,＜表达式＞}｜＜空＞
    public void parseValParameterTable() {
        if(isEqual(")")) {
        }
        else {
            do {
                Expression();//表达式
            }while(isTrue(","));
        }
        System.out.println("<值参数表>");
        outputStringBuilder.append("<值参数表>\n");
    }


    //＜有返回值函数调用语句＞ ::= ＜标识符＞'('＜值参数表＞')'
    public void ReturnFunctionCall() {
        idenExpr();//标识符
        jump("(");
        parseValParameterTable();//值参数表
        jump(")");
        System.out.println("<有返回值函数调用语句>");
        outputStringBuilder.append("<有返回值函数调用语句>\n");
    }

    //＜无返回值函数调用语句＞ ::= ＜标识符＞'('＜值参数表＞')'
    public void NotHaveReturnFunc() {
        idenExpr();
        jump("(");
        parseValParameterTable();
        jump(")");
        System.out.println("<无返回值函数调用语句>");
        outputStringBuilder.append("<无返回值函数调用语句>\n");
    }

    // 非main全部函数 ::=  {＜有返回值函数定义＞|＜无返回值函数定义＞}
    public void parseFunction() {
        while(isICFunc() || isVoidFunc()) {
            if(isICFunc()) {	// int | main
                HaveReturnFunc();
            }else {				// void
                parseNonReturnFunctionDefination();
            }
        }
    }

    // ＜主函数＞    ::= void main‘(’‘)’ ‘{’＜复合语句＞‘}’
    public void MainFunc() {
        jump("void");
        jump("main");
        jump("(");
        jump(")");
        jump("{");
        SomeSentence();
        jump("}");
        System.out.println("<主函数>");
        outputStringBuilder.append("<主函数>\n");
    }


    // ＜程序＞ ::= ［＜常量说明＞］［＜变量说明＞］{＜有返回值函数定义＞|＜无返回值函数定义＞}＜主函数＞
    public void Engine() {
        if(isEqual("const"))//常量说明
            parseConst();
        if(isNotFunc())
            parseVariable();//变量说明
        if(isICFunc() || isVoidFunc())//有无返回函数定义
            parseFunction();
        if(isMainFunc())
            MainFunc();
        System.out.println("<程序>");
        outputStringBuilder.append("<程序>\n");
    }

    public void InitGo() {
        flag = afterList.size();
        token = new String[flag];
        afterList.toArray(token);
        name = new String[flag];
        orignList.toArray(name);
        ReturnFunc = new LinkedList<String>();
        NoReturnFun = new LinkedList<String>();
        outputStringBuilder = new StringBuilder();
        outputFile = new File("output.txt");
        try {
            if(!outputFile.exists()) {
                outputFile.createNewFile();
            }
            outPutStream = new FileOutputStream(outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Engine();
        String op = outputStringBuilder.toString();
        byte[] bytes = null;
        try {
            bytes = op.substring(0,op.length()-1).getBytes("UTF-8");
            outPutStream.write(bytes);
            outPutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.InitGo();

    }
}

