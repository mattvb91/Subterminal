package mavonie.subterminal.Utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * https://github.com/MarcKuniansky/UnitConverter
 */
public class UnitConverter {


    /**
     * Constructor with no parameters
     */
    public UnitConverter() {
        //Can just be empty, there are no global variables
    }

    /**
     * Takes a number, a length unit, and a new unit and converts the number to the new unit.
     * <p>
     * Accepted units: inches, feet, yards, miles, millimeters, centimeters, meters, kilometers
     * Marc Kuniansky
     *
     * @param originalNum  must be a valid double.
     * @param originalUnit must be a string
     * @param desiredUnit  must be a string
     * @return the new double
     */
    public double lengthConvert(double originalNum, String originalUnit, String desiredUnit) {
        //Make two variable doubles, one the original double and one the new one
        double num1 = originalNum;
        double num2 = 0.0d;

        //Store the units into new strings. I find this to be safer, as I can't override the originals this way.
        //Also convert them to lower case
        String original = originalUnit.toLowerCase();
        String newU = desiredUnit.toLowerCase();

        //Now there is a series of if statements to check which units are being converted from/to and
        //to do the proper operation
        switch (original) {
            //Next, converting from feet
            case "feet": { //Begin conversions from feet
                switch (newU) {
                    case "inches":
                        //12 inches in a foot, multiply by 12
                        num2 = num1 * 12.0d;
                        break;
                    case "feet":
                        num2 = num1;
                        break;
                    case "yards":
                        //3 feet in a yard
                        num2 = num1 / 3.0d;
                        break;
                    case "miles":
                        //5,280 feet in a mile
                        num2 = num1 / 5280.0d;
                        break;
                    case "millimeters":
                        //304.8 millimeters in a foot
                        num2 = num1 * 304.8d;
                        break;
                    case "centimeters":
                        //30.48 Centimeters in a foot
                        num2 = num1 * 30.48d;
                        break;
                    case "meters":
                        //0.3048 meters in a foot
                        num2 = num1 * 0.3048d;
                        break;
                    case "kilometers":
                        //0.0003048 kilometers in a meter
                        num2 = num1 * 0.0003048d;
                        break;
                }
                break;
            } //End conversions from feet

            //Next, convert from miles
            case "miles": { //Begin conversions from miles
                switch (newU) {
                    case "inches":
                        //6330 inches in a mile
                        num2 = num1 * 6330.0d;
                        break;
                    case "feet":
                        //5280 feet in a mile
                        num2 = num1 * 5280.0d;
                        break;
                    case "yards":
                        //1760 yards in a mile
                        num2 = num1 * 1760.0d;
                        break;
                    case "miles":
                        num2 = num1;
                        break;
                    case "millimeters":
                        //1,609,000 millimeters in a mile
                        num2 = num1 * 1609340.0d;
                        break;
                    case "centimeters":
                        //16,0934 centimeters in a miles
                        num2 = num1 * 160934.0d;
                        break;
                    case "meters":
                        //1609.34 meters in a mile
                        num2 = num1 * 1609.34d;
                        break;
                    case "kilometers":
                        //1.60934 kilometers in a mile
                        num2 = num1 * 1.60934d;
                        break;
                }
                break;
            } //End converting from miles

            //Next, convert from meters
            case "meters": { //Begin converting from meters
                switch (newU) {
                    case "inches":
                        num2 = num1 * 39.3701d;
                        break;
                    case "feet":
                        num2 = num1 * 3.28084d;
                        break;
                    case "yards":
                        num2 = num1 * 1.09361d;
                        break;
                    case "miles":
                        num2 = num1 / 1609.34d;
                        break;
                    case "millimeters":
                        num2 = metricConvert(num1, "unit", "milli");
                        break;
                    case "centimeters":
                        num2 = metricConvert(num1, "unit", "centi");
                        break;
                    case "meters":
                        num2 = num1;
                        break;
                    case "kilometers":
                        num2 = this.metricConvert(num1, "unit", "kilo");
                        break;
                }
                break;
            } //End converting from meters

            //Finally, try converting from kilometers
            case "kilometers": { //Begin converting from kilometers
                switch (newU) {
                    case "inches":
                        num2 = num1 * 39370.1d;
                        break;
                    case "feet":
                        num2 = num1 * 3280.84d;
                        break;
                    case "yards":
                        num2 = num1 * 1093.61d;
                        break;
                    case "miles":
                        num2 = num1 / 1.60934d;
                        break;
                    case "millimeters":
                        num2 = metricConvert(num1, "kilo", "milli");
                        break;
                    case "centimeters":
                        num2 = metricConvert(num1, "kilo", "centi");
                        break;
                    case "meters":
                        num2 = metricConvert(num1, "kilo", "unit");
                        break;
                    case "kilometers":
                        num2 = num1;
                        break;
                }
                break;
            } //End converting from kilometers
        }

        //num2 is the number we want; return it
        return num2;
    } // End lengthConvert

    /**
     * Takes a number, a temperature unit, and a new unit and converts the number to the new unit.
     * <p>
     * Accepted units: Fahrenheit, Kelvin, Celsius
     * Noah Getz
     *
     * @param originalNum  must be a valid double.
     * @param originalUnit must be a string
     * @param desiredUnit  must be a string
     * @return the new double
     */
    public double tempConvert(double originalNum, String originalUnit, String desiredUnit) { //Begin tempConvert
        //Make two variable doubles, one the original double and one the new one
        double num2 = 0.0d;
        double num3;

        //Store the units into new strings. I find this to be safer, as I can't override the originals this way.
        //Also convert them to lower case
        String original = originalUnit.toLowerCase();
        String newU = desiredUnit.toLowerCase();

        switch (original) { //Begin conversion table
            case "celsius": { //Begin converting from Celsius
                switch (newU) {
                    case "celsius":
                        num2 = originalNum;
                        break;
                    case "fahrenheit":
                        num2 = (originalNum * (9.0 / 5.0)) + 32d;
                        break;
                    case "kelvin":
                        num2 = originalNum + 273.15d;
                        break;
                }
                break;
            } //end converting from Celsius
            case "fahrenheit": { //Begin converting from Fahrenheit
                switch (newU) {
                    case "celsius":
                        num3 = (-originalNum - 32d);
                        num2 = num3 * (5.0 / 9.0);
                        break;
                    case "fahrenheit":
                        num2 = originalNum;
                        break;
                    case "kelvin":
                        num2 = ((originalNum - 32d) * (5.0 / 9.0)) + 273.15d;
                        break;
                }
                break;
            }

        } //End conversion table

        //Return the final number, num2
        return num2;
    } //End tempConvert

    /**
     * Takes a number, a mass unit, and a new unit and converts the number to the new unit.
     * <p>
     * Accepted units: pounds, kilograms, grams, milligrams
     * Noah Getz
     *
     * @param originalNum  must be a valid double.
     * @param originalUnit must be a string
     * @param desiredUnit  must be a string
     * @return the new double
     */
    public double massConvert(double originalNum, String originalUnit, String desiredUnit) { //Begin massConvert
        //Make two variable doubles, one the original double and one the new one
        double num1 = originalNum;
        double num2 = 0.0d;


        //Store the units into new strings. I find this to be safer, as I can't override the originals this way.
        //Also convert them to lower case
        String original = originalUnit.toLowerCase();
        String newU = desiredUnit.toLowerCase();

        switch (original) { //Begin conversion table
            case "pounds":
                switch (newU) {
                    case "pounds":
                        num2 = originalNum;
                        break;
                    case "kilograms":
                        num2 = originalNum * 0.453592d;
                        break;
                    case "grams":
                        num2 = originalNum * 453.592d;
                        break;
                    case "milligrams":
                        num2 = originalNum * 453592.0d;
                        break;
                }
                break;
            case "kilograms":
                switch (newU) {
                    case "pounds":
                        num2 = originalNum * 2.20462d;
                        break;
                    case "kilograms":
                        num2 = originalNum;
                        break;
                    case "grams":
                        num2 = metricConvert(num1, "kilo", "unit");
                        break;
                    case "milligrams":
                        num2 = metricConvert(num1, "kilo", "milli");
                        break;
                }
                break;
        } //End conversion table

        //Return the final number
        return num2;
    } //End massConvert

    /**
     * Takes a number, a pressure unit, and a new unit and converts the number to the new unit.
     * <p>
     * Accepted units: torr, atm, mmHg, barr
     * Noah Getz
     *
     * @param originalNum  must be a valid double.
     * @param originalUnit must be a string
     * @param desiredUnit  must be a string
     * @return the new double
     */
    public double pressureConvert(double originalNum, String originalUnit, String desiredUnit) {
        //Make two variable doubles, one the original double and one the new one
        double num2 = 0.0d;


        //Store the units into new strings. I find this to be safer, as I can't override the originals this way.
        //Also convert them to lower case
        String original = originalUnit.toLowerCase();
        String newU = desiredUnit.toLowerCase();


        if (original.equals("torr")) {
            if (newU.equals("torr")) {
                num2 = originalNum;

            } else if (newU.equals("atm")) {
                num2 = originalNum * 0.0013157893594d;
            } else if (newU.equals("mmhg")) {
                num2 = originalNum * 0.99999984999d;
            } else {
                num2 = originalNum * 0.0013332237d;
            }
        } else if (original.equals("atm")) {
            if (newU.equals("atm")) {
                num2 = originalNum;

            } else if (newU.equals("torr")) {
                num2 = originalNum * 760.00006601d;
            } else if (newU.equals("mmhg")) {
                num2 = originalNum * 759.999952d;
            } else {
                num2 = originalNum * 1.0132501d;
            }
        } else if (original.equals("mmhg")) {
            if (newU.equals("mmhg")) {
                num2 = originalNum;

            } else if (newU.equals("torr")) {
                num2 = originalNum * 1.00000015d;
            } else if (newU.equals("atm")) {
                num2 = originalNum * 0.0013157895568d;
            } else {
                num2 = originalNum * 0.0013332239d;
            }
        } else {
            if (newU.equals("bar")) {
                num2 = originalNum;

            } else if (newU.equals("torr")) {
                num2 = originalNum * 750.06167382d;
            } else if (newU.equals("atm")) {
                num2 = originalNum * 0.98692316931d;
            } else {
                num2 = originalNum * 750.0615613d;
            }
        }
        return num2;
    }


    /**
     * Converts a number from one unit of speed to another
     * <p>
     * Recognized speed units: miles per hour, feet per second, meters per second,
     * kilometers per second, kilometers per hour.
     * <p>
     * Marc Kuniansky
     *
     * @param originalNumber must be a valid double
     * @param originalUnit   must be a valid String recognized by the method
     * @param newUnit        must be a valid String recognized by the method
     * @return double, the converted unit.
     */
    public double speedConvert(double originalNumber, String originalUnit, String newUnit) { //Begin convertSpeed
        //Make two doubles, one that holds the original and one that will be redefined where needed
        double num1 = originalNumber;
        double num2 = 0.0d;

        //Make two strings, capturing the units fed to the method
        String originalU = originalUnit.toLowerCase();
        String newU = newUnit.toLowerCase();

        //The series of if statements below figures out what unit to convert from/to, and does so.

        switch (originalU) { //Begin conversion table
            case "miles per hour":
                switch (newU) { //Begin converting from miles per hour
                    case "miles per hour":
                        num2 = originalNumber;
                        break;
                    case "feet per second":
                        num2 = num1 * 1.46667d;
                        break;
                    case "kilometers per second":
                        num2 = num1 * 0.00044704d;
                        break;
                    case "kilometers per hour":
                        num2 = num1 * 1.60934d;
                        break;
                    case "meters per second":
                        num2 = num1 * 0.44704d;
                        break;
                } //End converting from miles per hour
                break;
            case "feet per second":
                switch (newU) { //Begin converting from feet per second
                    case "miles per hour":
                        num2 = num1 * 0.681818d;
                        break;
                    case "feet per second":
                        num2 = originalNumber;
                        break;
                    case "kilometers per second":
                        num2 = num1 * 0.0003048d;
                        break;
                    case "kilometers per hour":
                        num2 = num1 * 1.09728d;
                        break;
                    case "meters per second":
                        num2 = num1 * 0.3048d;
                        break;
                } //End converting from feet per second
                break;
            case "kilometers per second":
                switch (newU) { //Begin converting from kilometers per second
                    case "miles per hour":
                        num2 = num1 * 2236.93629d;
                        break;
                    case "feet per second":
                        num2 = num1 * 3280.8399d;
                        break;
                    case "kilometers per second":
                        num2 = originalNumber;
                        break;
                    case "kilometers per hour":
                        num2 = num1 * 3600.0d;
                        break;
                    case "meters per second":
                        num2 = num1 * 0.277778d;
                        break;
                } //End converting from kilometers per second
                break;
            case "kilometers per hour":
                switch (newU) { //Begin converting from kilometers per hour
                    case "miles per hour":
                        num2 = num1 * 2.23694d;
                        break;
                    case "feet per second":
                        num2 = num1 * 0.911344d;
                        break;
                    case "kilometers per second":
                        num2 = num1 * 0.000277777778d;
                        break;
                    case "kilometers per hour":
                        num2 = originalNumber;
                        break;
                    case "meters per second":
                        num2 = num1 * 0.277778d;
                        break;
                } //end converting from kilometers per hour
                break;
            case "meters per second":
                switch (newU) { //Begin converting from meters per second
                    case "miles per hour":
                        num2 = num1 * 2.23694d;
                        break;
                    case "feet per second":
                        num2 = num1 * 3.28084d;
                        break;
                    case "kilometers per second":
                        num2 = num1 * 0.001d;
                        break;
                    case "kilometers per hour":
                        num2 = num1 * 3.6d;
                        break;
                    case "meters per second":
                        num2 = originalNumber;
                        break;
                } //End converting from meters per second
                break;
        } //End conversion table

        //Return the result
        return num2;
    } //End convertSpeed


    /**
     * Converts between metric prefixes. The type of unit is unimportant- the metric system operates on a base 10 system
     * and so converting between, say, millimeters and meters is exactly the same as converting between milliliters and liters.
     * <p>
     * Accepted prefixes: yotta, zeta, exa, peta, tera, giga, mega, kilo, hecto, deka, UNIT,
     * deci, centi, milli, micro, nano, pico, femto, atto, zepto, yocto
     * <p>
     * Marc Kuniansky
     *
     * @param originalNumber must be a valid double
     * @param originalUnit   must be a valid String matching one of the supported units
     * @param newUnit        must be a valid String matching one of the supported units
     * @return a double, the converted number
     */
    public double metricConvert(double originalNumber, String originalUnit, String newUnit) { //Begin metricConvert
        //This can use a slightly different, and much easier, algorithm than the others.
        //Because metric is so well organized, it doesn't matter what number is input- the conversion factors are the same.
        //So if I take the original number and convert it to UNITS (which is x*10^0) then convert from UNITS to the new unit,
        //I can very easily do these conversions with very little work. I will heavily utilize the math class here, I need to
        //use exponents quite a bit to simplify life.

        //First, I will need four doubles: the original number, the UNIT number, the final number, and a variable with which to catch the
        //powers of 10.
        double num1 = originalNumber;
        double unitNum = 0d;
        double finalNum = 0d;
        double tenP;

        //I like to grab the two strings to prevent accidental editing/deletion. I also send them to lower case.
        String originalUn = originalUnit.toLowerCase();
        String newUn = newUnit.toLowerCase();

        String originalU;
        if (originalUn.contains(" ")) {
            originalU = originalUn.substring(0, originalUn.indexOf(" "));
        } else {
            originalU = originalUn;
        }

        //If the string from the MetricActivity spinners is passed, there will be a space. Remove everything after it.
        String newU;
        if (newUn.contains(" ")) {
            newU = newUn.substring(0, newUn.indexOf(" "));
        } else {
            newU = newUn;
        }
        //String newU = newUn.substring(0, newUn.indexOf(" "));
        //Next, I use the first of two switch statements. This converts the original number to UNITS, or x*10^0.
        switch (originalU) {
            case "yotta":
                //Yotta is 10^24 units
                tenP = allExponents(10d, 24d);
                unitNum = tenP * num1;
                break;
            case "zeta":
                //Zeta is 10^21
                tenP = allExponents(10d, 21d);
                unitNum = tenP * num1;
                break;
            case "exa":
                //Exa is 10^18
                tenP = allExponents(10d, 18d);
                unitNum = tenP * num1;
                break;
            case "peta":
                //Peta is 10^15
                tenP = allExponents(10d, 15d);
                unitNum = tenP * num1;
                break;
            case "tera":
                //Tera is 10^12
                tenP = allExponents(10d, 12d);
                unitNum = tenP * num1;
                break;
            case "giga":
                //Giga is 10^9
                tenP = allExponents(10d, 9d);
                unitNum = tenP * num1;
                break;
            case "mega":
                //Mega is 10^6
                tenP = allExponents(10d, 6d);
                unitNum = tenP * num1;
                break;
            case "kilo":
                //Kilo is 10^3
                tenP = allExponents(10d, 3d);
                unitNum = tenP * num1;
                break;
            case "hecto":
                //Hecto is 10^2
                tenP = allExponents(10d, 2d);
                unitNum = tenP * num1;
                break;
            case "deka":
                //Deka is 10^1
                tenP = allExponents(10d, 1d);
                unitNum = tenP * num1;
                break;
            case "unit":
                //UNIT is the target, 10^0
                tenP = allExponents(10d, 0d);
                unitNum = tenP * num1;
                break;
            case "deci":
                //Deci is 10^-1
                tenP = allExponents(10d, -1d);
                unitNum = num1 * tenP;
                break;
            case "centi":
                //Centi is 10^-2
                tenP = allExponents(10d, -2d);
                unitNum = num1 * tenP;
                break;
            case "milli":
                //Milli is 10^-3
                tenP = allExponents(10d, -3d);
                unitNum = num1 * tenP;
                break;
            case "micro":
                //Micro is 10^-6
                tenP = allExponents(10d, -6d);
                unitNum = num1 * tenP;
                break;
            case "nano":
                //Nano is 10^-9
                tenP = allExponents(10d, -9d);
                unitNum = num1 * tenP;
                break;
            case "pico":
                //Pico is 10^-12
                tenP = allExponents(10d, -12d);
                unitNum = num1 * tenP;
                break;
            case "femto":
                //Femto is 10^-15
                tenP = allExponents(10d, -15d);
                unitNum = num1 * tenP;
                break;
            case "atto":
                //Atto is 10^-18
                tenP = allExponents(10d, -18d);
                unitNum = num1 * tenP;
                break;
            case "zepto":
                //Zepto is 10^-21
                tenP = allExponents(10d, -21d);
                unitNum = num1 * tenP;
                break;
            case "yocto":
                //Yocto is 10^-24
                tenP = allExponents(10d, -24d);
                unitNum = num1 * tenP;
                break;
        }

        //Next is a switch statement for all possible cases of the new unit. It takes
        //the number given by the first switch, unitNum, and converts it to the new unit
        //using math.
        switch (newU) { //Begin converting from base units (10^0) to new units.
            case "yotta":
                //Yotta is 10^24 units
                tenP = allExponents(10d, 24d);
                finalNum = unitNum / tenP;
                break;
            case "zeta":
                //Zeta is 10^21
                tenP = allExponents(10d, 21d);
                finalNum = unitNum / tenP;
                break;
            case "exa":
                //Exa is 10^18
                tenP = allExponents(10d, 18d);
                finalNum = unitNum / tenP;
                break;
            case "peta":
                //Peta is 10^15
                tenP = allExponents(10d, 15d);
                finalNum = unitNum / tenP;
                break;
            case "tera":
                //Tera is 10^12
                tenP = allExponents(10d, 12d);
                finalNum = unitNum / tenP;
                break;
            case "giga":
                //Giga is 10^9
                tenP = allExponents(10d, 9d);
                finalNum = unitNum / tenP;
                break;
            case "mega":
                //Mega is 10^6
                tenP = allExponents(10d, 6d);
                finalNum = unitNum / tenP;
                break;
            case "kilo":
                //Kilo is 10^3
                tenP = allExponents(10d, 3d);
                finalNum = unitNum / tenP;
                break;
            case "hecto":
                //Hecto is 10^2
                tenP = allExponents(10d, 2d);
                finalNum = unitNum / tenP;
                break;
            case "deka":
                //Deka is 10^1
                tenP = allExponents(10d, 1d);
                finalNum = unitNum / tenP;
                break;
            case "unit":
                //UNIT is the target, 10^0
                tenP = allExponents(10d, 0d);
                finalNum = unitNum / tenP;
                break;
            case "deci":
                //Deci is 10^-1
                tenP = allExponents(10d, -1d);
                finalNum = unitNum / tenP;
                break;
            case "centi":
                //Centi is 10^-2
                tenP = allExponents(10d, -2d);
                finalNum = unitNum / tenP;
                break;
            case "milli":
                //Milli is 10^-3
                tenP = allExponents(10d, -3d);
                finalNum = unitNum / tenP;
                break;
            case "micro":
                //Micro is 10^-6
                tenP = allExponents(10d, -6d);
                finalNum = unitNum / tenP;
                break;
            case "nano":
                //Nano is 10^-9
                tenP = allExponents(10d, -9d);
                finalNum = unitNum / tenP;
                break;
            case "pico":
                //Pico is 10^-12
                tenP = allExponents(10d, -12d);
                finalNum = unitNum / tenP;
                break;
            case "femto":
                //Femto is 10^-15
                tenP = allExponents(10d, -15d);
                finalNum = unitNum / tenP;
                break;
            case "atto":
                //Atto is 10^-18
                tenP = allExponents(10d, -18d);
                finalNum = unitNum / tenP;
                break;
            case "zepto":
                //Zepto is 10^-21
                tenP = allExponents(10d, -21d);
                finalNum = unitNum / tenP;
                break;
            case "yocto":
                //Yocto is 10^-24
                tenP = allExponents(10d, -24d);
                finalNum = unitNum / tenP;
                break;
        } //End converting from base units (10^0) to new units.

        //Finally, return the final number
        return finalNum;
    } //End metricConvert

    /**
     * Helping method which will allow an exponent to be either positive or negative, unlike the math pow() method.
     * Thanks to this page for the idea to do this, and for the skeleton code- http://stackoverflow.com/questions/4364634/calculate-the-power-of-any-exponent-negative-or-positive
     * <p>
     * Marc Kuniansky
     *
     * @param base     must be a valid double
     * @param exponent must be a valid double
     * @return the resulting number
     */
    private double allExponents(double base, double exponent) { //Begin allExponents
        double b = base;
        double e = Math.abs(exponent);
        double finalNum;
        if (exponent > 0) {
            finalNum = Math.pow(base, e);
        } else if (exponent < 0) {
            double p = Math.pow(base, e);
            finalNum = 1 / p;
        } else {
            finalNum = 1;
        }

        return finalNum;
    } //End allExponents

    public static String getFormattedDistance(Integer distance, Integer inHeightUnit) {
        if (distance == null) {
            return "";
        }

        String originalUnit, desiredUnit, append;

        if (inHeightUnit == Subterminal.HEIGHT_UNIT_METRIC) {
            originalUnit = "meters";
        } else {
            originalUnit = "feet";
        }

        if (Subterminal.getUser().getSettings().getDefaultHeightUnit() == Subterminal.HEIGHT_UNIT_METRIC) {
            desiredUnit = "meters";
            append = "m";
        } else {
            desiredUnit = "feet";
            append = "ft";
        }

        String value = NumberFormat.getNumberInstance(Locale.US).format(Math.round(new UnitConverter().lengthConvert(distance, originalUnit, desiredUnit)));

        return value + append;
    }

    public static int getConvertedInteger(Integer distance, Integer inHeightUnit) {
        String originalUnit, desiredUnit;

        if (inHeightUnit == Subterminal.HEIGHT_UNIT_METRIC) {
            originalUnit = "meters";
        } else {
            originalUnit = "feet";
        }

        if (Subterminal.getUser().getSettings().getDefaultHeightUnit() == Subterminal.HEIGHT_UNIT_METRIC) {
            desiredUnit = "meters";
        } else {
            desiredUnit = "feet";
        }

        return Integer.parseInt(Long.toString(Math.round(new UnitConverter().lengthConvert(distance, originalUnit, desiredUnit))));
    }

} //End converter class