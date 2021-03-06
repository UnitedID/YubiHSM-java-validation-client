/*
 * Copyright (c) 2011 United ID. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Stefan Wold <stefan.wold@unitedid.org>
 */

package org.unitedid.yhsm.ws.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unitedid.yhsm.ws.*;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class YubiHSMValidationClient {
    /** Logger */
    private final Logger log = LoggerFactory.getLogger(YubiHSMValidationClient.class);

    private ValidationServiceImplService validationServiceImplService;
    private ValidationService validationService;
    private URL wsdlURL;

    public YubiHSMValidationClient(String wsdl) {
        try {
            wsdlURL = new URL(wsdl);
            validationServiceImplService = new ValidationServiceImplService(wsdlURL);
            //BindingProvider port = (BindingProvider) validateAeadServiceImplService.getPort(ValidateAeadServiceImplService.class);
            //port.getRequestContext().put(BindingProvider.USERNAME_PROPERTY)
            validationService = validationServiceImplService.getValidationServiceImplPort();
        } catch (MalformedURLException e) {
            log.error("Malformed URL {}", wsdlURL.toString(), e);
        }
    }

    public boolean validateAEAD(String nonce, int keyHandle, String aead, String plaintext)
            throws YubiHSMErrorException_Exception, UnsupportedEncodingException {
        return validationService.validateAEAD(nonce, keyHandle, aead, plaintext.getBytes("UTF-8"));
    }

    public int validateOathHOTP(String nonce, int keyHandle, String aead, int counter, String otp, int lookAhead)
            throws YubiHSMErrorException_Exception {
        return validationService.validateOathHOTP(nonce, keyHandle, aead, counter, otp, lookAhead);
    }

    public boolean validateOathTOTP(String nonce, int keyHandle, String aead, String otp, int period, int drift,
                                int backwardDrift, int forwardDrift)
            throws YubiHSMErrorException_Exception {
        return validationService.validateOathTOTP(nonce, keyHandle, aead, otp, period, drift, backwardDrift,
                forwardDrift);
    }

    public static void main(String[] args)
            throws YubiHSMErrorException_Exception, MalformedURLException, UnsupportedEncodingException {
        URL wsdlURL = new URL(args[0]);
        System.out.println("using WSDL: " + wsdlURL.toString());
        ValidationServiceImplService validationServiceImplService = new ValidationServiceImplService(wsdlURL);
        ValidationService validationService = validationServiceImplService.getValidationServiceImplPort();

        /*if (validateAeadService.validateAEAD(args[1], Integer.parseInt(args[2]), args[3], args[4].getBytes("UTF-8"))) {
            System.out.println("AEAD validated successfully!");
        } else {
            System.out.println("AEAD validation failed!");
        }*/


        System.out.println(validationService.validateOathHOTP(args[1], Integer.parseInt(args[2]), args[3],
                Integer.parseInt(args[4]), args[5], Integer.parseInt(args[6])));

        System.exit(0);
    }
}
