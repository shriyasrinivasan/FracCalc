import java.util.*; 

/**
 * Calculates fractions.
 *
 * @author Shriya Srinivasan and Maya Singla
 * @version 1.0
 */
public class FracCalc {

    /**
     * Runs a fraction calculator.
     * 
     * @param args Not used
     */
    public static void main(String[] args) 
    {
        Scanner inputScanner = new Scanner(System.in);
        // Read a line and produce an answer.
        String inputLine = inputScanner.nextLine();
        System.out.println(produceAnswer(inputLine));
    }

    /**
     * ** IMPORTANT ** DO NOT DELETE THIS FUNCTION.
     * This function will be used to test your code
     * This function takes a String 'input' and produces the result.
     * 
     * @param input A fraction string that needs to be evaluated.
     * For your program, this will be the user input.
     *     e.g. input ==> "1/2 + 3/4"
     * @return The result of the fraction after it has been calculated
     *     e.g. return ==> "1_1/4"
     */
    public static String produceAnswer(String input)
    { 
        // Implement this function to produce the solution to the input
        // 1. split based on space (number operator number)
        // 2. convert each fraction to a proper/improper fraction
        // 3. look at operator and call appropriate method
        //   (addFrac, subFrac, mulFrac, divFrac)
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Bad Input!");
        }
        //assign all parts to strings
        String frac1 = parts[0];
        String op = parts[1];
        String frac2 = parts[2];
        //convert to simple frac
        int[] firstFrac = convertToSimpleFrac(frac1);
        int[] secondFrac = convertToSimpleFrac(frac2);
        if(op.equals("+")) {
            return addFrac(firstFrac, secondFrac);
        } else if (op.equals("-")) {
            return subFrac(firstFrac, secondFrac);
        } else if(op.equals("*")) {
            return mulFrac(firstFrac, secondFrac);
        } else if(op.equals("/")) {
            return divFrac(firstFrac, secondFrac);
        } else {
            throw new IllegalArgumentException("Bad operator " + op); 
        }
        
    }
  
    // Add two fractions together
   public static String addFrac(int[] firstFrac, int[] secondFrac) {
        // Find LCD. Used our lcm method we created.
        int lcd = lcm(firstFrac[1], secondFrac[1]);
        // Convert numerator of both fractions based on LCD
        int firstNum = firstFrac[0] * (lcd / firstFrac[1]);
        int secondNum = secondFrac[0] * (lcd / secondFrac[1]);
        // Add them and convert to a mixed number.
        int numerator = firstNum + secondNum;
        return mixedNum(numerator, lcd);
   }
    
   public static String subFrac(int [] firstFrac, int [] secondFrac) {
        // Convert the second fraction to a negative number and then call
        // addFrac
        secondFrac[0] = -1 * secondFrac[0];
        return addFrac(firstFrac, secondFrac);
   }
    
   public static String mulFrac(int [] firstFrac, int [] secondFrac) {
        int numerator = firstFrac[0] * secondFrac[0];
        int denom = firstFrac[1] * secondFrac[1];
        
        return mixedNum(numerator, denom);
   }
    
    public static String divFrac(int [] firstFrac, int [] secondFrac) {
        // Get the reciprocal of the second fraction.
        //renaming our numerator and denominator (just switching the numbers)
        int secondNum = secondFrac[1];
        int secondDenom = secondFrac[0];
        if (secondDenom < 0) {
            secondDenom = Math.abs(secondDenom);
            secondNum = -1 * secondNum;
        }
        secondFrac[0] = secondNum;
        secondFrac[1] = secondDenom;
        return mulFrac(firstFrac, secondFrac);
    }
    
    // TODO: Fill in the space below with any helper methods
    // This method converts a possibly mixed fraction into a simple
    // proper/improper fraction. Returns an int array with two elements.
    // First element numerator and second element is denominator
    public static int[] convertToSimpleFrac(String frac) {
        // Find if there is "_" (ex: 1_3/4)
        // If so, split based on that. Then find denominator of second string.
        int[] fracParts = new int[2];
        int indexOfUnderscore = frac.indexOf("_");
        String restOfString = frac.substring(indexOfUnderscore+1);
        int indexOfSlash = restOfString.indexOf("/");
        
        // Check for validity. 
        // If there is an _ there should be a /
        if ((indexOfUnderscore != -1) && (indexOfSlash == -1)) {
            throw new IllegalArgumentException("Bad fraction: " + frac);
        }
        
        // If no underscore or / then the string is the whole number
        // If there is an underscore, whole number is the part before it.
        // If there is a / then compute num and denom.
        if ((indexOfUnderscore == -1) && (indexOfSlash == -1)) {
            fracParts[0] = Integer.parseInt(frac);
            // Its a whole number, so denom is 1.
            fracParts[1] = 1;
            //test(fracParts);
            return fracParts;
        } 
        
        // We should have a fractional part.
        int wholeNumber = 0, numerator = 0, denominator = 0;
        if (indexOfUnderscore != -1) {
            String whole = frac.substring(0, indexOfUnderscore);
            wholeNumber = Integer.parseInt(whole);
        }
        if (indexOfSlash != -1) {
            String num = restOfString.substring(0,indexOfSlash);
            String denom = restOfString.substring(indexOfSlash+1);
            numerator = Integer.parseInt(num);
            denominator = Integer.parseInt(denom); 
        }
        boolean isNegative = (wholeNumber < 0);
        fracParts[0] = Math.abs(wholeNumber) * denominator + numerator;
        if (isNegative) {
            fracParts[0] = -1 * fracParts[0];
        }
      
        fracParts[1] = denominator;
        //test(fracParts);
        return fracParts;
    }
    
    public static String mixedNum(int num, int denom) {
        boolean isNeg = (num < 0);
        // Convert num to a abs value
        //num is numerator
        num = Math.abs(num);

        int wholeNumber = num / denom;
        num = num % denom;
        int gcf = gcf(num, denom);
        num = num / gcf;
        denom = denom / gcf;
        
        String prefix;
        if (isNeg) {
            prefix = "-";
        } else {
            prefix = "";
        }
        
        if (wholeNumber != 0) {
            if (num == 0) {
                // This is just a whole number
                return prefix + wholeNumber;
            } else {
                return prefix + wholeNumber + "_" + Math.abs(num) + "/" + denom;
            }
        } else {
            if (num == 0) {
                return "0";
            } else {
                return prefix + num + "/" + denom;
            }
        }
    }
        
    /**
     * GCF
     * 
     * @param p first int
     * @param q second int
     * @return The GCF
     */
    public static int gcf(int p, int q) {
        while (q != 0) {
            int temp = q;
            q = p % q;
            p = temp;
        }
        return p;
    }
    
    public static int lcm(int p, int q){
        return (p*q)/(gcf(p,q));
    }

    public static void test(int[] firstFrac){
        System.out.println(firstFrac[0]);
        System.out.println(firstFrac[1]);
    }
}

