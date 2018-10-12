package Application.LongAdd;

/**
 * Single thread sequential implementation of
 * add for long decimal values
 */
public class Add implements BinaryOperator {

    public Add() {}

    @Override
    public DecimalValue apply(DecimalValue a, DecimalValue b) {

        byte v1[] = a.getBuffer();
        byte v2[] = b.getBuffer();
        byte res[] = new byte[Math.max(v1.length, v2.length) + 1];

        byte carry = 0;
        int eqParts = Math.min(v1.length, v2.length);
        int length = 0;

        for (int i = 0; i < eqParts; ++i) {
            res[i] = (byte)((v1[i] + v2[i] + carry) % 10);
            carry = (byte)((v1[i] + v2[i] + carry) / 10);
        }

        if (v1.length > v2.length) {
            for (int i = eqParts; i < v1.length; ++i) {
                res[i] = (byte)((v1[i] + carry) % 10);
                carry = (byte)((v1[i] + carry) / 10);
            }

            length = v1.length;
        }
        else if (v2.length > v1.length) {
            for (int i = eqParts; i < v2.length; ++i) {
                res[i] = (byte)((v2[i] + carry) % 10);
                carry = (byte)((v2[i] + carry) / 10);
            }

            length = v2.length;
        }
        else {
            length = v1.length;
        }

        if (carry > 0) {
            res[length] = carry;
            length += 1;
        }

        return new DecimalValue(res, length);
    }

    public static void main(String ... args) {

        Add add = new Add();

        System.out.println(add.apply(new DecimalValue("1234545"), new DecimalValue("1234545")));
        System.out.println(add.apply(new DecimalValue("1234545"), new DecimalValue("1234545423423")));
        System.out.println(add.apply(new DecimalValue("1"), new DecimalValue("99999999")));
        System.out.println(add.apply(new DecimalValue("99999999"), new DecimalValue("1")));
        System.out.println(add.apply(new DecimalValue("0"), new DecimalValue("0")));
        System.out.println(add.apply(new DecimalValue(""), new DecimalValue("")));
        System.out.println(add.apply(new DecimalValue("5"), new DecimalValue("5")));
    }

}
