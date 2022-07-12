# TAKdict.NET
Class for veryfying integrity and digital signature of dictionary.
## Class Integrity methods
* **String getHash( String file_name )** - returns Base64 encoded SHA1 digest of file with given file name
* **bool verifyPgp( String file_name, String pub_key_filename )** - 
verifies pgp signature of file with given filename with public key, public key must be rsa key, given file must be clear signed signature (clear file content is embeded in signature)
* **bool verifyXades( String xml_path )** - verifies xades signature of given xades(xml) file, xades signature has to contain content of signed file in clear text (inneer signature), all certificates including root certificate must be installed in operating system
* **void checkCertRoot( String thumb_print )** - checks certificate chain up to root certificate, raises exeptions if something is wrong
* **String search( String text, String file_name, int b )** todo


***
## Example
File Example.cs contains example of usage of all methods. This repo contains all required files except xades signature (for now it is not uploaded because of privacy concerns).
* CoT_to_APP6B.csv - original file
* CoT_to_APP6B.csv.asc - pgp clear signature
* rsa.asc - public rsa key for pgp verification
***
## Additional notes
Xades and pgp signature must contain clear file (clear/inner signature).
Under Windows 10, certificates must be imported system wide (not as user).
Remeber to import certificates in correct containers.
Certificate revoke lists must be imported for every certificate system wide.
In order to do this enter this command in terminal with administrative rights:

```
certutil.exe -addstore CA cert_file.crl   (for intermediate certificates)
certutil.exe -addstore Root cert_file.crl (for root certificate)
```