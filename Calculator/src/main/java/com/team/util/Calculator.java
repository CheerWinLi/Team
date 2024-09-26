package com.team.util;

import com.team.entity.po.Fraction;

import java.util.Stack;

/**
 * @author :Lictory
 * @date : 2024/09/26
 */
public class Calculator {
    final public static Calculator calculator = new Calculator();
    /**
     * 数字栈,用于存储表达式中的数值
     */
    private Stack<Fraction> fractionStack = null;
    /**
     * 符号栈,用于存储运算符和括号和带分数号
     */
    private Stack<Character> operationStack = null;

    /**
     * 解析并计算四则运算表达式，返回计算结果
     *
     * @param numStr 四则运算表达式
     * @return 计算结果
     */
    public Fraction calculate(String numStr) {
        if (numStr != null) {
            //去除空格方便处理
            numStr = numStr.replaceAll(" ", "");
        } else {
            return new Fraction(0);
        }

        // 如果算术表达式尾部没有‘=’号，则在尾部添加‘=’，表示结束符
        if (numStr.length() >= 1 && !('=' == numStr.charAt(numStr.length() - 1))) {
            numStr += "=";
        }

        // 检查表达式是否合法
        if (!isStandard(numStr)) {
            System.out.println(new Throwable().getStackTrace()[0] + ":" + numStr);
            return new Fraction(0);
        }
        // 初始化栈
        fractionStack = new Stack<Fraction>();
        operationStack = new Stack<Character>();
        // 用于缓存数字，因为数字可能是多位的
        StringBuffer nowFractionNum = new StringBuffer();
        // 从表达式的第一个字符开始处理
        for (int i = 0; i < numStr.length(); i++) {
            // 获取一个字符
            char nowChar = numStr.charAt(i);
            // 若当前字符是数字
            if (isNumber(nowChar)) {
                // 加入到数字缓存中
                nowFractionNum.append(nowChar);
            } else {
                // 非数字的情况
                // 将数字缓存转为字符串
                String checkFractionNum = nowFractionNum.toString();
                if (!checkFractionNum.isEmpty()) {
                    // 将数字字符串转为长整型数
                    int num = Integer.parseInt(checkFractionNum);
                    // 将数字压栈
                    fractionStack.push(new Fraction(num));
                    // 重置数字缓存
                    nowFractionNum = new StringBuffer();
                }
                //若当前运算符优先级小于栈尾的优先级，则应当先处理前面运算符
                while (!comparePri(nowChar) && !operationStack.empty()) {
                    // 出栈，取出数字，后进先出
                    Fraction b = fractionStack.pop();
                    Fraction a = fractionStack.pop();
                    // 取出栈尾运算符进行相应运算，并把结果压栈用于下一次运算
                    switch (operationStack.pop()) {
                        //处理带分数
                        case '’':
                            fractionStack.push(Fraction.with(a, b));
                            break;
                        //处理普通运算符
                        case '+':
                            fractionStack.push(Fraction.add(a, b));
                            break;
                        case '-':
                            fractionStack.push(Fraction.sub(a, b));
                            break;
                        case '×':
                        case '*':
                            fractionStack.push(Fraction.mul(a, b));
                            break;
                        case '/':
                        case '÷':
                            fractionStack.push(Fraction.div(a, b));
                            break;
                        default:
                            break;
                    }
                }
                if (nowChar != '=') {
                    // 符号入栈
                    operationStack.push(nowChar);
                    if (nowChar == ')') {
                        // 去括号
                        operationStack.pop();
                        operationStack.pop();
                    }
                }
            }
        }
        return fractionStack.pop();
    }

    /**
     * 检查是不是合法的表达式
     *
     * @param numStr 表达式
     * @return 是否合法
     */
    private boolean isStandard(String numStr) {
        if (numStr == null || numStr.isEmpty()) {
            return false;
        }
        // 用来保存括号，检查左右括号是否匹配
        Stack<Character> stack = new Stack<Character>();
        // 用来标记'='符号是否存在多个
        boolean haveEq = false;
        for (int i = 0; i < numStr.length(); i++) {
            char nowChar = numStr.charAt(i);
            // 判断字符是否合法
            if (!(
                    isNumber(nowChar)
                            || '(' == nowChar
                            || ')' == nowChar
                            || '+' == nowChar
                            || '-' == nowChar
                            || '*' == nowChar
                            || '/' == nowChar
                            || '=' == nowChar
                            || '÷' == nowChar
                            || '×' == nowChar
                            || '’' == nowChar
            )) {
                return false;
            }
            // 将左括号压栈，用来给后面的右括号进行匹配
            if ('(' == nowChar) {
                stack.push(nowChar);
            }
            // 匹配括号
            if (')' == nowChar) {
                // 括号是否匹配
                if (stack.isEmpty() || !('(' == stack.pop())) {
                    return false;
                }
            }
            // 检查是否有多个'='号
            if ('=' == nowChar) {
                if (haveEq) {
                    return false;
                }
                haveEq = true;
            }
        }
        // 可能会有缺少右括号的情况
        if (!stack.isEmpty()) {
            return false;
        }
        // 检查'='号是否不在末尾
        return '=' == numStr.charAt(numStr.length() - 1);
    }

    /**
     * 判断字符是否是0-9的数字
     *
     * @param num 字符
     * @return boolean
     */
    private boolean isNumber(char num) {
        return num >= '0' && num <= '9';
    }

    /**
     * 比较优先级：如果当前运算符比栈顶元素运算符优先级高则返回true，否则返回false
     * 符号优先级说明（从高到低）:
     * 第1级: (
     * 第2级: * /
     * 第3级: + -
     * 第4级: )
     *
     * @param symbol 当前运算符
     * @return 是否比栈顶元素优先
     */
    private boolean comparePri(char symbol) {
        // 空栈返回ture
        if (operationStack.empty()) {
            return true;
        }
        // 查看堆栈顶部的对象，注意不是出栈
        char top = operationStack.peek();
        if (top == '(') {
            return true;
        }
        // 比较优先级
        switch (symbol) {
            // 优先级最高
            case '(':
                return true;
            case '/':
                return true;
            case '’':
                // 优先级比+和-高
                return top == '+' || top == '-' || top == '×' || top == '÷' || top == '*';
            case '×':
            case '*':
            case '÷':
                // 优先级比+和-高
                return top == '+' || top == '-';
            case '+':
            case '-':
                return false;
            case ')': // 优先级最低
                return false;
            case '=': // 结束符
                return false;
            default:
                break;
        }
        return true;
    }
}
