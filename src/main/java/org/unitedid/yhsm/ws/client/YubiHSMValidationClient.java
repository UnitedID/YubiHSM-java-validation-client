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

import org.unitedid.yhsm.ws.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.BindingProvider;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class YubiHSMValidationClient {
    /** Logger */
    private final Logger log = LoggerFactory.getLogger(YubiHSMValidationClient.class);

    private ValidateAeadServiceImplService validateAeadServiceImplService;
    private ValidateAeadService validateAeadService;
    private URL wsdlURL;

    public YubiHSMValidationClient(String wsdl) {
        try {
            wsdlURL = new URL(wsdl);
            validateAeadServiceImplService = new ValidateAeadServiceImplService(wsdlURL);
            //BindingProvider port = (BindingProvider) validateAeadServiceImplService.getPort(ValidateAeadServiceImplService.class);
            //port.getRequestContext().put(BindingProvider.USERNAME_PROPERTY)
            validateAeadService = validateAeadServiceImplService.getValidateAeadServiceImplPort();
        } catch (MalformedURLException e) {
            log.error("Malformed URL {}", wsdlURL.toString(), e);
        }
    }

    public boolean validateAEAD(String nonce, int keyHandle, String aead, String plaintext) throws YubiHSMErrorException_Exception, UnsupportedEncodingException {
        return validateAeadService.validateAEAD(nonce, keyHandle, aead, plaintext.getBytes("UTF-8"));
    }

    public static void main(String[] args) throws YubiHSMErrorException_Exception, MalformedURLException, UnsupportedEncodingException {
        URL wsdlURL = new URL(args[0]);
        System.out.println("using WSDL: " + wsdlURL.toString());
        ValidateAeadServiceImplService validateAeadServiceImplService = new ValidateAeadServiceImplService(wsdlURL);
        ValidateAeadService validateAeadService = validateAeadServiceImplService.getValidateAeadServiceImplPort();

        if (validateAeadService.validateAEAD(args[1], Integer.parseInt(args[2]), args[3], args[4].getBytes("UTF-8"))) {
            System.out.println("AEAD validated successfully!");
        } else {
            System.out.println("AEAD validation failed!");
        }
        System.exit(0);
    }
}
