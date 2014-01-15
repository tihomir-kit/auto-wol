package net.cmikavac.autowol.utils;

import java.util.regex.Pattern;

public class IPAddressValidator {
    private Pattern mPattern;

    private static final String IPADDRESS_PATTERN = 
        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    /**
     * Constructor. 
     */
    public IPAddressValidator(){
        mPattern = Pattern.compile(IPADDRESS_PATTERN);
    }

    /**
     * Creates an instance of IPAddressValidator.
     * @return      An instance of IPAddressValidator.
     */
    public static IPAddressValidator getInstance() {
        return new IPAddressValidator();
    }

   /**
    * Validates an IP address with regular expression.
    * @param ip     IP address to validate.
    * @return       Valid IP address?
    */
    public boolean validateIp(final String ip){
        return mPattern.matcher(ip).matches();
    }
}
