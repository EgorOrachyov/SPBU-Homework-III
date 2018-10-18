package Application.ParenthesesBalance;

public class Check {

    /**
     * Check balance for the string1 built from parentheses
     * @param a String
     * @return True if it is balanced
     */
    public static boolean compute(String a) {

        if (a.length() == 0) {
            return true;
        }

        byte[] data = a.getBytes();
        int balance = 0;

        for(byte e : data) {
            balance += (e == ')' ? -1 : 1);
            if (balance < 0) {
                return false;
            }
        }

        return  (balance == 0);
    }

    public static void main(String ... args) {

        System.out.println(Check.compute("(()()())((((()()))()))"));
        System.out.println(Check.compute(")(()()())((((()()))()))"));
        System.out.println(Check.compute("(()()())((((()())))))(()()"));

    }

}
