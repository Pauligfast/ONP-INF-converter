package com.company;

import java.util.Scanner;

public class Main {

    public static Scanner scanner = new Scanner(System.in);

    public static class Stack1 {

        int size;
        char[] array;
        private int top;

        public Stack1(int x) {
            size = x;
            array = new char[size];
            top = size;
        }

        public void push(char ch) {

            array[--top] = ch;

        }

        public char pop() {
            return array[top++];
        }

        public char top() {
            return array[top];
        }

        public boolean isEmpty() {
            return (top == size);
        }

        public boolean isFull() {
            return (top == 0);
        }
    }

    public static class Stack2 {

        private int size;
        String[] array;
        private int top;

        public Stack2(int x) {
            size = x;
            array = new String[size];
            top = size;
        }

        public void push(String str) {

            array[--top] = str;

        }

        public String pop() {
            return array[top++];
        }

        public String top() {
            return array[top];
        }

        public boolean isEmpty() {
            return (top == size);
        }

        public boolean isFull() {
            return (top == 0);
        }

    }

    public static class Stack3 {

        private int size;
        int[] array;
        private int top;

        public Stack3(int x) {
            size = x;
            array = new int[size];
            top = size;
        }

        public void push(int x) {
            if (isFull()) {
                System.out.println("full");
            } else {
                array[--top] = x;
            }
        }

        public int pop() {
            return array[top++];
        }

        public int top() {
            return array[top];
        }

        public boolean isEmpty() {
            return (top == size);
        }

        public boolean isFull() {
            return (top == 0);
        }

    }

    public static int opriority(char ch) {
        switch (ch) {
            case '~':
                return 0;
            case '^':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            case '+':
            case '-':
                return 3;
            case '>':
            case '<':
            case '=':
                return 4;
            default:
                return 5;
        }
    }

    public static boolean operand(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    public static boolean correct(String str) {
        int opnum = 0;
        int opernum = 0;
        int ch;

        for (int i = 0; i < str.length(); i++) {
            ch = opriority(str.charAt(i));
            if (ch == 5) {
                opnum++;
            } else if (ch != 0) {
                opernum++;
            }
            if (opnum < opernum + 1) {
                return false;
            }
        }
        return opnum == opernum + 1;
    }

    public static String cleanINF(String str) {
        String output = "";
        for (int i = 0; i < str.length(); i++) {
            char y = str.charAt(i);
            if (operand(y) || opriority(y) != 5) {
                output += str.charAt(i);
            }
        }
        return output;
    }

    public static String cleanONP(String str) {
        String output = "";
        for (int i = 0; i < str.length(); i++) {
            char y = str.charAt(i);
            if (operand(y) || opriority(y) != 5 || y == '(' || y == ')') {
                output += str.charAt(i);
            }
        }
        return output;
    }

    public static String InfixToONP(String str, Stack1 stack1) {

        str = cleanONP(str);
        int prior;
        String output = "";

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch == '(') {
                stack1.push(ch);
            } else if (ch == ')') {
                if (stack1.isEmpty()) {
                    return "error";
                }
                while (stack1.top() != '(') {
                    output += stack1.pop();
                    if (stack1.isEmpty()) {
                        return "error";
                    }
                }
                stack1.pop();
            } else if (operand(ch)) {
                output += ch;
            } else if (opriority(ch) != 5) {
                prior = opriority(ch);

                while (!stack1.isEmpty() && opriority(stack1.top()) <= prior) {
                    output += stack1.pop();
                }
                stack1.push(ch);
            }
        }

        while (!stack1.isEmpty()) {
            if (stack1.top() != '(') {
                output += stack1.pop();
            } else {
                return "error";
            }
        }

        if (correct(output)) {
            return output;
        } else {
            return "error";
        }
    }

    public static String ONPToInfix(String str, Stack3 stack3, Stack2 stack2) {

        str = cleanINF(str);

        if (!correct(str)) {
            return "error";
        } else {
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);

                if (operand(ch)) {
                    stack2.push("" + ch);
                    stack3.push(-1);
                } else if (opriority(ch) != 0) {
                    int prior = opriority(ch);

                    if (stack2.isEmpty()) {
                        return "error";
                    }
                    String r = stack2.pop();
                    int rp = stack3.pop();

                    if (stack2.isEmpty()) {
                        return "error";
                    }
                    String l = stack2.pop();
                    int lp = stack3.pop();

                    if (prior <= rp) {
                        r = "(" + r + ")";
                    }
                    if (prior < lp) {
                        l = "(" + l + ")";
                    }
                    stack2.push(l + ch + r);
                    stack3.push(prior);
                } else { //if ~
                    int prior = opriority(ch);

                    if (stack2.isEmpty()) {
                        return "error";
                    }
                    String r = stack2.pop();
                    int rp = stack3.pop();

                    if (prior <= rp) {
                        r = "(" + r + ")";
                    }
                    stack2.push(ch + r);
                    stack3.push(prior);
                }
            }
        }
        return stack2.pop();
    }

    public static void main(String[] args) {

        int numofreps = scanner.nextInt();
        scanner.nextLine();

        while (numofreps-- > 0) {

            Stack1 stack1 = new Stack1(256);
            Stack2 stack2 = new Stack2(256);
            Stack3 stackInt = new Stack3(256);
            String wyraz = scanner.nextLine();

            if (wyraz.substring(0, 5).equals("<INF>")) {
                System.out.print("<ONP>");
                wyraz = wyraz.substring(5);
                System.out.println(InfixToONP(wyraz, stack1));
            } else {
                System.out.print("<INF>");
                wyraz = wyraz.substring(5);
                System.out.println(ONPToInfix(wyraz, stackInt, stack2));
            }
        }

    }
}
