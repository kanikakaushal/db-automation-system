/*
 * @(#)CheckDigit.java	1.0 01/01/2011
 *
 * Copyright 2011 PSS. All rights reserved.
 */
package org.satsang.sms.core.member;

import java.util.List;

/**
 * This class implements the basic functionality for the check-digit using a
 * scheme similar to the standard EAN-13 system.
 * <p>
 * The check-digit appended at the end of the code is such that the checksum is
 * <code>0</code>. The checksum is the <code>modulo 10</code> sum of the even
 * position digits with weight <code>3</code> and odd position digits with
 * weight <code>1</code>. Both <code>3</code> and <code>1</code> are relatively
 * prime to <code>10</code>.
 * <p>
 * This class <code>CheckDigit</code> has methods for both appending the
 * check-digit and for validating a code with the check digit already appended.
 * Typical uses are generating bar codes and for validating scanned bar codes.
 *
 * @author  PSS
 * @version 1.0, 01/01/2011
 */

public class CheckDigit {

    /**
     * Generates and appends the check-digit at the end of a numeric code.
     * @param   code    the code (number) to without the check-digit.
     * @return  the code with the check-digit appended. The length is
     *          <code>1</code> longer than the argument.
     * @since 1.0
     */

    public static final long appendCheckDigit (long code) {
        int checkSum = getCheckSum(code, false);
        int checkDigit = 10 - checkSum;
        if (checkDigit == 10) {
            checkDigit = 0;
        }
        code = (code * 10) + checkDigit;
        return code;
    }
    
    /**
     * Validates the code and detects all single digit errors.
     * @param   code    the code (number) to with the check-digit appended.
     * @param   numDigits   the number of digits expected in the code exluding
     *                      leading zeros.
     * @return  <code>true</code> if the code is of the expected length and if
     *          the checksum is valid and <code>false</code> otherwise.
     * @since 1.0
     */

    public static final boolean validateCode (long code, int numDigits) {
        if (getNumDigits(code) != numDigits) {
            return false;
        }

        // Stop-gap arrangement to handle cases where contingency digit is not
        // zero starts here. This is here because some codes were incorrectly
        // generated. This part should be removed later.
//        if ((code/100000)%10 != 0) {
//            return true;
//        }
        // Stop-gap arrangement starts here.

        int checkSum = getCheckSum(code, true);
        return (checkSum == 0);
    }
    
    /**
     * Calculates the checksum.
     * @param   code    the code (number) for which the checksum is calculated.
     * @param   checkDigitAppended  <code>true</code> if the code already has
     *                              the check-digit appended and
     *                              <code>false</code> otherwise.
     * @return  the checksum, which is the <code>modulo 10</code> sum of the
     *          even position digits with weight <code>3</code> and odd position
     *          digits with weight <code>1</code>.
     * @since 1.0
     */

    private static final int getCheckSum (long code, boolean checkDigitAppended) {
        int checkSum = 0;
        boolean oddPosition = checkDigitAppended;
        while (code > 0) {
            int digit = (int)(code % 10);
            if (oddPosition) {
                checkSum += digit;
            } else {
                checkSum += digit * 3;
            }
            code /= 10;
            oddPosition = !oddPosition;
        }
        return checkSum % 10;
    }
    
    /**
     * Returns the number of digits excluding leading zeros.
     * @param   code    the code (number) for which the number of digits are to
     *                  be determined.
     * @return  the number of digits in the argument excluding leading zeros.
     * @since 1.0
     */

    private static final int getNumDigits (long code) {
        int numDigits = 0;
        while (code > 0) {
            code /= 10;
            numDigits ++;
        }
        return numDigits;
    }

    /**
     * Private constructor to avoid instantiation.
     */

    private CheckDigit(){
    }
    
    public static void main(String args[]){
    	Long[] ids = {105050300009L,
    			105050300010L,
    			105050300011L,
    			105050300012L,
    			105050300013L,
    			105050300014L,
    			105050300015L,
    			105050300016L,
    			105050300017L,
    			105050300018L,
    			105050300019L,
    			105050300020L,
    			105050300021L,
    			105050300022L,
    			105050300023L,
    			105050300024L,
    			105050300025L,
    			105050300026L,
    			105050300027L,
    			105050300028L,
    			105050300029L,
    			105050300030L,
    			105050300031L,
    			105050300032L,
    			105050300033L,
    			105050300034L,
    			105050300035L,
    			105050300036L,
    			105050300037L};
//    	System.out.println(CheckDigit.validateCode(1000301100022L, 13));
    	for(int i=0;i<ids.length; i++){
    		System.out.println(CheckDigit.appendCheckDigit(ids[i]));
    	}
    }
}
